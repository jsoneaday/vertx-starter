package com.dzhaven.starter

import io.vertx.core.AbstractVerticle

class UserVerticle : AbstractVerticle {
    override fun start() {
        vertx.eventBus().consumer("user.addr", msg -> {
            msg.reply("Welcome user")
        })
    }
}