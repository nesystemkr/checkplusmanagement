package kr.peelknight.socket.context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.xml.bind.DatatypeConverter;

import kr.peelknight.daemon.context.BaseDaemon;
import kr.peelknight.daemon.context.StopThread;
import kr.peelknight.socket.dao.SocketLogDao;
import kr.peelknight.socket.model.SH_ReadData;
import kr.peelknight.socket.model.SH_SocketLog;
import kr.peelknight.socket.model.SH_WriteData;
import kr.peelknight.util.Base64;

public abstract class SocketServerDaemon extends BaseDaemon {
	public static final int DATALENTYPE_ALL = 0;
	public static final int DATALENTYPE_DATA = 1;
	public static final int DATALENTYPE_FIXED = 2;
	public static final int LENBYTETYPE_STRING = 0;
	public static final int LENBYTETYPE_BYTE = 1;
	public static final int LENBYTETYPE_SHORT = 2;
	public static final int LENBYTETYPE_SHORTR = 3;
	public static final int LENBYTETYPE_INT = 4;
	public static final int LENBYTETYPE_INTR = 5;
	public static final int LENBYTETYPE_LONG = 6;
	public static final int LENBYTETYPE_LONGR = 7;
	
	protected int _port;
	protected boolean _isWebSocket;
	protected Acceptor _acceptor;
	protected int _authIdLen = 0;
	protected boolean _isSaveReadLog = false;
	
//	public static int LEN_BOM_SIZE;
//	public static byte[] BOM;
//	static {
//		BOM = new byte[3];
//		BOM[0] = (byte)0xFF;
//		BOM[1] = (byte)0xBB;
//		BOM[2] = (byte)0xBF;
//		LEN_BOM_SIZE = BOM.length;
//	}
	
	private List<SocketHolder> holderList = new ArrayList<>();
	
	public SocketServerDaemon(int port) {
		this(port, false);
	}
	
	public SocketServerDaemon(int port, boolean isWebSocket) {
		_port = port;
		_isWebSocket = isWebSocket;
	}
	
	public void start() {
		_acceptor = new Acceptor();
		_acceptor.start();
	}
	
	public void stop() {
		if (_acceptor != null) {
			_acceptor.isStop = true;
			_acceptor.interrupt();
		}
	}
	
	public boolean isRunning() {
		if (_acceptor == null) {
			return false;
		}
		return _acceptor.isRunning();
	}
	
	public abstract void clientConnected(SocketHolder holder);
	public abstract void clientDisconnected(SocketHolder holder);
	public abstract void receiveData(SocketHolder holder, byte[] data);
	public abstract boolean checkSocketDataParam(SocketHolder holder, String authId);
	
	public class Acceptor extends StopThread {
		public boolean _isRunning = false;
		public boolean isRunning() {
			return _isRunning;
		}
		
