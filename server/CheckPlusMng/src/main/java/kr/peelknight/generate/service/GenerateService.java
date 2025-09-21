package kr.peelknight.generate.service;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.generate.dao.GenerateDao;
import kr.peelknight.generate.model.GN_GenColInfo;
import kr.peelknight.generate.model.GN_Generate;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/generate")
public class GenerateService {
	GenerateDao dao = new GenerateDao();
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON})
	@Path("/bytable")
	public Response bytable(GN_Generate generate) {
		try {
			String classBaseName = removePrefix(generate.getTableName(), generate.getPrefix());
			String classVarName = toFirstLowerCase(classBaseName);
			String databaseName = generate.getDatabaseName();
			String tableName = generate.getTableName();
			String packageName = generate.getPackageName();
			List<GN_GenColInfo> listCols = dao.getColInfos(databaseName, tableName);
			if (listCols != null) {
				generate.setModel(generateModel(packageName, tableName, listCols));
				generate.setXml(generateXml(packageName, classBaseName, tableName, listCols));
				generate.setDao(generateDao(packageName, classBaseName, tableName, classVarName));
				generate.setService(generateService(packageName, classBaseName, tableName, classVarName));
				generate.setJsp(generateJSP(classBaseName));
			}
			return ResponseUtil.getResponse((new ModelHandler<GN_Generate>(GN_Generate.class)).convertToJson(generate));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	public String generateModel(String packageName, String className, List<GN_GenColInfo> listCols) {
		boolean isDateExist = false;
		for (int ii=0; ii<listCols.size(); ii++) {
			if ("Date".equals(listCols.get(ii).columnType)) {
				isDateExist = true;
				break;
			}
		}
		
		StringBuilder sb = new StringBuilder("");
		sb.append("package ").append(packageName).append(".model;\n");
		sb.append("\n");
		if (isDateExist == true) {
			sb.append("import java.util.Date;\n");
			sb.append("\n");
		}
		sb.append("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n");
		sb.append("import com.fasterxml.jackson.annotation.JsonInclude;\n");
		sb.append("\n");
		sb.append("import kr.peelknight.common.model.Model;\n");
		sb.append("\n");
		sb.append("@SuppressWarnings(\"serial\")\n");
		sb.append("@JsonIgnoreProperties(ignoreUnknown = true)\n");
		sb.append("@JsonInclude(JsonInclude.Include.NON_DEFAULT)\n");
		sb.append("public class ").append(className).append(" extends Model {\n");
		for (int ii=0; ii<listCols.size(); ii++) {
			sb.append("\tprivate ").append(listCols.get(ii).columnType).append(" ").append(listCols.get(ii).columnName).append(";\n");
		}
		for (int ii=0; ii<listCols.size(); ii++) {
			sb.append("\tpublic ").append(listCols.get(ii).columnType).append(" get")
					.append(toFirstUpperCase(listCols.get(ii).columnName)).append("() {\n");
			sb.append("\t\treturn ").append(listCols.get(ii).columnName).append(";\n");
			sb.append("\t}\n");
			sb.append("\tpublic void set").append(toFirstUpperCase(listCols.get(ii).columnName))
					.append("(").append(listCols.get(ii).columnType).append(" ")
					.append(listCols.get(ii).columnName).append(") {\n");
			sb.append("\t\tthis.").append(listCols.get(ii).columnName).append(" = ")
					.append(listCols.get(ii).columnName).append(";\n");
			sb.append("\t}\n");
		}
		sb.append("}\n");
		return sb.toString();
	}

	public String generateXml(String packageName, String classBaseName, String tableName, List<GN_GenColInfo> listCols) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
		sb.append("<mapper namespace=\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper\">\n");
		sb.append("\t<select id=\"select").append(classBaseName).append("\" resultType=\"").append(packageName).append(".model.").append(tableName).append("\"\n");
		sb.append("\t\t\tstatementType=\"PREPARED\" parameterType=\"java.util.Map\">\n");
		sb.append("\t\tSELECT").append(getColumnsString(listCols));
		sb.append("\t\t  FROM ").append(tableName).append("\n");
		sb.append(getWhereString(listCols));
		sb.append("\t\t<if test=\"offset != null and size != null\">\n");
		sb.append("\t\t LIMIT ${offset}, ${size}\n");
		sb.append("\t\t</if>\n");
		sb.append("\t</select>\n");
		sb.append("\n");
		
		sb.append("\t<select id=\"select").append(classBaseName).append("Paging\" resultType=\"kr.peelknight.common.model.CM_Paging\"\n");
		sb.append("\t\t\tstatementType=\"PREPARED\" parameterType=\"java.util.Map\">\n");
		sb.append("\t\tSELECT COUNT(idKey) as totalCount\n");
		sb.append("\t\t  FROM ").append(tableName).append("\n");
		sb.append(getWhereString(listCols));
		sb.append("\t</select>\n");
		sb.append("\n");
		
		sb.append("\t<insert id=\"insert").append(classBaseName).append("\" parameterType=\"").append(packageName).append(".model.").append(tableName).append("\"\n");
		sb.append("\t\tstatementType=\"PREPARED\" useGeneratedKeys=\"true\" keyProperty=\"idKey\">\n");
		sb.append("\t\tINSERT INTO ").append(tableName).append(" (\n");
		sb.append(getColumnsStringForInsert(listCols));
		sb.append("\t\t) VALUES (\n");
		sb.append(getParamsStringForInsert(listCols));
		sb.append("\t\t)\n");
		sb.append("\t</insert>\n");
		sb.append("\n");
		
		sb.append("\t<update id=\"update").append(classBaseName).append("\" parameterType=\"").append(packageName).append(".model.").append(tableName).append("\">\n");
		sb.append("\t\tUPDATE ").append(tableName).append(" SET\n");
		sb.append(getColumnsParamsStringForUpdate(listCols));
		sb.append(getWhereStringForUpdate(listCols));
		sb.append("\t</update>\n");
		sb.append("\n");
		
		sb.append("\t<delete id=\"delete").append(classBaseName).append("\" parameterType=\"").append(packageName).append(".model.").append(tableName).append("\">\n");
		sb.append("\t\tDELETE FROM ").append(tableName).append("\n");
		sb.append(getWhereStringForDelete(listCols));
		sb.append("\t</delete>\n");
		
		sb.append("</mapper>");
		
		return sb.toString();
	}
	
	public String generateDao(String packageName, String classBaseName, String tableName, String classVarName) {
		StringBuilder sb = new StringBuilder("");
		sb.append("package ").append(packageName).append(".dao;\n");
		sb.append("\n");
		sb.append("import java.util.HashMap;\n");
		sb.append("import java.util.List;\n");
		sb.append("\n");
		sb.append("import org.apache.ibatis.session.SqlSession;\n");
		sb.append("import org.apache.ibatis.session.SqlSessionFactory;\n");
		sb.append("\n");
		sb.append("import kr.peelknight.common.model.CM_Paging;\n");
		sb.append("import ").append(packageName).append(".model.").append(tableName).append(";\n");
		sb.append("import kr.peelknight.mybatis.MySqlSessionFactory;\n");
		sb.append("\n");
		sb.append("public class ").append(classBaseName).append("Dao {\n");
		sb.append("\tSqlSessionFactory factory =  null;\n");
		sb.append("\n");
		sb.append("\tpublic ").append(classBaseName).append("Dao() {\n");
		sb.append("\t\tfactory = MySqlSessionFactory.getSqlSessionFactory();\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\tpublic List<").append(tableName).append("> select").append(classBaseName).append("s(int offset, int size) {\n");
		sb.append("\t\tSqlSession session = factory.openSession();\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tHashMap<String, Object> param = new HashMap<String, Object>();\n");
		sb.append("\t\t\tif (offset >= 0) {\n");
		sb.append("\t\t\t\tparam.put(\"offset\", offset);\n");
		sb.append("\t\t\t\tparam.put(\"size\", size);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\treturn session.selectList(\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper.select").append(classBaseName).append("\", param);\n");
		sb.append("\t\t} finally {\n");
		sb.append("\t\t\tsession.close();\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\tpublic CM_Paging select").append(classBaseName).append("Paging() {\n");
		sb.append("\t\tSqlSession session = factory.openSession();\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tHashMap<String, Object> param = new HashMap<String, Object>();\n");
		sb.append("\t\t\treturn session.selectOne(\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper.select").append(classBaseName).append("Paging\", param);\n");
		sb.append("\t\t} finally {\n");
		sb.append("\t\t\tsession.close();\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\tpublic ").append(tableName).append(" select").append(classBaseName).append("ByIdKey(long idKey) {\n");
		sb.append("\t\tSqlSession session = factory.openSession();\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tHashMap<String, Object> param = new HashMap<String, Object>();\n");
		sb.append("\t\t\tparam.put(\"idKey\", idKey);\n");
		sb.append("\t\t\treturn session.selectOne(\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper.select").append(classBaseName).append("\", param);\n");
		sb.append("\t\t} finally {\n");
		sb.append("\t\t\tsession.close();\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\tpublic void insert").append(classBaseName).append("(").append(tableName).append(" ").append(classVarName).append(") {\n");
		sb.append("\t\tSqlSession session = factory.openSession();\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tsession.insert(\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper.insert").append(classBaseName).append("\", ").append(classVarName).append(");\n");
		sb.append("\t\t\tsession.commit();\n");
		sb.append("\t\t} finally {\n");
		sb.append("\t\t\tsession.close();\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\tpublic void update").append(classBaseName).append("(").append(tableName).append(" ").append(classVarName).append(") {\n");
		sb.append("\t\tSqlSession session = factory.openSession();\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tsession.update(\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper.update").append(classBaseName).append("\", ").append(classVarName).append(");\n");
		sb.append("\t\t\tsession.commit();\n");
		sb.append("\t\t} finally {\n");
		sb.append("\t\t\tsession.close();\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\tpublic void delete").append(classBaseName).append("(").append(tableName).append(" ").append(classVarName).append(") {\n");
		sb.append("\t\tSqlSession session = factory.openSession();\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tsession.delete(\"").append(packageName).append(".mybatis.").append(classBaseName.toLowerCase()).append("Mapper.delete").append(classBaseName).append("\", ").append(classVarName).append(");\n");
		sb.append("\t\t\tsession.commit();\n");
		sb.append("\t\t} finally {\n");
		sb.append("\t\t\tsession.close();\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	public String generateService(String packageName, String classBaseName, String tableName, String classVarName) {
		StringBuilder sb = new StringBuilder("");
		sb.append("package ").append(packageName).append(".service;\n");
		sb.append("\n");
		sb.append("import javax.servlet.http.HttpServletRequest;\n");
		sb.append("import javax.ws.rs.Consumes;\n");
		sb.append("import javax.ws.rs.DELETE;\n");
		sb.append("import javax.ws.rs.GET;\n");
		sb.append("import javax.ws.rs.POST;\n");
		sb.append("import javax.ws.rs.PUT;\n");
		sb.append("import javax.ws.rs.Path;\n");
		sb.append("import javax.ws.rs.PathParam;\n");
		sb.append("import javax.ws.rs.Produces;\n");
		sb.append("import javax.ws.rs.QueryParam;\n");
		sb.append("import javax.ws.rs.core.Context;\n");
		sb.append("import javax.ws.rs.core.MediaType;\n");
		sb.append("import javax.ws.rs.core.Response;\n");
		sb.append("import javax.ws.rs.core.Response.Status;\n");
		sb.append("\n");
		sb.append("import kr.peelknight.common.Constant;\n");
		sb.append("import ").append(packageName).append(".dao.").append(classBaseName).append("Dao;\n");
		sb.append("import ").append(packageName).append(".model.").append(tableName).append(";\n");
		sb.append("import kr.peelknight.common.model.CM_PagingList;\n");
		sb.append("import kr.peelknight.common.model.ModelHandler;\n");
		sb.append("import kr.peelknight.util.AuthToken;\n");
		sb.append("import kr.peelknight.util.ResponseUtil;\n");
		sb.append("\n");
		sb.append("@Path(\"/{version}/").append(classBaseName.toLowerCase()).append("\")\n");
		sb.append("public class ").append(classBaseName).append("Service {\n");
		sb.append("\t").append(classBaseName).append("Dao dao = new ").append(classBaseName).append("Dao();\n");
		sb.append("\n");
		sb.append("\t@SuppressWarnings(\"rawtypes\")\n");
		sb.append("\t@GET\n");
		sb.append("\t@Produces({MediaType.APPLICATION_JSON})\n");
		sb.append("\t@Path(\"/list/{page}\")\n");
		sb.append("\tpublic Response select").append(classBaseName).append("s(@Context HttpServletRequest request, @PathParam(\"page\") int page, @QueryParam(\"q\") String authToken) {\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tif (AuthToken.isValidToken(authToken) == false) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.EXPECTATION_FAILED);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\tint offset = (page - 1) * Constant.DEFAULT_SIZE;\n");
		sb.append("\t\t\tCM_PagingList<").append(tableName).append("> paging = new CM_PagingList<").append(tableName).append(">();\n");
		sb.append("\t\t\tpaging.setList(dao.select").append(classBaseName).append("s(offset, Constant.DEFAULT_SIZE));\n");
		sb.append("\t\t\tpaging.setPaging(dao.select").append(classBaseName).append("Paging());\n");
		sb.append("\t\t\tpaging.numbering(offset);\n");
		sb.append("\t\t\tpaging.l10n(request.getSession());\n");
		sb.append("\t\t\treturn ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));\n");
		sb.append("\t\t} catch (Exception e) {\n");
		sb.append("\t\t\te.printStackTrace();\n");
		sb.append("\t\t\treturn ResponseUtil.internalError(e.getMessage());\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\t@GET\n");
		sb.append("\t@Produces({MediaType.APPLICATION_JSON})\n");
		sb.append("\t@Path(\"/{idKey}\")\n");
		sb.append("\tpublic Response select").append(classBaseName).append("(@Context HttpServletRequest request, @PathParam(\"idKey\") long idKey, @QueryParam(\"q\") String authToken) {\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tif (AuthToken.isValidToken(authToken) == false) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.EXPECTATION_FAILED);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\t").append(tableName).append(" existOne = dao.select").append(classBaseName).append("ByIdKey(idKey);\n");
		sb.append("\t\t\tif (existOne == null) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.NOT_FOUND);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\texistOne.l10n(request.getSession());\n");
		sb.append("\t\t\treturn ResponseUtil.getResponse((new ModelHandler<").append(tableName).append(">(").append(tableName).append(".class)).convertToJson(existOne));\n");
		sb.append("\t\t} catch (Exception e) {\n");
		sb.append("\t\t\te.printStackTrace();\n");
		sb.append("\t\t\treturn ResponseUtil.internalError(e.getMessage());\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\t@POST\n");
		sb.append("\t@Consumes({MediaType.APPLICATION_JSON})\n");
		sb.append("\t@Produces({MediaType.APPLICATION_JSON})\n");
		sb.append("\tpublic Response insert").append(classBaseName).append("(").append(tableName).append(" ").append(classVarName).append(") throws Exception {\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tif (AuthToken.isValidToken(").append(classVarName).append(".getReqToken()) == false) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.EXPECTATION_FAILED);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\tdao.insert").append(classBaseName).append("(").append(classVarName).append(");\n");
		sb.append("\t\t\treturn ResponseUtil.getResponse((new ModelHandler<").append(tableName).append(">(").append(tableName).append(".class)).convertToJson(").append(classVarName).append("));\n");
		sb.append("\t\t} catch (Exception e) {\n");
		sb.append("\t\t\te.printStackTrace();\n");
		sb.append("\t\t\treturn ResponseUtil.internalError(e.getMessage());\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\t@PUT\n");
		sb.append("\t@Consumes({MediaType.APPLICATION_JSON})\n");
		sb.append("\t@Produces({MediaType.APPLICATION_JSON})\n");
		sb.append("\t@Path(\"/{idKey}\")\n");
		sb.append("\tpublic Response update").append(classBaseName).append("(").append(tableName).append(" ").append(classVarName).append(", @PathParam(\"idKey\") long idKey) throws Exception {\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tif (AuthToken.isValidToken(").append(classVarName).append(".getReqToken()) == false) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.EXPECTATION_FAILED);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\t").append(tableName).append(" existOne = dao.select").append(classBaseName).append("ByIdKey(").append(classVarName).append(".getIdKey());\n");
		sb.append("\t\t\tif (existOne == null) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.NOT_FOUND);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\tdao.update").append(classBaseName).append("(existOne);\n");
		sb.append("\t\t\treturn ResponseUtil.getResponse((new ModelHandler<").append(tableName).append(">(").append(tableName).append(".class)).convertToJson(existOne));\n");
		sb.append("\t\t} catch (Exception e) {\n");
		sb.append("\t\t\te.printStackTrace();\n");
		sb.append("\t\t\treturn ResponseUtil.internalError(e.getMessage());\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("\n");
		sb.append("\t@DELETE\n");
		sb.append("\t@Produces({MediaType.APPLICATION_JSON})\n");
		sb.append("\t@Path(\"/{idKey}\")\n");
		sb.append("\tpublic Response delete").append(classBaseName).append("(@PathParam(\"idKey\") long idKey, @QueryParam(\"q\") String authToken) {\n");
		sb.append("\t\ttry {\n");
		sb.append("\t\t\tif (AuthToken.isValidToken(authToken) == false) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.EXPECTATION_FAILED);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\t").append(tableName).append(" existOne = dao.select").append(classBaseName).append("ByIdKey(idKey);\n");
		sb.append("\t\t\tif (existOne == null) {\n");
		sb.append("\t\t\t\treturn ResponseUtil.getResponse(Status.NOT_FOUND);\n");
		sb.append("\t\t\t}\n");
		sb.append("\t\t\tdao.delete").append(classBaseName).append("(existOne);\n");
		sb.append("\t\t\treturn ResponseUtil.getResponse((new ModelHandler<").append(tableName).append(">(").append(tableName).append(".class)).convertToJson(existOne));\n");
		sb.append("\t\t} catch (Exception e) {\n");
		sb.append("\t\t\te.printStackTrace();\n");
		sb.append("\t\t\treturn ResponseUtil.internalError(e.getMessage());\n");
		sb.append("\t\t}\n");
		sb.append("\t}\n");
		sb.append("}\n");
		
		return sb.toString();
	}
	
	public String generateJSP(String classBaseName) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\n");
		sb.append("<%@ page import = \"kr.peelknight.util.L10N\" %>\n");
		sb.append("<jsp:include page=\"/common/include.jsp\"/>\n");
		sb.append("<jsp:include page=\"/common/header.jsp\"/>\n");
		sb.append("<script>\n");
		sb.append("startFuncs[startFuncs.length] = function() {\n");
		sb.append("\trefreshList()\n");
		sb.append("}\n");
		sb.append("\n");
		sb.append("function refreshList() {\n");
		sb.append("\tgetList(__currentPage)\n");
		sb.append("}\\n");
		sb.append("\n");
		sb.append("function getList(page) {\n");
		sb.append("\tvar url = \"${contextPath}/svc/v1/" + classBaseName + "/list/\" + page + \"?q=\" + getAuthToken();\n");
		sb.append("\tnesAjax(url,\n");
		sb.append("\t\t\tnull,\n");
		sb.append("\t\t\tfunction(data) {\n");
		sb.append("\t\t\t\t$(\"#listLayout\").html(TrimPath.processDOMTemplate(\"" + classBaseName + "_jst\", data));\n");
		sb.append("\t\t\t\tpagingInfo_success(data.paging, refreshList);\n");
		sb.append("\t\t\t},\n");
		sb.append("\t\t\tfunction(data) {\n");
		sb.append("\t\t\t\talert(JSON.stringify(data))\n");
		sb.append("\t\t\t},\n");
		sb.append("\t\t\t\"GET\");\n");
		sb.append("</script>\n");
		sb.append("\n");
		sb.append("<main role=\"main\" class=\"px-4\">\n");
		sb.append("\t<div class=\"row\">\n");
		sb.append("\t\t<div class=\"col-12\">\n");
		sb.append("\t\t\t<div class=\"table-responsive\" id=\"listLayout\"></div>\n");
		sb.append("\t\t</div>\n");
		sb.append("\t\t<div class=\"col-12\">\n");
		sb.append("\t\t\t<button class=\"btn btn-secondary\" style=\"float:right;\" onclick=\"openPopupForRegistSmartHub()\">ADD</button>\n");
		sb.append("\t\t</div>\n");
		sb.append("\t\t<div class=\"col-12\">\n");
		sb.append("\t\t\t<jsp:include page=\"/common/paging.jsp\"/>\n");
		sb.append("\t\t</div>\n");
		sb.append("\t</div>\n");
		sb.append("</main>\n");
		sb.append("\n");
		sb.append("<script type=\"text/template\" id=\"" + classBaseName + "_jst\">\n");
		sb.append("<table class=\"table table-sm\">\n");
		sb.append("\t<thead>\n");
		sb.append("\t\t<tr>\n");
		sb.append("\t\t\t<th class=\"p-2\">TITLE1</th>\n");
		sb.append("\t\t\t<th class=\"p-2\">TITLE2</th>\n");
		sb.append("\t\t</tr>\n");
		sb.append("\t</thead>\n");
		sb.append("\t<tbody>\n");
		sb.append("\t\t{for " + classBaseName + " in list}\n");
		sb.append("\t\t<tr class=\"bg-dark\">\n");
		sb.append("\t\t\t<td class=\"p-2\" onclick=\"openPopupForUpdateSmartHub({{" + classBaseName + ".field1})\" style=\"cursor:pointer;\">{{smarthub.smartHubName}</td>\n");
		sb.append("\t\t\t<td class=\"p-2\">{{" + classBaseName + ".field2}</td>\n");
		sb.append("\t\t</tr>\n");
		sb.append("\t\t{forelse}\n");
		sb.append("\t\t<tr><td class=\"p-2\" colspan=\"2\">No Data</tr>\n");
		sb.append("\t\t{/for}\n");
		sb.append("\t\t</tbody>\n");
		sb.append("\t</table>\n");
		sb.append("</script>\n");
		sb.append("\n");
		sb.append("<jsp:include page=\"/common/footer.jsp\"/>\n");
		return sb.toString();
	}
	
	public String getColumnsStringForInsert(List<GN_GenColInfo> listCols) {
		StringBuilder sb = new StringBuilder("");
		GN_GenColInfo temp;
		for (int ii=0; ii<listCols.size(); ii++) {
			temp = listCols.get(ii);
			if (ii == 0) {
				sb.append("\t\t\t");
			} else {
				sb.append(", ");
				if (ii % 5 == 0) {
					sb.append("\n\t\t\t");
				}
			}
			sb.append(temp.columnName);
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public String getParamsStringForInsert(List<GN_GenColInfo> listCols) {
		StringBuilder sb = new StringBuilder("");
		GN_GenColInfo temp;
		for (int ii=0; ii<listCols.size(); ii++) {
			temp = listCols.get(ii);
			if (ii == 0) {
				sb.append("\t\t\t");
			} else {
				sb.append(", ");
				if (ii % 5 == 0) {
					sb.append("\n\t\t\t");
				}
			}
			sb.append("#{").append(temp.columnName).append("}");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public String getColumnsParamsStringForUpdate(List<GN_GenColInfo> listCols) {
		StringBuilder sb = new StringBuilder("");
		GN_GenColInfo temp;
		for (int ii=0; ii<listCols.size(); ii++) {
			temp = listCols.get(ii);
			if (temp.isPrimaryKey == false) {
				if (sb.length() == 0) {
					sb.append("\t\t\t   ").append(temp.columnName).append(" = ").append("#{").append(temp.columnName).append("}");
				} else {
					sb.append(",\n\t\t\t   ").append(temp.columnName).append(" = ").append("#{").append(temp.columnName).append("}");
				}
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public String getWhereString(List<GN_GenColInfo> listCols) {
		StringBuilder sb = new StringBuilder("\t\t WHERE idKey <![CDATA[ > ]]> 0\n");
		GN_GenColInfo item;
		for (int ii=0; ii<listCols.size(); ii++) {
			item = listCols.get(ii);
			if (item.isPrimaryKey) {
				sb.append("\t\t<if test=\"").append(item.columnName).append(" != null\">\n");
				sb.append("\t\t   AND ").append(item.columnName).append(" = ").append("#{").append(item.columnName).append("}\n");
				sb.append("\t\t</if>\n");
			}
		}
		return sb.toString();
	}
	
	public String getWhereStringForUpdate(List<GN_GenColInfo> listCols) {
		return "\t\t WHERE idKey = #{idKey}\n";
	}
	
	public String getWhereStringForDelete(List<GN_GenColInfo> listCols) {
		return "\t\t WHERE idKey = #{idKey}\n";
	}
	
	public String getColumnsString(List<GN_GenColInfo> listCols) {
		StringBuilder sb = new StringBuilder("");
		GN_GenColInfo item;
		for (int ii=0; ii<listCols.size(); ii++) {
			item = listCols.get(ii);
			if (ii == 0) {
				sb.append(" ");
			} else {
				sb.append(", ");
				if (ii % 5 == 0) {
					sb.append("\n\t\t\t   ");
				}
			}
			sb.append(item.columnName);
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public String removePrefix(String tableName, String prefix) {
		if (tableName.contains(prefix)) {
			return tableName.replaceAll(prefix, "");
		}
		return tableName;
	}
	
	public String toFirstLowerCase(String classVarName) {
		return classVarName.substring(0, 1).toLowerCase() + classVarName.substring(1);
	}
	
	public String toFirstUpperCase(String classVarName) {
		return classVarName.substring(0, 1).toUpperCase() + classVarName.substring(1);
	}

}
