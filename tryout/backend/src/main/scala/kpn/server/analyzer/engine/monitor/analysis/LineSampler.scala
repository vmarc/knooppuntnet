package kpn.server.analyzer.engine.monitor.analysis

import kpn.core.util.Haversine
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineString

object LineSampler {

  def toSampleCoordinates(sampleDistanceMeters: Int, lineString: LineString): Seq[Coordinate] = {
    val referenceMeters = Haversine.meters(lineString)
    val distanceBetweenSamples = sampleDistanceMeters.toDouble * lineString.getLength / referenceMeters
    val densifiedLineString = Densifier.densify(lineString, distanceBetweenSamples)
    densifiedLineString.getCoordinates.toSeq
  }
}
