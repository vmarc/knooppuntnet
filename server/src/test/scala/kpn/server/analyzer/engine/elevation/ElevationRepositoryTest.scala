package kpn.server.analyzer.engine.elevation

import kpn.api.common.LatLon
import kpn.api.common.LatLonImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationRepositoryTest extends FunSuite with Matchers {

  test("get elevation for given coordinate") {

    val essen: LatLon = LatLonImpl.from(51.46774, 4.46839)
    val paris = LatLonImpl.from(48.8568537, 2.3411688)
    val vienna = LatLonImpl.from(48.12, 16.22)
    val newYork = LatLonImpl.from(40.7128, 74.0060)

    val repo = new ElevationRepositoryImpl()

    repo.elevation(essen) should equal(Some(13.0))
    repo.elevation(paris) should equal(Some(39.0))
    repo.elevation(vienna) should equal(Some(378.0))
    repo.elevation(newYork) should equal(None)
  }
}
