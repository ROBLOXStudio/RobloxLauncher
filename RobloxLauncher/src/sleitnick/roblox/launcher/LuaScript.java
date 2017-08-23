package sleitnick.roblox.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;



public class LuaScript {
	
	private static final ArrayList<String> LUA_VALID_EXT = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			this.add(".lua");
			this.add(".txt");
		}
	};
	
	private static String getSourceFromStream(InputStream stream) throws IOException {
		StringBuilder sb = new StringBuilder();
		int ch;
		while ((ch = stream.read()) != -1) {
			sb.append((char)ch);
		}
		return sb.toString();
	}
	
	private String source;
	
	private boolean isValidName(String name) {
		name = name.toLowerCase();
		for (String ext : LUA_VALID_EXT) {
			if (name.endsWith(ext.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Create a Lua script from the given name
	 * <p>
	 * Will search the class for the given file
	 * @param name Script name
	 */
	public LuaScript(String name) {
		if (!isValidName(name)) {
			throw new LuaFileException("Wrong Lua file type");
		}
		InputStream stream = getClass().getResourceAsStream(name);
		try {
			String source = getSourceFromStream(stream);
			this.source = source;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public LuaValue execute(String url) {
		Globals _G = JsePlatform.standardGlobals();
		_G.set("httpResponse", url);
		LuaValue chunk = _G.load(source);
		return chunk.call();
	}

}
