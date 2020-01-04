package kpn.core.tools.country

import kpn.api.custom.Country
import org.locationtech.jts.algorithm.locate.IndexedPointInAreaLocator
import org.locationtech.jts.geom._
import org.locationtech.jts.geom.impl.CoordinateArraySequence

// https://wiki.openstreetmap.org/wiki/Relation:multipolygon
// https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm

class PolygonBuilder(country: Country, data: SkeletonData) {

  private val factory: GeometryFactory = new GeometryFactory()

  def polygons(): Seq[Polygon] = {
    val countryRelation = data.relations(data.countryRelationId)
    val outers = findRingsWithRole(countryRelation, "outer").map(toRing)
    val inners = findRingsWithRole(countryRelation, "inner").map(toRing)
    log(outers, "outer")
    log(inners, "inner")
    val polygons = outers.map { outer =>
      val locator = new IndexedPointInAreaLocator(new Polygon(outer, Array[LinearRing](), factory))
      val holes = inners.filter { inner =>
        inner.getCoordinates.exists { c =>
          locator.locate(c) == Location.INTERIOR
        }
      }
      new Polygon(outer, holes.toArray, factory)
    }
    polygons
  }

  private def findRingsWithRole(relation: SkeletonRelation, role: String): Seq[Ring] = {
    val members = relation.members.filter(_.role.contains(role))
    val ways = members.map(_.ref).map(data.ways)
    RingBuilder.findRings(ways)
  }

  private def toRing(ring: Ring): LinearRing = {
    val coordinates = (Seq(ring.ways.head.nodeIds.head) ++ ring.ways.flatMap(_.nodeIds.tail)).map(data.nodes).map(toCoordinate)
    new LinearRing(new CoordinateArraySequence(coordinates.toArray), factory)
  }

  private def toCoordinate(node: SkeletonNode): Coordinate = {
    new Coordinate(node.latitude, node.longitude)
  }

  private def log(rings: Seq[LinearRing], role: String): Unit = {
    rings.zipWithIndex.foreach { case(ring, index) =>
      val id = "%02d".format(index + 1)
      val filename = s"/kpn/country/debug/${country.domain}-$role-$id.html"
      new RingWriter().write(filename, ring)
    }
  }
}
