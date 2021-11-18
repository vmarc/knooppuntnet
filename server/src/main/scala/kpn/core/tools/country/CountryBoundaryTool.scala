package kpn.core.tools.country

import java.io.FileWriter

import kpn.api.custom.Country
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.WKTWriter

object CountryBoundaryTool {

  def main(args: Array[String]): Unit = {
    Country.all.foreach { country =>
      val data = load(country)
      val polygons = new PolygonBuilder(country.domain, data).polygons()
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
    println("Done")
  }

  private def load(country: Country): SkeletonData = {
    println(s"Collecting boundary information for ${country.domain}")
    val loader = new CountryBoundaryLoader()
    val id = loader.countryId(country)
    val data = {
      val countryData = loader.countryData(country, id)
      if (country == Country.at) {
        patchAustria(countryData)
      }
      else {
        countryData
      }
    }
    log(data)
    data
  }

  /*
    Patches Austria data to resolve issue around node 2064631022 (Jungholz).  The
    RingBuilder logic choked on the four-sized border at this node. We work around this
    problem by splitting the node in two nodes.

    See: https://en.wikipedia.org/wiki/Jungholz

    Quadripoint:

      https://www.openstreetmap.org/node/2064631022

    Left border ways:

      https://www.openstreetmap.org/way/326054386
      https://www.openstreetmap.org/way/325724070

    Right border ways:

      https://www.openstreetmap.org/way/325724109
      https://www.openstreetmap.org/way/326054387

   */
  private def patchAustria(data: SkeletonData): SkeletonData = {

    val quadripointNodeId = 2064631022L
    val leftBorderWayIds = Seq(326054386L, 325724070L)
    val duplicatedQuadripointNodeId = 1L

    val duplicateNode = data.nodes(quadripointNodeId).copy(id = duplicatedQuadripointNodeId)
    val patchedNodes = data.nodes + (duplicatedQuadripointNodeId -> duplicateNode)
    val patchedWays = data.ways.map { case (id, way) =>
      val patchedWay = if (leftBorderWayIds.contains(id)) {
        way.copy(
          nodeIds = way.nodeIds.map { nodeId =>
            if (nodeId == quadripointNodeId) {
              duplicatedQuadripointNodeId
            }
            else {
              nodeId
            }
          })
      }
      else {
        way
      }
      id -> patchedWay
    }

    data.copy(nodes = patchedNodes, ways = patchedWays)
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
