package kpn.database.actions.locations

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.RouteType.hiking
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.SurveyParameter
import kpn.api.custom.Day
import kpn.core.doc.Label
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.Timestamps
import kpn.server.analyzer.engine.analysis.location.LocationSubset

class MongoQueryLocationNodesTest extends MongoTest {

  test("nodes") {
    val setup = new MongoQueryLocationNodesTestSetup(database)
    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1002L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    val subset = LocationSubset("", hiking, Seq("be"))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)
    query.countDocuments(subset, LocationNodesParameters()) should equal(2)
    assertEqual(
      query.find(subset, LocationNodesParameters()),
      Seq(
        LocationNodeInfo(
          0L,
          1001L,
          "01",
          "-",
          "0",
          "0",
          Timestamps.default,
          None,
          Seq.empty,
          None,
          Seq.empty
        ),
        LocationNodeInfo(
          1L,
          1002L,
          "02",
          "-",
          "0",
          "0",
          Timestamps.default,
          None,
          Seq.empty,
          None,
          Seq.empty
        )
      )
    )
  }

  test("include active nodes only") {
    val setup = new MongoQueryLocationNodesTestSetup(database)

    // active
    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName),
        )
      )
    )

    // not active
    database.nodes.save(
      newNodeDoc(
        1002L,
        active = false,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    val subset = LocationSubset("", hiking, Seq("be"))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)
    query.countDocuments(subset, LocationNodesParameters()) should equal(1)
    val locationNodeInfos = query.find(subset, LocationNodesParameters())
    locationNodeInfos.map(_.id) should equal(Seq(1001L))
  }

  test("include nodes with lastSurvey values only") {

    val setup = new MongoQueryLocationNodesTestSetup(database)

    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          ),
          lastSurvey = Some(Day(2020, 8))
        ),
        labels = Seq(
          Label.survey,
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1002L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    val subset = LocationSubset("", hiking, Seq("be"))
    val parameters = LocationNodesParameters(survey = Some(SurveyParameter.Older))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)
    query.countDocuments(subset, parameters) should equal(1)
    val locationNodeInfos = query.find(subset, parameters)
    assertEqual(
      locationNodeInfos,
      Seq(
        LocationNodeInfo(
          0L,
          1001L,
          "01",
          "-",
          "0",
          "0",
          Timestamps.default,
          Some(Day(2020, 8)),
          Seq.empty,
          None,
          Seq.empty
        )
      )
    )
  }

  test("include values with given location only") {
    val setup = new MongoQueryLocationNodesTestSetup(database)

    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1002L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.nl.entryName)
        )
      )
    )

    val subset = LocationSubset("", hiking, Seq("be"))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)
    query.countDocuments(subset, LocationNodesParameters()) should equal(1)
    val locationNodeInfos = query.find(subset, LocationNodesParameters())
    locationNodeInfos.map(_.id) should equal(Seq(1001L))
  }

  test("include values with given network type only") {
    val setup = new MongoQueryLocationNodesTestSetup(database)

    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1002L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.cycling),
          Label.location(Country.be.entryName)
        )
      )
    )

    val subset = LocationSubset("", hiking, Seq("be"))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)
    query.countDocuments(subset, LocationNodesParameters()) should equal(1)
    val locationNodeInfos = query.find(subset, LocationNodesParameters())
    locationNodeInfos.map(_.id) should equal(Seq(1001L))
  }

  test("only include nodes with facts") {
    val setup = new MongoQueryLocationNodesTestSetup(database)

    database.nodes.save(
      newNodeDoc(
        1001L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "01")
          )
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        )
      )
    )

    database.nodes.save(
      newNodeDoc(
        1007L,
        base = newNodeBaseData(
          names = Seq(
            newNodeName(name = "02")
          ),
        ),
        labels = Seq(
          Label.facts,
          Label.fact(Fact.NodeInvalidSurveyDate),
          Label.routeType(RouteType.hiking),
          Label.location(Country.be.entryName)
        ),
        facts = Seq(Fact.NodeInvalidSurveyDate)
      )
    )

    val parameters = LocationNodesParameters(fact = Some(Fact.NodeInvalidSurveyDate))
    val subset = LocationSubset("", hiking, Seq("be"))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)
    query.countDocuments(subset, parameters) should equal(1)
    assertEqual(
      query.find(subset, parameters),
      Seq(
        LocationNodeInfo(
          0L,
          1007L,
          "02",
          "-",
          "0",
          "0",
          Timestamps.default,
          None,
          Seq(Fact.NodeInvalidSurveyDate),
          None,
          Seq.empty
        )
      )
    )
  }

  test("paging") {
    val setup = new MongoQueryLocationNodesTestSetup(database)

    def buildNode(nodeId: Long, name: String): Unit = {
      database.nodes.save(
        newNodeDoc(
          nodeId,
          base = newNodeBaseData(
            names = Seq(
              newNodeName(name = name)
            )
          ),
          labels = Seq(
            Label.routeType(RouteType.hiking),
            Label.location(Country.be.entryName)
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

    val subset = LocationSubset("", hiking, Seq("be"))
    val query = new MongoQueryLocationNodes(database, setup.surveyDateInfo)

    def find(page: Int, pageSize: Int): Seq[Long] = {
      query.find(subset, LocationNodesParameters(pageSize = pageSize, pageIndex = page)).map(_.id)
    }

    query.countDocuments(subset, LocationNodesParameters()) should equal(8)

    find(0, 3) should equal(Seq(1001L, 1002L, 1003L))
    find(1, 3) should equal(Seq(1004L, 1005L, 1006L))
    find(2, 3) should equal(Seq(1007L, 1008L))
    find(3, 3) should equal(Seq.empty)

    find(0, 5) should equal(Seq(1001L, 1002L, 1003L, 1004L, 1005L))
    find(1, 5) should equal(Seq(1006L, 1007L, 1008L))
    find(2, 5) should equal(Seq.empty)
  }
}
