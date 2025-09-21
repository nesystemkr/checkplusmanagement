package kr.nesystem.checkplusmng.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import kr.peelknight.util.ResponseUtil;

@Path("/hello")
public class HelloService {
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHelloMessage(@PathParam("name") String name, @QueryParam("id") String id) {
		String message = String.format("{\"message\": \"Hello, %s! Your ID is %s\"}", name, id);
		return Response.ok(message).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{version}/{page}")
	public Response selectGroups(@PathParam("page") int page,
								 @QueryParam("q") String authToken) {
		try {
			StringBuilder sb = new StringBuilder("");
			sb.append(page);
			sb.append(authToken);
			return ResponseUtil.getResponse(sb.toString());
			//return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/kakaka")
	public Response selectGroups2(@QueryParam("q") String authToken) {
		try {
			StringBuilder sb = new StringBuilder("");
			sb.append(authToken);
			return ResponseUtil.getResponse(sb.toString());
			//return ResponseUtil.getResponse((new ModelHandler<CM_PagingList>(CM_PagingList.class)).convertToJson(paging));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
