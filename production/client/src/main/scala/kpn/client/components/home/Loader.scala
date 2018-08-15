package kpn.client.components.home

import kpn.client.components.common.PageStatus
import org.scalajs.dom.ext.AjaxException

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import scala.scalajs.js.timers.clearTimeout
import scala.scalajs.js.timers.setTimeout
import org.scalajs.dom

class Loader[T] {

  import scala.concurrent.ExecutionContext.Implicits.global

  def load(
    aaa: Future[T],
    initialStatus: PageStatus.Value,
    updatePageStatus: PageStatus.Value => Unit,
    updateResult: T => Unit
  ): Unit = {

    var status = initialStatus

    val delay: FiniteDuration = 500.millis

    def updateToLoading(): Unit = {
      if (status == PageStatus.LoadStarting) {
        status = PageStatus.Loading
        updatePageStatus(PageStatus.Loading)
      }
      else if (status == PageStatus.UpdateStarting) {
        status = PageStatus.Updating
        updatePageStatus(PageStatus.Updating)
      }
    }

    val timeoutHandle = setTimeout(delay)(updateToLoading())

    aaa.map { result: T =>
      clearTimeout(timeoutHandle)
      status = PageStatus.Ready
      dom.window.scrollTo(0, 0)
      updateResult(result)
    }.recover {

      case e: AjaxException =>

        status = PageStatus.Failure
        if (e.isTimeout) {
          println("Ajax timeout (" + e.getMessage + ")")
          println("status=" + e.xhr.status)
          println("readyState=" + e.xhr.readyState)
          println("statusText=" + e.xhr.statusText)
          clearTimeout(timeoutHandle)
          updatePageStatus(PageStatus.Timeout)
        }
        else {
          println("Ajax exception (" + e.getMessage + ")")
          println("status=" + e.xhr.status)
          println("readyState=" + e.xhr.readyState)
          println("statusText=" + e.xhr.statusText)
          clearTimeout(timeoutHandle)
          updatePageStatus(PageStatus.Failure)
        }

      case ex =>
        status = PageStatus.Failure
        println("recover, clearTimeout() ex=" + ex)
        clearTimeout(timeoutHandle)
        updatePageStatus(PageStatus.Failure)
    }
  }
}
