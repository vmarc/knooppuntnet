package kpn.core.db.views

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.core.db.TestDocBuilder
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Subset

class GraphEdgesTest extends FunSuite with Matchers {

  ignore("graph edges") {

    withDatabase { database =>

      val b = new TestDocBuilder(database)
      b.route(Subset.nlBicycle, 1, 2, 112)
      b.route(Subset.nlBicycle, 3, 4, 134)
      b.route(Subset.beHiking, 5, 6, 156)
      b.route(Subset.beHiking, 7, 8, 178)

      fail("re-implement")

//      val view = design.view(GraphEdges.name)
//      val result = view.query[GraphEdges.Key, GraphEdges.Value, Nothing]()
//
//      result.rows.map(row => (row.key, row.value)) should equal(
//        Seq(
//          (GraphEdges.Key("rcn", 2, 12), GraphEdges.Value(1, 2, 112)),
//          (GraphEdges.Key("rcn", 4, 34), GraphEdges.Value(3, 4, 134)),
//          (GraphEdges.Key("rwn", 6, 56), GraphEdges.Value(5, 6, 156)),
//          (GraphEdges.Key("rwn", 8, 78), GraphEdges.Value(7, 8, 178))
//        )
//      )
    }
  }

  ignore("view handles long route and node id's ok") {

    withDatabase { database =>

      val javascriptNumberMaxValue = math.pow(2, 52).toLong

      val routeId = javascriptNumberMaxValue - 1
      val startNodeId = javascriptNumberMaxValue - 2
      val endNodeId = javascriptNumberMaxValue - 3

      val b = new TestDocBuilder(database)
      b.route(Subset.nlBicycle, "01", "02", routeId, startNodeId, endNodeId, 123)

      fail("re-implement")
//      val view = design.view(GraphEdges.name)
//      val result = view.query[GraphEdges.Key, GraphEdges.Value, Nothing]()
//
//      val prefix = routeId.toString.takeRight(1).toInt
//
//      result.rows.map(row => (row.key, row.value)) should equal(
//        Seq(
//          (GraphEdges.Key("rcn", prefix, routeId), GraphEdges.Value(startNodeId, endNodeId, 123))
//        )
//      )
    }
  }
}
