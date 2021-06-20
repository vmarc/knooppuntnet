package kpn.core.mongo.actions.locations

import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationNodeInfo
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkType.hiking
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryLocationNodesTest extends UnitTest with SharedTestObjects {

  test("nodes") {
    withDatabase { database =>

      database.nodes.save(
        newNodeInfo(
          1001L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "01")
        )
      )

      database.nodes.save(
        newNodeInfo(
          1002L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "02"),
        )
      )

      val locationNodeInfos = new MongoQueryLocationNodes(database).execute(
        hiking,
        "be",
        LocationNodesType.all,
        0,
        5
      )

      locationNodeInfos should matchTo(
        Seq(
          LocationNodeInfo(
            1001L,
            "01",
            "01",
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
            "02",
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
        newNodeInfo(
          1001L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "01")
        )
      )

      // not active
      database.nodes.save(
        newNodeInfo(
          1002L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "02"),
          active = false
        )
      )

      val locationNodeInfos = new MongoQueryLocationNodes(database).execute(
        hiking,
        "be",
        LocationNodesType.all,
        0,
        5
      )

      locationNodeInfos.map(_.id) should equal(Seq(1001L))
    }
  }

  test("include nodes with lastSurvey values only") {
    withDatabase { database =>

      database.nodes.save(
        newNodeInfo(
          1001L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "01"),
          lastSurvey = Some(Day(2020, 8, None))
        )
      )

      database.nodes.save(
        newNodeInfo(
          1002L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "02"),
        )
      )

      val locationNodeInfos = new MongoQueryLocationNodes(database).execute(
        hiking,
        "be",
        LocationNodesType.survey,
        0,
        5
      )

      locationNodeInfos should matchTo(
        Seq(
          LocationNodeInfo(
            1001L,
            "01",
            "01",
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
        newNodeInfo(
          1001L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "01")
        )
      )

      database.nodes.save(
        newNodeInfo(
          1002L,
          location = Some(Location(Seq("nl"))),
          tags = Tags.from("rwn_ref" -> "02"),
        )
      )

      val locationNodeInfos = new MongoQueryLocationNodes(database).execute(
        hiking,
        "be",
        LocationNodesType.all,
        0,
        5
      )

      locationNodeInfos.map(_.id) should equal(Seq(1001L))
    }
  }

  test("include values with given network type only") {
    withDatabase { database =>

      database.nodes.save(
        newNodeInfo(
          1001L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "01")
        )
      )

      database.nodes.save(
        newNodeInfo(
          1002L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rcn_ref" -> "02"),
        )
      )

      val locationNodeInfos = new MongoQueryLocationNodes(database).execute(
        hiking,
        "be",
        LocationNodesType.all,
        0,
        5
      )

      locationNodeInfos.map(_.id) should equal(Seq(1001L))
    }
  }

  test("only include nodes with facts") {
    withDatabase { database =>

      database.nodes.save(
        newNodeInfo(
          1001L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "01")
        )
      )

      database.nodes.save(
        newNodeInfo(
          1007L,
          location = Some(Location(Seq("be"))),
          tags = Tags.from("rwn_ref" -> "07"),
          facts = Seq(Fact.NodeInvalidSurveyDate)
        )
      )

      val locationNodeInfos = new MongoQueryLocationNodes(database).execute(
        hiking,
        "be",
        LocationNodesType.facts,
        0,
        5
      )

      locationNodeInfos should matchTo(
        Seq(
          LocationNodeInfo(
            1007L,
            "07",
            "07",
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
  }

  test("paging") {
    withDatabase { database =>

      def buildNode(nodeId: Long, name: String): Unit = {
        database.nodes.save(
          newNodeInfo(
            nodeId,
            location = Some(Location(Seq("be"))),
            tags = Tags.from("rwn_ref" -> name)
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

      def query(page: Int, pageSize: Int): Seq[Long] = {
        new MongoQueryLocationNodes(database).execute(
          hiking,
          "be",
          LocationNodesType.all,
          page,
          pageSize
        ).map(_.id)
      }

      query(0, 3) should equal(Seq(1001L, 1002L, 1003L))
      query(1, 3) should equal(Seq(1004L, 1005L, 1006L))
      query(2, 3) should equal(Seq(1007L, 1008L))
      query(3, 3) should equal(Seq.empty)

      query(0, 5) should equal(Seq(1001L, 1002L, 1003L, 1004L, 1005L))
      query(1, 5) should equal(Seq(1006L, 1007L, 1008L))
      query(2, 5) should equal(Seq.empty)
    }
  }
}

