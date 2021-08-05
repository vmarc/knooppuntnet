package kpn.server.analyzer.load

import kpn.api.common.data.Member
import kpn.api.common.data.NodeMember
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.load.data.LoadedRoute
import org.scalamock.scalatest.MockFactory

import scala.io.Source

class RouteLoaderTest extends UnitTest with MockFactory {

  test("loadRoutes - fetch multiple routes with a single OverpassAPI request") {

    val routeIds = Seq(3148634L, 3148630L, 3144115L)
    val queryResult = readCaseStudy("load-routes.xml")

    val executor = stub[OverpassQueryExecutor]
    val countryAnalyzer = stub[CountryAnalyzer]

    (executor.executeQuery _).when(*, *).returns(queryResult)

    val routeLoader = new OldRouteLoaderImpl(executor)
    //    val loadedRoutes = routeLoader.loadRoutes(Timestamp(2020, 8, 11), routeIds)
    //
    //    assertRoute(loadedRoutes.head, 3148634L, "01-57", "01", "57")
    //    assertRoute(loadedRoutes(1), 3148630L, "01-58", "01", "58")
    //    assertRoute(loadedRoutes(2), 3144115L, "01-68", "01", "68")
  }

  private def assertRoute(loadedRoute: LoadedRoute, id: Long, name: String, startNodeName: String, endNodeName: String): Unit = {
    loadedRoute.id should equal(id)
    nodeName(loadedRoute.relation.members.head) should equal(Some(startNodeName))
    nodeName(loadedRoute.relation.members.last) should equal(Some(endNodeName))
  }

  private def readCaseStudy(name: String): String = {
    Source.fromInputStream(getClass.getResourceAsStream("/case-studies/" + name)).mkString
  }

  private def nodeName(member: Member): Option[String] = {
    member match {
      case nodeMember: NodeMember => nodeMember.node.tags("rwn_ref")
      case _ => None
    }
  }
}
