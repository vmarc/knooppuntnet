package kpn.core.mongo.actions.locations

import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryLocationNodeCountTest extends UnitTest with SharedTestObjects {

  test("node count") {
    withDatabase { database =>

      def buildNode(nodeId: Long, locations: Seq[String], tags: Tags, active: Boolean = true, lastSurvey: Option[Day] = None, facts: Seq[Fact] = Seq.empty): Unit = {
        database.nodes.save(
          newNodeInfo(
            nodeId,
            location = Some(Location(locations)),
            tags = tags,
            active = active,
            lastSurvey = lastSurvey,
            facts = facts
          )
        )
      }

      def count(networkType: NetworkType, location: String, locationNodesType: LocationNodesType): Long = {
        new MongoQueryLocationNodeCount(database).execute(networkType, location, locationNodesType)
      }

      buildNode(1001L, Seq("be", "Kalmthout"), Tags.from("rwn_ref" -> "01"))
      buildNode(1002L, Seq("be", "Kalmthout"), Tags.from("rwn_ref" -> "02"))
      buildNode(1003L, Seq("be", "Brasschaat"), Tags.from("rwn_ref" -> "03"), lastSurvey = Some(Day(2020, 8, Some(11))))
      buildNode(1004L, Seq("be", "Kalmthout"), Tags.from("rcn_ref" -> "04"))
      buildNode(1005L, Seq("be", "Kalmthout"), Tags.from("rwn_ref" -> "05"), active = false)
      buildNode(1006L, Seq("nl"), Tags.from("rwn_ref" -> "06"))
      buildNode(1007L, Seq("be"), Tags.from("rwn_ref" -> "07"), facts = Seq(Fact.NodeInvalidSurveyDate))

      count(hiking, "be", LocationNodesType.all) should equal(4)
      count(cycling, "be", LocationNodesType.all) should equal(1)
      count(hiking, "nl", LocationNodesType.all) should equal(1)
      count(hiking, "be", LocationNodesType.facts) should equal(1)
      count(hiking, "be", LocationNodesType.survey) should equal(1)
      count(hiking, "Kalmthout", LocationNodesType.all) should equal(2)
      count(hiking, "Brasschaat", LocationNodesType.all) should equal(1)
    }
  }
}
