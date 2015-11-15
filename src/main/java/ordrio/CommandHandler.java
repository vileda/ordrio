package ordrio;

import io.resx.core.MongoEventStore;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import ordrio.aggregate.Ordr;
import ordrio.command.CreateOrdrCommand;
import ordrio.command.CreateOrdrItemCommand;
import ordrio.event.OrdrCreatedEvent;
import ordrio.event.OrdrItemCreatedEvent;
import ordrio.event.UpdateOrdrEvent;

import static ordrio.Constants.getTodaysEventsQueryFor;

public class CommandHandler {
	private MongoEventStore eventStore;

	public CommandHandler(MongoEventStore eventStore) {
		this.eventStore = eventStore;
		attachCommandHandlers();
	}

	private void attachCommandHandlers() {
		eventStore.consumer(CreateOrdrCommand.class, message -> {
			CreateOrdrCommand createCommand = Json.decodeValue(message.body(), CreateOrdrCommand.class);
			OrdrCreatedEvent createdEvent = new OrdrCreatedEvent(createCommand.getId(), createCommand.getName());
			eventStore.publish(createdEvent, OrdrCreatedEvent.class)
					.subscribe(event -> {
						message.reply(Json.encode(createdEvent));
					});
		});

		eventStore.consumer(CreateOrdrItemCommand.class, message -> {
			CreateOrdrItemCommand createCommand = Json.decodeValue(message.body(), CreateOrdrItemCommand.class);
			OrdrItemCreatedEvent createdEvent = new OrdrItemCreatedEvent(createCommand.getId(), createCommand.getName(), createCommand.getOrdrId());
			eventStore.publish(createdEvent, OrdrItemCreatedEvent.class)
					.subscribe(event -> {
						String id = event.getOrdrId();
						JsonArray querys = new JsonArray()
								.add(new JsonObject().put("payload.id", id))
								.add(new JsonObject().put("payload.ordrId", id));
						JsonObject query = new JsonObject()
								.put("$or", querys);
						eventStore.load(query, Ordr.class, createdEvent)
								.subscribe(ordr -> eventStore
										.publish(new UpdateOrdrEvent(), ordr, getTodaysEventsQueryFor(id).encode())
										.subscribe(ign -> message.reply(Json.encode(createdEvent))));
					});
		});
	}
}
