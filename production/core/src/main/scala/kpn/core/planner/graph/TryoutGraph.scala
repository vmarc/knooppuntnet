package kpn.core.planner.graph

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.directions.Latlon
import kpn.core.planner.plan.Plan
import kpn.core.planner.plan.PlanLeg
import kpn.core.planner.plan.PlanLegFragment
import kpn.core.planner.plan.PlanNode
import kpn.core.repository.GraphRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl
import kpn.shared.NetworkType
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPathKey
import kpn.shared.common.TrackSegmentFragment
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteNetworkNodeInfo

object TryoutGraph {

  def main(args: Array[String]): Unit = {

    Couch.executeIn("master1") { database =>
      val graphRepostory = new GraphRepositoryImpl(database: Database)
      val routeRepostory = new RouteRepositoryImpl(database: Database)


      val graph = new NodeNetworkGraphImpl()
      println("Loading edges")
      val edges = graphRepostory.edges(NetworkType.hiking)
      edges.foreach { edge =>
        graph.add(edge)
      }
      println("Loading edges done")


      val pathOption = graph.findPath(289197467L /* 08 - Grensstraat */ , 299417894L /* 36 Oude Baan */)


      val planLegs = pathOption match {
        case None =>
          println("Could not find path")
          Seq()
        case Some(path) =>
          println("path=" + pathOption)

          path.legs.flatMap { leg =>
            val routeId = leg.pathKey.routeId
            routeRepostory.routeWithId(routeId) match {
              case None =>
                println(s"route $routeId not found")
                None
              case Some(route) =>
                val routeMap = route.analysis.get.map // TODO make more safe
                trackPathFromRouteMap(routeMap, leg.pathKey) match {
                  case None =>
                    None

                  case Some(trackPath) =>

                    nodeFromRouteMap(routeMap, leg.sinkNodeId).map(toPlanNode) match {
                      case None =>
                        println(s"sink node ${leg.sinkNodeId} not found in route $routeId")
                        None
                      case Some(sinkNode) =>
                        val fragments = trackPath.segments.flatMap(_.fragments).map(fragment => toPlanLegFragment(routeMap, fragment))
                        Some(PlanLeg(sinkNode, fragments))
                    }
                }
            }
          }
      }

      val sourcePlanNode = PlanNode(289197467L, "TODO", Latlon(0, 0)) // TODO pick up from first route document
    val plan = Plan(sourcePlanNode, planLegs)

      println(plan)

      val latlons = plan.legs.flatMap(leg => leg.fragments.map(_.sink))

      latlons.foreach(println)

    }
  }

  private def trackPathFromRouteMap(routeMap: RouteMap, trackPathKey: TrackPathKey): Option[TrackPath] = {
    trackPathKey.pathType match {
      case "forward" => routeMap.forwardPath
      case "backward" => routeMap.backwardPath
      case "start" => Some(routeMap.startTentaclePaths(trackPathKey.pathIndex - 1))
      case "end" => Some(routeMap.endTentaclePaths(trackPathKey.pathIndex - 1))
      case _ => None
    }
  }

  private def nodeFromRouteMap(routeMap: RouteMap, nodeId: Long): Option[RouteNetworkNodeInfo] = {
    val allNodes = routeMap.startNodes ++
      routeMap.endNodes ++
      routeMap.startTentacleNodes ++
      routeMap.endTentacleNodes
    allNodes.find(_.id == nodeId)
  }

  private def toPlanNode(nodeInfo: RouteNetworkNodeInfo): PlanNode = {
    PlanNode(nodeInfo.id, nodeInfo.name, Latlon(nodeInfo.lat.toDouble, nodeInfo.lon.toDouble))
  }

  private def toPlanLegFragment(routeMap: RouteMap, fragment: TrackSegmentFragment): PlanLegFragment = {

    val sink: Latlon = Latlon(fragment.trackPoint.lat.toDouble, fragment.trackPoint.lon.toDouble)
    val meters: Int = fragment.meters
    val orientation = fragment.orientation
    val streetName = fragment.streetIndex.flatMap { streetIndex =>
      if (streetIndex < routeMap.streets.size) {
        Some(routeMap.streets(streetIndex))
      }
      else {
        None
      }
    }

    PlanLegFragment(sink, meters, orientation, streetName)
  }
}
