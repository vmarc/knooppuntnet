package kpn.server.analyzer.engine.monitor.analysis

import kpn.core.util.UnitTest
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

class LineSimplifierTest extends UnitTest {

  test("simplify coordinates") {

    val (simpleCoordinates, detailedCoordinates) = setup()

    val result = LineSimplifier.simplify(detailedCoordinates)

    assertEqual(result, simpleCoordinates)
  }

  private def setup(): (List[Coordinate], List[Coordinate]) = {
    val coordinate1 = new Coordinate(51.4633666, 4.4553911)
    val coordinate2 = new Coordinate(51.4618272, 4.4562458)
    val coordinate3 = new Coordinate(51.4614496, 4.4550560)

    val simpleCoordinates = Array(coordinate1, coordinate2, coordinate3)

    val geometryFactory = new GeometryFactory
    val originalLineString = geometryFactory.createLineString(simpleCoordinates)

    val detailedCoordinates = LineSampler.toSampleCoordinates(10, originalLineString)

    (simpleCoordinates.toList, detailedCoordinates.toList)
  }
}
