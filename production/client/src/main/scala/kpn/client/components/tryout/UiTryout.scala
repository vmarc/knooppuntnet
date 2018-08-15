package kpn.client.components.tryout

import diode.data.Pot
import diode.react.ModelProxy
import diode.react.ReactPot._
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.app.GetStatistics
import kpn.client.common.Context
import kpn.shared.ApiResponse
import kpn.shared.statistics.Statistics

object UiTryout {

  private case class Props(context: Context, proxy: ModelProxy[Pot[ApiResponse[Statistics]]])

  private val component = ScalaComponent.builder[Props]("tryout")
    .render_P { props =>

      <.div(
        <.div(
          ^.onClick --> props.proxy.dispatchCB(GetStatistics()),
          "refresh stats"
        ),
        props.proxy().renderEmpty {
          println("RENDER EMPTY")
          "Empty"
        },
        props.proxy().renderStale { _ =>
          println("RENDER STALE")
          "Stale"
        },
        props.proxy().renderFailed { ex =>
          println("RENDER error loading")
          "Error loading"
        },
        props.proxy().renderPending(
          _ > 50,
          xx => {
            println("RENDER PENDING " + xx)
            "Loading..."
          }
        ),
        props.proxy().render { response =>
          println("RENDER RESULT")
          response.result match {
            case None =>
              <.div("no page")

            case Some(statistics) =>
              val s = statistics.get("NetworkCount")
              <.div(
                <.div("total=" + s.total),
                <.div("be.rcn=" + s.be.rcn),
                <.div("be.rwn=" + s.be.rwn),
                <.div("nl.rcn=" + s.nl.rcn),
                <.div("nl.rwn=" + s.nl.rwn),
                <.div("de.rcn=" + s.de.rcn)
              )
          }
        }
      )
    }
    .componentDidMount(_.props.proxy.dispatchCB(GetStatistics()))
    .build

  def apply(context: Context, proxy: ModelProxy[Pot[ApiResponse[Statistics]]]): VdomElement = {
    component(Props(context, proxy))
  }
}
