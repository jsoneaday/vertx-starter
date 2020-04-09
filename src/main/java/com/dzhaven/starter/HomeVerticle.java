package com.dzhaven.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HomeVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("home.addr", msg -> {
            msg.reply("welcome home");
        });
    }
}