package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.changes.OsmChangeParser
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.xml.XML

class Issue184_NodeDeletion extends IntegrationTest {

  private val afterCreate = OverpassData()
    .node(
      id = 8813846463L,
      tags = Tags.from(
        "network:type" -> "node_network",
        "rwn_ref" -> "o"
      )
    ).data

  private val afterUpdate = OverpassData()
    .node(
      id = 8813846463L,
      tags = Tags.from(
        "expected_rwn_route_relations" -> "3",
        "network:type" -> "node_network",
        "rwn_ref" -> "11"
      )
    ).data

  test("simulate node create/modify/delete") {

    testIntegration(OverpassData.empty, OverpassData.empty) {

      watched.nodes.asScala.toSet should equal(Set.empty)

      processCreate()
      watched.nodes.asScala.toSet should equal(Set(8813846463L))

      findNodeById(8813846463L).active shouldBe true

      processModify()
      watched.nodes.asScala.toSet should equal(Set(8813846463L))

      findNodeById(8813846463L).active shouldBe true

      processDelete()
      watched.nodes.asScala.toSet should equal(Set.empty)

      findNodeById(8813846463L).active shouldBe false
    }
  }

  private def processCreate(): Unit = {
    val timestamp = Timestamp(2021, 6, 8, 7, 15, 52)
    context.overpassRepository.setData(timestamp, afterCreate)
    val changeSet = buildChangeSet(xmlCreate(), timestamp)
    processChangeSet(1, changeSet)
  }

  private def processModify(): Unit = {
    val timestamp = Timestamp(2021, 6, 8, 7, 16, 26)
    context.overpassRepository.setData(timestamp, afterUpdate)
    val changeSet = buildChangeSet(xmlModify(), timestamp)
    processChangeSet(2, changeSet)
  }

  private def processDelete(): Unit = {
    val timestamp = Timestamp(2021, 6, 8, 18, 46, 38)
    context.overpassRepository.setData(timestamp, afterUpdate)
    val changeSet = buildChangeSet(xmlDelete(), timestamp)
    processChangeSet(3, changeSet)
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
