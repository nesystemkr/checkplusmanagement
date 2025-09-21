package kr.peelknight.socket.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import kr.peelknight.socket.context.SocketServerDaemon;
import kr.peelknight.util.CommonFunc;

public class SH_ReadData extends SH_SocketData {
	private List<byte[]> __buffers = new ArrayList<>();
	private final int __bufferSize = 3000000;
	private int __curIdx = 0;
	private int __curPos = 0;
	private int __curLen = 0;

	private int __dataLenType; //0: 전체길이 + 데이터, 1: 데이터길이 + 데이터, 2: 고정길이
	private int __fixDataLen;
	private int __lenByteType; //0: 문자열, 1:byte, 2:short, 3:short reverse, 4:int short, 5:int reverse, 6:long, 7:long reverse
	private int __lenByteSize;

	public SH_ReadData(int dataLenType, int fixDataLen, int lenByteType, int lenByteSize) {
		__buffers.add(new byte[__bufferSize]);
		__buffers.add(new byte[__bufferSize]);
		
		__dataLenType = dataLenType;
		__fixDataLen = fixDataLen;
		__lenByteType = lenByteType;
		__lenByteSize = lenByteSize;
	}
	
	public int nextIndex() {
		if (__buffers.size() >= __curIdx + 1) {
			return 0;
		} else {
			return __curIdx + 1;
		}
	}
	
	public synchronized void concatBuffer(byte[] readData, int readLen) throws Exception {
		if (readLen <= 0) {
			return;
		}
		if (__curPos + __curLen + readLen > __bufferSize) {
			if (__curLen + readLen > __bufferSize) {
				throw new Exception("Critical Error! Data is too large! Extend buffer size!");
			}
			int nextIdx = nextIndex();
			System.arraycopy(__buffers.get(__curIdx), __curPos, __buffers.get(nextIdx), 0, __curLen);
			__curIdx = nextIdx;
			__curPos = 0;
		}
		System.arraycopy(readData, 0, __buffers.get(__curIdx), __curPos + __curLen, readLen);
		__curLen += readLen;
	}
	
	public int getDataLength() {
//		if (__lenByteType == SocketServerDaemon.LENBYTETYPE_STRING) {
//			return CommonFunc.getIntFromObject(readString(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE, __lenByteSize));
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_BYTE) {
//			return CommonFunc.getIntFromObject(readByte(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE));
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_SHORT) {
//			return readShort(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE);
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_SHORTR) {
//			return readShortR(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE);
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_INT) {
//			return readInt(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE);
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_INTR) {
//			return readIntR(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE);
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_LONG) {
//			return (int)readLong(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE);
//		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_LONGR) {
//			return (int)readLongR(__buffers.get(__curIdx), __curPos + SocketServerDaemon.LEN_BOM_SIZE);
//		} else {
//			return 0;
//		}
		if (__lenByteType == SocketServerDaemon.LENBYTETYPE_STRING) {
			return CommonFunc.getIntFromObject(readString(__buffers.get(__curIdx), __curPos, __lenByteSize));
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_BYTE) {
			return CommonFunc.getIntFromObject(readByte(__buffers.get(__curIdx), __curPos));
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_SHORT) {
			return readShort(__buffers.get(__curIdx), __curPos);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_SHORTR) {
			return readShortR(__buffers.get(__curIdx), __curPos);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_INT) {
			return readInt(__buffers.get(__curIdx), __curPos);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_INTR) {
			return readIntR(__buffers.get(__curIdx), __curPos);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_LONG) {
			return (int)readLong(__buffers.get(__curIdx), __curPos);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_LONGR) {
			return (int)readLongR(__buffers.get(__curIdx), __curPos);
		} else {
			return 0;
		}
	}
	
//	public boolean checkNSkipBOM() {
//		if (__curLen < SocketServerDaemon.LEN_BOM_SIZE) {
//System.out.println("LENGTH IS NOT ENOUGH!!!!");
//			return false;
//		}
//		while(true) {
//			if (SocketServerDaemon.BOM[0] == __buffers.get(__curIdx)[__curPos] ||
//				SocketServerDaemon.BOM[1] == __buffers.get(__curIdx)[__curPos + 1] ||
//				SocketServerDaemon.BOM[2] == __buffers.get(__curIdx)[__curPos + 2]) {
//System.out.println("CHECK OK!!!!");
//				break;
//			}
//			__curPos++;
//			__curLen--;
////System.out.println("SKIPPED!!!!");
//			if (__curLen < SocketServerDaemon.LEN_BOM_SIZE) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	byte __lastOpcode = 0x0;
	public synchronized byte getLastOpcode() {
		return __lastOpcode;
	}
	
