package kr.nesystem.util;

import java.net.URI;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import kr.nesystem.model.Model;
import kr.nesystem.model.ModelHandler;

public class ResponseUtil {
	public static Response getResponse(Status status) {
		return ResponseUtil.getResponse(status, "{}", MediaType.APPLICATION_JSON, null);
	}

	public static Response getResponse(Object entity) {
		return ResponseUtil.getResponse(Status.OK, entity, MediaType.APPLICATION_JSON);
	}

	public static Response getResponse(Object entity, NewCookie cookie) {
		return ResponseUtil.getResponse(Status.OK, entity, MediaType.APPLICATION_JSON, cookie);
	}

	public static Response getResponse(Status status, Object entity) {
		return ResponseUtil.getResponse(status, entity, MediaType.APPLICATION_JSON);
	}

	public static Response getResponse(Status status, Object entity, String mediaType) {
		return ResponseUtil.getResponse(status, entity, mediaType, null);
	}

	public static Response getResponse(Status status, Object entity, String mediaType, NewCookie cookie) {
		ResponseBuilder builder = Response
				.status(status)
				.entity(entity)
				.type(mediaType)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true");
		if (cookie != null) {
			builder.cookie(cookie);
		}
		return builder.build();
	}
	
	public static Response getResponseForFile(Status status, Object entity, String fileName) {
		ResponseBuilder builder = Response
				.status(status)
				.entity(entity)
				.type(MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=" + fileName)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true");
		return builder.build();
	}
	
	public static Response getResponseForImage(Status status, Object entity, String fileName, String ext) {
		ResponseBuilder builder = Response
				.status(status)
				.entity(entity)
				.type("image/" + ext)
				.header("Content-Disposition", "attachment; filename=" + fileName)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true");
		return builder.build();
	}
	
	public static Response getResponseForPdf(Status status, Object entity, String fileName) {
		ResponseBuilder builder = Response
				.status(status)
				.entity(entity)
				.type("application/pdf")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Credentials", "true");
		return builder.build();
	}
	
	public static Response internalError(String errorMsg) {
		ModelHandler<Model> handler = new ModelHandler<Model>(Model.class);
		String json = errorMsg;
		try {
			Model entity = new Model();
			entity.setErrCd(-1);
			entity.setErrMsg(errorMsg);
			json = handler.convertToJson(entity);
		} catch (Exception e) {
		}
		return ResponseUtil.getResponse(Status.INTERNAL_SERVER_ERROR, json);
	}
	
	public static Response getResponseForReDirect(String url) {
		try {
			ResponseBuilder builder = Response
					.temporaryRedirect(new URI(url));
			return builder.build();
		} catch (Exception e) {
			return internalError(e.getMessage());
		}
	}
}
