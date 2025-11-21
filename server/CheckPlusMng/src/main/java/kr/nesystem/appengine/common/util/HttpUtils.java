package kr.nesystem.appengine.common.util;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;

public class HttpUtils {
	public static String invokeHttpRequest(URL endpointUrl, String httpMethod, Map<String, String> headers, String requestBody, String encoding) {
		HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, headers);
		try {
			if (requestBody != null) {
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes(requestBody);
				wr.flush();
				wr.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Request failed. " + e.getMessage(), e);
		}
		return executeHttpRequest(connection, encoding);
	}
	
	public static String invokeHttpRequest(URL endpointUrl, String httpMethod, Map<String, String> headers, byte[] requestBody, String encoding) {
		HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, headers);
		try {
			if (requestBody != null) {
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(requestBody);
				wr.flush();
				wr.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Request failed. " + e.getMessage(), e);
		}
		return executeHttpRequest(connection, encoding);
	}
	
	public static byte[] invokeHttpRequestByte(URL endpointUrl, String httpMethod, Map<String, String> headers, byte[] requestBody) {
		HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, headers);
		try {
			if (requestBody != null) {
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.write(requestBody);
				wr.flush();
				wr.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Request failed. " + e.getMessage(), e);
		}
		return executeHttpRequestByte(connection);
	}
	
	public static byte[] executeHttpRequestByte(HttpURLConnection connection) {
		try {
			// Get Response
			InputStream is;
			try {
				is = connection.getInputStream();
			} catch (IOException e) {
				is = connection.getErrorStream();
			}
			
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(0);
			int nRead = 0;
			byte[] readData = new byte[8192];
			while ((nRead = bis.read(readData)) > 0) {
				baf.append(readData, 0, nRead);
			}
			is.close();
			return baf.buffer();
		} catch (Exception e) {
			throw new RuntimeException("Request failed. " + e.getMessage(), e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public static String executeHttpRequest(HttpURLConnection connection, String encoding) {
		InputStream is = null;
		try {
			// Get Response
			try {
				is = connection.getInputStream();
			} catch (IOException e) {
				is = connection.getErrorStream();
			}
			
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(0);
			int nRead = 0;
			byte[] readData = new byte[8192];
			while ((nRead = bis.read(readData)) > 0) {
				baf.append(readData, 0, nRead);
			}
			is.close();
			return new String(baf.buffer(), encoding);
		} catch (Exception e) {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ie) {
				}
			}
			throw new RuntimeException("Request failed. " + e.getMessage(), e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public static HttpURLConnection createHttpConnection(URL endpointUrl, String httpMethod, Map<String, String> headers) {
		System.setProperty("jsse.enableSNIExtension", "false");
		try {
			HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
			connection.setRequestMethod(httpMethod);
			if (headers != null) {
//				System.out.println("--------- Request headers ---------");
				for (String headerKey : headers.keySet()) {
//					System.out.println(headerKey + ": " + headers.get(headerKey));
					connection.setRequestProperty(headerKey, headers.get(headerKey));
				}
			}
			
			connection.setUseCaches(false);
			connection.setDoInput(true);
			if (!"GET".equals(httpMethod) && !"DELETE".equals(httpMethod)) {
				connection.setDoOutput(true);
			}
			return connection;
		} catch (Exception e) {
			throw new RuntimeException("Cannot create connection. " + e.getMessage(), e);
		}
	}
	
	public static String urlEncode(String url, boolean keepPathSlash) {
		String encoded;
		try {
			encoded = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 encoding is not supported.", e);
		}
		if (keepPathSlash) {
			encoded = encoded.replace("%2F", "/");
		}
		return encoded;
	}
}
