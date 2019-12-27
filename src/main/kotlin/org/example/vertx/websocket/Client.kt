package org.example.vertx.websocket

import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.LoggerFactory

class Client : io.vertx.core.AbstractVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  override fun start() {
    val client = vertx.createHttpClient()

    client.websocket(8080, "localhost", "/some-uri") { websocket ->
      websocket.handler { data ->
        logger.info("Received data ${data.toString("ISO-8859-1")}")
        client.close()
      }
      websocket.writeBinaryMessage(Buffer.buffer("Hello world"))
    }
  }
}
