package kr.peelknight.generate.model;

public class GN_GenColInfo {
	public String columnName;
	public String columnType;
	public boolean isPrimaryKey;
	public GN_GenColInfo(String columnName, String columnType, boolean isPrimaryKey) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.isPrimaryKey = isPrimaryKey;
	}
}
