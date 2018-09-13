package kpn.client.components.map

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.map.vector.SelectedFeature
import kpn.client.common.map.vector.SelectedFeatureHolder
import kpn.client.common.map.vector.SelectedNode
import kpn.client.common.map.vector.SelectedRoute
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.PageState
import kpn.client.components.common.UiNetworkTypeIcon
import kpn.shared.NetworkType

import scalacss.ScalaCssReact._

object UiMapDetail {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val detail: StyleA = style(
      padding(15.px)
    )

    val networkType: StyleA = style(
      fontSize(20.px),
      paddingBottom(30.px)
    )

    val title: StyleA = style(
      fontWeight.bold,
      fontSize(20.px),
      paddingBottom(5.px)
    )

    val subTitle: StyleA = style(
      fontWeight.bold,
      paddingTop(20.px)
    )

    val moreDetails: StyleA = style(
      paddingBottom(20.px)
    )

    val note: StyleA = style(
      paddingTop(40.px),
      fontStyle.italic
    )
  }

  private case class State(pageState: PageState[Unit] = PageState(), selection: Option[SelectedFeature] = None)

  private case class Props(context: Context, networkType: NetworkType, selectionHolder: SelectedFeatureHolder)

  private class Backend(scope: BackendScope[Props, State]) {

    def update(selection: Option[SelectedFeature]): Unit = {
      scope.modState(_.copy(selection = selection)).runNow()
    }

    scope.props.runNow().selectionHolder.listener = Some(update)

    def render(props: Props, state: State): VdomElement = {
      new Renderer(props, state).render()
    }

  }

  private val component = ScalaComponent.builder[Props]("changes")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(networkType: NetworkType, selectionHolder: SelectedFeatureHolder)(implicit context: Context): VdomElement = {
    component(Props(context, networkType, selectionHolder))
  }

  private class Renderer(props: Props, state: State) {

    private implicit val context: Context = props.context

    def render(): VdomElement = {

      <.div(
        Styles.detail,
        networkTypeTitle(),
        state.selection match {
          case Some(selection: SelectedNode) => UiMapDetailNode(props.networkType, selection)
          case Some(selection: SelectedRoute) => UiMapDetailRoute(selection)
          case _ => UiMapDetailDefault()
        }
      )
    }

    private def networkTypeTitle(): VdomElement = {

      val title = props.networkType match {
        case NetworkType.hiking => nls("Hiking", "Wandelen")
        case NetworkType.bicycle => nls("Cycling", "Fietsen")
        case NetworkType.horse => nls("Horse", "Ruiter")
        case NetworkType.motorboat => nls("Motorboat", "Motorboot")
        case NetworkType.canoe => nls("Canoe", "Kano")
        case NetworkType.inlineSkates => "Inline Skates"
      }

      <.div(
        Styles.networkType,
        UiNetworkTypeIcon(props.networkType),
        " ",
        title
      )
    }
  }

}
