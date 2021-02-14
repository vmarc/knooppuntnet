package kpn.core.tools.typescript

import kpn.api.common.BoundsI
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.ChangesPage
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.LocationChangesTree
import kpn.api.common.LocationChangesTreeNode
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkFact
import kpn.api.common.NetworkFacts
import kpn.api.common.NetworkIntegrityCheckFailed
import kpn.api.common.NodeInfo
import kpn.api.common.NodeMapInfo
import kpn.api.common.NodeRoute
import kpn.api.common.Poi
import kpn.api.common.PoiAnalysis
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteSummary
import kpn.api.common.RoutesFact
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.changes.filter.ChangesFilterPeriod
import kpn.api.common.common.KnownElements
import kpn.api.common.common.NetworkRefs
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.ReferencedElements
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackSegment
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NodeData
import kpn.api.common.diff.NodeDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayData
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.network.NodeRouteReferenceDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.api.common.location.Ids
import kpn.api.common.location.Location
import kpn.api.common.location.LocationEditPage
import kpn.api.common.location.LocationFact
import kpn.api.common.location.LocationFactsPage
import kpn.api.common.location.LocationNode
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.monitor.LongdistanceRoute
import kpn.api.common.monitor.LongdistanceRouteChange
import kpn.api.common.monitor.LongdistanceRouteChangePage
import kpn.api.common.monitor.LongdistanceRouteChangeSummary
import kpn.api.common.monitor.LongdistanceRouteChangesPage
import kpn.api.common.monitor.LongdistanceRouteDetail
import kpn.api.common.monitor.LongdistanceRouteDetailsPage
import kpn.api.common.monitor.LongdistanceRouteMapPage
import kpn.api.common.monitor.LongdistanceRouteNokSegment
import kpn.api.common.monitor.LongdistanceRouteSegment
import kpn.api.common.monitor.LongdistanceRoutesPage
import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupDetail
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteChangeDetail
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangeSummary
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.common.monitor.RouteGroupDetail
import kpn.api.common.network.NetworkFactsPage
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.common.network.NetworkInfoNode
import kpn.api.common.network.NetworkInfoRoute
import kpn.api.common.network.NetworkMapPage
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkNodeFact
import kpn.api.common.network.NetworkNodesPage
import kpn.api.common.network.NetworkRouteFact
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.node.NodeChangeInfos
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.common.node.NodeNetworkReference
import kpn.api.common.node.NodeReferences
import kpn.api.common.planner.LegEndRoute
import kpn.api.common.planner.PlanLegDetail
import kpn.api.common.planner.PlanRoute
import kpn.api.common.planner.PlanSegment
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.route.RouteChangeInfo
import kpn.api.common.route.RouteChangeInfos
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteReferences
import kpn.api.common.route.WayGeometry
import kpn.api.common.status.BarChart
import kpn.api.common.status.BarChart2D
import kpn.api.common.status.BarChart2dValue
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.common.subset.SubsetFactDetailsPage
import kpn.api.common.subset.SubsetFactsPage
import kpn.api.common.subset.SubsetMapPage
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.common.subset.SubsetNodeFactDetailsPage
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.common.tiles.ClientPoiGroupDefinition
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import org.apache.commons.io.FileUtils

import java.io.File
import java.io.PrintStream
import scala.jdk.CollectionConverters._
import scala.reflect.runtime.universe._

object TypescriptTool {
  def main(args: Array[String]): Unit = {
    new TypescriptTool().generate()
  }
}

class TypescriptTool() {

  val root = "/home/marcv/wrk/projects1/knooppuntnet/server/src/main/scala/kpn/api/common"

  val targetDir = "/home/marcv/wrk/projects1/knooppuntnet/client/src/app"

