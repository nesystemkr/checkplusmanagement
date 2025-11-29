package kr.nesystem.hello;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import com.google.appengine.api.utils.SystemProperty;

@WebServlet(name = "HelloCheckPlus", value = "/hello")
public class HelloCheckPlus extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Properties properties = System.getProperties();

		response.setContentType("text/plain");
		response.getWriter().println("Hello App Engine - Standard using "
				+ SystemProperty.version.get() + " Java "
				+ properties.get("java.specification.version"));
	}
}
