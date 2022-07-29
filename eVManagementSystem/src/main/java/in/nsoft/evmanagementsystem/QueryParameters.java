package in.nsoft.evmanagementsystem;

public class QueryParameters {
	private  String sql;
	private String[] selectionArgs;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String[] getSelectionArgs() {
		return selectionArgs;
	}
	public void setSelectionArgs(String[] selectionArgs) {
		this.selectionArgs = selectionArgs;
	}
}
