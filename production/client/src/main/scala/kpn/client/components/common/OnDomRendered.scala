// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.CallbackTo
import org.scalajs.dom

import scala.scalajs.js

object OnDomRendered {
  def apply(action: => Unit): Callback = {
    CallbackTo {
      dom.window.setTimeout(
        () => {
          val callback: js.Function1[Double, _] = { e: Double =>
            action
          }
          dom.window.requestAnimationFrame(callback)
        },
        0
      )
    }
  }
}
