package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

class Issue408_AddNonRouteRelationToNetwork extends IntegrationTest {

  test("relation without 'network:type=node_network' should not be considered a route") {

    val dataBefore = OverpassData()
      .networkRelation(
        1,
        "name"
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01") // it was this node that made the relation to be considered a route
      .way(101, 1001, 1002)
      .relation(
        11,
        Seq(
          newMember(MemberType.Way, 101)
        ),
        Tags.from( // no network:type=node_network tag
          "network" -> "rwn",
          "route" -> "hiking",
          "type" -> "route",
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      val networkInfoDoc = findNetworkById(1)

      networkInfoDoc.routes.size should equal(0)
      networkInfoDoc.facts.map(_.fact.entryName) should equal(Seq("NetworkExtraMemberRelation"))
      networkInfoDoc.extraRelationIds should equal(Seq(11))
      database.routes.findById(11) should equal(None)
    }
  }
}
