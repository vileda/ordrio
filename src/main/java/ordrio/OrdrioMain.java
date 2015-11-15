package ordrio;

import io.vertx.rxjava.core.Vertx;

public class OrdrioMain {
	public static void main(String[] args) {
		new OrdrioMain().run(Vertx.vertx());
	}

	public void run(Vertx vertx) {
		vertx.deployVerticle("ordrio.OrdrioRouter");
	}
}
