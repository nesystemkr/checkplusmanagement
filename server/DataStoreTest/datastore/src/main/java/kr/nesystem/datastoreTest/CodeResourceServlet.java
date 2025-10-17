package kr.nesystem.datastoreTest;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.StructuredQuery.OrderBy;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Google App Engine Datastore 기반의 REST API 서블릿입니다. URL: /rest/codes (Code List)
 * URL: /rest/codes/{code} (Code Detail) * 기능: 코드 테이블(Kind: 'CodeItem')에 대한 CRUD
 * 작업을 수행합니다. Datastore 클라이언트 초기화 및 API Key 인증을 프로그램 내에서 처리합니다.
 */
@WebServlet(name = "CodeResourceServlet", urlPatterns = "/rest/codes/*")
public class CodeResourceServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(CodeResourceServlet.class.getName());
	// 3. 간단한 코드 테이블 정의
	private static final String CODE_KIND = "CodeItem";
	// 11. 서버 관련 변수 설정: Datastore 클라이언트 및 KeyFactory를 인스턴스 변수로 설정
	private Datastore datastore;
	private KeyFactory keyFactory;

	// 12. 간단한 인-프로그램 인증 (Auth)을 위한 API Key 설정
	// 실제 환경에서는 환경 변수나 Secret Manager를 사용해야 합니다.
