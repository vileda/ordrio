package ordrio;

import io.resx.core.EventStore;
import io.resx.core.MongoEventStore;
import io.resx.core.command.Command;
import io.resx.core.event.SourcedEvent;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CookieHandler;
import io.vertx.rxjava.ext.web.handler.SessionHandler;
import io.vertx.rxjava.ext.web.handler.StaticHandler;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.rxjava.ext.web.sstore.LocalSessionStore;
import io.vertx.rxjava.ext.web.sstore.SessionStore;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

import static ordrio.Constants.*;
import static ordrio.Routes.*;
import static org.apache.commons.lang3.Validate.notEmpty;

public class OrdrioRouter extends AbstractVerticle {

	public void start() {
		final EventBus eventBus = vertx.eventBus();
		final Configuration configuration = new Configuration();
		String mongo_url = notEmpty(configuration.getString(MONGO_URL_KEY));
		final JsonObject config = new JsonObject()
				.put("connection_string", mongo_url);
		final MongoEventStore eventStore = new MongoEventStore(vertx, eventBus, config);

		final SessionStore sessionStore = LocalSessionStore.create(vertx);
		final SessionHandler sessionHandler = SessionHandler.create(sessionStore);
		sessionHandler.setNagHttps(false);

		final HttpServer server = vertx.createHttpServer();

		final Router router = Router.router(vertx);
		final Router apiRouter = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		router.route().handler(CookieHandler.create());
		router.route().handler(sessionHandler);

		final SockJSHandlerOptions options = new SockJSHandlerOptions().setHeartbeatInterval(2000);

		final SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);

		final Map<String, MessageConsumer<String>> consumers = new HashMap<>();
		final WebsocketHandler websocketHandler = new WebsocketHandler(eventStore, consumers);
		sockJSHandler.socketHandler(websocketHandler);

		router.route(WEBSOCKET_PATH).handler(sockJSHandler);

		router.get(ORDR_WEB_PATH).handler(routingContext -> {
			routingContext.session().put("id", routingContext.request().getParam("id"));
			routingContext.response().sendFile("webroot/index.html");
		});

		final StaticHandler staticHandler = StaticHandler.create();
		staticHandler.setCachingEnabled(false);
		router.get().pathRegex(STATIC_PATH_REGEX).handler(staticHandler);

		new CommandHandler(eventStore);
		apiRouter.get(GET_ORDR_AGGREGATE_PATH).handler(new OrdrQueryResource(eventStore));
		apiRouter.post(CREATE_ORDR_PATH).handler(new CreateOrdrCommandResource(eventStore));
		apiRouter.post(CREATE_ORDR_ITEM_PATH).handler(new CreateOrdrItemCommandResource(eventStore));

		router.mountSubRouter(API_PATH_PREFIX, apiRouter);

		apiRouter.post(CREATE_ORDR_PATH).handler(new CreateOrdrCommandResource(eventStore));
		server.requestHandler(router::accept).listen(8080);
	}

	public static <T extends Command, R extends SourcedEvent> void publishCommand(final T payload, final EventStore eventStore, final RoutingContext routingContext, final Class<R> clazz) {
		final HttpServerResponse response = routingContext.response();

		eventStore.publish(payload, clazz)
				.onErrorResumeNext(message -> {
					response.setStatusCode(500).end(message.getMessage());
					return Observable.empty();
				})
				.subscribe(reply -> {
					JsonObject jsonObject = new JsonObject(Json.encode(reply));
					addHATEOAS(reply, jsonObject);
					response.setStatusCode(200).end(jsonObject.encode());
				});
	}

	private static <R extends SourcedEvent> void addHATEOAS(R reply, JsonObject jsonObject) {
		JsonArray links = new JsonArray();
		String id = reply.getId();

		if(jsonObject.containsKey("ordrId")) {
			id = jsonObject.getString("ordrId");
		}

		String getAggregatePath = GET_ORDR_AGGREGATE_PATH.replace(":id", id);
		String postOrdrItemPath = CREATE_ORDR_ITEM_PATH.replace(":id", id);

		links.add(new JsonObject()
				.put("rel", "self")
				.put("href", API_PATH_PREFIX + getAggregatePath)
				.put("method", "GET"))
		.add(new JsonObject()
				.put("rel", "add")
				.put("href", API_PATH_PREFIX + postOrdrItemPath)
				.put("method", "POST")
		);
		jsonObject.put("links", links);
	}
}
