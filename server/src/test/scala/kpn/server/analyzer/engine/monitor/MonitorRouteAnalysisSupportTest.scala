package kpn.server.analyzer.engine.monitor

import kpn.core.util.UnitTest
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

class MonitorRouteAnalysisSupportTest extends UnitTest {

  test("first densify, then simplify again") {

    val coordinate1 = new Coordinate(51.4633666, 4.4553911)
    val coordinate2 = new Coordinate(51.4618272, 4.4562458)
    val coordinate3 = new Coordinate(51.4614496, 4.4550560)

    val geomFactory = new GeometryFactory
    val originalLineString = geomFactory.createLineString(Array(coordinate1, coordinate2, coordinate3))

    val coordinates = MonitorRouteAnalysisSupport.toSampleCoordinates(10, originalLineString)

    coordinates.size should equal(35)

    val sequence = ReferenceCoordinateSequence(0.until(35))
    val newLineString = MonitorRouteAnalysisSupport.toLineString(coordinates, sequence)

    newLineString.getCoordinates should equal(Array(coordinate1, coordinate2, coordinate3))
  }
}
