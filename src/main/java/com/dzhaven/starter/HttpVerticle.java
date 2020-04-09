package com.dzhaven.starter;

import java.util.concurrent.CompletableFuture;

import io.netty.util.concurrent.CompleteFuture;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HttpVerticle extends AbstractVerticle {
@Override
  public void start(Future<Void> startFuture) throws Exception {
    Router router = Router.router(vertx);
    router.get("/api/home").handler(this::homeHandler);
    router.get("/api/user/:name").handler(this::userHandler);

    this.setupHttpServer(router)
        .compose(this::setupVerticles);
  }

  void homeHandler(RoutingContext ctx) {
      vertx.eventBus().request("home.addr", "", rep -> {
        ctx.request().response().end((String) rep.result().body());
      });
  }

  void userHandler(RoutingContext ctx) {
      vertx.eventBus().request("user.addr", ctx.request().getParam("name"), rep -> {
          ctx.request().response().end((String) rep.result().body());
      });
  }

  Future<Void> setupVerticles(Void unused) {
      Future<Void> homeFuture = Future.future(promise -> vertx.deployVerticle(new HomeVerticle()));
      return homeFuture;
  }

  Future<Void> setupHttpServer(Router router) {
    HttpServer server = vertx.createHttpServer().requestHandler(router);
    return Future.<HttpServer>future(promise -> server.listen(8888, promise)).mapEmpty();
  }  
}