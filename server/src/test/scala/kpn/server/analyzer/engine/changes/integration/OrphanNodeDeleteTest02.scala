package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.NodeDoc
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest02 extends AbstractTest {

  test("delete orphan node, and 'before' situation cannot be found in overpass database") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty
      val dataAfter = OverpassData.empty

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.nodes.watched.add(1001)

      tc.process(ChangeAction.Delete, newRawNode(1001))

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      tc.findNodeById(1001) should matchTo(
        NodeDoc(
          1001,
          labels = Seq(
            "facts",
            "fact-Deleted"
          ),
          active = false, // <-- !!
          Some(Country.nl),
          "",
          Seq.empty,
          "0",
          "0",
          Timestamp(2015, 8, 11, 0, 0, 0),
          None,
          Tags.empty,
          Seq(Fact.Deleted),
          Seq.empty,
          Seq.empty,
          None,
          Seq.empty
        )
      )

      // TODO is the following correct ???
      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.nodeChanges.findAll().isEmpty)
    }
  }
}
