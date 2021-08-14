package io.chrisdavenport.circuitjs

import cats.effect._
import io.chrisdavenport.circuit.CircuitBreaker
import scala.scalajs.js.annotation._
import scala.scalajs.js
import scala.concurrent.duration.{Duration, FiniteDuration}
import cats.effect.unsafe.IORuntime.global

@JSExportTopLevel("CircuitBreaker")
class Circuit(
  @JSExport val maxFailures: Int,
  @JSExport val resetTimeout: String,
  @JSExport val exponentialBackoffFactor: Int = 1,
  @JSExport val maxResetTimeout: String = "5 minutes"
){
  private val resetTimeoutInternal = Duration(resetTimeout) match{ 
    case d: FiniteDuration => d
    case _ => throw new RuntimeException(s"$resetTimeout not a viable reset Timeout value, try like '60 seconds'")
  }

  private val maxResetTimeoutInternal : Duration = Duration(maxResetTimeout)

  private val circuit = CircuitBreaker.in[SyncIO, IO](
    maxFailures = maxFailures,
    resetTimeout = resetTimeoutInternal,
    exponentialBackoffFactor = exponentialBackoffFactor.toDouble,
    maxResetTimeout = maxResetTimeoutInternal
  ).unsafeRunSync()

  @JSExport
  def protect[A](pr: js.Function0[js.Promise[A]]): js.Promise[A] = 
      circuit.protect(IO.fromPromise(IO(pr())))
        .unsafeToPromise()(global)

}