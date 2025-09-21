package kr.peelknight.common.service;

import java.util.ArrayList;
import java.util.Date;
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

import kr.peelknight.common.Constant;
import kr.peelknight.common.dao.AttachmentDao;
import kr.peelknight.common.dao.BoardContentDao;
import kr.peelknight.common.dao.BoardContentReplyDao;
import kr.peelknight.common.dao.BoardDao;
import kr.peelknight.common.model.CM_Board;
import kr.peelknight.common.model.CM_BoardAuth;
import kr.peelknight.common.model.CM_BoardContent;
import kr.peelknight.common.model.CM_BoardContentReply;
import kr.peelknight.common.model.CM_PagingList;
import kr.peelknight.common.model.ModelHandler;
import kr.peelknight.util.AuthToken;
import kr.peelknight.util.ResponseUtil;

@Path("/{version}/board")
public class BoardService {
	BoardDao dao = new BoardDao();
	AttachmentDao attachDao = new AttachmentDao();

	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/list/{page}")
	public Response selectBoards(@Context HttpServletRequest request,
								 @PathParam("page") int page,
								 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_Board> paging = new CM_PagingList<CM_Board>();
			paging.setList(dao.selectBoards(offset, Constant.DEFAULT_SIZE));
			paging.setPaging(dao.selectBoardPaging());
			paging.numbering(offset);
			if (paging.getList() != null) {
				for (int ii=0; ii<paging.getList().size(); ii++) {
					CM_Board board = paging.getList().get(ii);
					List<CM_BoardAuth> listAuth = dao.selectBoardAuthWithBoard(board);
					CM_BoardAuth boardAuth;
					if (listAuth != null) {
						for (int jj=0; jj<listAuth.size(); jj++) {
							boardAuth = listAuth.get(jj);
							boardAuth.l10n(request.getSession());
						}
					}
					board.setBoardAuths(listAuth);
					board.l10n(request.getSession());
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
	@Path("/{idKey}")
	public Response selectBoard(@Context HttpServletRequest request,
								@PathParam("idKey") long idKey,
								@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Board existOne = dao.selectBoardByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			List<CM_BoardAuth> listAuth = dao.selectBoardAuthWithBoard(existOne);
			CM_BoardAuth boardAuth;
			if (listAuth != null) {
				for (int jj=0; jj<listAuth.size(); jj++) {
					boardAuth = listAuth.get(jj);
					boardAuth.l10n(request.getSession());
				}
			}
			existOne.setBoardAuths(listAuth);
			existOne.l10n(request.getSession());
			return ResponseUtil.getResponse((new ModelHandler<CM_Board>(CM_Board.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/id/{boardId}")
	public Response selectBoardByBoardId(@PathParam("boardId") String boardId,
										 @QueryParam("q") String authToken) {
		try {
			CM_Board existOne = dao.selectBoardByBoardId(boardId);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			String userType = AuthToken.getUserType(authToken);
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(existOne.getIdKey(), userType);
			existOne.setAllowPrivateViewYN(false);
			existOne.setAllowUpdateYN(false);
			existOne.setAllowUpdateOthersYN(false);
			if (boardAuth != null) {
				existOne.setAllowPrivateViewYN("Y".equals(boardAuth.getAllowPrivateViewYN()));
				existOne.setAllowUpdateYN("Y".equals(boardAuth.getAllowUpdateYN()));
				existOne.setAllowUpdateOthersYN("Y".equals(boardAuth.getAllowUpdateOthersYN()));
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_Board>(CM_Board.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response insertBoard(CM_Board board) throws Exception {
		try {
			if (AuthToken.isValidToken(board.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Board existOne = dao.selectBoardByBoardId(board.getBoardId());
			if (existOne != null) {
				return ResponseUtil.getResponse(Status.CONFLICT);
			}
			dao.insertBoard(board);
			return ResponseUtil.getResponse((new ModelHandler<CM_Board>(CM_Board.class)).convertToJson(board));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}")
	public Response updateBoard(CM_Board board,
								@PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(board.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Board existOne = dao.selectBoardByIdKey(board.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setBoardType(board.getBoardType());
			existOne.setBoardName(board.getBoardName());
			existOne.setBoardId(board.getBoardId());
			existOne.setAttachmentYN(board.getAttachmentYN());
			existOne.setAnswerYN(board.getAnswerYN());
			existOne.setReplyYN(board.getReplyYN());
			existOne.setTopYN(board.getTopYN());
			existOne.setLoginViewYN(board.getLoginViewYN());
			existOne.setSecretYN(board.getSecretYN());
			existOne.setBoardAuths(board.getBoardAuths());
			existOne.setStatus(board.getStatus());
			dao.updateBoard(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Board>(CM_Board.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/status")
	public Response updateBoardStatus(CM_Board board,
									  @PathParam("idKey") long idKey) throws Exception {
		try {
			if (AuthToken.isValidToken(board.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Board existOne = dao.selectBoardByIdKey(board.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existOne.setStatus(board.getStatus());
			dao.updateBoard(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_Board>(CM_Board.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/auth/{userType}/privateview/")
	public Response updateBoardAuthPrivateView(CM_BoardAuth boardAuth,
											   @PathParam("idKey") long idKey,
											   @PathParam("userType") String userType) throws Exception {
		try {
			if (AuthToken.isValidToken(boardAuth.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (boardAuth.getBoardIdKey() != idKey) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (!userType.equals(boardAuth.getUserType())) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			CM_BoardAuth existOne = dao.selectBoardAuthByBoardIdKeyAndUserType(idKey, userType);
			if (existOne == null) {
				existOne = boardAuth;
				existOne.setAllowUpdateYN("N");
				existOne.setAllowUpdateOthersYN("N");
				dao.insertBoardAuth(existOne);
			} else {
				existOne.setAllowPrivateViewYN(boardAuth.getAllowPrivateViewYN());
				dao.updateBoardAuth(existOne);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardAuth>(CM_BoardAuth.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/auth/{userType}/updateothers/")
	public Response updateBoardAuthUpdateOthers(CM_BoardAuth boardAuth,
												@PathParam("idKey") long idKey,
												@PathParam("userType") String userType) throws Exception {
		try {
			if (AuthToken.isValidToken(boardAuth.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (boardAuth.getBoardIdKey() != idKey) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (!userType.equals(boardAuth.getUserType())) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			CM_BoardAuth existOne = dao.selectBoardAuthByBoardIdKeyAndUserType(idKey, userType);
			if (existOne == null) {
				existOne = boardAuth;
				existOne.setAllowPrivateViewYN("N");
				existOne.setAllowUpdateYN("N");
				dao.insertBoardAuth(existOne);
			} else {
				existOne.setAllowUpdateOthersYN(boardAuth.getAllowUpdateOthersYN());
				dao.updateBoardAuth(existOne);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardAuth>(CM_BoardAuth.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{idKey}/auth/{userType}/update/")
	public Response updateBoardAuthUpdate(CM_BoardAuth boardAuth,
										  @PathParam("idKey") long idKey,
										  @PathParam("userType") String userType) throws Exception {
		try {
			if (AuthToken.isValidToken(boardAuth.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			if (boardAuth.getBoardIdKey() != idKey) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			if (!userType.equals(boardAuth.getUserType())) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			CM_BoardAuth existOne = dao.selectBoardAuthByBoardIdKeyAndUserType(idKey, userType);
			if (existOne == null) {
				existOne = boardAuth;
				existOne.setAllowPrivateViewYN("N");
				existOne.setAllowUpdateOthersYN("N");
				dao.insertBoardAuth(existOne);
			} else {
				existOne.setAllowUpdateYN(boardAuth.getAllowUpdateYN());
				dao.updateBoardAuth(existOne);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardAuth>(CM_BoardAuth.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	BoardContentDao contentDao = new BoardContentDao();

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/byid/{boardId}/content/list/{page}")
	public Response selectBoardContentsById(@Context HttpServletRequest request,
											@PathParam("boardId") String boardId,
											@PathParam("page") int page,
											@QueryParam("q") String authToken) {
		try {
			CM_Board board = dao.selectBoardByBoardId(boardId);
			return selectBoardContents(request, board, page, authToken);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/list/{page}")
	public Response selectBoardContents(@Context HttpServletRequest request,
										@PathParam("boardIdKey") long boardIdKey,
										@PathParam("page") int page,
										@QueryParam("q") String authToken) {
		try {
			CM_Board board = dao.selectBoardByIdKey(boardIdKey);
			return selectBoardContents(request, board, page, authToken);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public Response selectBoardContents(HttpServletRequest request,
										CM_Board board,
										int page,
										String authToken) throws Exception {
		if (board == null) {
			return ResponseUtil.getResponse(Status.NOT_FOUND);
		}
		if ("Y".equals(board.getLoginViewYN())) {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
		}
		String status = null;
		if (!"1".equals(AuthToken.getUserType(authToken))) {
			status = "1";
		}
		int offset = (page - 1) * Constant.DEFAULT_SIZE;
		CM_PagingList<CM_BoardContent> paging = new CM_PagingList<CM_BoardContent>();
		paging.setList(contentDao.selectBoardContents(board.getIdKey(), status, offset, Constant.DEFAULT_SIZE));
		if (paging.getList() != null) {
			List<Long> parentIdKeys = new ArrayList<>();
			for (int ii=0; ii<paging.getList().size(); ii++) {
				parentIdKeys.add(paging.getList().get(ii).getIdKey());
			}
			if (parentIdKeys.size() > 0) {
				List<CM_BoardContent> children = contentDao.selectBoardChildContents(board.getIdKey(), parentIdKeys, status);
				CM_BoardContent parent;
				CM_BoardContent child; 
				for (int ii=0; ii<paging.getList().size(); ii++) {
					parent = paging.getList().get(ii);
					parent.setAnswers(new ArrayList<CM_BoardContent>());
					for (int jj=0; jj<children.size(); jj++) {
						child = children.get(jj);
						child.l10n(request.getSession());
						if (parent.getIdKey() == child.getParentIdKey()) {
							parent.getAnswers().add(child);
							children.remove(jj);
							jj--;
						}
					}
					parent.l10n(request.getSession());
				}
			}
		}
		paging.setPaging(contentDao.selectBoardContentPaging(board.getIdKey()));
		paging.numbering(offset);
		return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/byid/{boardId}/content/toplist")
	public Response selectBoardTopContents(@Context HttpServletRequest request,
										   @PathParam("boardId") String boardId,
										   @QueryParam("q") String authToken) {
		try {
			CM_Board board = dao.selectBoardByBoardId(boardId);
			return selectBoardTopContents(request, board, authToken);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/toplist")
	public Response selectBoardTopContents(@Context HttpServletRequest request,
										   @PathParam("boardIdKey") long boardIdKey,
										   @QueryParam("q") String authToken) {
		try {
			CM_Board board = dao.selectBoardByIdKey(boardIdKey);
			return selectBoardTopContents(request, board, authToken);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected Response selectBoardTopContents(HttpServletRequest request,
											  CM_Board board,
											  String authToken) throws Exception {
		if (board == null) {
			return ResponseUtil.getResponse(Status.NOT_FOUND);
		}
		if ("Y".equals(board.getLoginViewYN())) {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
		}
		String status = null;
		if (!"1".equals(AuthToken.getUserType(authToken))) {
			status = "1";
		}
		CM_PagingList<CM_BoardContent> paging = new CM_PagingList<CM_BoardContent>();
		paging.setList(contentDao.selectBoardTopContents(board.getIdKey(), status));
		CM_BoardContent item; 
		for (int ii=0; ii<paging.getList().size(); ii++) {
			item = paging.getList().get(ii);
			item.l10n(request.getSession());
		}
		return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/{idKey}")
	public Response selectBoardContent(@PathParam("boardIdKey") long boardIdKey,
									   @PathParam("idKey") long idKey,
									   @QueryParam("q") String authToken) {
		try {
			CM_Board board = dao.selectBoardByIdKey(boardIdKey);
			if (board == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if ("Y".equals(board.getLoginViewYN())) {
				if (AuthToken.isValidToken(authToken) == false) {
					return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
				}
			}
			CM_BoardContent existOne = contentDao.selectBoardContentByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if ("Y".equals(existOne.getPrivateYN())) {
				if (existOne.getCreator() != AuthToken.getIdKey(authToken)) {
					CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(authToken));
					if (boardAuth == null || !"Y".equals(boardAuth.getAllowPrivateViewYN())) {
						return ResponseUtil.getResponse(Status.FORBIDDEN);
					}
				}
			}
			existOne.setViewCount(existOne.getViewCount() + 1);
			contentDao.updateBoardContent(existOne);
			if (existOne.getAttachGroupIdKey() > 0) {
				existOne.setAttachmentGroup(attachDao.selectAttachmentGroupByIdKey(existOne.getAttachGroupIdKey()));
				if (existOne.getAttachmentGroup() != null) {
					existOne.getAttachmentGroup().setAttachments(attachDao.selectAttachments(existOne.getAttachGroupIdKey()));
				}
			}
			existOne.setReplies(replyDao.selectBoardContentReplys(existOne.getIdKey(), -1, 0));
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContent>(CM_BoardContent.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content")
	public Response insertBoardContent(@PathParam("boardIdKey") long boardIdKey,
									   CM_BoardContent boardContent) throws Exception {
		try {
			if (AuthToken.isValidToken(boardContent.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(boardContent.getReqToken()));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			long userIdKey = AuthToken.getIdKey(boardContent.getReqToken());
			if (userIdKey != boardContent.getCreator()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			boardContent.setViewCount(0);
			boardContent.setCreator(userIdKey);
			boardContent.setCreateDate(new Date());
			contentDao.insertBoardContent(boardContent);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContent>(CM_BoardContent.class)).convertToJson(boardContent));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/{idKey}")
	public Response updateBoardContent(@PathParam("boardIdKey") long boardIdKey,
									   @PathParam("idKey") long idKey,
									   CM_BoardContent boardContent) throws Exception {
		try {
			if (AuthToken.isValidToken(boardContent.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(boardContent.getReqToken()));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_BoardContent existOne = contentDao.selectBoardContentByIdKey(boardContent.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existOne.getCreator() != AuthToken.getIdKey(boardContent.getReqToken())) {
				if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateOthersYN())) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
			}
			long userIdKey = AuthToken.getIdKey(boardContent.getReqToken());
			existOne.setTitle(boardContent.getTitle());
			existOne.setContent(boardContent.getContent());
			if (existOne.getCreator() == AuthToken.getIdKey(boardContent.getReqToken())) {
				existOne.setPrivateYN(boardContent.getPrivateYN());
			}
			existOne.setTopYN(boardContent.getTopYN());
			existOne.setAttachGroupIdKey(boardContent.getAttachGroupIdKey());
			existOne.setModifier(userIdKey);
			existOne.setModifiedDate(new Date());
			contentDao.updateBoardContent(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContent>(CM_BoardContent.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/{idKey}/status")
	public Response updateBoardContentStatus(@PathParam("boardIdKey") long boardIdKey,
											 @PathParam("idKey") long idKey,
											 CM_BoardContent boardContent) throws Exception {
		try {
			if (AuthToken.isValidToken(boardContent.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(boardContent.getReqToken()));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_BoardContent existOne = contentDao.selectBoardContentByIdKey(boardContent.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existOne.getCreator() != AuthToken.getIdKey(boardContent.getReqToken())) {
				if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateOthersYN())) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
			}
			long userIdKey = AuthToken.getIdKey(boardContent.getReqToken());
			existOne.setStatus(boardContent.getStatus());
			existOne.setModifiedDate(new Date());
			existOne.setModifier(userIdKey);
			contentDao.updateBoardContent(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContent>(CM_BoardContent.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/{idKey}")
	public Response deleteBoardContent(@PathParam("boardIdKey") long boardIdKey,
									   @PathParam("idKey") long idKey,
									   @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(authToken));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_BoardContent existOne = contentDao.selectBoardContentByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existOne.getCreator() != AuthToken.getIdKey(authToken)) {
				if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateOthersYN())) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
			}
			contentDao.deleteBoardContent(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContent>(CM_BoardContent.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	BoardContentReplyDao replyDao = new BoardContentReplyDao();
	
	@SuppressWarnings("rawtypes")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/reply/list/{page}")
	public Response selectBoardContentReplyList(@PathParam("boardIdKey") long boardIdKey,
												@PathParam("page") int page,
												@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			int offset = (page - 1) * Constant.DEFAULT_SIZE;
			CM_PagingList<CM_BoardContentReply> paging = new CM_PagingList<CM_BoardContentReply>();
			paging.setList(replyDao.selectBoardContentReplys(boardIdKey, offset, Constant.DEFAULT_SIZE));
			paging.setPaging(replyDao.selectBoardContentReplyPaging(boardIdKey));
			paging.numbering(offset);
			CM_BoardContentReply reply;
			for (int ii=0; ii<paging.getList().size(); ii++) {
				reply = paging.getList().get(ii);
				if (reply.getAttachGroupIdKey() > 0) {
					reply.setAttachmentGroup(attachDao.selectAttachmentGroupByIdKey(reply.getAttachGroupIdKey()));
					if (reply.getAttachmentGroup() != null) {
						reply.getAttachmentGroup().setAttachments(attachDao.selectAttachments(reply.getAttachGroupIdKey()));
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
	@Path("/{boardIdKey}/content/reply/{idKey}")
	public Response selectBoardContentReply(@PathParam("boardIdKey") long boardIdKey,
											@PathParam("idKey") long idKey,
											@QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardContentReply existOne = replyDao.selectBoardContentReplyByIdKey(idKey);
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existOne.getAttachGroupIdKey() > 0) {
				existOne.setAttachmentGroup(attachDao.selectAttachmentGroupByIdKey(existOne.getAttachGroupIdKey()));
				if (existOne.getAttachmentGroup() != null) {
					existOne.getAttachmentGroup().setAttachments(attachDao.selectAttachments(existOne.getAttachGroupIdKey()));
				}
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContentReply>(CM_BoardContentReply.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/reply")
	public Response insertBoardContentReply(@PathParam("boardIdKey") long boardIdKey,
											CM_BoardContentReply boardContentReply) throws Exception {
		try {
			if (AuthToken.isValidToken(boardContentReply.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(boardContentReply.getReqToken()));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			long userIdKey = AuthToken.getIdKey(boardContentReply.getReqToken());
			if (userIdKey != boardContentReply.getCreator()) {
				return ResponseUtil.getResponse(Status.BAD_REQUEST);
			}
			boardContentReply.setCreator(userIdKey);
			boardContentReply.setCreateDate(new Date());
			replyDao.insertBoardContentReply(boardContentReply);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContentReply>(CM_BoardContentReply.class)).convertToJson(boardContentReply));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/reply/{idKey}")
	public Response updateBoardContentReply(@PathParam("boardIdKey") long boardIdKey,
											@PathParam("idKey") long idKey,
											CM_BoardContentReply boardContentReply) throws Exception {
		try {
			if (AuthToken.isValidToken(boardContentReply.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(boardContentReply.getReqToken()));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_BoardContentReply existOne = replyDao.selectBoardContentReplyByIdKey(boardContentReply.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existOne.getCreator() != AuthToken.getIdKey(boardContentReply.getReqToken())) {
				if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateOthersYN())) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
			}
			existOne.setReply(boardContentReply.getReply());
			existOne.setParentIdKey(boardContentReply.getParentIdKey());
			existOne.setAttachGroupIdKey(boardContentReply.getAttachGroupIdKey());
			existOne.setModifier(AuthToken.getIdKey(boardContentReply.getReqToken()));
			existOne.setModifiedDate(new Date());
			replyDao.updateBoardContentReply(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContentReply>(CM_BoardContentReply.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{boardIdKey}/content/reply/{idKey}/status")
	public Response updateBoardContentReplyStatus(@PathParam("boardIdKey") long boardIdKey,
												  @PathParam("idKey") long idKey,
												  CM_BoardContentReply boardContentReply) throws Exception {
		try {
			if (AuthToken.isValidToken(boardContentReply.getReqToken()) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_BoardAuth boardAuth = dao.selectBoardAuthByBoardIdKeyAndUserType(boardIdKey, AuthToken.getUserType(boardContentReply.getReqToken()));
			if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateYN())) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_BoardContentReply existOne = replyDao.selectBoardContentReplyByIdKey(boardContentReply.getIdKey());
			if (existOne == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existOne.getCreator() != AuthToken.getIdKey(boardContentReply.getReqToken())) {
				if (boardAuth == null || !"Y".equals(boardAuth.getAllowUpdateOthersYN())) {
					return ResponseUtil.getResponse(Status.BAD_REQUEST);
				}
			}
			long userIdKey = AuthToken.getIdKey(boardContentReply.getReqToken());
			existOne.setStatus(boardContentReply.getStatus());
			existOne.setModifiedDate(new Date());
			existOne.setModifier(userIdKey);
			replyDao.updateBoardContentReply(existOne);
			return ResponseUtil.getResponse((new ModelHandler<CM_BoardContentReply>(CM_BoardContentReply.class)).convertToJson(existOne));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
