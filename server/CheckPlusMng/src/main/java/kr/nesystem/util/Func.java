package kr.nesystem.util;

import java.math.BigDecimal;

public class Func {
	public static int toInt(Object obj) {
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
	
	public static boolean toBoolean(Object obj) {
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
}
