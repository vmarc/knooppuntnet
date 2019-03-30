package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.repository.GraphRepository
import kpn.core.repository.RouteRepository
import kpn.shared.LatLonImpl
import kpn.shared.NetworkType
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPathKey
import kpn.shared.planner.RouteLeg
import kpn.shared.planner.RouteLegFragment
import kpn.shared.planner.RouteLegNode
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteNetworkNodeInfo

class LegBuilderImpl(
  graphRepository: GraphRepository,
  routeRepository: RouteRepository
) extends LegBuilder {

  val graphMap: Map[String, NodeNetworkGraph] = NetworkType.all.map { networkType =>
    val graph = buildGraph(networkType)
    (networkType.name, graph)
  }.toMap

  override def build(networkType: String, legId: String, sourceNodeId: String, sinkNodeId: String): Option[RouteLeg] = {
    graphMap.get(networkType) match {
      case Some(graph) =>
        graph.findPath(sourceNodeId.toLong, sinkNodeId.toLong) match {
          case Some(path) =>

            val fragments: Seq[RouteLegFragment] = path.legs.flatMap { leg =>
              val routeId = leg.pathKey.routeId
              routeRepository.routeWithId(routeId, Couch.uiTimeout) match {
                case None =>
                  println(s"route $routeId not found")
                  None
                case Some(route) =>
                  val routeMap = route.analysis.get.map // TODO make more safe
                  trackPathFromRouteMap(routeMap, leg.pathKey) match {
                    case None => None
                    case Some(trackPath) =>

                      val latLons: Seq[LatLonImpl] = trackPath.segments.flatMap { trackPathSegment =>
                        trackPathSegment.fragments.map { fragment =>
                          val tp = fragment.trackPoint
                          LatLonImpl(tp.lat, tp.lon)
                        }
                      }

                      val meters = trackPath.segments.flatMap(_.fragments.map(_.meters)).sum

                      nodeFromRouteMap(routeMap, trackPath.endNodeId) match {
                        case Some(routeNetworkNodeInfo) =>
                          val nodeId = routeNetworkNodeInfo.id.toString
                          val nodeName = routeNetworkNodeInfo.name
                          val latLon = LatLonImpl(routeNetworkNodeInfo.lat, routeNetworkNodeInfo.lon)
                          val sink = RouteLegNode(nodeId, nodeName, latLon)

                          nodeFromRouteMap(routeMap, trackPath.startNodeId) match {
                            case Some(routeNetworkNodeInfo2) =>
                              val startLatLon = LatLonImpl(routeNetworkNodeInfo2.lat, routeNetworkNodeInfo2.lon)
                              val allLatLons = startLatLon +: latLons

                              Some(RouteLegFragment(sink, meters, allLatLons))
                            case None =>
                              println(s"route $routeId source node ${trackPath.startNodeId} not found")
                              None
                          }

                        case None =>
                          println(s"route $routeId sink node ${trackPath.endNodeId} not found")
                          None
                      }
                  }
              }
            }

            val routeLeg = RouteLeg(legId, fragments)
            Some(routeLeg)

          case None =>
            println(s"Could not find $networkType path between $sourceNodeId and $sinkNodeId")
            None
        }

      case _ =>
        println("Could not find graph for network type " + networkType)
        None
    }
  }

  private def buildGraph(networkType: NetworkType): NodeNetworkGraph = {
    val graph = new NodeNetworkGraphImpl()
    val edges = graphRepository.edges(networkType)

    println(s"Loaded ${networkType.name} ${edges.size} edges")

    edges.foreach(graph.add)
    graph
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

}