		public void run() {
			_isRunning = true;
			try {
				ServerSocket listener = new ServerSocket(_port);
				try {
					while (true) {
						if (isStop == true) {
							break;
						}
						Socket socket = listener.accept();
						SocketHolder holder = new SocketHolder(socket);
						if (_isWebSocket) {
							holder.handShake();
						}
						if (false == holder.checkAuthId()) {
							socket.close();
							socket = null;
							holder = null;
							continue;
						}
						holder.start();
						holderList.add(holder);
						clientConnected(holder);
					}
				} finally {
					listener.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			_isRunning = false;
		}
	}
	
	public class SocketHolder {
		private Socket _socket;
		private Scanner _scanner;
		private InputStream _is;
		private OutputStream _os;
		private ReadThread _readThread;
		private WriteThread _writeThread;
		private String _authId;
		private long _socketSeq;
		private int _dataLenType; //0: 전체길이 + 데이터, 1: 데이터길이 + 데이터, 2: 고정길이
		private int _fixDataLen;
		private int _lenByteType; //0: 문자열, 1:byte, 2:short, 3:short reverse, 4:int short, 5:int reverse, 6:long, 7:long reverse
		private int _lenByteSize;
		
		public SocketHolder() {
		}
		
		public SocketHolder(Socket socket) {
			_socket = socket;
			try {
				_is = _socket.getInputStream();
				_os = _socket.getOutputStream();
				_scanner = new Scanner(_is, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String getAuthId() {
			return _authId;
		}
		
		public void setAuthId(String authId) {
			_authId = authId;
		}
		
		public boolean checkAuthId() {
			if (_authIdLen <= 0) {
				return checkSocketDataParam(this, null);
			}
			byte[] authId = new byte[_authIdLen];
			byte[] tmp = new byte[_authIdLen];
			int authPos = 0;
			int tmpLen;
			try {
				while (true) {
					tmpLen = _is.read(tmp);
					System.arraycopy(tmp, 0, authId, authPos, tmpLen);
					authPos += tmpLen;
					if (authPos >= _authIdLen) {
						break;
					}
				}
				_authId = new String(authId);
				return checkSocketDataParam(this, _authId);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public void start() {
			_readThread = new ReadThread(this, _is);
			_readThread.start();
			_writeThread = new WriteThread(this, _os);
			_writeThread.start();
		}
		
		public void stop() {
			if (_readThread != null) {
				_readThread.stopThread();
				_readThread.interrupt();
			}
			if (_writeThread != null) {
				_writeThread.stopThread();
				_writeThread.interrupt();
			}
			holderList.remove(this);
			clientDisconnected(this);
		}
		
		public void handShake() {
			try {
				String data = _scanner.useDelimiter("\\r\\n\\r\\n").next();
				Matcher get = Pattern.compile("^GET").matcher(data);

				if (get.find()) {
					Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
					match.find();
					byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n" +
									   "Connection: Upgrade\r\n" +
									   "Upgrade: websocket\r\n" +
									   "Sec-WebSocket-Accept: " +
									   DatatypeConverter.printBase64Binary(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8"))) +
									   "\r\n\r\n").getBytes("UTF-8");
					_os.write(response, 0, response.length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		
		public void sendPong(byte[] data) {
			_writeThread.pushData(new SendData((byte)0xA, data));
		}
		
		public void sendData(String data) {
//System.out.println("" + this.toString() + ":" + data);
			sendData(data.getBytes());
		}
		
		public void sendData(byte[] data) {
			_writeThread.pushData(new SendData(data));
		}

		public void setDataLenType(int dataLenType) {
			_dataLenType = dataLenType;
		}

		public void setFixDataLen(int fixDataLen) {
			_fixDataLen = fixDataLen;
		}

		public void setLenByteType(int lenByteType) {
			_lenByteType = lenByteType;
		}

		public void setLenByteSize(int lenByteSize) {
			_lenByteSize = lenByteSize;
		}
		
		public int getDataLenType() {
			return _dataLenType;
		}
		
		public int getFixDataLen() {
			return _fixDataLen;
		}
		
		public int getLenByteType() {
			return _lenByteType;
		}
		
		public int getLenByteSize() {
			return _lenByteSize;
		}

		public long getSocketSeq() {
			return _socketSeq;
		}

		public void setSocketSeq(long socketSeq) {
			this._socketSeq = socketSeq;
		}
	}
	
	public class SendData {
		protected byte _opCode;
		protected byte[] _data;
		
		public SendData(byte[] data) {
			_opCode = 1;
			_data = data;
		}
		
		public SendData(byte opCode, byte[] data) {
			_opCode = opCode;
			_data = data;
		}
		
		public byte[] data() {
			return _data;
		}
		
		public byte opCode() {
			return _opCode;
		}
	}
	
	public class BaseThread extends Thread {
		public WeakReference<SocketHolder> _holder;
		public boolean _isStop = false;
		
		public BaseThread(SocketHolder holder) {
			super();
			_holder = new WeakReference<SocketHolder>(holder);
			setDaemon(true);
		}
	}
	
	public class ReadThread extends BaseThread {
		protected InputStream _is;
		protected SH_ReadData _readData;
		protected SocketLogDao _dao;
		protected SH_SocketLog _socketLog;
		
		public ReadThread(SocketHolder holder, InputStream is) {
			super(holder);
			_is = is;
			_readData = new SH_ReadData(_holder.get().getDataLenType(), _holder.get().getFixDataLen(), _holder.get().getLenByteType(), _holder.get().getLenByteSize());
		}
		
		public void stopThread() {
			_isStop = true;
			try {
				_is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try {
				byte[] readTmp = new byte[8192];
				if (_isSaveReadLog == true) {
					_dao = new SocketLogDao();
					_socketLog = new SH_SocketLog();
					_socketLog.setSocketSeq(_holder.get().getSocketSeq());
				}
				while (true) {
					if (isInterrupted() == true) {
						break;
					}
					int readLen = _is.read(readTmp);
					if (_isStop == true) {
						break;
					}
					if (_isSaveReadLog == true) {
						_socketLog.setDataLen(readLen);
						_socketLog.setData(Base64.encodeToString(readTmp, 0, readLen, Base64.NO_WRAP | Base64.URL_SAFE));
						_dao.insertSocketLog(_socketLog);
					}
					if (readLen <= 0) {
						break;
					}
					_readData.concatBuffer(readTmp, readLen);
					byte[] recvData;
					while (true) {
						if (_isWebSocket == true) {
							recvData = _readData.getWebNextData();
							byte opcode = _readData.getLastOpcode();
							if (opcode == 0x8) {
								_isStop = true;
							} else if (opcode == 0x9) {
								_holder.get().sendPong(recvData);
							}
						} else {
							recvData = _readData.getNextData();
						}
						if (recvData == null) {
							break;
						}
						receiveData(_holder.get(), recvData);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (_holder.get() != null) {
				_holder.get().stop();
			}
		}
	}
	
	public class WriteThread extends BaseThread {
		protected OutputStream _os;
		protected BlockingQueue<SendData> _dataQueue;
		protected SH_WriteData _writeData;

		public WriteThread(SocketHolder holder, OutputStream os) {
			super(holder);
			setDaemon(true);
			_os = os;
			_dataQueue = new ArrayBlockingQueue<>(100);
			_writeData = new SH_WriteData(_holder.get().getDataLenType(), _holder.get().getFixDataLen(), _holder.get().getLenByteType(), _holder.get().getLenByteSize());
		}
		
		public void stopThread() {
			_isStop = true;
			try {
				_os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void pushData(SendData aData) {
			try {
				if (_dataQueue.remainingCapacity() == 0) {
					_dataQueue.take();
				}
				_dataQueue.add(aData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try {
				while (true) {
					if (this.isInterrupted()) {
						break;
					}
					SendData sendData = _dataQueue.take();
					if (_isStop == true) {
						break;
					}
					if (_isWebSocket == true) {
						byte[] lenBytes;
						if (sendData.data() != null) {
							if (sendData.data().length <= 125) {
								lenBytes = new byte[2];
								lenBytes[1] = (byte)sendData.data().length;
							} else if (sendData.data().length <= 65535) {
								lenBytes = new byte[4];
								lenBytes[1] = 126;
								lenBytes[2] = (byte)((sendData.data().length >> 8) & 0xff);
								lenBytes[3] = (byte)(sendData.data().length & 0xff);
							} else {
								lenBytes = new byte[10];
								long lenTemp = sendData.data().length;
								lenBytes[1] = 127;
								lenBytes[2] = (byte)((lenTemp >> 54) & 0xff);
								lenBytes[3] = (byte)((lenTemp >> 48) & 0xff);
								lenBytes[4] = (byte)((lenTemp >> 40) & 0xff);
								lenBytes[5] = (byte)((lenTemp >> 32) & 0xff);
								lenBytes[6] = (byte)((lenTemp >> 24) & 0xff);
								lenBytes[7] = (byte)((lenTemp >> 16) & 0xff);
								lenBytes[8] = (byte)((lenTemp >> 8 ) & 0xff);
								lenBytes[9] = (byte)(lenTemp         & 0xff);
							}
						} else {
							lenBytes = new byte[2];
							lenBytes[1] = (byte)0x0;
						}
						lenBytes[0] = (byte)(0x80 + sendData.opCode());
						_os.write(lenBytes);
						//SH_ReadData.printBin(lenBytes);
						if (sendData.data() != null) {
							_os.write(sendData.data());
						}
						_os.flush();
					} else {
//						__os.write(SocketServerDaemon.BOM);
						long dataLen = sendData.data().length;
						if (_holder.get().getDataLenType() == SocketServerDaemon.DATALENTYPE_ALL) {
							dataLen += _holder.get().getLenByteSize();
						}
						byte[] lenBytes = _writeData.getDataLengthBytes(dataLen);
						if (lenBytes != null) {
							_os.write(lenBytes);
						}
						_os.write(sendData.data());
						_os.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (_holder.get() != null) {
				_holder.get().stop();
			}
		}
	}
}
