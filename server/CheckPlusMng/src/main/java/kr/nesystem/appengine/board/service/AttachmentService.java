package kr.nesystem.appengine.board.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import kr.nesystem.appengine.board.dao.AttachmentDao;
import kr.nesystem.appengine.board.dao.AttachmentGroupDao;
import kr.nesystem.appengine.board.model.CM_Attachment;
import kr.nesystem.appengine.board.model.CM_AttachmentGroup;
import kr.nesystem.appengine.common.Constant;
import kr.nesystem.appengine.common.model.ModelHandler;
import kr.nesystem.appengine.common.util.AuthToken;
import kr.nesystem.appengine.common.util.CommonFunc;
import kr.nesystem.appengine.common.util.ResponseUtil;

@Path("/{version}/attachment")
public class AttachmentService {
	AttachmentDao dao = new AttachmentDao();
	AttachmentGroupDao groupDao = new AttachmentGroupDao();

	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/upload")
	public Response uploadAttachment(@FormDataParam("reqToken") String authToken,
									 @FormDataParam("uploadfiles") FormDataBodyPart body) throws Exception {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_AttachmentGroup group = new CM_AttachmentGroup();
			group.setAttachments(new ArrayList<CM_Attachment>());
			for (BodyPart part : body.getParent().getBodyParts()) {
				InputStream is = part.getEntityAs(InputStream.class);
				ContentDisposition meta = part.getContentDisposition();
				if (meta.getFileName() != null) {
					CM_Attachment attachment = saveAttachment(is, meta);
					if (attachment == null) {
						return ResponseUtil.getResponse(Status.INTERNAL_SERVER_ERROR);
					}
					group.getAttachments().add(attachment);
				}
			}
			group.setGroupFileCount(group.getAttachments().size());
			group.setUserIdKey(AuthToken.getIdKey(authToken));
			dao.insert(group);
			return ResponseUtil.getResponse((new ModelHandler<CM_AttachmentGroup>(CM_AttachmentGroup.class)).convertToJson(group));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public CM_Attachment saveAttachment(InputStream is, ContentDisposition meta) throws Exception {
		File rootDir = new File(Constant.ATTACHMENT_BASE_DIR);
		if (!rootDir.exists()) {
			Files.createDirectory(rootDir.toPath());
			//rootDir.mkdir();
			CommonFunc.setFileReadable(Constant.ATTACHMENT_BASE_DIR, true);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		File yearDir = new File(Constant.ATTACHMENT_BASE_DIR + "/" + String.valueOf(cal.get(Calendar.YEAR)));
		if (yearDir.exists()) {
			if (!yearDir.isDirectory()) {
				return null;
			}
		} else {
			//boolean rt = yearDir.mkdir();
			Files.createDirectory(yearDir.toPath());
			CommonFunc.setFileReadable(yearDir, true);
		}
		File monthDir = new File(yearDir, String.valueOf(cal.get(Calendar.MONTH) + 1));
		if (monthDir.exists()) {
			if (!monthDir.isDirectory()) {
				return null;
			}
		} else {
			//monthDir.mkdir();
			Files.createDirectory(monthDir.toPath());
			CommonFunc.setFileReadable(monthDir, true);
		}
		UUID uuid = UUID.randomUUID();
		String serverFileName = "ATTACH_" + uuid.toString();
		File attachFile = new File(monthDir, serverFileName);
		int saveSize = CommonFunc.writeFile(is, attachFile);
		if (saveSize == -1) {
			return null;
		}
		String fileName = new String(meta.getFileName().getBytes("iso-8859-1"), "utf-8");
		String ext = CommonFunc.getFileExt(fileName);
		if (isImageFileExt(ext) == true) {
			Metadata metadata = ImageMetadataReader.readMetadata(attachFile);
			ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			int orientation = 1;
			try {
				orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			} catch (Exception e) {
			}
			BufferedImage originImage = ImageIO.read(attachFile);
			switch (orientation) {
			case 1:
				break;
			case 2: // Flip X
				originImage = Scalr.rotate(originImage, Rotation.FLIP_HORZ);
				break;
			case 3: // PI rotation
				originImage = Scalr.rotate(originImage, Rotation.CW_180);
				break;
			case 4: // Flip Y
				originImage = Scalr.rotate(originImage, Rotation.FLIP_VERT);
				break;
			case 5: // - PI/2 and Flip X
				originImage = Scalr.rotate(originImage, Rotation.CW_90);
				originImage = Scalr.rotate(originImage, Rotation.FLIP_HORZ);
				break;
			case 6: // -PI/2 and -width
				originImage = Scalr.rotate(originImage, Rotation.CW_90);
				break;
			case 7: // PI/2 and Flip
				originImage = Scalr.rotate(originImage, Rotation.CW_90);
				originImage = Scalr.rotate(originImage, Rotation.FLIP_VERT);
				break;
			case 8: // PI / 2
				originImage = Scalr.rotate(originImage, Rotation.CW_270);
				break;
			default:
				break;
			}
			
//			BufferedImage resizeImage = CommonFunc.resizeImage(originImage, 520);
//			CommonFunc.writeImage(resizeImage, monthDir + "/IMAGE_" + uuid.toString() + ".jpg", false);
			BufferedImage thumbnailImage = CommonFunc.thumbnailImage(originImage, 200, 200);
			String thumbFileName = "THUMBN_" + uuid.toString();
			File thumbFile = new File(monthDir, thumbFileName);
			CommonFunc.writeImage(thumbnailImage, thumbFile, false);
		}
		CM_Attachment attachment = new CM_Attachment();
		attachment.setFileName(fileName);
		attachment.setFileSize(saveSize);
		attachment.setDownCount(0);
		attachment.setServerFilePath(attachFile.getAbsolutePath());
		return attachment;
	}
	
	public String getImageType(String ext) {
		if ("jpg".equals(ext) || 
			"jpeg".equals(ext)) {
			return "jpeg";
		}
		if ("png".equals(ext)) {
			return "png";
		}
		if ("gif".equals(ext)) {
			return "gif";
		}
		if ("bmp".equals(ext)) {
			return "bmp";
		}
		return null;
	}
	
	public boolean isImageFileExt(String ext) {
		if (ext != null) {
			ext = ext.toLowerCase();
			if ("jpg".equals(ext) || 
				"jpeg".equals(ext) ||
				"png".equals(ext) ||
				"gif".equals(ext) ||
				"bmp".equals(ext)) {
				return true;
			}
		}
		return false;
	}
	
	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/upload/{groupIdKey}")
	public Response appendAttachment(@PathParam("groupIdKey") long groupIdKey,
									 @FormDataParam("authToken") String authToken,
									 @FormDataParam("uploadfiles") FormDataBodyPart body) throws Exception {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_AttachmentGroup existGroup = groupDao.select(null, groupIdKey);
			if (existGroup == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (existGroup.getUserIdKey() != AuthToken.getIdKey(authToken)) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			existGroup.setAttachments(new ArrayList<CM_Attachment>());
			for(BodyPart part : body.getParent().getBodyParts()){
				InputStream is = part.getEntityAs(InputStream.class);
				ContentDisposition meta = part.getContentDisposition();
				if (meta.getFileName() != null) {
					CM_Attachment attachment = saveAttachment(is, meta);
					if (attachment == null) {
						return ResponseUtil.getResponse(Status.INTERNAL_SERVER_ERROR);
					}
					existGroup.getAttachments().add(attachment);
				}
			}
			existGroup.setGroupFileCount(existGroup.getGroupFileCount() + existGroup.getAttachments().size());
			dao.update(existGroup);
			return ResponseUtil.getResponse((new ModelHandler<CM_AttachmentGroup>(CM_AttachmentGroup.class)).convertToJson(existGroup));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/group/{groupIdKey}/item/{attachmentIdKey}")
	public Response deleteAttachment(@PathParam("groupIdKey") long groupIdKey,
									 @PathParam("attachmentIdKey") long attachmentIdKey,
									 @QueryParam("q") String authToken) {
		try {
			if (AuthToken.isValidToken(authToken) == false) {
				return ResponseUtil.getResponse(Status.EXPECTATION_FAILED);
			}
			CM_Attachment attachment = dao.select(null, attachmentIdKey);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (false == deleteAttachGroupFile(groupIdKey, attachmentIdKey, authToken)) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse((new ModelHandler<CM_Attachment>(CM_Attachment.class)).convertToJson(attachment));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	public boolean deleteAttachGroupFile(long groupIdKey, long attachmentIdKey, String authToken) throws Exception {
		CM_AttachmentGroup group = groupDao.select(null, groupIdKey);
		if (group == null) {
			return false;
		}
		if (!"1".equals(AuthToken.getUserType(authToken))) {
			if (group.getUserIdKey() != AuthToken.getIdKey(authToken)) {
				return false;
			}
		}
		if (attachmentIdKey > 0) {
			CM_Attachment attachment = dao.select(null, attachmentIdKey);
			if (attachment == null) {
				return false;
			}
			dao.delete(attachment);
			deleteAttachFile(attachment.getServerFilePath());
			List<CM_Attachment> list = dao.selectAttachments(null, groupIdKey);
			group.setGroupFileCount(list.size());
			dao.update(group);
		} else {
			CM_Attachment attachment;
			List<CM_Attachment> list = dao.selectAttachments(null, groupIdKey);
			dao.delete(list);
			groupDao.delete(group);
			for (int ii=0; ii<list.size(); ii++) {
				attachment = list.get(ii);
				deleteAttachFile(attachment.getServerFilePath());
			}
		}
		return true;
	}
	
	public void deleteAttachFile(String serverFilePath) {
		try {
			File file = new File(serverFilePath);
			file.delete();
			file = new File(serverFilePath.replaceAll("ATTACH_", "THUMBN_"));
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/group/{groupIdKey}")
	public Response selectAttachmentGroup(@PathParam("groupIdKey") long groupIdKey) {
		try {
			CM_AttachmentGroup group = groupDao.select(null, groupIdKey);
			if (group == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			group.setAttachments(dao.selectAttachments(null, groupIdKey));
			return ResponseUtil.getResponse((new ModelHandler<CM_AttachmentGroup>(CM_AttachmentGroup.class)).convertToJson(group));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/{attachmentIdKey}/pdf")
	public Response downloadAttachmentPdf(@PathParam("attachmentIdKey") long attachmentIdKey) {
		try {
			CM_Attachment attachment = dao.select(null, attachmentIdKey);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			attachment.setDownCount(attachment.getDownCount() + 1);
			dao.update(attachment);
			byte[] temp = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(temp);
			fis.close();
			String fileName = new String(attachment.getFileName().getBytes("utf-8"), "iso-8859-1");
			return ResponseUtil.getResponseForPdf(Status.OK, temp, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/{attachmentIdKey}")
	public Response downloadAttachment(@PathParam("attachmentIdKey") long attachmentIdKey) {
		try {
			CM_Attachment attachment = dao.select(null, attachmentIdKey);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			attachment.setDownCount(attachment.getDownCount() + 1);
			dao.update(attachment);
			byte[] temp = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(temp);
			fis.close();
			String fileName = new String(attachment.getFileName().getBytes("utf-8"), "iso-8859-1");
			String ext = CommonFunc.getFileExt(attachment.getFileName());
			if (isImageFileExt(ext) == true) {
				return ResponseUtil.getResponseForImage(Status.OK, temp, fileName, getImageType(ext));
			} else {
				return ResponseUtil.getResponseForFile(Status.OK, temp, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/{attachmentIdKey}/check")
	public Response checkAttachment(@PathParam("attachmentIdKey") long attachmentIdKey) {
		try {
			CM_Attachment attachment = dao.select(null, attachmentIdKey);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/{attachmentIdKey}/thumbnail")
	public Response downloadAttachmentThumbnail(@PathParam("attachmentIdKey") long attachmentIdKey) {
		try {
			CM_Attachment attachment = dao.select(null, attachmentIdKey);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			String fileName = new String(attachment.getFileName().getBytes("utf-8"), "iso-8859-1");
			String ext = CommonFunc.getFileExt(attachment.getFileName());
			if (isImageFileExt(ext) == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			String thumbnailPath = attachment.getServerFilePath().replaceAll("ATTACH_", "THUMBN_");
			file = new File(thumbnailPath);
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			byte[] temp = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(temp);
			fis.close();
			return ResponseUtil.getResponseForImage(Status.OK, temp, fileName, getImageType(ext));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/group/{groupIdKey}/{imageIndex}")
	public Response downloadAttachmentGroup(@PathParam("groupIdKey") long groupIdKey,
											@PathParam("imageIndex") int imageIndex) {
		try {
			CM_AttachmentGroup group = groupDao.select(null, groupIdKey);
			if (group == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			group.setAttachments(dao.selectAttachments(null, groupIdKey));
			if (group.getAttachments() == null || group.getAttachments().size() == 0) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (group.getAttachments().size() <= imageIndex) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_Attachment attachment = group.getAttachments().get(imageIndex);
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			attachment.setDownCount(attachment.getDownCount() + 1);
			dao.update(attachment);
			byte[] temp = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(temp);
			fis.close();
			String fileName = new String(attachment.getFileName().getBytes("utf-8"), "iso-8859-1");
			String ext = CommonFunc.getFileExt(attachment.getFileName());
			if (isImageFileExt(ext) == true) {
				return ResponseUtil.getResponseForImage(Status.OK, temp, fileName, getImageType(ext));
			} else {
				return ResponseUtil.getResponseForFile(Status.OK, temp, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/group/{groupIdKey}/{imageIndex}/check")
	public Response checkAttachmentGroup(@PathParam("groupIdKey") long groupIdKey,
										 @PathParam("imageIndex") int imageIndex) {
		try {
			CM_AttachmentGroup group = groupDao.select(null, groupIdKey);
			if (group == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			group.setAttachments(dao.selectAttachments(null, groupIdKey));
			if (group.getAttachments() == null || group.getAttachments().size() == 0) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (group.getAttachments().size() <= imageIndex) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_Attachment attachment = group.getAttachments().get(imageIndex);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			return ResponseUtil.getResponse(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
	
	@GET
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	@Path("/group/{groupIdKey}/{imageIndex}/thumbnail")
	public Response downloadAttachmentGroupThumbnail(@PathParam("groupIdKey") long groupIdKey,
													 @PathParam("imageIndex") int imageIndex) {
		try {
			CM_AttachmentGroup group = groupDao.select(null, groupIdKey);
			if (group == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			group.setAttachments(dao.selectAttachments(null, groupIdKey));
			if (group.getAttachments() == null || group.getAttachments().size() == 0) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			if (group.getAttachments().size() <= imageIndex) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			CM_Attachment attachment = group.getAttachments().get(imageIndex);
			if (attachment == null) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			String fileName = new String(attachment.getFileName().getBytes("utf-8"), "iso-8859-1");
			String ext = CommonFunc.getFileExt(attachment.getFileName());
			if (isImageFileExt(ext) == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			File file = new File(attachment.getServerFilePath());
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			String thumbnailPath = attachment.getServerFilePath().replaceAll("ATTACH_", "THUMBN_");
			file = new File(thumbnailPath);
			if (file.exists() == false) {
				return ResponseUtil.getResponse(Status.NOT_FOUND);
			}
			byte[] temp = new byte[(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(temp);
			fis.close();
			return ResponseUtil.getResponseForImage(Status.OK, temp, fileName, getImageType(ext));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtil.internalError(e.getMessage());
		}
	}
}