  val ignoredClasses: Seq[String] = Seq(
    // following classes have been manually changed in Typescript after changing List to Array, enable again when switching to interfaces
    classOf[Location].getSimpleName,
    classOf[LocationChangeSetSummary].getSimpleName,
    classOf[LocationChangesTree].getSimpleName,
    classOf[LocationChangesTreeNode].getSimpleName,
    classOf[LocationEditPage].getSimpleName,
    classOf[LocationFact].getSimpleName,
    classOf[LocationFactsPage].getSimpleName,
    classOf[LocationNode].getSimpleName,
    classOf[LocationNodeInfo].getSimpleName,
    classOf[LocationNodesPage].getSimpleName,
    classOf[LocationRoutesPage].getSimpleName,

    classOf[ChangesFilter].getSimpleName,
    classOf[ChangesFilterPeriod].getSimpleName,
    classOf[ChangeSet].getSimpleName,
    classOf[ChangeSetData].getSimpleName,
    classOf[ChangeSetPage].getSimpleName,
    classOf[ChangeSetElementRefs].getSimpleName,
    classOf[ChangeSetSummary].getSimpleName,
    classOf[ChangesPage].getSimpleName,
    classOf[BarChart].getSimpleName,
    classOf[BarChart2D].getSimpleName,
    classOf[BarChart2dValue].getSimpleName,

    classOf[NetworkChanges].getSimpleName,
    classOf[NetworkFact].getSimpleName,
    classOf[NetworkFactRefs].getSimpleName,
    classOf[NetworkFacts].getSimpleName,
    classOf[NetworkFactsPage].getSimpleName,
    classOf[NetworkInfo].getSimpleName,
    classOf[NetworkInfoDetail].getSimpleName,
    classOf[NetworkInfoNode].getSimpleName,
    classOf[NetworkInfoRoute].getSimpleName,
    classOf[NetworkIntegrityCheckFailed].getSimpleName,
    classOf[NetworkMapPage].getSimpleName,
    classOf[NetworkNodeDetail].getSimpleName,
    classOf[NetworkNodeFact].getSimpleName,
    classOf[NetworkNodesPage].getSimpleName,
    classOf[NetworkRefs].getSimpleName,
    classOf[NetworkRouteFact].getSimpleName,
    classOf[NetworkRoutesPage].getSimpleName,
    classOf[NodeChange].getSimpleName,
    classOf[NodeChangeInfo].getSimpleName,
    classOf[NodeChangeInfos].getSimpleName,
    classOf[NodeData].getSimpleName,
    classOf[NodeDiffs].getSimpleName,
    classOf[NodeInfo].getSimpleName,
    classOf[NodeIntegrity].getSimpleName,
    classOf[NodeIntegrityDetail].getSimpleName,
    classOf[NodeMapInfo].getSimpleName,
    classOf[NodeNetworkReference].getSimpleName,
    classOf[NodeReferences].getSimpleName,
    classOf[NodeRoute].getSimpleName,
    classOf[NodeRouteExpectedCount].getSimpleName,
    classOf[NodeRouteReferenceDiffs].getSimpleName,
    classOf[NodeRouteRefs].getSimpleName,

    classOf[ClientPoiConfiguration].getSimpleName,
    classOf[ClientPoiGroupDefinition].getSimpleName,
    classOf[FactDiffs].getSimpleName,
    classOf[Fact].getSimpleName,
    classOf[GeometryDiff].getSimpleName,
    classOf[IdDiffs].getSimpleName,
    classOf[Ids].getSimpleName,
    classOf[KnownElements].getSimpleName,
    classOf[LegEndRoute].getSimpleName,
    classOf[MapNodeDetail].getSimpleName,
    classOf[MapRouteDetail].getSimpleName,
    classOf[PlanLegDetail].getSimpleName,
    classOf[PlanRoute].getSimpleName,
    classOf[PlanSegment].getSimpleName,
    classOf[Poi].getSimpleName,
    classOf[PoiAnalysis].getSimpleName,
    classOf[RawData].getSimpleName,
    classOf[RawRelation].getSimpleName,
    classOf[RawWay].getSimpleName,
    classOf[RefChanges].getSimpleName,
    classOf[RefDiffs].getSimpleName,
    classOf[ReferencedElements].getSimpleName,
    classOf[RouteChange].getSimpleName,
    classOf[RouteChangeInfo].getSimpleName,
    classOf[RouteChangeInfos].getSimpleName,
    classOf[RouteData].getSimpleName,
    classOf[RouteDiff].getSimpleName,
    classOf[RouteInfo].getSimpleName,
    classOf[RouteInfoAnalysis].getSimpleName,
    classOf[RouteLocationAnalysis].getSimpleName,
    classOf[RouteMap].getSimpleName,
    classOf[RouteNodeDiff].getSimpleName,
    classOf[RouteReferences].getSimpleName,
    classOf[RouteSummary].getSimpleName,
    classOf[RoutesFact].getSimpleName,
    classOf[SubsetFactDetailsPage].getSimpleName,
    classOf[SubsetFactsPage].getSimpleName,
    classOf[SubsetMapPage].getSimpleName,
    classOf[SubsetNetworksPage].getSimpleName,
    classOf[SubsetNodeFactDetailsPage].getSimpleName,
    classOf[SubsetOrphanNodesPage].getSimpleName,
    classOf[SubsetOrphanRoutesPage].getSimpleName,
    classOf[TagDiffs].getSimpleName,
    classOf[ToStringBuilder].getSimpleName, // TODO should this be in the api ???
    classOf[TrackPath].getSimpleName,
    classOf[TrackSegment].getSimpleName,
    classOf[Way].getSimpleName,
    classOf[WayData].getSimpleName,
    classOf[WayGeometry].getSimpleName,
    classOf[WayUpdate].getSimpleName,
  )

