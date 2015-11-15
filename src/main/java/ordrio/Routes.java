package ordrio;

public class Routes {
	static final String API_PATH_PREFIX = "/api";
	static final String GET_ORDR_AGGREGATE_PATH = "/aggregate/ordr/:id";
	static final String CREATE_ORDR_PATH = "/ordr";
	static final String CREATE_ORDR_ITEM_PATH = "/ordr/:id/item";
	static final String WEBSOCKET_PATH = "/socket*";
	static final String ORDR_WEB_PATH = "/o/:id";
	static final String STATIC_PATH_REGEX = "^(/|/(js|css)/.*)";
}
