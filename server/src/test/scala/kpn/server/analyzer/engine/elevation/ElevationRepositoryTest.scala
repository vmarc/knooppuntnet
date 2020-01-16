package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationRepositoryTest extends FunSuite with Matchers {

  test("get elevation for given coordinate") {

    val essen = LatLonD(51.46774, 4.46839)
    val paris = LatLonD(48.8568537, 2.3411688)
    val vienna = LatLonD(48.12, 16.22)
    val newYork = LatLonD(40.7128, 74.0060)

    val repo = new ElevationRepositoryImpl()

    repo.elevation(essen) should equal(Some(13))
    repo.elevation(paris) should equal(Some(39))
    repo.elevation(vienna) should equal(Some(378))
    repo.elevation(newYork) should equal(None)
  }
}
