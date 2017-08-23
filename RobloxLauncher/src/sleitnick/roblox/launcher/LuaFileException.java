package sleitnick.roblox.launcher;

public class LuaFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String msg;
	
	public LuaFileException() {
		this("Lua file exception");
	}
	
	public LuaFileException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
	
}
