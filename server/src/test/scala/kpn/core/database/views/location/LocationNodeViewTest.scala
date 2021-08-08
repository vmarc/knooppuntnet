package kpn.core.database.views.location

import kpn.api.common.NodeName
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.LocationKey
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.database.Database
import kpn.core.mongo.doc.NodeDoc
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NodeRepositoryImpl

class LocationNodeViewTest extends UnitTest with SharedTestObjects {

  test("node") {

    pending

    withCouchDatabase { database =>
      val repo = new NodeRepositoryImpl(null, database, false)
      repo.save(
        buildNode(1001L)
      )

      val expected = Seq(
        newLocationNodeInfo()
      )

      query(database, LocationNodesType.all, "nl") should matchTo(expected)
      query(database, LocationNodesType.all, "province") should matchTo(expected)
      query(database, LocationNodesType.all, "municipality") should matchTo(expected)

      query(database, LocationNodesType.facts, "nl") shouldBe empty
      query(database, LocationNodesType.survey, "nl") shouldBe empty

      queryCount(database, LocationNodesType.all, "nl") should equal(1)
      queryCount(database, LocationNodesType.all, "province") should equal(1)
      queryCount(database, LocationNodesType.all, "municipality") should equal(1)

      queryCount(database, LocationNodesType.facts, "nl") should equal(0)
      queryCount(database, LocationNodesType.survey, "nl") should equal(0)
    }
  }

  test("node with lastSurvey") {

    pending

    withCouchDatabase { database =>
      val repo = new NodeRepositoryImpl(null, database, false)
      repo.save(buildNode(1001))
      repo.save(buildNode(1002).copy(lastSurvey = Some(Day(2020, 8, Some(12)))))
      repo.save(buildNode(1003).copy(lastSurvey = Some(Day(2020, 8, Some(11)))))

      val expected = Seq(
        newLocationNodeInfo(1003, lastSurvey = Some(Day(2020, 8, Some(11)))),
        newLocationNodeInfo(1002, lastSurvey = Some(Day(2020, 8, Some(12)))),
      )

      query(database, LocationNodesType.survey, "nl") should matchTo(expected)
      queryCount(database, LocationNodesType.survey, "nl") should equal(2)
    }
  }

  test("node with facts") {

    pending

    withCouchDatabase { database =>
      val repo = new NodeRepositoryImpl(null, database, false)
      repo.save(buildNode(1001))
      repo.save(buildNode(1002).copy(facts = Seq(Fact.OrphanNode)))

      val expected = Seq(
        newLocationNodeInfo(1002, factCount = 1),
      )

      query(database, LocationNodesType.facts, "nl") should matchTo(expected)
      queryCount(database, LocationNodesType.facts, "nl") should equal(1)
    }
  }

  private def buildNode(id: Long): NodeDoc = {
    newNodeDoc(
      id = id,
      country = Some(Country.nl),
      names = Seq(
        NodeName(
          NetworkType.cycling,
          NetworkScope.local,
          "01",
          None,
          proposed = false
        )
      ),
      latitude = "1",
      longitude = "2",
      lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
      tags = Tags.from(
        "lcn_ref" -> "01",
        "expected_lcn_route_relations" -> "3"
      ),
      locations = Seq("nl", "province", "municipality")
    )
  }

  private def newLocationNodeInfo(id: Long = 1001, factCount: Int = 0, lastSurvey: Option[Day] = None): LocationNodeInfo = {
    LocationNodeInfo(
      id = id,
      name = "01",
      longName = "-",
      latitude = "1",
      longitude = "2",
      lastUpdated = Timestamp(2019, 8, 11, 12, 34, 56),
      lastSurvey = lastSurvey,
      factCount = factCount,
      expectedRouteCount = "3",
      routeReferences = Seq.empty
    )
  }

  private def query(database: Database, locationNodesType: LocationNodesType, locationName: String): Seq[LocationNodeInfo] = {
    val parameters = LocationNodesParameters(locationNodesType, 99)
    LocationNodeView.query(
      database,
      LocationKey(NetworkType.cycling, Country.nl, locationName),
      parameters,
      stale = false
    )
  }

  private def queryCount(database: Database, locationNodesType: LocationNodesType, locationName: String): Long = {
    LocationNodeView.queryCount(
      database,
      LocationKey(NetworkType.cycling, Country.nl, locationName),
      locationNodesType,
      stale = false
    )
  }
}
