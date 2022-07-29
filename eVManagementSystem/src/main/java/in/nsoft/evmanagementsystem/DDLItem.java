package in.nsoft.evmanagementsystem;

public final class DDLItem {
	private String id;
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public DDLItem(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	//Modified Nitish 26-02-2014
	public DDLItem() {
		super();		
	}
	//End Nitish 26-02-2014
	@Override
	public String toString()
	{
		return this.value ;
	}
}
