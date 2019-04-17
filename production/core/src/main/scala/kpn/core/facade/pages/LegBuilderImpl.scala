package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.planner.graph.NodeNetworkGraphImpl
import kpn.core.repository.GraphRepository
import kpn.core.repository.RouteRepository
import kpn.shared.NetworkType
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPathKey
import kpn.shared.planner.RouteLeg
import kpn.shared.planner.RouteLegFragment
import kpn.shared.planner.RouteLegNode
import kpn.shared.planner.RouteLegRoute
import kpn.shared.planner.RouteLegSegment
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

            val legRoutes: Seq[RouteLegRoute] = path.segments.flatMap { segment =>
              val routeId = segment.pathKey.routeId
              routeRepository.routeWithId(routeId, Couch.uiTimeout) match {
                case None =>
                  println(s"route $routeId not found")
                  None
                case Some(route) =>
                  val routeMap = route.analysis.get.map // TODO make more safe
                  trackPathFromRouteMap(routeMap, segment.pathKey) match {
                    case None => None
                    case Some(trackPath) =>

                      val segments: Seq[RouteLegSegment] = trackPath.segments.map { trackPathSegment =>
                        val fragments: Seq[RouteLegFragment] = trackPathSegment.fragments.map { fragment =>
                          RouteLegFragment(
                            fragment.trackPoint.lat,
                            fragment.trackPoint.lon,
                            fragment.meters,
                            fragment.orientation,
                            fragment.streetIndex
                          )
                        }
                        val meters = fragments.map(_.meters).sum
                        RouteLegSegment(
                          meters,
                          trackPathSegment.surface,
                          fragments
                        )
                      }

                      val meters = segments.map(_.meters).sum

                      nodeFromRouteMap(routeMap, trackPath.endNodeId) match {
                        case Some(sinkRouteNetworkNodeInfo) =>
                          val sinkNodeId = sinkRouteNetworkNodeInfo.id.toString
                          val sinkodeName = sinkRouteNetworkNodeInfo.name
                          val sink = RouteLegNode(sinkNodeId, sinkodeName, sinkRouteNetworkNodeInfo.lat, sinkRouteNetworkNodeInfo.lon)

                          nodeFromRouteMap(routeMap, trackPath.startNodeId) match {
                            case Some(sourceRouteNetworkNodeInfo) =>
                              val sourceNodeId = sourceRouteNetworkNodeInfo.id.toString
                              val sourceNodeName = sourceRouteNetworkNodeInfo.name
                              val source = RouteLegNode(sourceNodeId, sourceNodeName, sourceRouteNetworkNodeInfo.lat, sinkRouteNetworkNodeInfo.lon)
                              val meters = segments.map(_.meters).sum
                              Some(RouteLegRoute(source, sink, meters, segments, routeMap.streets))

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

            val routeLeg = RouteLeg(legId, legRoutes)
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
