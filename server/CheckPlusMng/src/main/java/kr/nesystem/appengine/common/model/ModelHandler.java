package kr.nesystem.appengine.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelHandler<T> {
	private final Class<T> modelClass;
	
	public ModelHandler(Class<T> modelClass) {
		this.modelClass = modelClass;
	}
	
	public T convertFromJson(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		return mapper.readValue(json, modelClass);
	}
	
	public String convertToJson(T data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(data);
	}
	
	public CM_PagingList<T> convertPagingFromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
			JavaType type = mapper.getTypeFactory().constructParametricType(CM_PagingList.class, this.modelClass);
			return mapper.readValue(json, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<T> convertListFromJson(String json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, this.modelClass);
			return mapper.readValue(json, type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String convertListToJson(List<T> list) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
			return mapper.writeValueAsString(list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