//	private static final String HARDCODED_API_KEY = "my-secure-api-key-1234";

	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 서블릿 초기화 시 Datastore 클라이언트를 설정합니다. Google Cloud 환경(GCP/GAE) 또는 로컬 에뮬레이터에서 자동
	 * 인증됩니다.
	 */
	@Override
	public void init() throws ServletException {
		// DatastoreOptions.getDefaultInstance()는 GAE 환경 및 로컬 환경에서 자동으로
		// 프로젝트 ID를 감지하고 인증 정보를 가져옵니다. (환경변수 최소화)
		//this.datastore = DatastoreOptions.getDefaultInstance().getService();
		try {
			this.datastore = DatastoreOptions.newBuilder()
					.setCredentials(GoogleCredentials.getApplicationDefault())
					.build()
					.getService();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.keyFactory = datastore.newKeyFactory().setKind(CODE_KIND);
		logger.info("Datastore client initialized successfully.");
	}

	/**
	 * API Key를 확인하여 인증을 수행하는 헬퍼 메서드입니다.
	 */
	private boolean isAuthenticated(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String apiKey = req.getHeader("X-API-KEY");
		return true;
//		if (HARDCODED_API_KEY.equals(apiKey)) {
//			return true;
//		} else {
//			logger.warning("Authentication failed: Missing or invalid X-API-KEY header.");
//			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			resp.setContentType("application/json");
//			resp.getWriter().write("{\"error\": \"Unauthorized. Please provide a valid X-API-KEY.\"}");
//			return false;
//		}
	}

	/**
	 * URL 경로에서 {code} 값을 추출합니다. 예: /rest/codes/CODE_A -> CODE_A
	 */
	private String getCodeFromPath(HttpServletRequest req) {
		String pathInfo = req.getPathInfo();
		if (pathInfo != null && pathInfo.length() > 1) {
			// pathInfo는 /CODE_A 형식으로 오므로, 첫 '/'를 제거하고 반환
			return pathInfo.substring(1);
		}
		return null;
	}

	/**
	 * 엔티티를 Map<String, Object> 형식으로 변환합니다.
	 */
	private Map<String, Object> entityToMap(Entity entity) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", entity.getKey().getName());
		map.put("codeName", entity.getString("codeName"));
		map.put("createdAt", entity.getTimestamp("createdAt").toDate().toString());
		return map;
	}

	/**
	 * 5. 입력한 데이터를 리스트로 조회 / 6. 상세 데이터 조회 (GET)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// GET 요청은 인증 없이 허용하거나, 필요에 따라 인증을 추가할 수 있습니다.
		// 이 예제에서는 인증을 생략하고 조회만 허용합니다.

		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String code = getCodeFromPath(req);

		if (code != null) {
			// 6. 리스트 중 하나를 선택하면 한 행의 상세 데이터를 보여준다. (Detail Read)
			try {
				Key key = keyFactory.newKey(code);
				Entity codeItem = datastore.get(key);

				if (codeItem != null) {
					mapper.writeValue(out, entityToMap(codeItem));
					resp.setStatus(HttpServletResponse.SC_OK);
				} else {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					out.write("{\"error\": \"Code not found: " + code + "\"}");
				}
			} catch (Exception e) {
				logger.severe("Error reading code detail: " + e.getMessage());
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.write("{\"error\": \"Internal Server Error\"}");
			}

		} else {
			// 5. 입력한 데이터를 리스트로 볼 수 있다. (List Read)
			try {
				// Datastore Query: Kind가 CodeItem인 모든 엔티티를 createdAt 내림차순으로 조회
				Query<Entity> query = Query.newEntityQueryBuilder().setKind(CODE_KIND)
						.setOrderBy(OrderBy.desc("createdAt")).setLimit(100).build();

				QueryResults<Entity> results = datastore.run(query);
				List<Map<String, Object>> codeList = new ArrayList<>();

				while (results.hasNext()) {
					codeList.add(entityToMap(results.next()));
				}

				mapper.writeValue(out, codeList);
				resp.setStatus(HttpServletResponse.SC_OK);

			} catch (Exception e) {
				logger.severe("Error reading code list: " + e.getMessage());
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				out.write("{\"error\": \"Internal Server Error\"}");
			}
		}
	}

	/**
	 * 4. 데이터를 입력할 수 있다. (POST: Create)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!isAuthenticated(req, resp))
			return; // 12. 인증 체크

		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		try {
			// 1. JSON 요청 본문 파싱
			String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			Map<String, String> data = mapper.readValue(jsonBody, Map.class);

			String code = data.get("code");
			String codeName = data.get("codeName");

			if (code == null || code.isEmpty() || codeName == null || codeName.isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.write("{\"error\": \"Code and codeName are required.\"}");
				return;
			}

			// 2. Datastore에 데이터 저장 (Key는 code 값을 사용)
			Key key = keyFactory.newKey(code);

			// 트랜잭션을 사용하여 원자성 보장 (키 중복 체크)
			Transaction txn = datastore.newTransaction();
			try {
				if (txn.get(key) != null) {
					// 이미 존재하는 키라면 에러 반환
					txn.rollback();
					resp.setStatus(HttpServletResponse.SC_CONFLICT);
					out.write("{\"error\": \"Code already exists: " + code + "\"}");
					return;
				}

				Entity codeItem = Entity.newBuilder(key).set("codeName", codeName)
						.set("createdAt", com.google.cloud.Timestamp.now()).build();

				txn.add(codeItem);
				txn.commit();

				resp.setStatus(HttpServletResponse.SC_CREATED);
				mapper.writeValue(out, entityToMap(codeItem)); // 저장된 데이터 반환

			} catch (Exception e) {
				if (txn.isActive()) {
					txn.rollback();
				}
				throw e;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error creating code item: " + e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.write("{\"error\": \"Internal Server Error\"}");
		}
	}

	/**
	 * 8. 조회한 데이터를 수정해서 업데이트할 수 있다. (PUT: Update)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!isAuthenticated(req, resp))
			return; // 12. 인증 체크

		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String code = getCodeFromPath(req);
		if (code == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.write("{\"error\": \"Code must be provided in the URL path.\"}");
			return;
		}

		try {
			String jsonBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			Map<String, String> data = mapper.readValue(jsonBody, Map.class);
			String newCodeName = data.get("codeName");

			if (newCodeName == null || newCodeName.isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.write("{\"error\": \"codeName is required for update.\"}");
				return;
			}

			Key key = keyFactory.newKey(code);

			// 트랜잭션을 사용하여 원자성 및 동시성 제어
			Transaction txn = datastore.newTransaction();
			try {
				Entity existingEntity = txn.get(key);

				if (existingEntity == null) {
					txn.rollback();
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					out.write("{\"error\": \"Code not found: " + code + "\"}");
					return;
				}

				// 기존 엔티티를 기반으로 새 엔티티 빌드 (createdAt 필드 유지)
				Entity updatedItem = Entity.newBuilder(key, existingEntity).set("codeName", newCodeName).build();

				txn.update(updatedItem);
				txn.commit();

				resp.setStatus(HttpServletResponse.SC_OK);
				mapper.writeValue(out, entityToMap(updatedItem));

			} catch (Exception e) {
				if (txn.isActive()) {
					txn.rollback();
				}
				throw e;
			}

		} catch (Exception e) {
			logger.severe("Error updating code item: " + e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.write("{\"error\": \"Internal Server Error\"}");
		}
	}

	/**
	 * 7. 리스트에서 삭제를 선택하면 해당 데이터를 삭제한다. (DELETE)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!isAuthenticated(req, resp))
			return; // 12. 인증 체크

		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String code = getCodeFromPath(req);
		if (code == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.write("{\"error\": \"Code must be provided in the URL path.\"}");
			return;
		}

		try {
			Key key = keyFactory.newKey(code);
			datastore.delete(key);

			resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
			logger.info("Code deleted: " + code);

		} catch (Exception e) {
			logger.severe("Error deleting code item: " + e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.write("{\"error\": \"Internal Server Error\"}");
		}
	}
}
