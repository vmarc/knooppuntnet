package kpn.server.analyzer.engine.elevation

import kpn.server.analyzer.engine.tiles.domain.Point
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElevationRepositoryTest extends FunSuite with Matchers {

  private val repo = new ElevationRepositoryImpl()

  test("Essen") {
    val essen = Point(51.46774, 4.46839)
    repo.elevation(essen) should equal(Some(13))
  }

  test("Paris") {
    val paris = Point(48.8568537, 2.3411688)
    repo.elevation(paris) should equal(Some(39))
  }

  test("Vienna") {
    val vienna = Point(48.12, 16.22)
    repo.elevation(vienna) should equal(Some(378))
  }

  test("New York") {
    val newYork = Point(40.7128, 74.0060)
    repo.elevation(newYork) should equal(None)
  }
}
