package kr.nesystem.appengine.messagequeue.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.util.Kafka;
import kr.nesystem.appengine.messagequeue.model.MQ_EMail;

public class EMail {
	public static void pushEmailToGMail(String to, String title, String content) {
		MQ_EMail email = new MQ_EMail();
		email.setTo(to);
		email.setTitle(title);
		email.setContent(content);
		pushEmailToGMail(email);
	}
	
	public static void pushEmailToSendMail(String to, String title, String content) {
		MQ_EMail email = new MQ_EMail();
		email.setTo(to);
		email.setTitle(title);
		email.setContent(content);
		pushEmailToSendMail(email);
	}
	
	public static void pushEmailToGMail(MQ_EMail item) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StringBuilder sbKey = new StringBuilder("");
		sbKey.append(item.getTo()).append("_").append(sdf.format(new Date()));
		ObjectMapper mapper = new ObjectMapper();
		try {
			Kafka.push("gmails", sbKey.toString(), mapper.writeValueAsString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pushEmailToSendMail(MQ_EMail item) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		StringBuilder sbKey = new StringBuilder("");
		sbKey.append(item.getTo()).append("_").append(sdf.format(new Date()));
		ObjectMapper mapper = new ObjectMapper();
		try {
			Kafka.push("emails", sbKey.toString(), mapper.writeValueAsString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmailWithGMail(String toEMail, String title, String content) {
		final String username = Constant.GMAIL_SENDER;
		final String password = Constant.GMAIL_PASSWORD;
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS
		
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("onejunkim@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEMail));
			message.setSubject(title);
			message.setText(content);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmailWithHTMLNGMail(String toEMail, String title, String content) {
		final String username = Constant.GMAIL_SENDER;
		final String password = Constant.GMAIL_PASSWORD;
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS
		
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("onejunkim@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEMail));
			message.setSubject(title);
			message.setDataHandler(new DataHandler(new HTMLDataSource(content)));
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	static class HTMLDataSource implements DataSource {
		private String html;
		
		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			if (html == null) {
				throw new IOException("html message is null!");
			}
			return new ByteArrayInputStream(html.getBytes());
		}
		
		@Override
		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}
		
		@Override
		public String getContentType() {
			return "text/html";
		}
		
		@Override
		public String getName() {
			return "HTMLDataSource";
		}
	}
	
	public static void sendEmailSSLWithGMail(String toEMail, String title, String content) {
		final String username = Constant.GMAIL_SENDER;
		final String password = Constant.GMAIL_PASSWORD;
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.starttls.enable","true");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("onejunkim@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEMail));
			message.setSubject(title);
			message.setText(content);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmailWithSendMail(String toEMail, String title, String content) {
		final String from = "lucidcore.mail@gmail.com";
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "127.0.0.1");
		Session session = Session.getDefaultInstance(prop);

		try {
			Message message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
			message.setFrom(new InternetAddress(from));
			if ("admin".equals(toEMail)) {
				toEMail = "onejunkim@gmail.com";
			}
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEMail));
			message.setSubject(title);
			message.setText(content);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
