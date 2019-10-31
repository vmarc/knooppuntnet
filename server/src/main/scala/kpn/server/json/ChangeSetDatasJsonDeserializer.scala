package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.core.database.doc.ChangeSetDatas
import kpn.shared.ChangeSetSummary
import kpn.shared.changes.ChangeSetData
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange
import org.springframework.boot.jackson.JsonComponent

import scala.collection.mutable.ArrayBuffer

@JsonComponent
class ChangeSetDatasJsonDeserializer extends JsonDeserializer[ChangeSetDatas] {

  private class ChangeSetElements {
    val summaries: ArrayBuffer[ChangeSetSummary] = ArrayBuffer()
    val networkChanges: ArrayBuffer[NetworkChange] = ArrayBuffer()
    val routeChanges: ArrayBuffer[RouteChange] = ArrayBuffer()
    val nodeChanges: ArrayBuffer[NodeChange] = ArrayBuffer()
  }

  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ChangeSetDatas = {
    val elements = changeSetElements(jsonParser: JsonParser)
    val replicationNumbers = replicationNumbersIn(elements)
    val datas = replicationNumbers.map(replicationNumber => toReplicationChangeSetData(elements, replicationNumber))
    ChangeSetDatas(datas)
  }

  private def changeSetElements(jsonParser: JsonParser): ChangeSetElements = {

    val rows: JsonNode = {
      val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
      node.get("rows")
    }

    val elements = new ChangeSetElements()

    if (rows.isArray) {
      (0 until rows.size()).foreach { index =>
        val row = rows.get(index)
        val doc = row.get("doc")
        processRow(jsonParser, elements, doc)
      }
    }

    elements
  }

  private def replicationNumbersIn(elements: ChangeSetElements): Seq[Int] = {
    val replicationNumbers = elements.networkChanges.map(_.key.replicationNumber) ++
      elements.nodeChanges.map(_.key.replicationNumber) ++
      elements.routeChanges.map(_.key.replicationNumber) ++
      elements.summaries.map(_.key.replicationNumber)
    replicationNumbers.distinct.sorted
  }

  private def processRow(jsonParser: JsonParser, elements: ChangeSetElements, doc: JsonNode): Unit = {

    val networkChangeNode = doc.get("networkChange")
    if (networkChangeNode != null) {
      val networkChange = jsonParser.getCodec.treeToValue(networkChangeNode, classOf[NetworkChange])
      elements.networkChanges += networkChange
    }

    val nodeChangeNode = doc.get("nodeChange")
    if (nodeChangeNode != null) {
      val nodeChange = jsonParser.getCodec.treeToValue(nodeChangeNode, classOf[NodeChange])
      elements.nodeChanges += nodeChange
    }

    val routeChangeNode = doc.get("routeChange")
    if (routeChangeNode != null) {
      val routeChange = jsonParser.getCodec.treeToValue(routeChangeNode, classOf[RouteChange])
      elements.routeChanges += routeChange
    }

    val changeSetSummaryNode = doc.get("changeSetSummary")
    if (changeSetSummaryNode != null) {
      val changeSetSummary = jsonParser.getCodec.treeToValue(changeSetSummaryNode, classOf[ChangeSetSummary])
      elements.summaries += changeSetSummary
    }
  }

  private def toReplicationChangeSetData(elements: ChangeSetElements, replicationNumber: Int): ChangeSetData = {

    val changeSetSummaries = elements.summaries.filter(_.key.replicationNumber == replicationNumber)
    val networkChanges = elements.networkChanges.filter(_.key.replicationNumber == replicationNumber)
    val routeChanges = elements.routeChanges.filter(_.key.replicationNumber == replicationNumber)
    val nodeChanges = elements.nodeChanges.filter(_.key.replicationNumber == replicationNumber)

    assert(changeSetSummaries.size == 1, "Expecting single change set summary object, but found " + changeSetSummaries.size)

    ChangeSetData(
      changeSetSummaries.head,
      Seq(networkChanges: _*),
      Seq(routeChanges: _*),
      Seq(nodeChanges: _*)
    )
  }

}
