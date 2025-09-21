package kr.peelknight.socket.model;

import kr.peelknight.socket.context.SocketServerDaemon;

public class SH_WriteData extends SH_SocketData {
	@SuppressWarnings("unused")
	private int __dataLenType; //0: 전체길이 + 데이터, 1: 데이터길이 + 데이터, 2: 고정길이
	@SuppressWarnings("unused")
	private int __fixDataLen;
	private int __lenByteType; //0: 문자열, 1:byte, 2:short, 3:short reverse, 4:int short, 5:int reverse, 6:long, 7:long reverse
	private int __lenByteSize;

	public SH_WriteData(int dataLenType, int fixDataLen, int lenByteType, int lenByteSize) {
		__dataLenType = dataLenType;
		__fixDataLen = fixDataLen;
		__lenByteType = lenByteType;
		__lenByteSize = lenByteSize;
	}
	
	public byte[] getDataLengthBytes(long dataLen) {
		byte[] ret = null;
		if (__lenByteType == SocketServerDaemon.LENBYTETYPE_STRING) {
			ret = new byte[__lenByteSize];
			byte[] tmp =  String.valueOf(dataLen).getBytes();
			if (tmp.length > ret.length) {
				for (int ii=0; ii<ret.length; ii++) {
					ret[ii] = '9';
				}
			} else {
				int idx = 0;
				for (int ii=0; ii<ret.length; ii++) {
					if ((ret.length - ii) > tmp.length) {
						ret[ii] = '0';
					} else {
						ret[ii] = tmp[idx];
						idx++;
					}
				}
			}
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_BYTE) {
			ret = new byte[1];
			putByte(ret, 0, (byte)dataLen);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_SHORT) {
			ret = new byte[2];
			putShort(ret, 0, (short)dataLen);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_SHORTR) {
			ret = new byte[2];
			putShortR(ret, 0, (short)dataLen);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_INT) {
			ret = new byte[4];
			putInt(ret, 0, (int)dataLen);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_INTR) {
			ret = new byte[4];
			putIntR(ret, 0, (int)dataLen);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_LONG) {
			ret = new byte[8];
			putLong(ret, 0, dataLen);
		} else if (__lenByteType == SocketServerDaemon.LENBYTETYPE_LONGR) {
			ret = new byte[8];
			putLongR(ret, 0, dataLen);
		}
		return ret;
	}
}
