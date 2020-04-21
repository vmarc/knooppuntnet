package kpn.server.analyzer.engine.elevation

import kpn.api.common.LatLonImpl
import kpn.server.analyzer.engine.analysis.common.Converter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ElevationRepositoryTest extends AnyFunSuite with Matchers {

  private val repo = new ElevationRepositoryImpl()

  test("Essen") {
    val essen = LatLonImpl.from(51.46774, 4.46839)
    repo.elevation(Converter.latLonToPoint(essen)) should equal(Some(13))
  }

  test("Paris") {
    val paris = LatLonImpl.from(48.8568537, 2.3411688)
    repo.elevation(Converter.latLonToPoint(paris)) should equal(Some(39))
  }

  test("Vienna") {
    val vienna = LatLonImpl.from(48.12, 16.22)
    repo.elevation(Converter.latLonToPoint(vienna)) should equal(Some(378))
  }

  test("New York") {
    val newYork = LatLonImpl.from(40.7128, 74.0060)
    repo.elevation(Converter.latLonToPoint(newYork)) should equal(None)
  }
}
