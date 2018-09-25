package kpn.client.components.demo

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.ComponentsPageArgs
import kpn.client.common.Context
import kpn.client.components.changeset.diff.UiNetworkNodeDiff
import kpn.client.components.common.CssSettings.default._
import kpn.shared.NodeIntegrityCheck
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.network.NetworkNodeDiff
import kpn.shared.diff.network.NodeIntegrityCheckDiff
import kpn.shared.diff.network.NodeRouteReferenceDiffs

import scalacss.ScalaCssReact._

object UiComponentsPage {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val indent: StyleA = style(
      paddingLeft(25.px)
    )
  }

  private case class Props(args: ComponentsPageArgs)

  private val component = ScalaComponent.builder[Props]("components")
    .render_P { props =>
      new Renderer()(props.args.context).render()
    }
    .build


  def apply(args: ComponentsPageArgs): VdomElement = component(Props(args))

  private class Renderer()(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        Styles.indent,
        <.div("Components"),
        <.div(
          Styles.indent,
          networkNodeDiff()
        )
      )
    }

    private def networkNodeDiff(): VdomElement = {
      <.div(
        <.hr(),
        <.div("UiNetworkNodeDiff"),
        <.div(
          Styles.indent,
          UiNetworkNodeDiff(NetworkNodeDiff(), KnownElements()),
          <.hr(),
          UiNetworkNodeDiff(NetworkNodeDiff(roleConnection = Some(true)), KnownElements()),
          <.hr(),
          UiNetworkNodeDiff(NetworkNodeDiff(roleConnection = Some(false)), KnownElements()),
          <.hr(),
          UiNetworkNodeDiff(NetworkNodeDiff(definedInNetworkRelation = Some(true)), KnownElements()),
          <.hr(),
          UiNetworkNodeDiff(NetworkNodeDiff(definedInNetworkRelation = Some(false)), KnownElements()),
          <.hr(),
          UiNetworkNodeDiff(
            NetworkNodeDiff(
              routeReferenceDiffs = Some(
                NodeRouteReferenceDiffs(
                  removed = Seq(Ref(1, "01-02"), Ref(2, "02-03"), Ref(3, "03-04")),
                  added = Seq(Ref(4, "04-05")),
                  remaining = Seq(Ref(5, "06-07"), Ref(6, "07-08"))
                )
              )
            ),
            KnownElements(routeIds = Set(2, 3, 6))
          ),
          <.hr(),
          UiNetworkNodeDiff(
            NetworkNodeDiff(
              nodeIntegrityCheckDiff = Some(
                NodeIntegrityCheckDiff(
                  before = Some(
                    NodeIntegrityCheck(
                      nodeName = "01",
                      nodeId = 1,
                      actual = 3,
                      expected = 2,
                      failed = true
                    )
                  ),
                  after = Some(
                    NodeIntegrityCheck(
                      nodeName = "01",
                      nodeId = 1,
                      actual = 2,
                      expected = 2,
                      failed = false
                    )
                  )
                )
              )
            ),
            KnownElements()
          ),
          <.hr(),
          UiNetworkNodeDiff(
            NetworkNodeDiff(
              tagDiffs = Some(
                TagDiffs(
                  mainTags = Seq(
                    TagDetail(TagDetailType.Same, "key", Some("same"), Some("after"))
                  ),
                  extraTags = Seq(
                    TagDetail(TagDetailType.Add, "key", None, Some("after")),
                    TagDetail(TagDetailType.Delete, "key", Some("before"), None),
                    TagDetail(TagDetailType.Update, "key", Some("before"), Some("after")),
                    TagDetail(TagDetailType.Same, "key", Some("same"), Some("after"))
                  )
                )
              )
            ),
            KnownElements()
          )
        )
      )
    }
  }

}
