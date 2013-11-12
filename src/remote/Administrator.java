package remote;
import local.User;

public interface Administrator {
	public void accessDB(DBSystem db);
	
	public void deactiveUser(User user);
	public void activeUser(User user);

}
