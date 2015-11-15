package ordrio;

import io.resx.core.MongoEventStore;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import ordrio.aggregate.Ordr;

import static ordrio.Constants.getTodaysEventsQueryFor;

public class OrdrQueryResource implements Handler<RoutingContext> {
	private final MongoEventStore eventStore;

	public OrdrQueryResource(MongoEventStore eventStore) {
		this.eventStore = eventStore;
	}

	@Override
	public void handle(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");

		JsonArray querys = new JsonArray()
				.add(new JsonObject().put("payload.id", id))
				.add(new JsonObject().put("payload.ordrId", id));
		JsonObject query = new JsonObject()
				.put("$or", querys);

		eventStore.load(query, Ordr.class).subscribe(ordr -> {
			if(ordr.getId() == null)
				routingContext.response().setStatusCode(404).end("aggregate not found");
			else
				routingContext.response().end(Json.encode(ordr));
		});
	}
}
