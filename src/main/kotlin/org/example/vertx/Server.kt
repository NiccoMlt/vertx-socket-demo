package org.example.vertx

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.ext.web.handler.sockjs.sockJSHandlerOptionsOf

class Server : CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  override suspend fun start() {
    val router: Router = Router.router(vertx)

    router.route().handler(LoggerHandler.create(LoggerFormat.SHORT))

    // SockJS WebSocket-like server
    val sockJSOptions: SockJSHandlerOptions = sockJSHandlerOptionsOf(heartbeatInterval = 2000)
    val sockJSHandler: SockJSHandler = SockJSHandler.create(vertx, sockJSOptions)
    sockJSHandler.socketHandler { sockJSSocket ->
      // logger.info(sockJSSocket.headers())
      sockJSSocket.handler { buffer ->
        val stringBuffer = buffer.toString()
        // logger.info(stringBuffer)
        sockJSSocket.write(stringBuffer)
      }
    }
    router
      .route("/sock/*")
      .handler(sockJSHandler)
    router
      .route("/ws.html")
      .handler { it.response().sendFile("ws.html") }

    // SockJS EventBus Bridge
    val sockBridgeOptions = BridgeOptions()
    val sockJsBridge = sockJSHandler.bridge(sockBridgeOptions)
    router
      .mountSubRouter("/eventbus", sockJsBridge)
    router
      .route("/eb.html")
      .handler { it.response().sendFile("eb.html") }

    // React.js frontend
    router
      .get()
      .handler(StaticHandler.create())

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listenAwait(8080)

    logger.info("Server started")
  }
}
