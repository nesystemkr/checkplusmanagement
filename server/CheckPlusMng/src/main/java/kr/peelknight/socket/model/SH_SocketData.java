package kr.peelknight.socket.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class SH_SocketData {
	public static void putLongR(byte[] arrData, int pos, long value) {
		byte[] tmp = ByteBuffer.allocate(8).putLong(value).array();
		arrData[pos] = tmp[7];
		arrData[pos + 1] = tmp[6];
		arrData[pos + 2] = tmp[5];
		arrData[pos + 3] = tmp[4];
		arrData[pos + 4] = tmp[3];
		arrData[pos + 5] = tmp[2];
		arrData[pos + 6] = tmp[1];
		arrData[pos + 7] = tmp[0];
	}

	public static void putLong(byte[] arrData, int pos, long value) {
		byte[] tmp = ByteBuffer.allocate(8).putLong(value).array();
		arrData[pos] = tmp[0];
		arrData[pos + 1] = tmp[1];
		arrData[pos + 2] = tmp[2];
		arrData[pos + 3] = tmp[3];
		arrData[pos + 4] = tmp[4];
		arrData[pos + 5] = tmp[5];
		arrData[pos + 6] = tmp[6];
		arrData[pos + 7] = tmp[7];
	}

	public static void putIntR(byte[] arrData, int pos, int value) {
		byte[] tmp = ByteBuffer.allocate(4).putInt(value).array();
		arrData[pos] = tmp[3];
		arrData[pos + 1] = tmp[2];
		arrData[pos + 2] = tmp[1];
		arrData[pos + 3] = tmp[0];
	}

	public static void putInt(byte[] arrData, int pos, int value) {
		byte[] tmp = ByteBuffer.allocate(4).putInt(value).array();
		arrData[pos] = tmp[0];
		arrData[pos + 1] = tmp[1];
		arrData[pos + 2] = tmp[2];
		arrData[pos + 3] = tmp[3];
	}

	public static void putShortR(byte[] arrData, int pos, short value) {
		byte[] tmp = ByteBuffer.allocate(2).putShort(value).array();
		arrData[pos] = tmp[1];
		arrData[pos + 1] = tmp[0];
	}

	public static void putShort(byte[] arrData, int pos, short value) {
		byte[] tmp = ByteBuffer.allocate(2).putShort(value).array();
		arrData[pos] = tmp[0];
		arrData[pos + 1] = tmp[1];
	}

	public static void putString(byte[] arrData, int pos, String value) {
		putString(arrData, pos, value, "euc-kr");
	}

	public static void putString(byte[] arrData, int pos, String value, String encoding) {
		try {
			byte[] tmp = value.getBytes(encoding);
			putBytes(arrData, pos, tmp, tmp.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void putString(byte[] arrData, int pos, String value, int len, String encoding) {
		try {
			byte[] tmp = value.getBytes(encoding);
			if (tmp.length > len) {
				putBytes(arrData, pos, tmp, len);
			} else {
				putBytes(arrData, pos, tmp, tmp.length);
				for (int ii=0; ii<len - tmp.length; ii++) {
					arrData[ii + pos + tmp.length] = ' ';
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void putBytes(byte[] arrData, int pos, byte[] value, int len) {
		System.arraycopy(value, 0, arrData, pos, len);
	}
	
	public static void putByte(byte[] arrData, int pos, byte value) {
		arrData[pos] = value;
	}

	public static void putDoubleR(byte[] arrData, int pos, double value) {
		byte[] tmp = ByteBuffer.allocate(8).putDouble(value).array();
		arrData[pos] = tmp[7];
		arrData[pos + 1] = tmp[6];
		arrData[pos + 2] = tmp[5];
		arrData[pos + 3] = tmp[4];
		arrData[pos + 4] = tmp[3];
		arrData[pos + 5] = tmp[2];
		arrData[pos + 6] = tmp[1];
		arrData[pos + 7] = tmp[0];
	}

	public static void putDouble(byte[] arrData, int pos, double value) {
		byte[] tmp = ByteBuffer.allocate(8).putDouble(value).array();
		arrData[pos] = tmp[0];
		arrData[pos + 1] = tmp[1];
		arrData[pos + 2] = tmp[2];
		arrData[pos + 3] = tmp[3];
		arrData[pos + 4] = tmp[4];
		arrData[pos + 5] = tmp[5];
		arrData[pos + 6] = tmp[6];
		arrData[pos + 7] = tmp[7];
	}

	public static void putFloatR(byte[] arrData, int pos, float value) {
		byte[] tmp = ByteBuffer.allocate(4).putFloat(value).array();
		arrData[pos] = tmp[3];
		arrData[pos + 1] = tmp[2];
		arrData[pos + 2] = tmp[1];
		arrData[pos + 3] = tmp[0];
	}

	public static void putFloat(byte[] arrData, int pos, float value) {
		byte[] tmp = ByteBuffer.allocate(4).putFloat(value).array();
		arrData[pos] = tmp[0];
		arrData[pos + 1] = tmp[1];
		arrData[pos + 2] = tmp[2];
		arrData[pos + 3] = tmp[3];
	}

	public static long readLong(byte[] arrData, int pos) {
		if (arrData.length >= pos + 8) {
			return ByteBuffer.wrap(arrData, pos, 8).getLong();
		} else {
			return 0;
		}
	}

	public static long readLongR(byte[] arrData, int pos) {
		if (arrData.length >= pos + 8) {
			byte[] tmp = new byte[8];
			tmp[0] = arrData[pos + 7];
			tmp[1] = arrData[pos + 6];
			tmp[2] = arrData[pos + 5];
			tmp[3] = arrData[pos + 4];
			tmp[4] = arrData[pos + 3];
			tmp[5] = arrData[pos + 2];
			tmp[6] = arrData[pos + 1];
			tmp[7] = arrData[pos];
			return ByteBuffer.wrap(tmp, 0, 8).getLong();
		} else {
			return 0;
		}
	}

	public static int readInt(byte[] arrData, int pos) {
		if (arrData.length >= pos + 4) {
			return ByteBuffer.wrap(arrData, pos, 4).getInt();
		} else {
			return 0;
		}
	}

	public static int readIntR(byte[] arrData, int pos) {
		if (arrData.length >= pos + 4) {
			byte[] tmp = new byte[4];
			tmp[0] = arrData[pos + 3];
			tmp[1] = arrData[pos + 2];
			tmp[2] = arrData[pos + 1];
			tmp[3] = arrData[pos];
			return ByteBuffer.wrap(tmp, 0, 4).getInt();
		} else {
			return 0;
		}
	}

	public static short readShort(byte[] arrData, int pos) {
		if (arrData.length >= pos + 2) {
			return ByteBuffer.wrap(arrData, pos, 2).getShort();
		} else {
			return 0;
		}
	}

	public static short readShortR(byte[] arrData, int pos) {
		if (arrData.length >= pos + 2) {
			byte[] tmp = new byte[2];
			tmp[0] = arrData[pos + 1];
			tmp[1] = arrData[pos];
			return ByteBuffer.wrap(tmp, 0, 2).getShort();
		} else {
			return 0;
		}
	}
	
	public static String readString(byte[] arrData, int pos, int len) {
		return readString(arrData, pos, len, "UTF-8");
	}
	
	public static String readString(byte[] arrData, int pos, int len, String encoding) {
		try {
			if (arrData.length >= pos + len) {
				return new String(arrData, pos, len, encoding).trim();
			} else {
				return "";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void readBytes(byte[] arrData, int pos, byte[] target) {
		int copyLen = target.length;
		if (arrData.length - pos < copyLen) {
			copyLen = arrData.length - pos;
		}
		System.arraycopy(arrData, pos, target, 0, copyLen);
	}
	
	public static byte readByte(byte[] arrData, int pos) {
		if (arrData.length >= pos) {
			return arrData[pos];
		} else {
			return 0;
		}
	}
	
	public static double readDouble(byte[] arrData, int pos) {
		if (arrData.length >= pos + 8) {
			return ByteBuffer.wrap(arrData, pos, 8).getDouble();
		} else {
			return 0;
		}
	}

	public static double readDoubleR(byte[] arrData, int pos) {
		if (arrData.length >= pos + 8) {
			byte[] tmp = new byte[8];
			tmp[0] = arrData[pos + 7];
			tmp[1] = arrData[pos + 6];
			tmp[2] = arrData[pos + 5];
			tmp[3] = arrData[pos + 4];
			tmp[4] = arrData[pos + 3];
			tmp[5] = arrData[pos + 2];
			tmp[6] = arrData[pos + 1];
			tmp[7] = arrData[pos];
			return ByteBuffer.wrap(tmp, 0, 8).getDouble();
		} else {
			return 0;
		}
	}

	public static float readFloat(byte[] arrData, int pos) {
		if (arrData.length >= pos + 4) {
			return ByteBuffer.wrap(arrData, pos, 4).getFloat();
		} else {
			return 0;
		}
	}

	public static float readFloatR(byte[] arrData, int pos) {
		if (arrData.length >= pos + 4) {
			byte[] tmp = new byte[4];
			tmp[0] = arrData[pos + 3];
			tmp[1] = arrData[pos + 2];
			tmp[2] = arrData[pos + 1];
			tmp[3] = arrData[pos];
			return ByteBuffer.wrap(tmp, 0, 4).getFloat();
		} else {
			return 0;
		}
	}
}
