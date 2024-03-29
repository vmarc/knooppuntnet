package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.changes.OsmChangeParser
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

import scala.xml.XML

class Issue184_NodeDeletion extends IntegrationTest {

  test("simulate node create/modify/delete") {

    pending

    testIntegration(OverpassData.empty, OverpassData.empty) {

      processCreate()
      watched.nodes.ids.toSeq should equal(Seq(8813846463L))

      processModify()
      watched.nodes.ids.toSeq should equal(Seq(8813846463L))

      processDelete()
      watched.nodes.ids.toSeq should equal(Seq.empty)

      var saveIndex = 0
      //      (tc.nodeRepository.save _).verify(
      //        where { nodeDoc: NodeDoc =>
      //          saveIndex = saveIndex + 1
      //          if (saveIndex == 1) {
      //            nodeDoc.active
      //          }
      //          else if (saveIndex == 2) {
      //            nodeDoc.active
      //          }
      //          else if (saveIndex == 3) {
      //            !nodeDoc.active
      //          }
      //          else {
      //            false
      //          }
      //        }
      //      ).repeat(3)
    }
  }

  private def processCreate(): Unit = {
    val changeSet = buildChangeSet(xmlCreate(), Timestamp(2021, 6, 8, 7, 15, 58))
    val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
    val context = ChangeSetContext(
      ReplicationId(1),
      changeSet,
      elementIds
    )
    // tc.changeProcessor.process(context)
  }

  private def processModify(): Unit = {

    val nodeBeforeModify = OverpassData()
      .node(
        id = 8813846463L,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rwn_ref" -> "o"
        )
      ).data

    val changeSet = buildChangeSet(xmlModify(), Timestamp(2021, 6, 8, 7, 16, 57))
    val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
    val context = ChangeSetContext(ReplicationId(2), changeSet, elementIds)
    // tc.changeProcessor.process(context)
  }

  private def processDelete(): Unit = {

    val nodeBeforeDelete = OverpassData()
      .node(
        id = 8813846463L,
        tags = Tags.from(
          "expected_rwn_route_relations" -> "3",
          "network:type" -> "node_network",
          "rwn_ref" -> "11"
        )
      ).data

    val changeSet = buildChangeSet(xmlDelete(), Timestamp(2021, 6, 8, 18, 45, 43))
    val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
    val context = ChangeSetContext(ReplicationId(3), changeSet, elementIds)
    // tc.changeProcessor.process(context)
  }

  private def buildChangeSet(xmlString: String, timestamp: Timestamp): ChangeSet = {
    val xml = XML.loadString(xmlString)
    val osmChange = new OsmChangeParser().parse(xml)
    val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
    changeSets.head
  }

  private def xmlCreate(): String = {
    """
      <osmChange>
        <create>
          <node id="8813846463" version="1" timestamp="2021-06-08T07:15:51Z" uid="7103674" user="Peter Elderson" changeset="106013851" lat="51.2709244" lon="5.3986269">
            <tag k="network:type" v="node_network"/>
            <tag k="rwn_ref" v="o"/>
          </node>
        </create>
      </osmChange>
    """
  }

  private def xmlModify(): String = {
    """
      <osmChange>
        <modify>
          <node id="8813846463" version="2" timestamp="2021-06-08T07:16:25Z" uid="7103674" user="Peter Elderson" changeset="106013851" lat="51.2709244" lon="5.3986269">
            <tag k="expected_rwn_route_relations" v="3"/>
            <tag k="network:type" v="node_network"/>
            <tag k="rwn_ref" v="11"/>
          </node>
        </modify>
      </osmChange>
    """
  }

  private def xmlDelete(): String = {
    """
      <osmChange>
        <delete>
          <node id="8813846463" version="3" timestamp="2021-06-08T18:46:37Z" uid="7103674" user="Peter Elderson" changeset="106052497" lat="51.2709244" lon="5.3986269"/>
        </delete>
      </osmChange>
    """
  }
}
