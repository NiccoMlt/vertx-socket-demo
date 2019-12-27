package org.example.vertx

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.logging.SLF4JLogDelegateFactory
import org.example.vertx.sockjs.SockJsClient

fun main() {
  val vertx = Vertx.vertx()
  vertx.deployVerticle(MainVerticle())
}

class MainVerticle : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger(this.javaClass)

  companion object {
    private const val LOGGER_DELEGATE_FACTORY_PROPERTY = "vertx.logger-delegate-factory-class-name"
  }

  override fun start(startPromise: Promise<Void>) {
    if (System.getProperty(LOGGER_DELEGATE_FACTORY_PROPERTY) == null) {
      System.setProperty(
        LOGGER_DELEGATE_FACTORY_PROPERTY,
        SLF4JLogDelegateFactory::class.qualifiedName ?: throw ClassNotFoundException()
      )
    }

    vertx.deployVerticle(Server()) { serverDeploy: AsyncResult<String> ->
      if (serverDeploy.succeeded()) {
        vertx.deployVerticle(SockJsClient()) { clientDeploy: AsyncResult<String> ->
          if (clientDeploy.succeeded()) {
            startPromise.complete()
          } else {
            logger.error(clientDeploy.cause())
            startPromise.fail(clientDeploy.cause())
          }
        }
        // startPromise.complete() // TODO
      } else {
        logger.error(serverDeploy.cause())
        startPromise.fail(serverDeploy.cause())
      }
    }
  }
}
