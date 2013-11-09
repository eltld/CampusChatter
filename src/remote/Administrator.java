package remote;

public interface Administrator {
	public void showAllUser();
	
	public void accessDB(DBSystem db);
	
	public void deactiveUser();
	public void activeUser();

}