	public synchronized byte[] getNextData() {
//		if (false == checkNSkipBOM()) {
//			return null;
//		}
		int nextLen = 0;
		if (__dataLenType == SocketServerDaemon.DATALENTYPE_ALL ||
			__dataLenType == SocketServerDaemon.DATALENTYPE_DATA) {
			if (__curLen >= __lenByteSize) {
				nextLen = getDataLength();
			}
		} else if (__dataLenType == SocketServerDaemon.DATALENTYPE_FIXED) {
			nextLen = __fixDataLen;
		} else {
			return null;
		}
		
//		if (nextLen == 0 || nextLen > __curLen - SocketServerDaemon.LEN_BOM_SIZE) {
//			return null;
//		} else {
//			__curPos += SocketServerDaemon.LEN_BOM_SIZE;
//			__curLen -= SocketServerDaemon.LEN_BOM_SIZE;
//			byte[] retData = null;
//			if (__dataLenType == SocketServerDaemon.DATALENTYPE_FIXED) {
//				retData = new byte[nextLen];
//				System.arraycopy(__buffers.get(__curIdx), __curPos, retData, 0, nextLen);
//			} else if (__dataLenType == SocketServerDaemon.DATALENTYPE_DATA) {
//				retData = new byte[nextLen];
//				__curPos += __lenByteSize;
//				__curLen -= __lenByteSize;
//				System.arraycopy(__buffers.get(__curIdx), __curPos, retData, 0, nextLen);
//			} else if (__dataLenType == SocketServerDaemon.DATALENTYPE_ALL) {
//				retData = new byte[nextLen - __lenByteSize];
//				System.arraycopy(__buffers.get(__curIdx), __curPos + __lenByteSize, retData, 0, nextLen - __lenByteSize);
//			}
//			__curPos += nextLen;
//			__curLen -= nextLen;
//			return retData;
//		}
		if (nextLen == 0 || nextLen > __curLen) {
			return null;
		} else {
			byte[] retData = null;
			if (__dataLenType == SocketServerDaemon.DATALENTYPE_FIXED) {
				retData = new byte[nextLen];
				System.arraycopy(__buffers.get(__curIdx), __curPos, retData, 0, nextLen);
			} else if (__dataLenType == SocketServerDaemon.DATALENTYPE_DATA) {
				retData = new byte[nextLen];
				__curPos += __lenByteSize;
				__curLen -= __lenByteSize;
				System.arraycopy(__buffers.get(__curIdx), __curPos, retData, 0, nextLen);
			} else if (__dataLenType == SocketServerDaemon.DATALENTYPE_ALL) {
				retData = new byte[nextLen - __lenByteSize];
				System.arraycopy(__buffers.get(__curIdx), __curPos + __lenByteSize, retData, 0, nextLen - __lenByteSize);
			}
			__curPos += nextLen;
			__curLen -= nextLen;
			return retData;
		}
	}
	
