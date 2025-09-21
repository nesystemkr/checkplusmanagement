package kr.peelknight.fragment.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

import kr.peelknight.common.Constant;
import kr.peelknight.common.dao.CodeDao;
import kr.peelknight.fragment.CodeInterface;
import kr.peelknight.fragment.dao.FragmentCachedJsonDao;
import kr.peelknight.fragment.dao.FragmentDao;
import kr.peelknight.fragment.dao.GridColButtonDao;
import kr.peelknight.fragment.dao.GridColModelDao;
import kr.peelknight.fragment.dao.GridFragmentDao;
import kr.peelknight.fragment.dao.PopupFragmentDao;
import kr.peelknight.fragment.dao.PopupRowDao;
import kr.peelknight.fragment.model.CM_Fragment;
import kr.peelknight.fragment.model.CM_FragmentCachedJson;
import kr.peelknight.fragment.model.CM_GridColButton;
import kr.peelknight.fragment.model.CM_GridColModel;
import kr.peelknight.fragment.model.CM_GridFragment;
import kr.peelknight.fragment.model.CM_PopupFragment;
import kr.peelknight.fragment.model.CM_PopupRow;
import kr.peelknight.common.model.CM_Code;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.Model;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.L10N;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/fragment")
public class FragmentService {
	FragmentDao dao = new FragmentDao();
	FragmentCachedJsonDao cacheDao = new FragmentCachedJsonDao();
	GridFragmentDao gridDao = new GridFragmentDao();
	GridColModelDao colModelDao = new GridColModelDao();
	GridColButtonDao colButtonDao = new GridColButtonDao();
	PopupFragmentDao popupDao = new PopupFragmentDao();
	PopupRowDao rowDao = new PopupRowDao();
	CodeDao codeDao = new CodeDao();
	
	public List<CM_Code> selectCodes(String codeType, HttpSession session) {
		List<CM_Code> codes = codeDao.selectCodeByType(codeType);
		if (codes != null) {
			CM_Code code;
			for (int ii=0; ii<codes.size(); ii++) {
				code = codes.get(ii);
				code.l10n(session);
			}
		}
		return codes;
	}
	
