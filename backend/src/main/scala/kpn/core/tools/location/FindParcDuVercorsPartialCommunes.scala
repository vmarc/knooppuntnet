package kpn.core.tools.location

import kpn.core.tools.config.Dirs
import kpn.server.analyzer.engine.analysis.location.ParcDuVercors
import org.apache.commons.io.FileUtils
import org.locationtech.jts.io.geojson.GeoJsonReader

import java.io.File

object FindParcDuVercorsPartialCommunes {
  def main(args: Array[String]): Unit = {
    val vercorsBoundary = ParcDuVercors.boundaryGeometry
    ParcDuVercors.communes.foreach { commune =>
      val file = new File(s"${Dirs.root}/locations/fr/geometries/$commune.json")
      val geoJson = FileUtils.readFileToString(file, "UTF-8")
      val geometry = new GeoJsonReader().read(geoJson)
      val intersection = vercorsBoundary.intersection(geometry)
      val areaOutsideParcDuVercors = Math.abs(geometry.getArea - intersection.getArea)
      if (areaOutsideParcDuVercors > 0.0001) {
        println(s"$commune")
      }
    }
  }
}