	public synchronized byte[] getWebNextData() {
//byte byte1 = __buffers.get(__curIdx)[0];
//byte byte2 = __buffers.get(__curIdx)[1];
//byte byte3 = -127;
//System.out.println(String.format("%d%d%d%d%d%d%d%d %d%d%d%d%d%d%d%d %d%d%d%d%d%d%d%d\n",
//		(byte1 & 0b10000000) != 0 ? 1 : 0,
//		(byte1 & 0b01000000) != 0 ? 1 : 0,
//		(byte1 & 0b00100000) != 0 ? 1 : 0,
//		(byte1 & 0b00010000) != 0 ? 1 : 0,
//		(byte1 & 0b00001000) != 0 ? 1 : 0,
//		(byte1 & 0b00000100) != 0 ? 1 : 0,
//		(byte1 & 0b00000010) != 0 ? 1 : 0,
//		(byte1 & 0b00000001) != 0 ? 1 : 0,
//		(byte2 & 0b10000000) != 0 ? 1 : 0,
//		(byte2 & 0b01000000) != 0 ? 1 : 0,
//		(byte2 & 0b00100000) != 0 ? 1 : 0,
//		(byte2 & 0b00010000) != 0 ? 1 : 0,
//		(byte2 & 0b00001000) != 0 ? 1 : 0,
//		(byte2 & 0b00000100) != 0 ? 1 : 0,
//		(byte2 & 0b00000010) != 0 ? 1 : 0,
//		(byte2 & 0b00000001) != 0 ? 1 : 0,
//		(byte3 & 0b10000000) != 0 ? 1 : 0,
//		(byte3 & 0b01000000) != 0 ? 1 : 0,
//		(byte3 & 0b00100000) != 0 ? 1 : 0,
//		(byte3 & 0b00010000) != 0 ? 1 : 0,
//		(byte3 & 0b00001000) != 0 ? 1 : 0,
//		(byte3 & 0b00000100) != 0 ? 1 : 0,
//		(byte3 & 0b00000010) != 0 ? 1 : 0,
//		(byte3 & 0b00000001) != 0 ? 1 : 0)
//		);
//System.out.println("web data start " + __curPos + " , " + __curLen);
		__lastOpcode = 0x0;
		int tmpPos;
		for (int ii=__curPos; ii<(__curPos + __curLen); ii++) {
			if ((__buffers.get(__curIdx)[ii] & 0x80) == 0x80) {
				byte opcode = (byte)(__buffers.get(__curIdx)[ii] & 0xF);
				if (opcode != 0x1 && opcode != 0x2 && opcode != 0x8 && opcode != 0x9 && opcode != 0xA) {
					continue;
				}
				tmpPos = ii + 1;
				boolean masked = ((__buffers.get(__curIdx)[tmpPos] & 0x80) == 0x80);
				int frameLen = (__buffers.get(__curIdx)[tmpPos] & 0x7F);
				tmpPos++;
				if (frameLen == 126) {
					byte[] sizebytes = new byte[2];
					sizebytes[0] = __buffers.get(__curIdx)[tmpPos];
					sizebytes[1] = __buffers.get(__curIdx)[tmpPos + 1] ;
					frameLen = new BigInteger(sizebytes).intValue();
					tmpPos += 2;
				} else if (frameLen == 127) {
					byte[] sizebytes = new byte[8];
					for (int jj=0; jj<8; jj++) {
						sizebytes[jj] = __buffers.get(__curIdx)[tmpPos + jj];
					}
					frameLen = new BigInteger(sizebytes).intValue();
					tmpPos += 8;
				}
				
				byte[] retData = null;
				byte[] key = null;
				if (masked == true) {
					if ((__curPos + __curLen) < (tmpPos + 4 + frameLen)) { //저장된 길이가 전체길이보다 모자랄 경우는 탈출한다.
						return null;
					}
					key = new byte[4];
					for (int jj=0; jj<4; jj++) {
						key[jj] = __buffers.get(__curIdx)[tmpPos + jj];
					}
					tmpPos += 4;
				}
				if (frameLen > 0) {
					retData = new byte[frameLen];
					System.arraycopy(__buffers.get(__curIdx), tmpPos, retData, 0, frameLen);
					if (masked == true) {
						for (int jj=0; jj<frameLen; jj++) {
							retData[jj] = (byte)(retData[jj] ^ key[jj & 0x3]);
						}
					}
				}
				int tmpLen = (tmpPos - __curPos) + frameLen;
				__curPos += tmpLen;
				__curLen -= tmpLen;
				__lastOpcode = opcode;
//System.out.println("web data return not null " + __curPos + " , " + __curLen + " " + tmpPos + " " + frameLen);
				return retData;
			}
		}
//System.out.println("web data return null " + __curPos + " , " + __curLen);
		__curPos = __curPos + __curLen;
		__curLen = 0;
		return null;
	}
	
	static public void printBin(byte[] bytes) {
		for (int ii = 0; ii < bytes.length; ii++) {
			printBin(bytes[ii]);
			if (ii == bytes.length - 1) {
				System.out.print("\n");
			} else {
				System.out.print(" ");
			}
		}
	}
	
	static public void printBin(byte byt) {
		System.out.print(String.format("%d%d%d%d%d%d%d%d",
						(byt & 0b10000000) != 0 ? 1 : 0,
						(byt & 0b01000000) != 0 ? 1 : 0,
						(byt & 0b00100000) != 0 ? 1 : 0,
						(byt & 0b00010000) != 0 ? 1 : 0,
						(byt & 0b00001000) != 0 ? 1 : 0,
						(byt & 0b00000100) != 0 ? 1 : 0,
						(byt & 0b00000010) != 0 ? 1 : 0,
						(byt & 0b00000001) != 0 ? 1 : 0));
	}
}
