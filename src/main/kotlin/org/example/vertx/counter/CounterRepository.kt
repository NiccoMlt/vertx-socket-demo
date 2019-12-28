package org.example.vertx.counter

import io.vertx.core.shareddata.SharedData

class CounterRepository internal constructor(private val data: SharedData) {
  fun get(): Int? {
    val counter = data.getLocalMap<String, String>(KEY)
    return if (counter.isNotEmpty()) counter[COUNTER]?.toIntOrNull() else null
  }

  fun update(counter: Int) {
    val map = data.getLocalMap<String, String>(KEY)
    map[COUNTER] = counter.toString()
  }

  companion object {
    private const val KEY = "key"
    private const val COUNTER = "counter"
  }
}
