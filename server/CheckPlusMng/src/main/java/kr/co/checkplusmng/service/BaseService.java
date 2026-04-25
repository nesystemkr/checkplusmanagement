package kr.co.checkplusmng.service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import kr.co.checkplusmng.dao.MWIDBaseDao;
import kr.co.checkplusmng.model.MW_IDBaseModel;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.CM_PagingList;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.ResponseUtil;

public class BaseService<T extends MW_IDBaseModel> {
	protected MWIDBaseDao<T> _dao;
	protected Class<T> _entityClass;
	protected String _prefix;
	
	public BaseService(Class<T> entityClass, String prefix) {
		this._entityClass = entityClass;
		this._prefix = prefix;
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insert(T model) throws Exception {
		try {
			if (AuthToken.isValidToken(model.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (model.getIdString() == null || model.getIdString().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			T existOne = _dao.selectByIdString(null, model.getIdString());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			_dao.insert(model);
			return ResponseUtil.getResponse((new ModelHandler<T>(_entityClass)).convertToJson(model));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response update(@PathParam("idKey") long idKey,
						   T model) {
		try {
			if (AuthToken.isValidToken(model.getAuthToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (idKey != model.getIdKey()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (model.getIdString() == null || model.getIdString().length() == 0) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			T existOne = _dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			updateItem(existOne, model);
			_dao.update(existOne);
			return ResponseUtil.getResponse((new ModelHandler<T>(_entityClass)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	protected void updateItem(T existOne, T newOne) {
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response delete(@PathParam("idKey") long idKey,
						   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			T existOne = _dao.select(null, idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			_dao.delete(existOne);
			return ResponseUtil.getResponse((new ModelHandler<T>(_entityClass)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response list(@Context HttpServletRequest request,
						 @PathParam("page") int page,
						 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<T> paging = _dao.pagingList(request.getSession(), null, offset, Constant.DEFAULT_SIZE);
			if (paging != null && paging.getList() != null) {
				for (int ii = 0; ii < paging.getList().size(); ii++) {
					T item = paging.getList().get(ii);
					fillupSubData(item);
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	//User detail
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response one(@Context HttpServletRequest request,
						@PathParam("idKey") long idKey,
						@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			T existOne = _dao.select(request.getSession(), idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			fillupSubData(existOne);
			return ResponseUtil.getResponse((new ModelHandler<T>(_entityClass)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	protected void fillupSubData(T item) {
	}
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/newId")
	public Response newId(@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			List<T> list = _dao.list(null, null, -1, 0);
			int index = 1;
			String compId;
			boolean isFound;
			String format = _prefix + "_%05d";
			if (list != null) {
				while (true) {
					compId = String.format(format, index);
					isFound = false;
					for (int ii = 0; ii < list.size(); ii++) {
						if (compId.equals(list.get(ii).getIdString())) {
							isFound = true;
							break;
						}
					}
					if (isFound == false) {
						break;
					}
					index++;
				}
			} else {
				compId = String.format(format, index);
			}
			T ret = _entityClass.getDeclaredConstructor().newInstance();
			ret.setIdString(compId);
			return ResponseUtil.getResponse((new ModelHandler<T>(_entityClass)).convertToJson(ret));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
