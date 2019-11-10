package kpn.core.tools.country

import java.io.FileWriter

import kpn.api.custom.Country
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.WKTWriter

object CountryBoundaryTool {
  def main(args: Array[String]): Unit = {
    Country.all.foreach { country =>
      println(s"Collecting boundary information for ${country.domain}")
      val loader = new CountryBoundaryLoader(country)
      val id = loader.countryId(country)
      val data = loader.countryData(country, id)
      log(data)
      val polygons = new PolygonBuilder(country, data).polygons(id)
      log(polygons)
      val writer = new WKTWriter()
      polygons.zipWithIndex.foreach { case (polygon, index) =>
        val suffix = "%02d".format(index + 1)
        val filename = s"/kpn/country/${country.domain}-$suffix.poly"
        val w = new FileWriter(filename)
        try {
          writer.writeFormatted(polygon, w)
        } finally {
          w.close()
        }
      }
    }
  }

  private def log(data: SkeletonData): Unit = {
    println(s"  nodes: ${data.nodes.size}")
    println(s"  ways: ${data.ways.size}")
    println(s"  relations: ${data.relations.size}")
  }

  private def log(polygons: Seq[Polygon]): Unit = {
    println(s"  polygons: ${polygons.size}")
    polygons.foreach { polygon =>
      val nodeCount = polygon.getExteriorRing.getCoordinates.length
      val holeCount = polygon.getNumInteriorRing
      val holeInfo = holeCount match {
        case 0 => "(no holes)"
        case 1 => "with one hole"
        case _ => s"with $holeCount holes"
      }
      println(s"     polygon with $nodeCount nodes $holeInfo")
      0 until holeCount foreach { index =>
        val holeNodeCount = polygon.getInteriorRingN(index).getCoordinates.length
        println(s"       hole with $holeNodeCount nodes")
      }
    }
  }
}
