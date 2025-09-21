package kr.peelknight.common.model;

public class CM_GenColInfo
{
	public String columnName;
	public String columnType;
	public boolean isPrimaryKey;
	public CM_GenColInfo(String columnName, String columnType, boolean isPrimaryKey) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.isPrimaryKey = isPrimaryKey;
	}
}
