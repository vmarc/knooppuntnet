package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class DependencySorterTest extends UnitTest {

  test("determine dependency sort order") {

    val dependencies = Seq(
      RouteDependency(1, 11),
      RouteDependency(1, 12),
      RouteDependency(1, 13),
      RouteDependency(11, 111),
      RouteDependency(11, 112),
      RouteDependency(12, 121),
      RouteDependency(12, 122),
    )

    DependencySorter.sort(dependencies) should equal(Seq(11, 12, 1))
  }

  test("determine dependency sort order - 3 level hierarch") {

    val dependencies = Seq(
      RouteDependency(1, 11),
      RouteDependency(11, 111),
      RouteDependency(111, 1111),
    )

    DependencySorter.sort(dependencies) should equal(Seq(111, 11, 1))
  }

  test("no dependencies") {
    DependencySorter.sort(Seq.empty) should equal(Seq.empty)
  }
}
