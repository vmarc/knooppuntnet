package kpn.core.learning

import org.scalatest.FunSuite
import org.scalatest.Matchers
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.edge.WUnDiEdge
import scalax.collection.edge.Implicits._
import scalax.collection.edge.LDiEdge
import scalax.collection.edge.WkHyperEdge


class GraphTest extends FunSuite with Matchers {

  test("what is shortest path when there are to paths with same length") {

//    val edge1 = 1~2 // % 5
//
//    val edge: WUnDiEdge[Int] = 1~2 % 5
//
//
//    //val edge2 = WUnDiEdge(1,2,5)
//
//    val routeMeters = 5
//    val routeId = "100"
//    val nodeId1 = "1"
//    val nodeId2 = "2"
//
//    //val routeEdge = (nodeId1 ~ nodeId2 % routeMeters)(routeId)
//
//    //val routeEdge = nodeId1 ~ nodeId2 % routeMeters # routeId
//
//    val routeEdge = WkHyperEdge(nodeId1, nodeId2)(routeMeters) //# routeId

//    val nodes = List(5)
//    val edges = List(3~1 % 5)
//    val g3 = Graph.from(nodes, edges)


//      ______
//   1 /   2  \ 4
//     \___3__/
//
    val g = Graph(
      1~2 % 1,
      1~3 % 2,
      2~>4 % 1,
      3~4 % 1
    )

    val p = g.get(1).shortestPathTo(g.get(4)).get

//    println("path=" + p.nodes)
//    println("edges=" + p.edges)



//      p.nodes should equal(List(3, 4, 5, 1))
//      p.edges should equal(List(3~4 % 1, 4~>5 % 0, 5~1 % 3))
//      p.weight should equal(4)
  }
}
