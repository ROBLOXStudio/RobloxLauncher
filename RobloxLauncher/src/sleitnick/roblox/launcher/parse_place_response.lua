-- Parse Place Response
-- Stephen Leitnick
-- October 19, 2014

-- Is given the global variable 'httpResponse' which is a string



-- Get PlaceID from the Play button (which assumes non-place assets will NOT have the button):
local placeId = (
	isPersonalServer
		and
	httpResponse:match([[class="VisitButton VisitButtonPersonalServer" placeid="(.-)">]])
		or
	httpResponse:match([[class="VisitButton VisitButtonPlay" placeid="(.-)">]])
)

-- If response shows that the ID given isn't a ROBLOX place:
if (not placeId) then
	return false
end

-- Parse info:
local placeName = httpResponse:match([[<h1 class="notranslate">(.-)</h1>]])
local placeCreatorId, placeCreatorName = httpResponse:match([[<a href="/User%.aspx%?ID=(.-)" class="tooltip" original%-title=".-">(.-)</a>]])

-- Get place thumbnail:
local placeThumbnail = httpResponse:match([[<img src="(.-)" height="280" width="500"]])

-- Perhaps Video was shown first, so search for gallery image:
if (not placeThumbnail) then
	placeThumbnail = httpResponse:match([[<img src="(.-)" height="70" width="120"]])
end

return {
	ID = placeId;
	Name = placeName;
	Creator = placeCreatorName;
	CreatorID = placeCreatorId;
	Thumbnail = placeThumbnail;
	IsPersonalServer = isPersonalServer;
}