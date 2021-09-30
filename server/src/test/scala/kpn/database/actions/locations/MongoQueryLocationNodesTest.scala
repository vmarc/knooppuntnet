package kpn.database.actions.locations

import kpn.api.common.SharedTestObjects
import kpn.api.common.location.LocationNodeInfo
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.hiking
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryLocationNodesTest extends UnitTest with SharedTestObjects {

  test("nodes") {
    withDatabase { database =>

      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "01")
          )
        )
      )

      database.nodes.save(
        newNodeDoc(
          1002L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          )
        )
      )

      val query = new MongoQueryLocationNodes(database)
      query.countDocuments(hiking, "be", LocationNodesType.all) should equal(2)
      query.find(hiking, "be", LocationNodesType.all, 0, 5) should matchTo(
        Seq(
          LocationNodeInfo(
            1001L,
            "01",
            "-",
            "0",
            "0",
            defaultTimestamp,
            None,
            0,
            "-",
            Seq.empty
          ),
          LocationNodeInfo(
            1002L,
            "02",
            "-",
            "0",
            "0",
            defaultTimestamp,
            None,
            0,
            "-",
            Seq.empty
          )
        )
      )
    }
  }

  test("include active nodes only") {
    withDatabase { database =>

      // active
      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "01")
          )
        )
      )

      // not active
      database.nodes.save(
        newNodeDoc(
          1002L,
          active = false,
          labels = Seq(
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          )
        )
      )

      val query = new MongoQueryLocationNodes(database)
      query.countDocuments(hiking, "be", LocationNodesType.all) should equal(1)
      val locationNodeInfos = query.find(hiking, "be", LocationNodesType.all, 0, 5)
      locationNodeInfos.map(_.id) should equal(Seq(1001L))
    }
  }

  test("include nodes with lastSurvey values only") {
    withDatabase { database =>

      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.active,
            Label.survey,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "01")
          ),
          lastSurvey = Some(Day(2020, 8, None))
        )
      )

      database.nodes.save(
        newNodeDoc(
          1002L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          )
        )
      )

      val query = new MongoQueryLocationNodes(database)
      query.countDocuments(hiking, "be", LocationNodesType.survey) should equal(1)
      val locationNodeInfos = query.find(hiking, "be", LocationNodesType.survey, 0, 5)
      locationNodeInfos should matchTo(
        Seq(
          LocationNodeInfo(
            1001L,
            "01",
            "-",
            "0",
            "0",
            defaultTimestamp,
            Some(Day(2020, 8, None)),
            0,
            "-",
            Seq.empty
          )
        )
      )
    }
  }

  test("include values with given location only") {
    withDatabase { database =>

      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "01")
          )
        )
      )

      database.nodes.save(
        newNodeDoc(
          1002L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.nl.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          )
        )
      )

      val query = new MongoQueryLocationNodes(database)
      query.countDocuments(hiking, "be", LocationNodesType.all) should equal(1)
      val locationNodeInfos = query.find(hiking, "be", LocationNodesType.all, 0, 5)
      locationNodeInfos.map(_.id) should equal(Seq(1001L))
    }
  }

  test("include values with given network type only") {
    withDatabase { database =>

      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "01")
          )
        )
      )

      database.nodes.save(
        newNodeDoc(
          1002L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.cycling),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          )
        )
      )

      val query = new MongoQueryLocationNodes(database)
      query.countDocuments(hiking, "be", LocationNodesType.all) should equal(1)
      val locationNodeInfos = query.find(hiking, "be", LocationNodesType.all, 0, 5)
      locationNodeInfos.map(_.id) should equal(Seq(1001L))
    }
  }

  test("only include nodes with facts") {
    withDatabase { database =>

      database.nodes.save(
        newNodeDoc(
          1001L,
          labels = Seq(
            Label.active,
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "01")
          )
        )
      )

      database.nodes.save(
        newNodeDoc(
          1007L,
          labels = Seq(
            Label.active,
            Label.facts,
            Label.fact(Fact.NodeInvalidSurveyDate),
            Label.networkType(NetworkType.hiking),
            Label.location(Country.be.domain)
          ),
          names = Seq(
            newNodeName(name = "02")
          ),
          facts = Seq(Fact.NodeInvalidSurveyDate)
        )
      )

      val query = new MongoQueryLocationNodes(database)
      query.countDocuments(hiking, "be", LocationNodesType.facts) should equal(1)
      query.find(hiking, "be", LocationNodesType.facts, 0, 5) should matchTo(
        Seq(
          LocationNodeInfo(
            1007L,
            "02",
            "-",
            "0",
            "0",
            defaultTimestamp,
            None,
            1,
            "-",
            Seq.empty
          )
        )
      )
    }
  }

  test("TODO route references") {
    pending
  }

  test("paging") {
    withDatabase { database =>

      def buildNode(nodeId: Long, name: String): Unit = {
        database.nodes.save(
          newNodeDoc(
            nodeId,
            labels = Seq(
              Label.active,
              Label.networkType(NetworkType.hiking),
              Label.location(Country.be.domain)
            ),
            names = Seq(
              newNodeName(name = name)
            )
          )
        )
      }

      buildNode(1001L, "01")
      buildNode(1002L, "02")
      buildNode(1003L, "03")
      buildNode(1004L, "04")
      buildNode(1005L, "05")
      buildNode(1006L, "06")
      buildNode(1007L, "07")
      buildNode(1008L, "08")

      val query = new MongoQueryLocationNodes(database)

      def find(page: Int, pageSize: Int): Seq[Long] = {
        query.find(
          hiking,
          "be",
          LocationNodesType.all,
          page,
          pageSize
        ).map(_.id)
      }

      query.countDocuments(hiking, "be", LocationNodesType.all) should equal(8)

      find(0, 3) should equal(Seq(1001L, 1002L, 1003L))
      find(1, 3) should equal(Seq(1004L, 1005L, 1006L))
      find(2, 3) should equal(Seq(1007L, 1008L))
      find(3, 3) should equal(Seq.empty)

      find(0, 5) should equal(Seq(1001L, 1002L, 1003L, 1004L, 1005L))
      find(1, 5) should equal(Seq(1006L, 1007L, 1008L))
      find(2, 5) should equal(Seq.empty)
    }
  }
}
