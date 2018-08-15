package kpn.client.components.changes.filter

import chandu0101.scalajs.react.components.materialui._
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls

object UiRowsPerPage {

  private case class Props(context: Context, selection: Int, onChange: (TouchTapEvent, Int, Int) => Callback)

  private val component = ScalaComponent.builder[Props]("rows-per-page")
    .render_P { props =>

      implicit val context: Context = props.context

      val suffix = nls("changes per page", "wijzigingen per pagina")

      <.div(
        MuiSelectField[Int](
          value = props.selection,
          onChange = props.onChange
        )(
          MuiMenuItem[Int](key = "10", value = 10, primaryText = <.span("10 " + suffix).render)(),
          MuiMenuItem[Int](key = "25", value = 25, primaryText = <.span("25 " + suffix).render)(),
          MuiMenuItem[Int](key = "50", value = 50, primaryText = <.span("50 " + suffix).render)(),
          MuiMenuItem[Int](key = "100", value = 100, primaryText = <.span("100 " + suffix).render)(),
          MuiMenuItem[Int](key = "250", value = 250, primaryText = <.span("250 " + suffix).render)()
        )
      )
    }
    .build

  def apply(selection: Int, onChange: (TouchTapEvent, Int, Int) => Callback)(implicit context: Context): VdomElement = {
    component(Props(context, selection, onChange))
  }
}
