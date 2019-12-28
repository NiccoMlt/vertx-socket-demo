package org.example.vertx

import io.vertx.core.eventbus.EventBus
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions
import io.vertx.kotlin.core.eventbus.completionHandlerAwait
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.ext.web.handler.sockjs.sockJSHandlerOptionsOf
import org.example.vertx.counter.CounterHandler
import org.example.vertx.counter.CounterRepository

class Server : CoroutineVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  override suspend fun start() {
    val router: Router = Router.router(vertx)
    val sockJSOptions: SockJSHandlerOptions = sockJSHandlerOptionsOf(heartbeatInterval = 2000)
    val sockJSHandler: SockJSHandler = SockJSHandler.create(vertx, sockJSOptions)

    handleLogs(router)
    handleEventBusBridge(router, sockJSHandler)
    // handleSocket(router, sockJSHandler)
    handleStaticReact(router)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listenAwait(8080)

    logger.info("Server started")
  }

  private fun handleLogs(router: Router) {
    router.route().handler(LoggerHandler.create(LoggerFormat.SHORT))
  }

  private fun handleSocket(router: Router, sockJSHandler: SockJSHandler) {
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
  }

  private suspend fun handleEventBusBridge(router: Router, sockJSHandler: SockJSHandler) {
    val data = vertx.sharedData()
    val repository = CounterRepository(data)
    val eb: EventBus = vertx.eventBus()

    val inConsumer = eb.consumer<Int>("in")
    inConsumer.handler { message ->
      logger.info("I have received a message: ${message.body()}")
    }
    inConsumer.completionHandlerAwait()

    val counterHandler = CounterHandler(eb, repository)

    val sockBridgeOptions = BridgeOptions()
      .addOutboundPermitted(PermittedOptions().setAddressRegex("out"))
      .addInboundPermitted(PermittedOptions().setAddressRegex("in"))
    val sockJsBridge = sockJSHandler.bridge(sockBridgeOptions, counterHandler)

    router
      .mountSubRouter("/eventbus", sockJsBridge)
    router
      .route("/eb.html")
      .handler { it.response().sendFile("eb.html") }
  }

  private fun handleStaticReact(router: Router) {
    router
      .get()
      .handler(StaticHandler.create())
  }
}
