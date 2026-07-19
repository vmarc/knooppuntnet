package kpn.server.analyzer.engine.context

import it.unimi.dsi.fastutil.longs.LongSet
import org.scalatest.funsuite.AnyFunSuite

import scala.jdk.CollectionConverters.ListHasAsScala

class WatchedRoutesTest extends AnyFunSuite {

  test("empty watched routes") {
    val watchedRoutes = new WatchedRoutes()
    assert(watchedRoutes.isEmpty)
    assert(watchedRoutes.size == 0)
  }

  test("add and get route") {
    val watchedRoutes = new WatchedRoutes()
    val elementIds = ElementIds()
    watchedRoutes.add(1, elementIds)
    assert(watchedRoutes.get(1).contains(elementIds))
  }

  test("delete route") {
    val watchedRoutes = new WatchedRoutes()
    val elementIds = ElementIds()
    watchedRoutes.add(1, elementIds)
    watchedRoutes.delete(1)
    assert(watchedRoutes.get(1).isEmpty)
  }

  test("delete non-existing route") {
    val watchedRoutes = new WatchedRoutes()
    val elementIds = ElementIds()
    watchedRoutes.delete(1)
    assert(watchedRoutes.get(1).isEmpty)
  }

  test("route references") {
    val watchedRoutes = new WatchedRoutes()
    val elementIds = ElementIds(
      nodeIds = LongSet.of(1, 2),
      wayIds = LongSet.of(3, 4),
      relationIds = LongSet.of(5, 6)
    )
    watchedRoutes.add(1, elementIds)

    assert(watchedRoutes.routesReferencingNode(1).map(_.asScala.toSeq).contains(Seq(1L)))
    assert(watchedRoutes.routesReferencingNode(2).map(_.asScala.toSeq).contains(Seq(1L)))
    assert(watchedRoutes.routesReferencingNode(3).isEmpty)

    assert(watchedRoutes.routesReferencingWay(3).map(_.asScala.toSeq).contains(Seq(1L)))
    assert(watchedRoutes.routesReferencingWay(4).map(_.asScala.toSeq).contains(Seq(1L)))
    assert(watchedRoutes.routesReferencingWay(5).isEmpty)

    assert(watchedRoutes.routesReferencingRelation(5).map(_.asScala.toSeq).contains(Seq(1L)))
    assert(watchedRoutes.routesReferencingRelation(6).map(_.asScala.toSeq).contains(Seq(1L)))
    assert(watchedRoutes.routesReferencingRelation(7).isEmpty)

    watchedRoutes.delete(1L)

    assert(watchedRoutes.routesReferencingNode(1).isEmpty)
    assert(watchedRoutes.routesReferencingNode(2).isEmpty)
    assert(watchedRoutes.routesReferencingWay(3).isEmpty)
    assert(watchedRoutes.routesReferencingWay(4).isEmpty)
    assert(watchedRoutes.routesReferencingRelation(5).isEmpty)
    assert(watchedRoutes.routesReferencingRelation(6).isEmpty)
  }

  test("route references update") {
    val watchedRoutes = new WatchedRoutes()
    val elementIds1 = ElementIds(
      nodeIds = LongSet.of(1001, 1002),
      wayIds = LongSet.of(101, 102),
      relationIds = LongSet.of(11, 12)
    )
    val elementIds2 = ElementIds(
      nodeIds = LongSet.of(1002, 1003),
      wayIds = LongSet.of(102, 103),
      relationIds = LongSet.of(12, 13)
    )
    watchedRoutes.add(1, elementIds1)

    assert(watchedRoutes.routesReferencingNode(1001).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingNode(1002).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingNode(1003).isEmpty)

    assert(watchedRoutes.routesReferencingWay(101).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingWay(102).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingWay(103).isEmpty)

    assert(watchedRoutes.routesReferencingRelation(11).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingRelation(12).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingRelation(13).isEmpty)

    watchedRoutes.add(1, elementIds2)

    assert(watchedRoutes.routesReferencingNode(1001).isEmpty)
    assert(watchedRoutes.routesReferencingNode(1002).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingNode(1003).map(_.asScala.toSeq).contains(Seq(1)))

    assert(watchedRoutes.routesReferencingWay(101).isEmpty)
    assert(watchedRoutes.routesReferencingWay(102).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingWay(103).map(_.asScala.toSeq).contains(Seq(1)))

    assert(watchedRoutes.routesReferencingRelation(11).isEmpty)
    assert(watchedRoutes.routesReferencingRelation(12).map(_.asScala.toSeq).contains(Seq(1)))
    assert(watchedRoutes.routesReferencingRelation(13).map(_.asScala.toSeq).contains(Seq(1)))

    watchedRoutes.delete(1)

    assert(watchedRoutes.routesReferencingNode(1001).isEmpty)
    assert(watchedRoutes.routesReferencingNode(1002).isEmpty)
    assert(watchedRoutes.routesReferencingNode(1003).isEmpty)

    assert(watchedRoutes.routesReferencingWay(101).isEmpty)
    assert(watchedRoutes.routesReferencingWay(102).isEmpty)
    assert(watchedRoutes.routesReferencingWay(103).isEmpty)

    assert(watchedRoutes.routesReferencingRelation(11).isEmpty)
    assert(watchedRoutes.routesReferencingRelation(12).isEmpty)
    assert(watchedRoutes.routesReferencingRelation(13).isEmpty)
  }

  test("size and isEmpty") {
    val watchedRoutes = new WatchedRoutes()
    assert(watchedRoutes.isEmpty)
    watchedRoutes.add(1L, ElementIds())
    assert(!watchedRoutes.isEmpty)
    assert(watchedRoutes.size == 1)
  }

  test("contains") {
    val watchedRoutes = new WatchedRoutes()
    watchedRoutes.add(1L, ElementIds())
    assert(watchedRoutes.contains(1L))
    assert(!watchedRoutes.contains(2L))
  }
}
