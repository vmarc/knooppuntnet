package kpn.core.tools.country

import kpn.api.custom.Country
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import kpn.api.common.data.raw.RawMember
import org.locationtech.jts.geom.Coordinate

class PolygonBuilderTest extends AnyFunSuite with Matchers {

  test("build polygon with 1 outer ring and 1 inner ring") {

    // outer ring

    val a = SkeletonNode(101, 0, 0)
    val b = SkeletonNode(102, 10, 0)
    val c = SkeletonNode(103, 10, 10)
    val d = SkeletonNode(104, 0, 10)

    val ab = SkeletonWay(10, Seq(a.id, b.id))
    val bc = SkeletonWay(11, Seq(b.id, c.id))
    val cd = SkeletonWay(12, Seq(c.id, d.id))
    val da = SkeletonWay(13, Seq(d.id, a.id))

    // inner ring

    val e = SkeletonNode(105, 4, 4)
    val f = SkeletonNode(106, 6, 4)
    val g = SkeletonNode(107, 6, 6)
    val h = SkeletonNode(108, 4, 6)

    val ef = SkeletonWay(14, Seq(e.id, f.id))
    val fg = SkeletonWay(15, Seq(f.id, g.id))
    val gh = SkeletonWay(16, Seq(g.id, h.id))
    val he = SkeletonWay(17, Seq(h.id, e.id))

    val r = SkeletonRelation(
      1,
      Seq(
        RawMember("way", ab.id, Some("outer")),
        RawMember("way", bc.id, Some("outer")),
        RawMember("way", cd.id, Some("outer")),
        RawMember("way", da.id, Some("outer")),
        RawMember("way", ef.id, Some("inner")),
        RawMember("way", fg.id, Some("inner")),
        RawMember("way", gh.id, Some("inner")),
        RawMember("way", he.id, Some("inner"))
      )
    )

    val nodes = Seq(a, b, c, d, e, f, g, h).map(n => n.id -> n).toMap
    val ways = Seq(ab, bc, cd, da, ef, fg, gh, he).map(w => w.id -> w).toMap
    val relations = Seq(r).map(r => r.id -> r).toMap
    val data: SkeletonData = SkeletonData(r.id, nodes, ways, relations)

    val polygons = new PolygonBuilder(Country.be, data: SkeletonData).polygons()

    polygons.size should equal(1)
    val polygon = polygons.head
    val outer = polygon.getExteriorRing.getCoordinates.map(toInts)
    outer should equal(Seq((0, 0), (10, 0), (10, 10), (0, 10), (0, 0)))

    polygon.getNumInteriorRing should equal(1)
    val inner = polygon.getInteriorRingN(0).getCoordinates.map(toInts)
    inner should equal(Seq((4, 4), (6, 4), (6, 6), (4, 6), (4, 4)))
  }

  private def toInts(coordinate: Coordinate): (Int, Int) = (coordinate.x.toInt, coordinate.y.toInt)
}
