package kr.nesystem.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.ibatis.io.Resources;

public class Constant {
	public static int DEFAULT_SIZE = 10;
	public static String ATTACHMENT_BASE_DIR = "/var/attachments";
	public static String LOGININFO_IMPL_CLASS = "kr.peelknight.common.impl.LoginInfoImplement";
	public static boolean MODE_OFFLINE = false;
	public static String FCM_PUSH_PROJECTID = "rentalwelderandroid";
	public static String FCM_PUSH_CREDENTIALS_PATH = "/var/lib/tomcat/webapps/ROOT/WEB-INF/classes/fcmcredentials.json";
	public static boolean APNS_PUSH_DEV = false;
	public static String APNS_PUSH_TEAMID = "VE554S57ZZ";
	public static String APNS_PUSH_KEYID = "JD7MZ5734P";
	public static String APNS_PUSH_P8_PATH = "/var/lib/tomcat/webapps/ROOT/WEB-INF/classes/apnstokenkey.p8";
	public static String APNS_PUSH_APP_BUNDLEID = "kr.peelknight.RentalWelder";
	public static String GMAIL_SENDER = "";
	public static String GMAIL_PASSWORD = "";
	public static String SMS_SERVERID = "";
	public static String SMS_SERVERPASSWROD = "";
	public static String SMS_SERVERSENDER = "";
	public static boolean FRAGMENT_CACHED = false;
	static {
		Properties prop = new Properties();
		try {
			prop.load(Resources.getResourceAsStream("server.properties"));
			DEFAULT_SIZE = Func.toInt(prop.getProperty("DefaultSize"));
			ATTACHMENT_BASE_DIR = prop.getProperty("AttachmentBaseDir");
			LOGININFO_IMPL_CLASS = prop.getProperty("LoginInfoImplClass");
			MODE_OFFLINE = Func.toBoolean(prop.getProperty("ModeOffline"));
			FCM_PUSH_PROJECTID = prop.getProperty("FcmPushProjectId");
			FCM_PUSH_CREDENTIALS_PATH = prop.getProperty("FcmPushCredentialsPath");
			APNS_PUSH_DEV = Func.toBoolean(prop.getProperty("ApnsPushDev"));
			APNS_PUSH_TEAMID = prop.getProperty("ApnsPushTeamId");
			APNS_PUSH_KEYID = prop.getProperty("ApnsPushKeyId");
			APNS_PUSH_P8_PATH = prop.getProperty("ApnsPushP8Path");
			APNS_PUSH_APP_BUNDLEID = prop.getProperty("ApnsPushAppBundleId");
			GMAIL_SENDER = prop.getProperty("GMailSender");
			GMAIL_PASSWORD = prop.getProperty("GMailPassword");
			SMS_SERVERID = prop.getProperty("SMSServerId");
			SMS_SERVERPASSWROD = prop.getProperty("SMSServerPassword");
			SMS_SERVERSENDER = prop.getProperty("SMSServerSender");
			FRAGMENT_CACHED = Func.toBoolean(prop.getProperty("FragmentCached"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
}
