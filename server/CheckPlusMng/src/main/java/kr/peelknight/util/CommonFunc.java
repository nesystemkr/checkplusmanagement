package kr.peelknight.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.imgscalr.Scalr;

public class CommonFunc {
	private static int webIndex = 0;
	
	public synchronized static int getWebIndex(){
		return ++webIndex;
	}
	
	public static String getFileExt(String fileName) {
		String tmpExt = "";
		if (fileName.lastIndexOf(".") >= 0) {
			tmpExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		return tmpExt.toLowerCase();
	}
	
	public static int writeFile(InputStream is, String filePath, String fileName) throws Exception {
		return writeFile(is, new File(filePath), fileName);
	}
	
	public static int writeFile(InputStream is, File fileDir, String fileName) throws Exception {
		return writeFile(is, new File(fileDir, fileName));
	}
	
	public static int writeFile(InputStream is, File file) throws Exception {
		if (is == null) {
			return -1;
		}
		
		try {
			OutputStream out = null;
			int total = 0;
			int read = 0;
			byte[] bytes = new byte[8192];
			
			out = new FileOutputStream(file);
			while ((read = is.read(bytes)) != -1) {
				total += read;
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			
			CommonFunc.setFileReadable(file, false);
			
			return total;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String getStringFromObject(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}
	
	public static double getDoubleFromObject(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Integer) {
			return ((Integer)obj).doubleValue();
		} else if (obj instanceof Long) {
			return ((Long)obj).doubleValue();
		} else if (obj instanceof Double) {
			return ((Double)obj).doubleValue();
		}
		try {
			return Double.parseDouble((String)obj);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static long getLongFromObject(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Integer) {
			return ((Integer)obj).intValue();
		} else if (obj instanceof Long) {
			return ((Long)obj).longValue();
		} else if (obj instanceof Double) {
			return ((Double)obj).longValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal)obj).longValue();
		}
		try {
			return Long.parseLong((String)obj);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int getIntFromObject(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Integer) {
			return ((Integer)obj).intValue();
		} else if (obj instanceof Long) {
			return ((Long)obj).intValue();
		} else if (obj instanceof Double) {
			return ((Double)obj).intValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal)obj).intValue();
		}
		try {
			return Integer.parseInt((String)obj);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Date getDateFromObject(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Date) {
			return (Date)obj;
		} else {
			long timeVal = getLongFromObject(obj);
			Date retDt = new Date();
			retDt.setTime(timeVal);
			return retDt;
		}
	}
	
	public static boolean getBooleanFromObject(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Boolean) {
			return ((Boolean)obj).booleanValue();
		} else if (obj instanceof Integer) {
			return ((Integer)obj).intValue() == 1 ? true : false;
		} else if (obj instanceof Long) {
			return ((Long)obj).intValue() == 1 ? true : false;
		} else if (obj instanceof Double) {
			return ((Double)obj).intValue() == 1 ? true : false;
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal)obj).intValue() == 1 ? true : false;
		}
		try {
			return Boolean.parseBoolean((String)obj);
		} catch (Exception e) {
			return false;
		}
}
	
	public static String getNowTime() {
		Date now = new Date();
		long secondSince1970 = now.getTime() / 1000;
		return String.valueOf(secondSince1970);
	}
	
	public static List<?> getListFromObject(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof List) {
			return (List<?>)obj;
		} else {
			return null;
		}
	}
	
	public static String sha256(String input) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (digest != null) {
			digest.reset();
			try {
				return bin2hex(digest.digest(input.getBytes("utf-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String MD5(String input) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (digest != null) {
			digest.reset();
			try {
				return Base64.encodeToString(digest.digest(input.getBytes("utf-8")), Base64.NO_WRAP | Base64.URL_SAFE);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String sha256(String input, String key) {
		Mac sha256_HMAC;
		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			return bin2hex(sha256_HMAC.doFinal(input.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String bin2hex(byte[] data) {
		return String.format("%0" + (data.length*2) + "x", new BigInteger(1, data));
	}
	
	public static byte[] hex2bin(String data) {
		if (data.length() % 2 != 0) {
			return null;
		}
		int len = data.length() / 2;
		byte[] ret = new byte[len];
		for (int ii=0; ii < len; ii++) {
			
			ret[ii] = (byte)Integer.parseInt(data.substring(ii * 2, (ii + 1) * 2), 16);
		}
		return ret;
	}
	
	public static String getHashedPassword(String password, String userId) {
		String sSalt = CommonFunc.sha256(userId.toLowerCase());
		String sSalt2 = CommonFunc.sha256("SPOf-W&7_yf030xZcv5rTJWk3PBtc");
		String sPass = CommonFunc.sha256(password);
		String sSource = String.format("%s_%s_%s", sSalt, sPass, sSalt2);
		String sHashed = CommonFunc.sha256(sSource);
		return sHashed;
	}
	
	public static Date getAddedDate(Date date, int addDay) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (addDay != 0) {
			cal.add(Calendar.DATE, addDay);
		}
		return cal.getTime();
	}
	
	public static Date getDateFromString(String date, int addDay) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		if (date.length() == 6) {
			cal.set(2000 + Integer.parseInt(date.substring(0, 2)), Integer.parseInt(date.substring(2, 4)) - 1, Integer.parseInt(date.substring(4, 6)), 0, 0, 0);
		} else if (date.length() == 8) {
			cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)), 0, 0, 0);
		} else {
			return null;
		}
		if (addDay != 0) {
			cal.add(Calendar.DATE, addDay);
		}
		return cal.getTime();
	}
	
	public static Date getDateFromString(String date) {
		return getDateFromString(date, 0);
	}
	
	public static Date getDateTimeFromString(String date) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = Calendar.getInstance();
		if (date.length() == 6) {
			cal.set(2000 + Integer.parseInt(date.substring(0, 2)),
						   Integer.parseInt(date.substring(2, 4)) - 1,
						   Integer.parseInt(date.substring(4, 6)),
						   0, 0, 0);
		} else if (date.length() == 8) {
			cal.set(Integer.parseInt(date.substring(0, 4)),
					Integer.parseInt(date.substring(4, 6)) - 1,
					Integer.parseInt(date.substring(6, 8)),
					0, 0, 0);
		} else if (date.length() == 12) {
			cal.set(2000 + Integer.parseInt(date.substring(0, 2)),
						   Integer.parseInt(date.substring(2, 4)) - 1,
						   Integer.parseInt(date.substring(4, 6)),
						   Integer.parseInt(date.substring(6, 8)),
						   Integer.parseInt(date.substring(8, 10)),
						   Integer.parseInt(date.substring(10, 12)));
		} else if (date.length() == 14) {
			cal.set(Integer.parseInt(date.substring(0, 4)),
					Integer.parseInt(date.substring(4, 6)) - 1,
					Integer.parseInt(date.substring(6, 8)),
					Integer.parseInt(date.substring(8, 10)),
					Integer.parseInt(date.substring(10, 12)),
					Integer.parseInt(date.substring(12, 14)));
		} else if (date.length() == 19) {
			cal.set(Integer.parseInt(date.substring(0, 4)),
					Integer.parseInt(date.substring(5, 7)) - 1,
					Integer.parseInt(date.substring(8, 10)),
					Integer.parseInt(date.substring(11, 13)),
					Integer.parseInt(date.substring(14, 16)),
					Integer.parseInt(date.substring(17, 19)));
		} else {
			return null;
		}
		return cal.getTime();
	}
	
	public static String encKey(long userIdKey) {
		String ret = String.format("n!%de@s#y%d$s*t^e&m%d", userIdKey, userIdKey, userIdKey);
		if (ret.length() < 32) {
			ret += ret;
		}
		int start = (int)(userIdKey % 17);
		return ret.substring(start, start + 16);
	}
	
	public static String encrypt(String sPlain, String sKey) {
		try {
			Key aesKey = new SecretKeySpec(sKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encrypted = cipher.doFinal(sPlain.getBytes());
			return new String(Base64.encode(encrypted, Base64.NO_WRAP | Base64.URL_SAFE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(String sEncypted, String sKey) {
		try {
			Key aesKey = new SecretKeySpec(sKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] encrypted = Base64.decode(sEncypted, Base64.NO_WRAP | Base64.URL_SAFE);
			return new String(cipher.doFinal(encrypted));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage resizeImage(BufferedImage image, int maxSize) {
		int targetWidth = 0;
		int targetHeight = 0;
		if (image.getHeight() > image.getWidth()) {
			if (image.getHeight() > maxSize) {
				targetHeight = maxSize;
				targetWidth = Math.round((float)maxSize * image.getWidth() / image.getHeight());
			}
		} else {
			if (image.getWidth() > maxSize) {
				targetWidth = maxSize;
				targetHeight = Math.round((float)maxSize * image.getHeight() / image.getWidth());
			}
		}
		BufferedImage resized;
		if (targetWidth > 0 && targetHeight > 0) {
			resized = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, targetWidth, targetHeight , Scalr.OP_ANTIALIAS);
		} else {
			resized = image;
		}
		return resized;
	}
	
	public static BufferedImage thumbnailImage(BufferedImage image, int width, int height) {
		int fromR_left = 0;
		int fromR_top = 0;
		int fromR_right = 0;
		int fromR_bottom = 0;
		
		int toR_left = 0;
		int toR_top = 0;
		int toR_right = 0;
		int toR_bottom = 0;
		
		float ratio = width;
		ratio /= height;
		
		if (image.getHeight() >= (image.getWidth() / ratio)) {
			int nHeightTemp = Math.round(image.getWidth() / ratio);
			fromR_left = 0;
			fromR_right = image.getWidth();
			fromR_top = (image.getHeight() - nHeightTemp) / 2;
			fromR_bottom = fromR_top + nHeightTemp;
		} else {
			int nWidthTemp = Math.round(image.getHeight() * ratio);
			fromR_top = 0;
			fromR_bottom = image.getHeight();
			fromR_left = (image.getWidth() - nWidthTemp) / 2;
			fromR_right = fromR_left + nWidthTemp;
		}
		
		if ((fromR_bottom - fromR_top) > height || (fromR_right - fromR_left) > width) {
			toR_bottom = height;
			toR_right = width;
		} else {
			toR_bottom = fromR_bottom - fromR_top;
			toR_right = fromR_right - fromR_left;
		}
		
		BufferedImage cropped = Scalr.crop(image, fromR_left, fromR_top, fromR_right - fromR_left, fromR_bottom - fromR_top, Scalr.OP_ANTIALIAS);
		return Scalr.resize(cropped, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, toR_right - toR_left, toR_bottom - toR_top, Scalr.OP_ANTIALIAS);
	}

	public static BufferedImage thumbnailImage(BufferedImage image, int maxWidth, Point retSize) {
		int toR_right = 0;
		int toR_bottom = 0;
		
		BufferedImage ret;
		if (image.getWidth() > maxWidth) {
			toR_right = maxWidth;
			toR_bottom =  Math.round((float)toR_right * image.getHeight() / image.getWidth());
			ret = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, toR_right, toR_bottom, Scalr.OP_ANTIALIAS);
		} else {
			ret = image;
		}
		
		if (retSize != null) {
			retSize.x = ret.getWidth();
			retSize.y = ret.getHeight();
		}
		return ret;
	}
	
	public static BufferedImage circleImage(BufferedImage image, int diameter) {
		BufferedImage bitmap = thumbnailImage(image, diameter, diameter);
		BufferedImage createdImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		Area area = new Area(new Rectangle(0, 0, diameter, diameter));
		area.subtract(new Area(new Ellipse2D.Double(0, 0, diameter, diameter)));
		Graphics2D g = (Graphics2D)createdImage.getGraphics();
		g.drawImage(bitmap, 0, 0, null);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
//		g.setComposite(AlphaComposite.Clear);
		g.fill(area);
		g.dispose();
		return createdImage;
	}
	
	public static void writeImage(BufferedImage image, String filePath, Boolean isPNG) {
		File file = new File(filePath);
		writeImage(image, file, isPNG);
	}

	public static void writeImage(BufferedImage image, File file, Boolean isPNG) {
		try {
			BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			convertedImg.getGraphics().drawImage(image, 0, 0, null);
			convertedImg.getGraphics().dispose();
			ImageOutputStream ios =  ImageIO.createImageOutputStream(file);
			ImageWriter writer;
			if (isPNG.booleanValue()) {
				writer = ImageIO.getImageWritersByFormatName("png").next();
			} else {
				writer = ImageIO.getImageWritersByFormatName("jpeg").next();
			}
			ImageWriteParam param = writer.getDefaultWriteParam();
			if (isPNG.booleanValue()) {
				param.setCompressionQuality(1.0F); // Highest quality
			} else {
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // Needed see javadoc
				param.setCompressionQuality(0.85f);
			}
			writer.setOutput(ios);
			writer.write(convertedImg);
			writer.dispose();
			ios.close();
			setFileReadable(file, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setFileReadable(File file, boolean isDir) {
		setFileReadable(file.getAbsolutePath(), isDir);
	}
	
	public static void setFileReadable(String filePath, boolean isDir) {
		try {
			Set<PosixFilePermission> pfpSet = new HashSet<PosixFilePermission>();
			pfpSet.add(PosixFilePermission.OWNER_READ);
			pfpSet.add(PosixFilePermission.OWNER_WRITE);
			pfpSet.add(PosixFilePermission.GROUP_READ);
			pfpSet.add(PosixFilePermission.GROUP_WRITE);
			pfpSet.add(PosixFilePermission.OTHERS_READ);
			if (isDir == true) {
				pfpSet.add(PosixFilePermission.OWNER_EXECUTE);
				pfpSet.add(PosixFilePermission.GROUP_EXECUTE);
				pfpSet.add(PosixFilePermission.OTHERS_EXECUTE);
			}
			Files.setPosixFilePermissions(Paths.get(filePath), pfpSet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Double DoubleFromStr(String str) {
		try {
			return Double.valueOf(str);
		} catch (Exception e) {
			return Double.valueOf(0);
		}
	}
	
	public static double strToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0;
		}
	}

	public static long strToLong(String str) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return 0;
		}
	}
}
