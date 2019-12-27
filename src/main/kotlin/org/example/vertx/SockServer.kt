package org.example.vertx

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions
import io.vertx.kotlin.ext.web.handler.sockjs.sockJSHandlerOptionsOf

fun main() {
  Vertx.vertx().deployVerticle(SockServer())
}

class SockServer : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {
    val router = Router.router(vertx)
    val sockJSOptions: SockJSHandlerOptions = sockJSHandlerOptionsOf(heartbeatInterval = 2000)
    val sockJSHandler: SockJSHandler = SockJSHandler.create(vertx, sockJSOptions)

    sockJSHandler.socketHandler { sockJSSocket ->
      // Just echo the data back
      println(sockJSSocket.headers().toString())
      println(sockJSSocket.localAddress().toString())
      println(sockJSSocket.remoteAddress().toString())
      println(sockJSSocket.uri().toString())
      println(sockJSSocket.webUser()?.toString() ?: "No user")
      println(sockJSSocket.webSession()?.toString() ?: "No session")
      sockJSSocket.handler {
        println(it.toString())
        sockJSSocket.write(it.toString())
      }
    }

    router.route("/*").handler(sockJSHandler)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080) {
        if (it.succeeded()) {
          startFuture.complete()
        } else {
          startFuture.fail(it.cause())
        }
      }
  }
}
