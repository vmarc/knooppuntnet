package kpn.client.components.common

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls
import kpn.client.common.Nls.nls

object UiPageContent {

  private case class Props(context: Context, initialTitle: String, status: PageStatus.Value, contents: CallbackTo[TagMod])

  private val component = ScalaComponent.builder[Props]("content")
    .render_P { props =>

      implicit val context: Context = props.context

      if (props.status == PageStatus.NotAuthorized) {
        <.div(
          <.h1(props.initialTitle),
          if (Nls.nlsNL) {
            <.div(
              <.br(),
              <.i(
                "Deze informatie is enkel beschikbaar voor OpenStreetMap gebruikers, na ",
                context.gotoLogin(),
                "."
              )
            )
          }
          else {
            <.div(
              <.br(),
              <.i(
                "This information is available to registered OpenStreetMap contributors only, after ",
                context.gotoLogin(),
                "."
              )
            )
          }
        )
      }
      else if (props.status == PageStatus.Loading || props.status == PageStatus.LoadStarting) {
        <.div(
          <.h1(props.initialTitle),
          <.div(^.cls := "ui active inline loader")
        )
      }
      else if (props.status == PageStatus.NotFound) {
        <.div(
          <.h1(props.initialTitle),
          <.div(
            nls("Not found", "Niet gevonden")
          )
        )
      }
      else if (props.status == PageStatus.Timeout) {
        <.div(
          <.h1(props.initialTitle),
          <.div(
            nls("Timeout", "Timeout")
          )
        )
      }
      else if (props.status == PageStatus.Failure) {
        <.div(
          <.h1(props.initialTitle),
          <.div(
            nls("Failure", "Fout")
          )
        )
      }
      else {
        <.div(
          props.contents.runNow()
        )
      }
    }
    .build

  def apply(initialTitle: String, status: PageStatus.Value, contents: CallbackTo[TagMod])(implicit context: Context): VdomElement =
    component(Props(context, initialTitle, status, contents))

}
