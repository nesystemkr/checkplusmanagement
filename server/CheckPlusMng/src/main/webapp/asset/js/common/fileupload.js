function initializeFileUpload(options) {
	return new JSFileUpload.fileUploader(options);
}
var JSFileUpload
if (!JSFileUpload) {
	JSFileUpload = {};
}
JSFileUpload.fileUploader =  function(options) {
	var vMaxFileSize = 10*1024*1024
	var vMaxTotalFileSize = 50*1024*1024
	var vMaxFileCount = 10
	var vImageFilter = /^(image\/bmp|image\/gif|image\/jpg|image\/jpeg|image\/png)$/i;
	var vUploadUrl = "/svc/v1/attachment/upload"
	var vDnloadUrl = "/svc/v1/attachment"
	var vDeleteUrl = "/svc/v1/attachment/group"
	var vMaxFileNameLen = 100

	var dragEventAttached = false

	var vDiv
	var vSupportDragAndDropAPI
	var vAttachGroupIdKey
	var vIsImage = false
	var vIsSingle = false
	var vContextPath = ""
	var vIsEditable = true
	var vIsShowTip = true;
	
	var vContentDiv
	var vInputFile
	var vDragArea
	var vDragTip
	var vListType
	var vFileCountText
	var vFileSizeText
	var vFileInfoCnt = 0
	var vHtFileInfo = []
	var vTotalSize = 0
	var vFileCount = 0
	var vUploadedFileCount = 0;
	
	if (options) {
		if (options.id != undefined) {
			vDiv = $("#" + options.id)
		}
		if (options.supportDragAndDropAPI != undefined) {
			vSupportDragAndDropAPI = options.supportDragAndDropAPI
		} else {
			checkDragAndDropAPI()
		}
		if (options.attachGroupIdKey != undefined && options.attachGroupIdKey > 0) {
			vAttachGroupIdKey = options.attachGroupIdKey 
		}
		if (options.isImage != undefined) {
			vIsImage = options.isImage
		}
		if (options.isSingle != undefined) {
			vIsSingle = options.isSingle
		}
		if (options.contextPath != undefined) {
			vContextPath = options.contextPath
		}
		if (options.maxTotalFileSize != undefined) {
			vMaxTotalFileSize = options.maxTotalFileSize
		}
		if (options.maxFileSize != undefined) {
			vMaxFileSize = options.maxFileSize
		}
		if (options.maxFileCount != undefined) {
			vMaxFileCount = options.maxFileCount
		}
		if (options.uploadUrl != undefined) {
			vUploadUrl = options.uploadUrl
		}
		if (options.dnloadUrl != undefined) {
			vDnloadUrl = options.dnloadUrl
		}
		if (options.deleteUrl != undefined) {
			vDeleteUrl = options.deleteUrl
		}
		if (options.maxFileNameLen != undefined) {
			vMaxFileNameLen = options.maxFileNameLen
		}
		if (options.isEditable != undefined) {
			vIsEditable = options.isEditable
		}
		if (options.isShowTip != undefined) {
			vIsShowTip = options.isShowTip
		}
	}
	build()
	
	function build() {
		if (vDiv) {
			vDiv.empty()
			vContentDiv = addDiv(vDiv)
		}
		if (vDiv && vSupportDragAndDropAPI) {
			var pTmp = addEle(vContentDiv, "p", "file_upload_dsc")
			if (vIsEditable) {
				vFileCountText = addEle(pTmp, "em")
				vFileCountText.html("0")
				pTmp.append('/' + vMaxFileCount + '개 <span class="file_upload_bar">|</span>')
				vFileSizeText = addEle(pTmp, "em")
				vFileSizeText.html("0MB")
				pTmp.append('/' + getUnitString(vMaxTotalFileSize))
			}
			vDragArea = addDiv(vDiv, "file_upload_drag_area")
			vListType = addEle(vDragArea, "ul", "file_upload_list_type")
			if (vIsShowTip && vIsEditable) {
				vDragTip = addEle(vDragArea, "em", "file_upload_tip")
				if (this.vIsImage) {
					vDragTip.html("마우스로 드래그해서 이미지를 추가해주세요.")
				} else {
					vDragTip.html("마우스로 드래그해서 파일을 추가해주세요.")
				}
			}
			addDragEvent();
		} else {
			vInputFile = addInputFile(vContentDiv, "file_upload_input")
			vInputFile.addClass()
			if (vIsSingle == false) {
				vInputFile.prop("multiple", true)
			}
			if (vContentDiv && vIsShowTip && vIsEditable) {
				if (vIsImage) {
					vContentDiv.append('<p class="file_upload_dsc"><strong>10MB</strong>이하의 이미지 파일만 등록할 수 있습니다.</p>')
				} else {
					vContentDiv.append('<p class="file_upload_dsc"><strong>10MB</strong>이하의 파일만 등록할 수 있습니다.</p>')
				}
			}
		}
		if (vAttachGroupIdKey != undefined && vAttachGroupIdKey > 0) {
			innerSetGroupIdKey(vAttachGroupIdKey)
		}
	}
	
	function innerSetGroupIdKey(groupIdKey) {
		vAttachGroupIdKey = groupIdKey
		var url = vContextPath + "/svc/v1/attachment/group/" + vAttachGroupIdKey;
		nesAjax(url,
				null,
				function(data) {
					vTotalSize = 0;
					vFileCount= 0;
					vUploadedFileCount = 0;
					if (data.attachments) {
						var attachment
						for (var ii=0; ii<data.attachments.length; ii++) {
							attachment = data.attachments[ii]
							var itemTmp = addAttachment(attachment)
							vTotalSize += attachment.fileSize
							if (itemTmp) {
								vListType.append(itemTmp)
							}
						}
						updateViewTotalSize();
						vFileCount = data.attachments.length
						vUploadedFileCount = data.attachments.length;
						updateViewCount(vFileCount, 0);
					}
				},
				function(data) {
				},
				"GET");
	}
	
	function checkDragAndDropAPI() {
		if ('draggable' in document.createElement('span')) {
			vSupportDragAndDropAPI = true
		} else {
			vSupportDragAndDropAPI = false
		}
	}
	
	function addDragEvent() {
		dragEventAttached = true;
		var dropArea = vDragArea[0];
		dropArea.addEventListener("dragenter", dragEnter, false);
		dropArea.addEventListener("dragexit", dragExit, false);
		dropArea.addEventListener("dragover", dragOver, false);
		dropArea.addEventListener("drop", drop, false);
	}

	function removeDragEvent() {
		dragEventAttached = false;
		var dropArea = vDragArea[0];
		dropArea.removeEventListener("dragenter", dragEnter, false);
		dropArea.removeEventListener("dragexit", dragExit, false);
		dropArea.removeEventListener("dragover", dragOver, false);
		dropArea.removeEventListener("drop", drop, false);	
	}
	
	function dragEnter(ev) {
		ev.stopPropagation();
		ev.preventDefault();
	}

	function dragExit(ev) {
		ev.stopPropagation();
		ev.preventDefault();
	}

	function dragOver(ev) {
		ev.stopPropagation();
		ev.preventDefault();
	}

	function drop(ev) {
		ev.stopPropagation();
		ev.preventDefault();
		
		if (vFileCount >= vMaxFileCount) {
			alert("최대 " + vMaxFileCount + "개까지만 등록할 수 있습니다.");
			return;
		}
		
		if (typeof ev.dataTransfer.files == 'undefined') {
			alert("HTML5를 지원하지 않는 브라우저입니다.");
		} else {
			var wel;
			var files;
			var nCount;
			var listTag = [];
			
			files = ev.dataTransfer.files;
			nCount = files.length;
			
			if (!!files && nCount === 0) {
				alert("정상적인 첨부방식이 아닙니다.");
				return;
			}
			for (var i=0, j=vFileCount; i<nCount; i++) {
				if (vIsImage == true && !vImageFilter.test(files[i].type)) {
					alert("이미지파일만 업로드 가능합니다.");
				} else if(files[i].size > vMaxFileSize) {
					alert("이미지 용량이 " + getUnitString(vMaxFileSize) + "를 초과하여 등록할 수 없습니다.");
				} else {
					if (j < vMaxFileCount) {
						var itemTmp = addNewFile(files[i]);
						if (itemTmp) {
							listTag[listTag.length] = itemTmp
							j = j + 1;
							vFileInfoCnt = vFileInfoCnt + 1;
						} else {
							break;
						}
					} else {
						alert("최대 " + vMaxFileCount + "개까지만 등록할 수 있습니다.");
						break;
					}
				}
			}
			if (j > 0) {
				if (listTag.length > 0) {
					for (var ii=0; ii<listTag.length; ii++) {
						vListType.append(listTag[ii]);
					}
				}
				updateViewTotalSize();
				vFileCount = j;
				updateViewCount(vFileCount, 0);
			}
		}
	}

	function refreshTotalFileSize(sParentId){
		var delFileSize = vHtFileInfo[sParentId].size;
		if (vTotalSize - delFileSize > -1 ){
			vTotalSize = vTotalSize - delFileSize;
		}
	}

	function removeFileInfo(sParentId) {
		vHtFileInfo[sParentId] = null;
	}

	function getDeleteNewFileClosure(sParentId) {
		return function() {
			var ele = $("#"+sParentId);
			refreshTotalFileSize(sParentId);
			updateViewTotalSize();
			updateViewCount(vFileCount, -1);
			removeFileInfo(sParentId);
			ele.remove()
			if (!dragEventAttached) {
				addDragEvent();
			}
		}
	}
	
	function addNewFile(oFile){
		var sFileSize = 0;
		var sFileName = "";
		var bExceedLimitTotalSize = false;
		var liEle
		
		sFileSize = getUnitString(oFile.size);
		sFileName = cuttingNameByLength(oFile.name);
		bExceedLimitTotalSize = checkTotalImageSize(oFile.size);

		if (bExceedLimitTotalSize == true) {
			alert("전체 파일 용량이 " + getUnitString(vMaxTotalFileSize) + "를 초과하여 등록할 수 없습니다. \n\n (파일명 : "+sFileName+", 사이즈 : "+sFileSize+")");
			return
		} else {
			vHtFileInfo['file' + vFileInfoCnt] = oFile;
			liId = "file" + vFileInfoCnt
			liEle = createEle("li")
			liEle.attr("id", liId)
			addEle(liEle, "span").html(sFileName)
			addEle(liEle, "em").html(sFileSize)
			liEle.append("&nbsp;&nbsp;")
			var ancEle = addEle(liEle, "a")
			ancEle.html('<i class="fas fa-times" style="color:red;cursor:pointer;"></i>')
			ancEle.click(getDeleteNewFileClosure(liId))
			return liEle;
		}
	}

	function addAttachment(attachment) {
		var sFileSize = 0;
		var sFileName = "";
		var liEle
		
		sFileSize = getUnitString(attachment.fileSize);
		sFileName = cuttingNameByLength(attachment.fileName);
		
		liId = "file_" + attachment.idKey
		liEle = createEle("li")
		liEle.attr("id", liId)
		var spanEle = addEle(liEle, "span")
		spanEle.html(sFileName)
		spanEle.click(getDownloadAttachmentClosure(attachment.idKey))
		spanEle.css("cursor","pointer")
		addEle(liEle, "em").html(sFileSize)
		liEle.append("&nbsp;&nbsp;")
		if (vIsEditable == true) {
			var ancEle = addEle(liEle, "a")
			ancEle.html('<i class="fas fa-times" style="color:red;cursor:pointer;"></i>')
			ancEle.click(getDeleteAttachmentClosure(liId, attachment.idKey, attachment.fileSize))
		}
		return liEle;
	}

	function getDownloadAttachmentClosure(attachmentIdKey) {
		return function() {
			var url = vContextPath + vDnloadUrl + "/" + attachmentIdKey + "/check";
			nesAjax(url,
					null,
					function(data) {
						window.location = vContextPath + vDnloadUrl + "/" + attachmentIdKey;
					},
					function(data) {
						alert("파일이 존재하지 않습니다.");
					},
					"GET");
		}
	}

	function getDeleteAttachmentClosure(parentId, attachmentIdKey, fileSize) {
		return function() {
			var url = vContextPath + vDeleteUrl + "/" + vAttachGroupIdKey + "/item/" + attachmentIdKey + "?q=" + getAuthToken();
			nesAjax(url,
					null,
					function(data) {
						$("#" + parentId).remove()
						vTotalSize -= fileSize
						updateViewTotalSize();
						updateViewCount(vFileCount, -1);
					},
					function(data) {
						alert("삭제에 실패했습니다.");
					},
					"DELETE");
		}
	}

	function cuttingNameByLength(sName) {
		var sTemp;
		var nIndex;
		if(sName.length > vMaxFileNameLen){
			nIndex = sName.lastIndexOf(".");
			var ext = sName.substring(nIndex, sName.length);
			sTemp = sName.substring(0, (vMaxFileNameLen - ext.length - 3)) + "..." + ext;
		} else {
			sTemp = sName;
		}
		return sTemp;
	}

	function checkTotalImageSize(bytesSize) {
		if (vTotalSize + bytesSize < vMaxTotalFileSize) {
			vTotalSize = vTotalSize + bytesSize;
			return false;
		} else {
			return true;
		}
	}

	function getUnitString(bytesSize) {
		var imageSize;
		var sUnit;
		
		if (bytesSize < 0){
			bytesSize = 0;
		}
		
		if (bytesSize < 1024) {
			imageSize = Number(bytesSize);
			sUnit = 'B';
			return imageSize + sUnit;
		} else if (bytesSize > (1024*1024)) {
			imageSize = Number(parseInt((bytesSize || 0), 10) / (1024*1024));
			sUnit = 'MB';
			return imageSize.toFixed(2) + sUnit;
		} else {
			imageSize = Number(parseInt((bytesSize || 0), 10) / 1024);
			sUnit = 'KB';
			return imageSize.toFixed(0) + sUnit;
		}
	}

	function updateViewCount(nCount, nVariable) {
		var nCnt = nCount + nVariable;
		if (vFileCountText) {
			vFileCountText.html(nCnt +"");
		}
		vFileCount = nCnt;
		if (vDragTip) {
			if (vFileCount == 0) {
				vDragTip.removeClass("blind")
			} else {
				vDragTip.addClass("blind")
			}
		}
		return nCnt;
	}

	function updateViewTotalSize() {
		if (vFileSizeText) {
			vFileSizeText.html(getUnitString(vTotalSize));
		}
	}
	
	this.setEditable = function(editable) {
		vIsEditable = editable
		build()
	}
	
	this.setGroupIdKey = function(groupIdKey) {
		if (vAttachGroupIdKey == groupIdKey) {
			return
		}
		vAttachGroupIdKey = groupIdKey
		build()
	}

	this.clickInputFile = function(succ, fail) {
		if (vInputFile == undefined) {
			alert('clickInputFile not working!!!')
			return
		}
		vInputFile.change(function() {
			innerUploadFiles(vInputFile[0].files, succ, fail)
			innerResetInputFile()
		})
		vInputFile.click()
	}
	
	this.resetInputFile = function() {
		innerResetInputFile()
	}
	
	function innerResetInputFile() {
		if (vInputFile == undefined) {
			return
		}
		vInputFile.remove()
		vInputFile = addInputFile(vContentDiv, "file_upload_input")
		if (vIsSingle == false) {
			vInputFile.prop("multiple", true)
		}
	}
	
	this.uploadFile = function(succ, fail) {
		if (vSupportDragAndDropAPI == true) {
			html5Upload(succ, fail)
		} else {
			generalUpload(succ, fail)
		}
	}
	
	this.countOfSelected = function() {
		if (vSupportDragAndDropAPI == true) {
			return html5Count()
		} else {
			return generalCount()
		}
	}
	
	this.countOfUploaded = function() {
		return vUploadedFileCount
	}
	
	this.countOfAll = function() {
		return this.countOfSelected() + this.countOfUploaded()
	}
	
	this.resetFile = function() {
		vFileInfoCnt = 0;
		vHtFileInfo = [];
		vTotalSize = 0;
		vFileCount = 0
		if (vListType) {
			vListType.empty()
		}
		updateViewTotalSize();
		updateViewCount(vFileCount, 0);
	}
	
	this.uploadFiles = function(files, succ, fail) {
		innerUploadFiles(files, succ, fail)
	}
	
	function innerUploadFiles(files, succ, fail) {
		if (files && files.length > 0) {
			if (files.length > vMaxFileCount) {
				alert("최대 " + vMaxFileCount + "개까지만 등록할 수 있습니다.")
				resetInputFile()
				return
			}
			for (var ii=0; ii<files.length; ii++) {
				if (vIsImage == true && !vImageFilter.test(files[ii].type)) {
					alert("이미지파일만 업로드 가능합니다.");
					resetInputFile()
					return
				} else if(files[ii].size > vMaxFileSize) {
					alert("이미지 용량이 " + getUnitString(vMaxFileSize) + "를 초과하여 등록할 수 없습니다.");
					resetInputFile()
					return
				}
			}
			var url = vContextPath + vUploadUrl
			if (vAttachGroupIdKey > 0) {
				url += "/" + vAttachGroupIdKey
			}
			nesAjaxFileUpload(url, files, succ, fail)
		} else {
			if (succ) {
				succ()
			}
		}
	}
	
	function generalUpload(succ, fail) {
		if (vInputFile[0].files && vInputFile[0].files.length > 0) {
			var url = vContextPath + vUploadUrl
			if (vAttachGroupIdKey > 0) {
				url += "/" + vAttachGroupIdKey
			}
			nesAjaxFileUpload(url, vInputFile[0].files, succ, fail)
		} else {
			succ()
		}
	}
	
	function generalCount() {
		return vInputFile ? (vInputFile[0].files ? vInputFile[0].files.length : 0) : 0;
	}
	
	function html5Count() {
		return vFileInfoCnt ? vFileInfoCnt : 0;
	}
	
	function html5Upload(succ, fail) {
		var files = []
		for (var jj=0; jj<vFileInfoCnt; jj++) {
			files[files.length] = vHtFileInfo['file'+jj];
		}
		if (files.length > 0) {
			var url = vContextPath + vUploadUrl
			if (vAttachGroupIdKey > 0) {
				url += "/" + vAttachGroupIdKey
			}
			nesAjaxFileUpload(url, files, succ, fail)
		} else {
			succ()
		}
	}
	
	function nesAjaxFileUpload($url, files, $func, $error) {
		var formData = new FormData();
		for (var ii=0; ii<files.length; ii++) {
			formData.append("uploadfiles", files[ii]);
		}
		formData.append("reqToken", getAuthToken());
		$.ajax({
			type: 'POST',
			url: $url,
			data: formData,
			processData: false,
			contentType: false,
			success: function(data) {
				$func(data);
			},
			error: function(data) {
				try {
					$error(data);
				} catch(e) {
				}
			}
		});
	}
}
