package ordrio;

import io.resx.core.MongoEventStore;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;
import ordrio.command.CreateOrdrCommand;
import ordrio.event.OrdrCreatedEvent;

import java.util.UUID;

public class CreateOrdrCommandResource implements Handler<RoutingContext> {
	private final MongoEventStore eventStore;

	public CreateOrdrCommandResource(MongoEventStore eventStore) {
		this.eventStore = eventStore;
	}

	@Override
	public void handle(RoutingContext routingContext) {
		OrdrioRouter.publishCommand(parseOrdrRequest(routingContext),
				eventStore, routingContext, OrdrCreatedEvent.class);
	}

	private CreateOrdrCommand parseOrdrRequest(RoutingContext routingContext) {
		CreateOrdrCommand ordr = new CreateOrdrCommand();
		String name = routingContext.request().getFormAttribute("name");
		String id = UUID.randomUUID().toString();
		ordr.setName(name);
		ordr.setId(id);
		return ordr;
	}
}
