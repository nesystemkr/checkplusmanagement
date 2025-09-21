package kr.peelknight.fragment;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import kr.peelknight.common.model.CM_Code;

public interface CodeInterface {
	public List<CM_Code> selectCodes(HttpSession session);
	public Map<String, String> selectCodeByTypeAsMap(HttpSession session);
}