	public Map<String, String> selectCodeByTypeAsMap(String codeType, HttpSession session) {
		Map<String, String> ret = new HashMap<>();
		List<CM_Code> codes = selectCodes(codeType, session);
		if (codes != null) {
			CM_Code code;
			for (int ii=0; ii<codes.size(); ii++) {
				code = codes.get(ii);
				ret.put(code.getCode(), code.getCodeNameLocale());
			}
		}
		return ret;
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectFragments(@Context HttpServletRequest request,
									@PathParam("page") int page) {
		try {
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_Fragment> paging = new CM_PagingList<CM_Fragment>();
			paging.setList(dao.selectFragments(offset, Constant.DEFAULT_SIZE));
			paging.setPaging(dao.selectFragmentPaging());
			paging.numbering(offset);
			paging.l10n(request.getSession());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response selectFragment(@Context HttpServletRequest request,
								   @PathParam("idKey") long idKey) {
		try {
			CM_Fragment existOne = dao.selectFragmentByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.l10n(request.getSession());
			return ResponseUtil.getResponse((new ModelHandler<Model>(Model.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/id/{fragmentId}")
	public Response selectFragment(@Context HttpServletRequest request,
								   @PathParam("fragmentId") String fragmentId) {
		try {
			CM_Fragment existFragment = dao.selectFragmentByFragmentId(fragmentId);
			if (existFragment == null) {
				return null;
			}
			String ret = null;
			if (Constant.FRAGMENT_CACHED == true) {
				CM_FragmentCachedJson cached = cacheDao.selectFragmentCachedJsonByIdKey(existFragment.getIdKey(), L10N.getLang(request.getSession()));
				if (cached != null) {
					if (cached.getCachedJson() != null && cached.getCachedJson().length() > 0) {
						ret = cached.getCachedJson();
					}
				}
			}
			if (ret == null || ret.length() == 0) {
				Model existOne = selectProperFragment(existFragment, request.getSession());
				if (existOne == null) {
					return ResponseUtil.getResponse(Status.NOT_FOUND);
				}
				existOne.l10n(request.getSession());
				ret = (new ModelHandler<Model>(Model.class)).convertToJson(existOne);
				cacheDao.insertOrUpdateFragmentCachedJson(existFragment.getIdKey(), L10N.getLang(request.getSession()), ret);
			}
			return ResponseUtil.getResponse(ret);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public Model selectProperFragment(Object fragmentId, HttpSession session) {
		CM_Fragment existFragment = null;
		if (fragmentId instanceof CM_Fragment) {
			existFragment = (CM_Fragment)fragmentId;
		} else {
			existFragment = dao.selectFragmentByFragmentId((String)fragmentId);
		}
		if (existFragment == null) {
			return null;
		}
		if (existFragment.getFragmentType() == CM_Fragment.FRAGMENTTYPE_GRID) {
			return selectGridFragment(existFragment.getIdKey(), session);
		} else if (existFragment.getFragmentType() == CM_Fragment.FRAGMENTTYPE_POPUP) {
			return selectPopupFragment(existFragment.getIdKey(), session);
		} else {
			return null;
		}
	}
	
	public CM_GridFragment selectGridFragment(long fragmentIdKey, HttpSession session) {
		CM_GridFragment existOne = gridDao.selectGridFragmentByIdKey(fragmentIdKey);
		if (existOne != null) {
			if (existOne.getPopupFragmentId() != null) {
				existOne.setPopupFragment((CM_PopupFragment)selectProperFragment(existOne.getPopupFragmentId(), session));
			}
			existOne.setColModel(colModelDao.selectGridColModels(fragmentIdKey, -1, 0));
			Map<String, Map<String, String>> mapTmp = null;
			List<CM_GridColButton> buttons = colButtonDao.selectGridColButtons(fragmentIdKey, 0, -1, 0);
			if (existOne.getColModel() != null) {
				CM_GridColModel item;
				for (int ii=0; ii<existOne.getColModel().size(); ii++) {
					item = existOne.getColModel().get(ii);
					item.setButtonPreSet(0);
					if (buttons != null && buttons.size() > 0) {
						CM_GridColButton button;
						for (int jj=0; jj<buttons.size(); jj++) {
							button = buttons.get(jj);
							if (item.getIdKey() == button.getColIdKey()) {
								if (item.getButtons() == null) {
									item.setButtons(new ArrayList<>());
								}
								item.getButtons().add(button);
								if (button.getPreSet() == 1) {
									item.setButtonPreSet(item.getButtonPreSet() + 1);
								} else if (button.getPreSet() == 2) {
									item.setButtonPreSet(item.getButtonPreSet() + 2);
								} else if (button.getPreSet() == 3) {
									item.setButtonPreSet(item.getButtonPreSet() + 4);
								}
							}
						}
					}
					if (item.getCodeType() != null && item.getCodeType().trim().length() > 0) {
						if (mapTmp == null) {
							mapTmp = new HashMap<>();
						}
						if (mapTmp.containsKey(item.getCodeType()) == false) {
							if (item.getCodeType().indexOf("code.type.") == 0) {
								try {
									Class<?> clazz = Class.forName(item.getCodeType());
									Constructor<?> ctor = clazz.getConstructor();
									Object object = ctor.newInstance();
									mapTmp.put(item.getCodeType(), ((CodeInterface)object).selectCodeByTypeAsMap(session));
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								mapTmp.put(item.getCodeType(), selectCodeByTypeAsMap(item.getCodeType(), session));
							}
						}
						item.setSelectValues(mapTmp.get(item.getCodeType()));
					}
				}
			}
			existOne.l10n(session);
		}
		return existOne;
	}
	
	public CM_PopupFragment selectPopupFragment(long fragmentIdKey, HttpSession session) {
		CM_PopupFragment existOne = popupDao.selectPopupFragmentByIdKey(fragmentIdKey);
		if (existOne != null) {
			if (existOne.getGridFragmentId() != null) {
				existOne.setGridFragment((CM_GridFragment)selectProperFragment(existOne.getGridFragmentId(), session));
			}
			existOne.setRows(rowDao.selectPopupRows(fragmentIdKey, -1, 0));
			Map<String, List<CM_Code>> mapTmp = null;
			if (existOne.getRows() != null) {
				CM_PopupRow item;
				for (int ii=0; ii<existOne.getRows().size(); ii++) {
					item = existOne.getRows().get(ii);
					if (item.getCodeType() != null && item.getCodeType().trim().length() > 0) {
						if (mapTmp == null) {
							mapTmp = new HashMap<>();
						}
						if (mapTmp.containsKey(item.getCodeType()) == false) {
							if (item.getCodeType().indexOf("code.type.") == 0) {
								try {
									Class<?> clazz = Class.forName(item.getCodeType());
									Constructor<?> ctor = clazz.getConstructor();
									Object object = ctor.newInstance();
									mapTmp.put(item.getCodeType(), ((CodeInterface)object).selectCodes(session));
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								mapTmp.put(item.getCodeType(), selectCodes(item.getCodeType(), session));
							}
						}
						item.setCodes(mapTmp.get(item.getCodeType()));
					}
				}
			}
			existOne.l10n(session);
		}
		return existOne;
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertFragment(CM_Fragment fragment) throws Exception {
		try {
			if (AuthToken.isValidToken(fragment.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			fragment.setCreateDate(new Date());
			dao.insertFragment(fragment);
			return ResponseUtil.getResponse((new ModelHandler<CM_Fragment>(CM_Fragment.class)).convertToJson(fragment));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response updateFragment(CM_Fragment fragment,
								   @PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(fragment.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Fragment existOne = dao.selectFragmentByIdKey(fragment.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setFragmentId(fragment.getFragmentId());
			existOne.setFragmentType(fragment.getFragmentType());
			existOne.setComment(fragment.getComment());
			dao.updateFragment(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Fragment>(CM_Fragment.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response deleteFragment(@PathParam("idKey") long idKey,
								   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Fragment existOne = dao.selectFragmentByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			dao.deleteFragment(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Fragment>(CM_Fragment.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/grid/{idKey}")
	public Response selectGridFragment(@PathParam("idKey") long idKey) {
		try {
			CM_GridFragment existOne = gridDao.selectGridFragmentByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_GridFragment>(CM_GridFragment.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/grid/{idKey}")
	public Response updateGridFragment(CM_GridFragment gridFragment,
									   @PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(gridFragment.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_GridFragment existOne = gridDao.selectGridFragmentByIdKey(gridFragment.getFragmentIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setServiceUrl(Z2N(gridFragment.getServiceUrl()));
			existOne.setNeedAuth(Z2N(gridFragment.getNeedAuth()));
			existOne.setJqGrid(Z2N(gridFragment.getJqGrid()));
			existOne.setShowCheckBox(Z2N(gridFragment.getShowCheckBox()));
			existOne.setStretchColumn(Z2N(gridFragment.getStretchColumn()));
			existOne.setPaging(Z2N(gridFragment.getPaging()));
			existOne.setPopupFragmentId(Z2N(gridFragment.getPopupFragmentId()));
			existOne.setGridAddButton(Z2N(gridFragment.getGridAddButton()));
			existOne.setGridDelButton(Z2N(gridFragment.getGridDelButton()));
			existOne.setGridSaveButton(Z2N(gridFragment.getGridSaveButton()));
			existOne.setGridSaveUrl(Z2N(gridFragment.getGridSaveUrl()));
			existOne.setGridRefreshButton(Z2N(gridFragment.getGridRefreshButton()));
			existOne.setSearchArea(Z2N(gridFragment.getSearchArea()));
			existOne.setColModel(gridFragment.getList());
			CM_GridColModel colModel;
			int buttonPreSet;
			for (int ii=0; ii<existOne.getColModel().size(); ii++) {
				colModel = existOne.getColModel().get(ii);
				if ("D".equals(colModel.getTouch())) {
					continue;
				}
				buttonPreSet = 0;
				if ("U".equals(colModel.getTouch())) {
					List<CM_GridColButton>buttons = colButtonDao.selectGridColButtons(existOne.getFragmentIdKey(), colModel.getIdKey(), -1, 0);
					if (buttons != null) {
						for (int jj=0; jj<buttons.size(); jj++) {
							if (buttons.get(jj).getPreSet() == CM_GridColButton.GRIDBUTTON_DETAIL) {
								buttonPreSet += 1;
							} else if (buttons.get(jj).getPreSet() == CM_GridColButton.GRIDBUTTON_UPDATE) {
								buttonPreSet += 2;
							} else if (buttons.get(jj).getPreSet() == CM_GridColButton.GRIDBUTTON_DELETE) {
								buttonPreSet += 4;
							}
						}
						colModel.setButtons(buttons);
					}
				}
				if (buttonPreSet == colModel.getButtonPreSet()) {
					continue;
				}
				if (colModel.getButtons() == null) {
					colModel.setButtons(new ArrayList<>());
				}
				if ((buttonPreSet & 1) != (colModel.getButtonPreSet() & 1)) {
					if ((colModel.getButtonPreSet() & 1) == 1) { //과거에 없었으나 추가됨
						CM_GridColButton newOne = new CM_GridColButton(CM_GridColButton.GRIDBUTTON_DETAIL);
						newOne.setTouch("I");
						colModel.getButtons().add(newOne);
					}
					if ((buttonPreSet & 1) == 1) { //과거에 존재 현재는 없음.
						for (int jj=0; jj<colModel.getButtons().size(); jj++) {
							if (colModel.getButtons().get(jj).getPreSet() == 1) {
								colModel.getButtons().get(jj).setTouch("D");
							}
						}
					}
				}
				if ((buttonPreSet & 2) != (colModel.getButtonPreSet() & 2)) {
					if ((colModel.getButtonPreSet() & 2) == 2) { //과거에 없었으나 추가됨 
						CM_GridColButton newOne = new CM_GridColButton(CM_GridColButton.GRIDBUTTON_UPDATE);
						newOne.setTouch("I");
						colModel.getButtons().add(newOne);
					}
					if ((buttonPreSet & 2) == 2) { //과거에 존재 현재는 없음.
						for (int jj=0; jj<colModel.getButtons().size(); jj++) {
							if (colModel.getButtons().get(jj).getPreSet() == 2) {
								colModel.getButtons().get(jj).setTouch("D");
							}
						}
					}
				}
				if ((buttonPreSet & 4) != (colModel.getButtonPreSet() & 4)) {
					if ((colModel.getButtonPreSet() & 4) == 4) { //과거에 없었으나 추가됨 
						CM_GridColButton newOne = new CM_GridColButton(CM_GridColButton.GRIDBUTTON_DELETE);
						newOne.setTouch("I");
						colModel.getButtons().add(newOne);
					}
					if ((buttonPreSet & 4) == 4) { //과거에 존재 현재는 없음.
						for (int jj=0; jj<colModel.getButtons().size(); jj++) {
							if (colModel.getButtons().get(jj).getPreSet() == 3) {
								colModel.getButtons().get(jj).setTouch("D");
							}
						}
					}
				}
			}
			gridDao.updateGridFragment(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_GridFragment>(CM_GridFragment.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/grid/{gridIdKey}/colmodels/{page}")
	public Response selectGridColModels(@Context HttpServletRequest request,
										@PathParam("gridIdKey") long gridIdKey,
										@PathParam("page") int page) {
		try {
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_GridColModel> paging = new CM_PagingList<CM_GridColModel>();
			paging.setList(colModelDao.selectGridColModels(gridIdKey, offset, Constant.DEFAULT_SIZE));
			paging.setPaging(colModelDao.selectGridColModelPaging(gridIdKey));
			paging.numbering(offset);
			List<CM_GridColButton> buttons = colButtonDao.selectGridColButtons(gridIdKey, 0, -1, 0);
			if (paging.getList() != null) {
				CM_GridColModel item;
				for (int ii=0; ii<paging.getList().size(); ii++) {
					item = paging.getList().get(ii);
					item.setButtonPreSet(0);
					if (buttons != null && buttons.size() > 0) {
						CM_GridColButton button;
						for (int jj=0; jj<buttons.size(); jj++) {
							button = buttons.get(jj);
							if (item.getIdKey() == button.getColIdKey()) {
								if (item.getButtons() == null) {
									item.setButtons(new ArrayList<>());
								}
								item.getButtons().add(button);
								if (button.getPreSet() == 1) {
									item.setButtonPreSet(item.getButtonPreSet() + 1);
								} else if (button.getPreSet() == 2) {
									item.setButtonPreSet(item.getButtonPreSet() + 2);
								} else if (button.getPreSet() == 3) {
									item.setButtonPreSet(item.getButtonPreSet() + 4);
								}
							}
						}
					}
					if (item.getCodeType() != null && item.getCodeType().trim().length() > 0) {
						item.setSelectValues(selectCodeByTypeAsMap(item.getCodeType(), request.getSession()));
					}
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/popup/{idKey}")
	public Response selectPopupFragment(@PathParam("idKey") long idKey) {
		try {
			CM_PopupFragment existOne = popupDao.selectPopupFragmentByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_PopupFragment>(CM_PopupFragment.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public String Z2N(String str) {
		if (str != null) {
			if (str.trim().length() == 0) {
				return null;
			}
		}
		return str;
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/popup/{idKey}")
	public Response updatePopupFragment(CM_PopupFragment popupFragment,
										@PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(popupFragment.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_PopupFragment existOne = popupDao.selectPopupFragmentByIdKey(popupFragment.getFragmentIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setWidth(popupFragment.getWidth());
			existOne.setHeight(popupFragment.getHeight());
			existOne.setSelectTitle(Z2N(popupFragment.getSelectTitle()));
			existOne.setSelectUrl(Z2N(popupFragment.getSelectUrl()));
			existOne.setPrevUrl(Z2N(popupFragment.getPrevUrl()));
			existOne.setNextUrl(Z2N(popupFragment.getNextUrl()));
			existOne.setSelectErrorMsg(Z2N(popupFragment.getSelectErrorMsg()));
			existOne.setInsertTitle(Z2N(popupFragment.getInsertTitle()));
			existOne.setInsertUrl(Z2N(popupFragment.getInsertUrl()));
			existOne.setInsertErrorMsg(Z2N(popupFragment.getInsertErrorMsg()));
			existOne.setUpdateTitle(Z2N(popupFragment.getUpdateTitle()));
			existOne.setUpdateUrl(Z2N(popupFragment.getUpdateUrl()));
			existOne.setUpdateMethod(Z2N(popupFragment.getUpdateMethod()));
			existOne.setUpdateErrorMsg(Z2N(popupFragment.getUpdateErrorMsg()));
			existOne.setDeleteConfirm(Z2N(popupFragment.getDeleteConfirm()));
			existOne.setDeleteUrl(Z2N(popupFragment.getDeleteUrl()));
			existOne.setDeleteMethod(Z2N(popupFragment.getDeleteMethod()));
			existOne.setDeleteErrorMsg(Z2N(popupFragment.getDeleteErrorMsg()));
			existOne.setConfirmButton(Z2N(popupFragment.getConfirmButton()));
			existOne.setCancelButton(Z2N(popupFragment.getCancelButton()));
			existOne.setGridFragmentId(Z2N(popupFragment.getGridFragmentId()));
			existOne.setRows(popupFragment.getList());
			popupDao.updatePopupFragment(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_PopupFragment>(CM_PopupFragment.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/popup/{popupIdKey}/rows/{page}")
	public Response selectPopupRows(@Context HttpServletRequest request,
									@PathParam("popupIdKey") long popupIdKey,
									@PathParam("page") int page) {
		try {
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_PopupRow> paging = new CM_PagingList<CM_PopupRow>();
			paging.setList(rowDao.selectPopupRows(popupIdKey, offset, Constant.DEFAULT_SIZE));
			paging.setPaging(rowDao.selectPopupRowPaging(popupIdKey));
			paging.numbering(offset);
			paging.l10n(request.getSession());
			return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}