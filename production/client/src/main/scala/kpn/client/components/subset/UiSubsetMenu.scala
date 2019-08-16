// Migrated to Angular: subset-page-header-block.component.ts
package kpn.client.components.subset

import chandu0101.scalajs.react.components.Implicits._
import chandu0101.scalajs.react.components.materialui.MuiMenuItem
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.raw.ReactElement
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.RouteConfiguration.GotoSubsetChanges
import kpn.client.RouteConfiguration.GotoSubsetFacts
import kpn.client.RouteConfiguration.GotoSubsetMap
import kpn.client.RouteConfiguration.GotoSubsetNetworks
import kpn.client.RouteConfiguration.GotoSubsetOrphanNodes
import kpn.client.RouteConfiguration.GotoSubsetOrphanRoutes
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.SubsetPageArgs
import kpn.client.components.menu.UiMenuItem
import kpn.shared.subset.SubsetInfo

import scala.scalajs.js

object UiSubsetMenu {

  case class Target(name: String)

  private case class Props(args: SubsetPageArgs, target: Target, subsetInfo: SubsetInfo)

  val targetNetworks = Target("networks")
  val targetFacts = Target("facts")
  val targetOrphanNodes = Target("orphan-nodes")
  val targetOrphanRoutes = Target("orphan-routes")
  val targetChanges = Target("changes")
  val targetMap = Target("map")

  private val component = ScalaComponent.builder[Props]("subset-menu")
    .render_P { props =>
      new Renderer(props.subsetInfo, props.target)(props.args.context).render()
    }
    .build

  def apply(context: SubsetPageArgs, target: Target, subsetInfo: SubsetInfo): VdomElement = {
    component(Props(context, target, subsetInfo))
  }

  private class Renderer(subsetInfo: SubsetInfo, target: Target)(implicit val context: Context) {

    def render(): VdomElement = {
      MuiMenuItem[String](
        primaryText = nls("Analysis details", "Analyse details"),
        initiallyOpen = true,
        primaryTogglesNestedList = true,
        nestedItems = js.Array[ReactElement](
          networks().rawElement,
          facts().rawElement,
          orphanNodes().rawElement,
          orphanRoutes().rawElement,
          changes().rawElement
        )
      )()
    }

    private def networks(): VdomElement = {
      UiMenuItem(
        nls("Networks", "Netwerken"),
        target == targetNetworks,
        Some(subsetInfo.networkCount),
        GotoSubsetNetworks(context.lang, subsetInfo.country, subsetInfo.networkType)
      )
    }

    private def facts(): VdomElement = {
      UiMenuItem(
        nls("Facts", "Feiten"),
        target == targetFacts,
        Some(subsetInfo.factCount),
        GotoSubsetFacts(context.lang, subsetInfo.country, subsetInfo.networkType)
      )
    }

    private def orphanNodes(): VdomElement = {
      UiMenuItem(
        nls("Orphan Nodes", "Knooppuntwezen"),
        target == targetOrphanNodes,
        Some(subsetInfo.orphanNodeCount),
        GotoSubsetOrphanNodes(context.lang, subsetInfo.country, subsetInfo.networkType)
      )
    }

    private def orphanRoutes(): VdomElement = {
      UiMenuItem(
        nls("Orphan Routes", "Routewezen"),
        target == targetOrphanRoutes,
        Some(subsetInfo.orphanRouteCount),
        GotoSubsetOrphanRoutes(context.lang, subsetInfo.country, subsetInfo.networkType)
      )
    }

    private def changes(): VdomElement = {
      UiMenuItem(
        nls("Changes", "Wijzigingen"),
        target == targetChanges,
        None,
        GotoSubsetChanges(context.lang, subsetInfo.country, subsetInfo.networkType)
      )
    }

    private def map(): VdomElement = {
      UiMenuItem(
        nls("Map", "Kaart"),
        target == targetMap,
        None,
        GotoSubsetMap(context.lang, subsetInfo.country, subsetInfo.networkType)
      )
    }
  }

}
