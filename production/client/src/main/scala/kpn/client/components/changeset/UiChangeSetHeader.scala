package kpn.client.components.changeset

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.ChangeSetPageArgs
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiOsmLink
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSetPage

object UiChangeSetHeader {

  private case class Props(args: ChangeSetPageArgs, page: ChangeSetPage)

  private val component = ScalaComponent.builder[Props]("change-set-header")
    .render_P { props =>
      new Renderer(props.args, props.page).render()
    }
    .build

  def apply(args: ChangeSetPageArgs, page: ChangeSetPage): TagMod = component(Props(args, page))

  private class Renderer(args: ChangeSetPageArgs, page: ChangeSetPage) {

    implicit val context: Context = args.context

    private val summary = page.summary

    def render(): VdomElement = {

      val link = <.div(
        UiOsmLink.osmChangeSet(summary.key.changeSetId),
        " ",
        <.a(
          ^.cls := "external",
          ^.href := s"https://overpass-api.de/achavi/?changeset=${summary.key.changeSetId}",
          ^.rel := "nofollow",
          ^.target := "_blank",
          "achavi"
        )
      )

      <.table(
        <.tbody(
          row(nls("Changeset", "Wijzigingenset"), link),
          row(nls("Minute diff", "Replicatie nummer"), ReplicationId(summary.key.replicationNumber).name),
          page.changeSetInfo.whenDefined { changeSetInfo =>
            changeSetInfo.tags("comment").whenDefined { comment =>
              row(nls("Comment", "Commentaar"), comment)
            }
          },
          row(nls("Analysis", "Analyse"), UiChangeSetAnalysis(page))
        )
      )
    }

    private def row(key: String, value: TagMod): TagMod = {
      <.tr(
        <.td(key),
        <.td(value)
      )
    }
  }

}
