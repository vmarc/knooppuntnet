package kpn.server.analyzer.engine.elevation

import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationFacadeTest extends FunSuite with Matchers {

  test("route elevation profile") {
    // load route 10460202 50-53 Austria
    // See: https://cycling.waymarkedtrails.org/#route?id=10460202&map=15!47.2252!14.777
    // length 6.01 km
    // expected accumulated ascent: 85 m
    // expected accumulated descent: 100 m
  }

}
