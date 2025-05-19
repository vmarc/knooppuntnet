package kpn.core.doc

import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest

class RouteRelationTest extends UnitTest with SharedTestObjects {
  test("relationIds") {
    val root = newRouteRelation(
      1,
      relations = Seq(
        newRouteRelation(2),
        newRouteRelation(
          3,
          relations = Seq(
            newRouteRelation(4)
          )
        )
      )
    )

    assertEqual(
      RouteRelation.relationIds(root),
      Seq(
        1,
        2,
        3,
        4
      )
    )
  }
}
