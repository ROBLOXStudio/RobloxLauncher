package sleitnick.roblox.launcher;



public class RobloxVersionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_MSG = "Current ROBLOX version not installed";
	
	private final String msg;
	
	public RobloxVersionException() {
		msg = DEFAULT_MSG;
	}
	
	public RobloxVersionException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}

}
