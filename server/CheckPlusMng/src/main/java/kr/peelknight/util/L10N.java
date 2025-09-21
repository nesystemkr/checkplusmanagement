package kr.peelknight.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

import kr.peelknight.common.dao.CodeDao;
import kr.peelknight.common.dao.L10NDao;
import kr.peelknight.common.model.CM_Code;
import kr.peelknight.common.model.CM_L10N;
import kr.peelknight.common.model.CM_L10NLocale;

public class L10N implements Serializable {
	private static final long serialVersionUID = 1L; 
	static Map<String, Map<String, String>> localeStringsMap;
	static Map<String, String> defaultStrings;
	static {
		loadResource();
	};
	
	public static void loadResource() {
		localeStringsMap = new HashMap<>();
		defaultStrings = new HashMap<>();
		
		try {
			L10NDao dao = new L10NDao();
			CodeDao codeDao = new CodeDao();
			
			List<CM_L10N> l10ns = dao.selectL10Ns(-1, 0, null);
			for (int ii=0; ii<l10ns.size(); ii++) {
				defaultStrings.put(l10ns.get(ii).getIdString(), l10ns.get(ii).getDefaultString());
			}
			List<CM_Code> locales = codeDao.selectCodeByType("LOCALE");
			for (int ii=0; ii<locales.size(); ii++) {
				Map<String, String> localeStrings = new HashMap<>();
				List<CM_L10NLocale> l10nLocales = dao.selectL10NLocales(null, locales.get(ii).getCode(), -1, 0);
				for (int jj=0; jj<l10nLocales.size(); jj++) {
					localeStrings.put(l10nLocales.get(jj).getIdString(), l10nLocales.get(jj).getLocaleString());
				}
				localeStringsMap.put(locales.get(ii).getCode(), localeStrings);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void loadResourceFromPropertiess() {
//		String propfile = "lang.properties";
//		String propfile_en = "lang_en.properties";
//		InputStream inputStream = null;
//		
//		try {
//			inputStream = Resources.getResourceAsStream(propfile);
//			Properties prop = new Properties();
//			prop.load(inputStream);
//			Set<Object> keys = prop.keySet();
//			for (Object key : keys) {
//				String val = prop.getProperty((String)key);
//				resource_ko.put((String)key, new String(val.getBytes("ISO-8859-1"), "UTF-8"));
//			}
//			inputStream = Resources.getResourceAsStream(propfile_en);
//			prop = new Properties();
//			prop.load(inputStream);
//			keys = prop.keySet();
//			for (Object key : keys) {
//				String val = prop.getProperty((String)key);
//				resource_en.put((String)key, new String(val.getBytes("ISO-8859-1"), "UTF-8"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	private String locale;
	
	public L10N(HttpSession session) {
		locale = getLang(session);
	}
	
	public String get(String idString) {
		return L10N.get(idString, locale);
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public static String get(String idString, HttpSession session) {
		return get(idString, getLang(session));
	}

	public static String get(String idString, String locale) {
		if (locale != null) {
			Map<String, String> localeStrings = localeStringsMap.get(locale);
			if (localeStrings != null) {
				String localeString = localeStrings.get(idString);
				if (localeString != null) {
					return localeString;
				}
			}
		}
		String defaultString = defaultStrings.get(idString);
		if (defaultString != null) {
			return defaultString;
		} else {
			return idString;
		}
	}
	
	public static String getLang(HttpSession session) {
		return (String)session.getAttribute("Locale");
	}
}
