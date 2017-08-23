package sleitnick.roblox.launcher;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import org.luaj.vm2.LuaValue;



public class RobloxPlace implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String PLACE_URL = "http://www.roblox.com/PlaceItem.aspx?id=#ID";
	
	private final int placeId;
	private String placeName;
	private URI placeUri = null;
	
	private String creator;
	private int creatorId;
	
	private String thumbnail;
	
	private boolean isPersonalServer;
	
	private void getPlaceInformation() throws RobloxPlaceException {
		String url = PLACE_URL.replaceAll("#ID", Integer.toString(placeId));
		try {
			placeUri = new URL(url).toURI();
		} catch (MalformedURLException e1) {
		} catch (URISyntaxException e1) {
		}
		String response = HttpService.get(url, true);
		if (response.isEmpty()) {
			throw new RobloxPlaceException("Failed to connect to ROBLOX", placeId, true);
		}
		LuaScript parseResponse = new LuaScript("parse_place_response.lua");
		LuaValue luaResponse = parseResponse.execute(response);
		if (luaResponse == LuaValue.FALSE) {
			throw new RobloxPlaceException(placeId);
		} else {
			try {
				placeName = URLDecoder.decode(luaResponse.get("Name").tojstring(), "ASCII");
			} catch (UnsupportedEncodingException e) {
				placeName = luaResponse.get("Name").tojstring();
			}
			try {
				thumbnail = URLDecoder.decode(luaResponse.get("Thumbnail").tojstring(), "UTF-8");
			} catch(UnsupportedEncodingException e) {
				thumbnail = luaResponse.get("Thumbnail").tojstring();
			}
			try {
				creator = URLDecoder.decode(luaResponse.get("Creator").tojstring(), "UTF-8");
			} catch(UnsupportedEncodingException e) {
				creator = luaResponse.get("Creator").tojstring();
			}
			creatorId = luaResponse.get("CreatorID").toint();
			isPersonalServer = luaResponse.get("IsPersonalServer").toboolean();
		}
	}
	
	/**
	 * Reflect a Roblox place
	 * @param placeId Place ID representing the place
	 * @throws RobloxPlaceException
	 */
	public RobloxPlace(int placeId) throws RobloxPlaceException {
		this.placeId = placeId;
		getPlaceInformation();
	}
	
	/**
	 * Get the place ID
	 * @return Place ID
	 */
	public int getPlaceId() {
		return placeId;
	}
	
	/**
	 * Get the place name
	 * @return Place name
	 */
	public String getPlaceName() {
		return placeName;
	}
	
	/**
	 * Get the creator ID
	 * @return Creator ID
	 */
	public int getCreatorId() {
		return creatorId;
	}
	
	/**
	 * Get the creator name
	 * @return Creator name
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * Get the thumbnail URL
	 * @return Thumbnail URL
	 */
	public String getThumbnail() {
		return thumbnail;
	}
	
	/**
	 * Whether or not the place is a personal server
	 * @return <true> if personal server
	 */
	public boolean isPersonalServer() {
		return isPersonalServer;
	}
	
	/**
	 * Open the place in the browser
	 */
	public void openInBrowser() {
		Browser.browse(placeUri);
	}

}
