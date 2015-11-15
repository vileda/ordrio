package ordrio;

import io.resx.core.MongoEventStore;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;
import ordrio.command.CreateOrdrCommand;
import ordrio.command.CreateOrdrItemCommand;
import ordrio.event.OrdrCreatedEvent;
import ordrio.event.OrdrItemCreatedEvent;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrdrItemCommandResource implements Handler<RoutingContext> {
	private final MongoEventStore eventStore;

	public CreateOrdrItemCommandResource(MongoEventStore eventStore) {
		this.eventStore = eventStore;
	}

	@Override
	public void handle(RoutingContext routingContext) {
		OrdrioRouter.publishCommand(parseOrdrItemRequest(routingContext),
				eventStore, routingContext, OrdrItemCreatedEvent.class);
	}

	private CreateOrdrItemCommand parseOrdrItemRequest(RoutingContext routingContext) {
		CreateOrdrItemCommand item = new CreateOrdrItemCommand();
		item.setName(routingContext.request().getFormAttribute("name"));
		item.setOrdrId(routingContext.request().getFormAttribute("ordrId"));
		item.setAmount(new BigDecimal(routingContext.request().getFormAttribute("amount")));
		item.setSinglePrice(new BigDecimal(routingContext.request().getFormAttribute("singlePrice")));
		item.setId(UUID.randomUUID().toString());
		return item;
	}
}
