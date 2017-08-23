package sleitnick.roblox.launcher;



public class RobloxPlaceException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final String msg;
	private final int id;
	private final boolean failedToConnect;
	
	public RobloxPlaceException() {
		this("Invalid ROBLOX place", -1, false);
	}
	
	public RobloxPlaceException(String msg) {
		this(msg, -1, false);
	}
	
	public RobloxPlaceException(int id) {
		this("The given ID(" + id + ") is not a valid ROBLOX place", id, false);
	}
	
	public RobloxPlaceException(String msg, int id) {
		this(msg, id, false);
	}
	
	public RobloxPlaceException(String msg, int id, boolean failedToConnect) {
		this.msg = msg;
		this.id = id;
		this.failedToConnect = failedToConnect;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
	public int getID() {
		return id;
	}
	
	public boolean getFailedToConnect() {
		return failedToConnect;
	}
	
}
