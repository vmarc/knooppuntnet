package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.custom.NetworkType
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.tools.export.GeoJsonLineStringGeometry
import kpn.core.util.UnitTest
import kpn.server.api.planner.leg.LegBuilderImpl
import kpn.server.json.Json
import kpn.server.repository.GraphRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class Issue150_RoutingAgainstOneWayDirectionTest extends UnitTest {

  test("bicyle routing against one-way direction 1") {

    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(null, database, false)
      val routeAnalysis1 = CaseStudy.routeAnalysis("12410463")
      val routeAnalysis2 = CaseStudy.routeAnalysis("1029893")

      routeRepository.save(routeAnalysis1.route)
      routeRepository.save(routeAnalysis2.route)

      val graphRepository = new GraphRepositoryImpl(null, database, graphLoadEnabled = true, mongoEnabled = false)
      graphRepository.loadGraphs()

      val legBuilder = new LegBuilderImpl(graphRepository, routeRepository)
      val params = LegBuildParams(
        NetworkType.cycling.name,
        LegEnd.node(7741683309L),
        LegEnd.node(42784896L)
      )

      legBuilder.leg(params) match {
        case None =>
        case Some(planLegDetail) =>

          planLegDetail.routes.foreach { planRoute =>
            val start = planRoute.sourceNode.latLon
            val latlons = start +: planRoute.segments.flatMap(_.fragments).map(_.latLon)
            val coordinates = latlons.toArray.map(c => Array(c.lon, c.lat))
            val line = GeoJsonLineStringGeometry(
              "LineString",
              coordinates
            )
            val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
            println("PLAN ROUTE")
            println(json.writeValueAsString(line))
          }

          val startNode = planLegDetail.routes.head.sourceNode.latLon
          val latLons = startNode +: planLegDetail.routes.flatMap(_.segments).flatMap(_.fragments).map(_.latLon)
          val coordinates = latLons.toArray.map(c => Array(c.lon, c.lat))
          val line = GeoJsonLineStringGeometry(
            "LineString",
            coordinates
          )
          val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
          println(json.writeValueAsString(line))
      }
    }
  }

  test("bicyle routing against one-way direction") {

    withCouchDatabase { database =>

      val routeRepository = new RouteRepositoryImpl(null, database, false)
      val routeAnalysis1 = CaseStudy.routeAnalysis("12410463")
      val routeAnalysis2 = CaseStudy.routeAnalysis("1029893")

      routeRepository.save(routeAnalysis1.route)
      routeRepository.save(routeAnalysis2.route)

      val graphRepository = new GraphRepositoryImpl(null, database, graphLoadEnabled = true, mongoEnabled = false)
      graphRepository.loadGraphs()

      val graph: NodeNetworkGraph = graphRepository.graph(NetworkType.cycling).get

      val startNode = "7741683309" // node 57

      val graphPath = graph.findPath(startNode, "42784896").get

      graphPath.source should equal(startNode)

      val route1forwardPath = routeAnalysis1.route.analysis.map.forwardPath.get
      val route2forwardPath = routeAnalysis2.route.analysis.map.forwardPath.get
      val route2startTentablePath1 = routeAnalysis2.route.analysis.map.startTentaclePaths.head

      val Seq(segment1a, segment1b, segment2a, segment2b, segment3a, segment3b) = graphPath.segments

      segment1a.sink should equal("12410463.101")
      segment1a.pathKey.routeId should equal(routeAnalysis1.id)
      segment1a.pathKey.pathId should equal(route1forwardPath.pathId + 100) // 101 oneWay false

      segment1b.sink should equal("7741672259")
      segment1b.pathKey.routeId should equal(routeAnalysis1.id)
      segment1b.pathKey.pathId should equal(route1forwardPath.pathId + 100) // 101 oneWay false

      // backward (reverse) order:  101
      route1forwardPath.startNodeId should equal(7741672259L) // node 09-57_09.a 09-53_09.b  (node on the right)
      route1forwardPath.endNodeId should equal(7741683309L) // node 57


      segment2a.sink should equal("1029893.3")
      segment2a.pathKey.routeId should equal(routeAnalysis2.id)
      segment2a.pathKey.pathId should equal(route2startTentablePath1.pathId) // 3

      segment2b.sink should equal("7751484678")
      segment2b.pathKey.routeId should equal(routeAnalysis2.id)
      segment2b.pathKey.pathId should equal(route2startTentablePath1.pathId) // 3

      route2startTentablePath1.startNodeId should equal(7741672259L) // node 09-53_09.b  (node on the right)
      route2startTentablePath1.endNodeId should equal(7751484678L) // node 09-53_09.a  (node on the left)
      val xx = route2startTentablePath1.segments


      segment3a.sink should equal("1029893.1")
      segment3a.pathKey.routeId should equal(routeAnalysis2.id)
      segment3a.pathKey.pathId should equal(route2forwardPath.pathId) // 1 oneWay true

      segment3b.sink should equal("42784896")
      segment3b.pathKey.routeId should equal(routeAnalysis2.id)
      segment3b.pathKey.pathId should equal(route2forwardPath.pathId) // 1 oneWay true

      route2forwardPath.startNodeId should equal(7751484678L) // node 09-53_09.a  (node on the left)
      route2forwardPath.endNodeId should equal(42784896L) // node 53

      val graphEdges = GraphEdgesView.query(database, NetworkType.cycling, stale = false)

      graphEdges.foreach { edge =>
        print(s"${edge.pathKey.routeId} ${edge.pathKey.pathId} ${edge.sourceNodeId} ${edge.sinkNodeId} ${edge.meters} ")
        val index = graphPath.segments.indexWhere(path => path.sink == edge.sinkNodeId.toString && path.pathKey == edge.pathKey)
        println(s"  $index")
      }

      //      route1
      //      route:12410463	[ "cycling", 12410463, 1 ]	[ 7741672259, 7741683309, 1478 ]
      //      route:12410463	[ "cycling", 12410463, 3 ]	[ 7751484678, 7741672259, 54 ]
      //      route:12410463	[ "cycling", 12410463, 101 ]	[ 7741683309, 7741672259, 1478 ]     ==> 1
      //
      //      route2
      //      route:1029893	[ "cycling", 1029893, 1 ]	[ 7751484678, 42784896, 1432 ]       ==> 3
      //      route:1029893	[ "cycling", 1029893, 2 ]	[ 42784896, 7741672259, 1452 ]
      //      route:1029893	[ "cycling", 1029893, 3 ]	[ 7741672259, 7751484678, 54 ]       ==> 2

      // val collection = GeoJson.featureCollection(paths.map(trackPathToGeoJson))

      // TODO convert graphPath to planRoutes using code split off from LegBuilderImpl
      // TODO cover planRoutes to geojson --> view in browser tool to see if correct route is choosen

      // TODO further investigate: route2startTentablePath1 does not seem to be one-way, but it looks like it should be ???
    }
  }
}
