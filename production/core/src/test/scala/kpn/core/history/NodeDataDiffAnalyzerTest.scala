package kpn.core.history

import kpn.shared.LatLonImpl
import kpn.shared.SharedTestObjects
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.diff.NodeData
import kpn.shared.diff.NodeDataUpdate
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.node.NodeMoved
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeDataDiffAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  test("no change") {
    val n1 = nodeData(1, "51.5291500", "4.297700", 3, Timestamp(2015, 8, 11, 0, 0, 0), 100, Tags.from("a" -> "1"))
    val n2 = nodeData(1, "51.5291500", "4.297700", 3, Timestamp(2015, 8, 11, 0, 0, 0), 100, Tags.from("a" -> "1"))
    new NodeDataDiffAnalyzer(n1, n2).analysis should equal(None)
  }

  test("node moved") {
    val n1 = nodeData(1, "51.5291500", "4.297700", 3, Timestamp(2015, 8, 11, 0, 0, 0), 100, Tags.from("a" -> "1"))
    val n2 = nodeData(1, "51.5291600", "4.297800", 4, Timestamp(2015, 8, 11, 12, 0, 0), 100, Tags.from("a" -> "1"))
    new NodeDataDiffAnalyzer(n1, n2).analysis should equal(Some(NodeDataUpdate(n1, n2, None, Some(NodeMoved(LatLonImpl("51.5291500", "4.297700"), LatLonImpl
    ("51.5291600", "4.297800"), 7)))))
  }

  test("tags changed") {
    val n1 = nodeData(1, "51.5291500", "4.297700", 3, Timestamp(2015, 8, 11, 0, 0, 0), 100, Tags.from("a" -> "1"))
    val n2 = nodeData(1, "51.5291500", "4.297700", 4, Timestamp(2015, 8, 11, 12, 0, 0), 100, Tags.from("a" -> "2"))
    val expectedDiffs = TagDiffs(Seq(), Seq(TagDetail(TagDetailType.Update, "a", Some("1"), Some("2"))))
    new NodeDataDiffAnalyzer(n1, n2).analysis should equal(Some(NodeDataUpdate(n1, n2, Some(expectedDiffs))))
  }

  test("only meta info changed") {
    val n1 = nodeData(1, "51.5291500", "4.297700", 3, Timestamp(2015, 8, 11, 0, 0, 0), 100, Tags.from("a" -> "1"))
    val n2 = nodeData(1, "51.5291500", "4.297700", 4, Timestamp(2015, 8, 11, 12, 0, 0), 101, Tags.from("a" -> "1"))
    new NodeDataDiffAnalyzer(n1, n2).analysis should equal(Some(NodeDataUpdate(n1, n2, None)))
  }

  def nodeData(
    id: Long,
    latitude: String,
    longitude: String,
    version: Int = 0,
    timestamp: Timestamp,
    changeSetId: Long,
    tags: Tags
  ): NodeData = {
    val rawNode = newNode(id, latitude, longitude, version, timestamp, changeSetId, tags).raw
    NodeData(
      Seq(),
      "name",
      rawNode
    )
  }

}
