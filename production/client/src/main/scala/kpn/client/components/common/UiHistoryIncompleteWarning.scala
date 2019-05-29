// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.Nls.nlsNL
import kpn.shared.Timestamp

import scalacss.ScalaCssReact._

object UiHistoryIncompleteWarning {

  private case class Props(context: Context)

  private val component = ScalaComponent.builder[Props]("history-incomplete-warning")
    .render_P { props =>

      implicit val context: Context = props.context

      <.p(
        GlobalStyles.note,
        <.span(
          nls(
            "Older changes cannot be shown. The history in the analysis database " +
              s"does not go beyond ${Timestamp.redaction.yyyymmdd} (",
            "Oudere wijzigingen kunnen niet getoond worden. De geschiedenis in de " +
              s"knooppuntnet analyse databank gaat niet verder terug dan ${Timestamp.redaction.yyyymmdd} ("
          )
        ),
        if (nlsNL) {
          <.a(
            ^.cls := "external",
            ^.href := "https://wiki.openstreetmap.org/wiki/NL:Open_Database_License",
            ^.target := "_blank",
            "Licentie aanpassing"
          )
        }
        else {
          <.a(
            ^.cls := "external",
            ^.href := "https://wiki.openstreetmap.org/wiki/Open_Database_License",
            ^.target := "_blank",
            "License change"
          )
        },
        <.span(").")
      )
    }
    .build

  def apply()(implicit context: Context): VdomElement = {
    component(Props(context))
  }
}
