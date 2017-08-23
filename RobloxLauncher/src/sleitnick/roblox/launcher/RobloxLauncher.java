package sleitnick.roblox.launcher;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

public final class RobloxLauncher {
	
	public static final String URL_ROBLOX_VERSION = "http://setup.roblox.com/version";
	public static final String URL_REQUEST_GAME = "http://www.roblox.com/Game/PlaceLauncher.ashx?request=RequestGame&placeId=#ID";
	public static final String URL_REQUEST_GROUP_BUILD_GAME = "http://www.roblox.com/Game/PlaceLauncher.ashx?request=RequestGroupBuildGame&placeId=#ID";
	public static final String URL_DOWNLOAD_ROBLOX = "http://www.roblox.com/install/setup.ashx";
	
	private static String robloxVersion;
	static {
		String response = HttpService.get(URL_ROBLOX_VERSION, false);
		robloxVersion = response;
	}
	
	private static final String LOCAL_APP_DATA = System.getenv("LOCALAPPDATA");
	private static final File RBX_DIR = new File(LOCAL_APP_DATA, "Roblox/Versions/" + robloxVersion);
	
	
	/**
	 * Current version installed
	 * @return <code>true</code> if current version is installed
	 */
	public static boolean hasCurrentVersion() {
		return RBX_DIR.exists();
	}
	
	/**
	 * Launch the given place
	 * @param place Place to launch
	 * @param frame Frame to launch from
	 * @throws RobloxVersionException
	 */
	protected static void launch(RobloxPlace place, Frame frame) throws RobloxVersionException {
		
		if (!hasCurrentVersion()) {
			throw new RobloxVersionException();
		}
		
		String joinRequest = HttpService.get((place.isPersonalServer() ? URL_REQUEST_GROUP_BUILD_GAME : URL_REQUEST_GAME).replaceAll("#ID", Integer.toString(place.getPlaceId())), false);
		
		if (joinRequest.contains("JoinPlace=") || joinRequest.contains("Game/Join.ashx")) {
			frame.setVisible(false);
			try {
				String launchCmd = (RBX_DIR.getAbsolutePath() + "/RobloxPlayerBeta.exe --id " + place.getPlaceId());
				Process rbxProcess = Runtime.getRuntime().exec(launchCmd);
				rbxProcess.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frame.setVisible(true);
		} else {
			System.out.println("Join request failed");
			System.out.println(joinRequest);
		}
		
	}

}
