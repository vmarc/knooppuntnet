package kpn.core.report

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class BoundaryBuilderTest extends AnyFunSuite with Matchers {

  test("build") {

    val nodes = Seq(
      LatLon(1,1), // on boundary
      LatLon(1,4), // on boundary
      LatLon(2,2), // inside boundary
      LatLon(2,3), // inside boundary
      LatLon(3,2), // inside boundary
      LatLon(3,3), // inside boundary
      LatLon(4,1), // on boundary
      LatLon(4,4)  // on boundary
    )

    val expected = Seq( // boundary points in counter-clockwise order
      LatLon(1,1),
      LatLon(1,4),
      LatLon(4,4),
      LatLon(4,1)
    )

    new BoundaryBuilder().boundary(nodes) should equal(expected)
  }
}
