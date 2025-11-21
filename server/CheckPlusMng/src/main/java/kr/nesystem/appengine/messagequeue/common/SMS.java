package kr.nesystem.appengine.messagequeue.common;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.util.HttpUtils;
import kr.nesystem.appengine.common.util.Kafka;
import kr.nesystem.appengine.messagequeue.model.MQ_SMS;

public class SMS {
	public static void pushSMS(MQ_SMS item) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StringBuilder sbKey = new StringBuilder("");
		sbKey.append(item.getTarget()).append("_").append(sdf.format(new Date()));
		ObjectMapper mapper = new ObjectMapper();
		try {
			Kafka.push("gmails", sbKey.toString(), mapper.writeValueAsString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String sendSMS(String target, String title, String content) {
//		String mediaType = MediaType.APPLICATION_JSON;
//		int httpCode = Status.OK.getStatusCode();
		String resultMsg = "";
		URL endpointUrl;
		try {
			endpointUrl = new URL("http://agent1.kssms.kr/proc/RemoteSms.html");
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to parse service endpoint: " + e.getMessage());
		}
		
		StringBuilder postData = new StringBuilder("");
		postData.append("id=").append(Constant.SMS_SERVERID).append("&");
		postData.append("pass=").append(Constant.SMS_SERVERPASSWROD).append("&");
		postData.append("type=sms&");
		postData.append("reservetime=&");
		postData.append("reserve_chk=&");
		postData.append("phone=").append(target).append("&");
		postData.append("callback=").append(Constant.SMS_SERVERSENDER).append("&");
		postData.append("msg=").append(content).append("&");
		postData.append("upfile=&");
		postData.append("subject=").append(title).append("&");
		postData.append("scripttype=utf8&");
		postData.append("etc1=&");
		postData.append("etc2=&");
		try {
			resultMsg = HttpUtils.invokeHttpRequest(endpointUrl, "POST", null, postData.toString().getBytes("UTF-8"), "euc-kr");
		} catch (UnsupportedEncodingException e) {
			resultMsg = e.getMessage();
			e.printStackTrace();
		}
		return resultMsg;
	}
}
