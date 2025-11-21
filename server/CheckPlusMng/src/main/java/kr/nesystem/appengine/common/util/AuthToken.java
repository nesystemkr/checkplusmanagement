package kr.nesystem.appengine.common.util;

import java.util.Date;

public class AuthToken {
	protected static String keyenc = "_7eElkN!ghT5aRAm";
	public static String getToken(long idKey, long expireDateTime, String userType, long lastSeq) {
		String origin = String.format("%s=%s=%s=%s",
								String.valueOf(idKey),
								String.valueOf(expireDateTime),
								userType,
								String.valueOf(lastSeq));
		return CommonFunc.encrypt(origin, keyenc);
	}
	
	public static boolean isAdmin(String token) {
		return isAdmin(token, null);
	}
	
	public static boolean isAdmin(String token, String token2) {
		String userType = getUserType(token, token2);
		if ("1".equals(userType) || "2".equals(userType)) {
			return true;
		}
		return false;
	}
	
	public static String getUserType(String token) {
		return AuthToken.getUserType(token, null);
	}
	
	public static String getUserType(String token, String token2) {
		if (token == null && token2 == null) {
			return "";
		}
		if ("null".equals(token) || "null".equals(token2) ||
			"undefined".equals(token) || "undefined".equals(token2)) {
			return "";
		}
		try {
			String userType;
			String decrypto = "";
			if (token != null) {
				decrypto = new String(CommonFunc.decrypt(token, keyenc));
			}
			if (token2 != null) {
				decrypto = new String(CommonFunc.decrypt(token2, keyenc));
			}
			String fields[] = decrypto.split("=");
			if (fields.length < 3) {
				return "";
			}
			userType = fields[2];
			return userType;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return "";
		}
	}
	
	public static long getIdKey(String token) {
		return AuthToken.getIdKey(token, null);
	}
	
	public static long getIdKey(String token, String token2) {
		if (token == null && token2 == null) {
			return -1;
		}
		if ("null".equals(token) || "null".equals(token2) ||
			"undefined".equals(token) || "undefined".equals(token2)) {
			return -1;
		}
		try {
			long idKey = 0;
			String decrypto = "";
			if (token != null) {
				decrypto = new String(CommonFunc.decrypt(token, keyenc));
			}
			if (token2 != null) {
				decrypto = new String(CommonFunc.decrypt(token2, keyenc));
			}
			String fields[] = decrypto.split("=");
			idKey = Long.parseLong(fields[0]);
			return idKey;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return -1;
		}
	}
	
	public static long getLastSeq(String token) {
		return AuthToken.getLastSeq(token, null);
	}
	
	public static long getLastSeq(String token, String token2) {
		if (token == null && token2 == null) {
			return -1;
		}
		if ("null".equals(token) || "null".equals(token2) ||
			"undefined".equals(token) || "undefined".equals(token2)) {
			return -1;
		}
		try {
			long lastSeq;
			String decrypto = "";
			if (token != null) {
				decrypto = new String(CommonFunc.decrypt(token, keyenc));
			}
			if (token2 != null) {
				decrypto = new String(CommonFunc.decrypt(token2, keyenc));
			}
			String fields[] = decrypto.split("=");
			if (fields.length < 4) {
				return -1;
			}
			lastSeq = Long.parseLong(fields[3]);
			return lastSeq;
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return -1;
		}
	}
	
	public static Date getExpireDate(String token) {
		return AuthToken.getExpireDate(token, null);
	}
	
	public static Date getExpireDate(String token, String token2) {
		if (token == null && token2 == null) {
			return null;
		}
		if ("null".equals(token) || "null".equals(token2) ||
			"undefined".equals(token) || "undefined".equals(token2)) {
			return null;
		}
		try {
			String decrypto = "";
			if (token != null) {
				decrypto = new String(CommonFunc.decrypt(token, keyenc));
			}
			if (token2 != null) {
				decrypto = new String(CommonFunc.decrypt(token2, keyenc));
			}
			String fields[] = decrypto.split("=");
			if (fields.length < 2) {
				return null;
			}
			long expireDate = Long.parseLong(fields[1]);
			return new Date(expireDate);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return null;
		}
	}
	
	
	public static boolean isValidToken(String token) {
		return AuthToken.isValidToken(token, null);
	}
	
	public static boolean isValidToken(String token, boolean checkLastSeq) {
		return AuthToken.isValidToken(token, null, checkLastSeq);
	}
	
	public static boolean isValidToken(String token, String token2) {
		return AuthToken.isValidToken(token, token2, true);
	}
	
	//private static UserDao userDao = null;
	public static boolean isValidToken(String token, String token2, boolean checkLastSeq) {
		if (token == null && token2 == null) {
			return false;
		}
		if ("null".equals(token) || "null".equals(token2) ||
			"undefined".equals(token) || "undefined".equals(token2)) {
			return false;
		}
		String decrypto = "";
		if (token != null) {
			decrypto = new String(CommonFunc.decrypt(token, keyenc));
		}
		if (token2 != null) {
			decrypto = new String(CommonFunc.decrypt(token2, keyenc));
		}
		String fields[] = decrypto.split("=");
		if (fields == null || (fields.length != 4 && fields.length != 3)) {
			return false;
		}
		
		// 만기일 체크
		long expireDate = Long.parseLong(fields[1]);
		Date currentDate = new Date();
		if (expireDate < currentDate.getTime()) {
			return false;
		}
		
//FIXME 중복로그인 일단 막음
//		if (checkLastSeq == true) {
//			// 최종일련번호 체크
//			if (userDao == null) {
//				userDao = new UserDao();
//			}
//			CM_UserSumup userSumup = userDao.selectUserSumupByIdKey(Long.parseLong(fields[0]));
//			if (userSumup.getLastLoginSeq() != Long.parseLong(fields[3])) {
//				return false;
//			}
//		}
//FIXME 중복로그인 일단 막음
		return true;
	}
}
