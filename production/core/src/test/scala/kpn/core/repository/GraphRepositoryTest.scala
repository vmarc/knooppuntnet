package kpn.core.repository

import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.core.db.TestDocBuilder
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.GraphEdges
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Subset
import kpn.shared.NetworkType

class GraphRepositoryTest extends FunSuite with Matchers {

  ignore("graph edges") { // implementation in GraphRepositoryImpl has been commented out

    withDatabase { database =>

      val b = new TestDocBuilder(database)
      b.route(Subset.nlBicycle, 1, 2, 112)
      b.route(Subset.nlBicycle, 3, 4, 134)
      b.route(Subset.beHiking, 5, 6, 156)
      b.route(Subset.beHiking, 7, 8, 178)

      val repo: GraphRepository = new GraphRepositoryImpl(database)

      val rcnEdges = repo.edges(NetworkType.bicycle)

      rcnEdges.size should equal(2)

      rcnEdges(0).label should equal("12")
      rcnEdges(0).weight should equal(112)
      rcnEdges(0)._1 should equal("1")
      rcnEdges(0)._2 should equal("2")

      rcnEdges(1).label should equal("34")
      rcnEdges(1).weight should equal(134)
      rcnEdges(1)._1 should equal("3")
      rcnEdges(1)._2 should equal("4")

      val rwnEdges = repo.edges(NetworkType.hiking)

      rwnEdges.size should equal(2)

      rwnEdges(0).label should equal("56")
      rwnEdges(0).weight should equal(156)
      rwnEdges(0)._1 should equal("5")
      rwnEdges(0)._2 should equal("6")

      rwnEdges(1).label should equal("78")
      rwnEdges(1).weight should equal(178)
      rwnEdges(1)._1 should equal("7")
      rwnEdges(1)._2 should equal("8")

    }
  }
}
