package org.example.vertx

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions
import io.vertx.kotlin.ext.web.handler.sockjs.sockJSHandlerOptionsOf

class Server : io.vertx.core.AbstractVerticle() {

  override fun start() {
    val router: Router = Router.router(vertx)
    val sockJSOptions: SockJSHandlerOptions = sockJSHandlerOptionsOf(heartbeatInterval = 2000)
    val sockJSHandler: SockJSHandler = SockJSHandler.create(vertx, sockJSOptions)

    sockJSHandler.socketHandler { sockJSSocket ->
      // Just echo the data back
      sockJSSocket.handler { sockJSSocket.write(it) }
    }

    router.route("/sock/*").handler(sockJSHandler)
    router.route("/").handler {
      it.response().sendFile("ws.html")
    }

    vertx
      .createHttpServer()
      // .websocketHandler { ws ->
      //   ws.handler { ws.writeBinaryMessage(it) }
      // }
      .requestHandler(router)
      .listen(8080)
  }
}