  val newClasses = Seq(
    // for these classes, Typescript interfaces are generated instead of classes
    classOf[MonitorRoute], // not used in API ?
    classOf[MonitorGroupPage],
    classOf[MonitorRouteDetail],
    classOf[MonitorRouteChangesPage],
    classOf[MonitorRouteDetail],
    classOf[MonitorRouteDetailsPage],
    classOf[MonitorRouteMapPage],
    classOf[MonitorRouteNokSegment],
    classOf[MonitorRouteSegment],
    classOf[MonitorRouteChangePage],
    classOf[MonitorRouteChange],
    classOf[MonitorRouteChangeSummary],
    classOf[MonitorRouteReferenceInfo],
    classOf[MonitorGroup],
    classOf[MonitorAdminGroupPage],
    classOf[BoundsI],
    classOf[ChangeKeyI],
    classOf[MonitorGroupsPage],
    classOf[MonitorChangesPage],
    classOf[MonitorChangesParameters],
    classOf[MonitorGroupChangesPage],
    classOf[MonitorGroupDetail],
    classOf[MonitorRouteChangeDetail],
    classOf[RouteGroupDetail],
    classOf[LongdistanceRoute],
    classOf[LongdistanceRouteChange],
    classOf[LongdistanceRouteChangePage],
    classOf[LongdistanceRouteChangesPage],
    classOf[LongdistanceRouteChangeSummary],
    classOf[LongdistanceRouteDetail],
    classOf[LongdistanceRouteDetailsPage],
    classOf[LongdistanceRouteMapPage],
    classOf[LongdistanceRouteNokSegment],
    classOf[LongdistanceRouteSegment],
    classOf[LongdistanceRoutesPage],
  )

  def generate(): Unit = {

    val mirror = runtimeMirror(classOf[RawNode].getClassLoader)
    val scalaTypes: Seq[Type] = scalaClassNames().map(className => mirror.staticClass(className).typeSignature)
    val caseClasses: Seq[Type] = scalaTypes.filter(isCaseClass)

    val newClassNames = newClasses.map(_.getSimpleName)

    caseClasses.foreach { caseClass =>
      val className = caseClass.typeSymbol.name.toString
      val classInfo = new ClassAnalyzer().analyze(caseClass)
      val file = new File(targetDir + "/" + classInfo.fileName)
      file.getParentFile.mkdirs()
      val out = new PrintStream(file)
      if (newClassNames.contains(className)) {
        new TypescriptWriter(out, classInfo).writeInterface()
      }
      else {
        new TypescriptWriter(out, classInfo).write()
      }
      out.close()
    }

    println("end")
  }

  private def scalaClassNames(): Seq[String] = {
    val files = FileUtils.listFiles(new File(root), Array("scala"), true).asScala.toSeq
    files.flatMap { file =>
      if (ignoredClasses.exists(n => file.getName.endsWith(n + ".scala"))) {
        None
      }
      else {
        val className = file.getAbsolutePath.drop(root.length - "kpn/api/common".length).dropRight(".scala".length).replace('/', '.')
        Some(className)
      }
    }
  }

  private def isCaseClass(scalaType: Type): Boolean = {
    scalaType.typeSymbol.toString.contains("NetworkNameMissing") ||
      scalaType.members.collect({ case m: MethodSymbol if m.isCaseAccessor => m }).nonEmpty
  }

}
