package kr.nesystem.appengine.common.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import kr.nesystem.appengine.common.dao.L10NDao;
import kr.nesystem.appengine.common.model.CM_L10N;
import kr.nesystem.appengine.common.model.CM_PagingList;

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
			CM_PagingList<CM_L10N> l10ns = dao.selectL10Ns(null, -1, 0, null);
			for (int ii=0; ii<l10ns.getList().size(); ii++) {
				defaultStrings.put(l10ns.getList().get(ii).getIdString(), l10ns.getList().get(ii).getDefaultString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
		if (session != null) {
			return (String)session.getAttribute("Locale");
		}
		return null;
	}
}
