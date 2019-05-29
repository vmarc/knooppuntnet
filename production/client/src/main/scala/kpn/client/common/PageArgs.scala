// TODO migrate to Angular
package kpn.client.common

import kpn.client.RouteConfiguration.Goto
import kpn.client.RouteConfiguration.GotoChangeSets
import kpn.client.RouteConfiguration.GotoChanges
import kpn.client.RouteConfiguration.GotoComponents
import kpn.client.RouteConfiguration.GotoGlossary
import kpn.client.RouteConfiguration.GotoMap
import kpn.client.RouteConfiguration.GotoNetworkPage
import kpn.client.RouteConfiguration.GotoNode
import kpn.client.RouteConfiguration.GotoOverview
import kpn.client.RouteConfiguration.GotoRoute
import kpn.client.RouteConfiguration.GotoSubsetPage
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset

trait PageArgs {
  def context: Context

  def currentPage: Goto
}

case class MapPageArgs(
  context: Context,
  currentPage: GotoMap,
  networkType: NetworkType
) extends PageArgs

case class SubsetPageArgs(
  context: Context,
  currentPage: GotoSubsetPage,
  subset: Subset,
  fact: Option[Fact]
) extends PageArgs

case class NetworkPageArgs(
  context: Context,
  currentPage: GotoNetworkPage,
  networkId: Long
) extends PageArgs

case class ChangeSetsPageArgs(
  context: Context,
  currentPage: GotoChangeSets
) extends PageArgs

case class ChangeSetPageArgs(
  context: Context,
  currentPage: Goto,
  changeSetId: Long,
  replicationNumber: Int,
  networkId: Long
) extends PageArgs

case class NodePageArgs(
  context: Context,
  currentPage: GotoNode,
  nodeId: Long
) extends PageArgs

case class RoutePageArgs(
  context: Context,
  currentPage: GotoRoute,
  routeId: Long
) extends PageArgs

case class ChangesPageArgs(
  context: Context,
  currentPage: GotoChanges
) extends PageArgs

case class ComponentsPageArgs(
  context: Context,
  currentPage: GotoComponents
) extends PageArgs

case class GlossaryPageArgs(
  context: Context,
  currentPage: GotoGlossary,
  entry: Option[String]
) extends PageArgs

case class OverviewPageArgs(
  context: Context,
  currentPage: GotoOverview
) extends PageArgs
