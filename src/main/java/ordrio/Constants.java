package ordrio;

import io.vertx.core.json.JsonObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class Constants {
	public static final String CREATE_ORDR_COMMAND_ADDRESS = "create.ordr.command";
	public static final String CREATE_ORDR_ITEM_COMMAND_ADDRESS = "create.ordr.item.command";
	public static final String ORDR_CREATED_EVENT_ADDRESS = "created.ordr.event";
	public static final String ORDR_ITEM_CREATED_EVENT_ADDRESS = "created.ordr.item.event";
	public static final String UPDATE_ORDR_EVENT_ADDRESS = "update.ordr.event";
	static final String MONGO_URL_KEY = "MONGO_URL";

	public static JsonObject getTodaysEventsQueryFor(final String id) {
		return new JsonObject()
				.put("payload.id", id);
	}
}
