package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteLocatorTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

  test("location found by node based locator") {

    val nodeBasedLocator = stub[RouteNodeBasedLocator]
    val wayBasedLocator = stub[RouteWayBasedLocator]
    (nodeBasedLocator.locate _).when(*).returns(Some(Location(Seq("one"))))

    val route = newRoute()
    val locator = new RouteLocatorImpl(nodeBasedLocator, wayBasedLocator)

    locator.locate(route) should equal(Some(RouteLocationAnalysis(Location(Seq("one")))))
  }

  test("location found by way based locator") {

    val nodeBasedLocator = stub[RouteNodeBasedLocator]
    val wayBasedLocator = stub[RouteWayBasedLocator]
    (nodeBasedLocator.locate _).when(*).returns(None)
    (wayBasedLocator.locate _).when(*).returns(Some(RouteLocationAnalysis(Location(Seq("one")))))

    val route = newRoute()
    val locator = new RouteLocatorImpl(nodeBasedLocator, wayBasedLocator)

    locator.locate(route) should equal(Some(RouteLocationAnalysis(Location(Seq("one")))))
  }

  test("location not found by either locator") {

    val nodeBasedLocator = stub[RouteNodeBasedLocator]
    val wayBasedLocator = stub[RouteWayBasedLocator]
    (nodeBasedLocator.locate _).when(*).returns(None)
    (wayBasedLocator.locate _).when(*).returns(None)

    val route = newRoute()
    val locator = new RouteLocatorImpl(nodeBasedLocator, wayBasedLocator)

    locator.locate(route) should equal(None)
  }

}
