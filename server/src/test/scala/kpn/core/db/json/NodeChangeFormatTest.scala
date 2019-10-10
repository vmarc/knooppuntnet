package kpn.core.db.json

import kpn.core.db.json.JsonFormats.NodeChangeFormat
import kpn.shared.Fact
import kpn.shared.LatLonImpl
import kpn.shared.SharedTestObjects
import kpn.shared.Subset
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.common.Ref
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.node.NodeMoved
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json._

class NodeChangeFormatTest extends FunSuite with Matchers with SharedTestObjects {

  test("serialize/deserialize") {
    serializeAndDeserialize(nodeChange())
  }

  test("default values for 'happy' and 'investigate'") {
    val nodeChange = NodeChangeFormat.read(incompleteJsonString.parseJson)
    nodeChange.happy should equal(false)
    nodeChange.investigate should equal(false)
  }

  test("serialize/deserialize - no before") {
    serializeAndDeserialize(nodeChange().copy(before = None))
  }

  test("serialize/deserialize - no after") {
    serializeAndDeserialize(nodeChange().copy(after = None))
  }

  test("serialize/deserialize - no tagDiffs") {
    serializeAndDeserialize(nodeChange().copy(tagDiffs = None))
  }

  test("serialize/deserialize - no nodeMoved") {
    val before = newNodeChange(nodeMoved = None)
    val json = NodeChangeFormat.write(before)
    val after = NodeChangeFormat.read(json)
    after should equal(before)
  }

  private def nodeChange(): NodeChange = {
    NodeChange(
      key = newChangeKey(),
      changeType = ChangeType.Create,
      subsets = Seq(Subset.nlHiking, Subset.beBicycle),
      "02",
      before = Some(
        newRawNode(2)
      ),
      after = Some(
        newRawNode(3)
      ),
      connectionChanges = Seq(
        RefBooleanChange(
          ref = Ref(1, "01"),
          after = true
        )
      ),
      roleConnectionChanges = Seq(
        RefBooleanChange(
          ref = Ref(1, "network-1"),
          after = true
        )
      ),
      definedInNetworkChanges = Seq(
        RefBooleanChange(
          ref = Ref(2, "network-2"),
          after = true
        )
      ),
      tagDiffs = Some(
        TagDiffs()
      ),
      nodeMoved = Some(
        NodeMoved(
          before = LatLonImpl("1", "2"),
          after = LatLonImpl("3", "4"),
          distance = 2
        )
      ),
      addedToRoute = Seq(
        Ref(11, "01-02")
      ),
      removedFromRoute = Seq(
        Ref(12, "01-03")
      ),
      addedToNetwork = Seq(
        Ref(1, "network-1")
      ),
      removedFromNetwork = Seq(
        Ref(1, "network-2")
      ),
      factDiffs = FactDiffs(),
      facts = Seq(
        Fact.Added,
        Fact.WasOrphan
      ),
      happy = true,
      investigate = true
    )
  }

  private def incompleteJsonString: String = {
    // values for 'happy' and 'investigate'
    """
      |{
      |  "key": {
      |    "replicationNumber": 1,
      |    "timestamp": "2015-08-11T00:00:00Z",
      |    "changeSetId": 123,
      |    "elementId": 0
      |  },
      |  "changeType": {
      |    "name": "Create"
      |  },
      |  "subsets": ["nl:rwn", "be:rcn"],
      |  "name": "02",
      |  "before": {
      |    "changeSetId": 0,
      |    "timestamp": "2015-08-11T00:00:00Z",
      |    "latitude": "0",
      |    "tags": [],
      |    "longitude": "0",
      |    "version": 0,
      |    "id": 1
      |  },
      |  "after": {
      |    "changeSetId": 0,
      |    "timestamp": "2015-08-11T00:00:00Z",
      |    "latitude": "0",
      |    "tags": [],
      |    "longitude": "0",
      |    "version": 0,
      |    "id": 2
      |  },
      |  "connectionChanges": [{
      |    "ref": {
      |      "id": 1,
      |      "name": "01"
      |    },
      |    "after": true
      |  }],
      |  "roleConnectionChanges": [{
      |    "ref": {
      |      "id": 1,
      |      "name": "network-1"
      |    },
      |    "after": true
      |  }],
      |  "definedInNetworkChanges": [{
      |    "ref": {
      |      "id": 2,
      |      "name": "network-2"
      |    },
      |    "after": true
      |  }],
      |  "tagDiffs": {
      |    "mainTags": [],
      |    "extraTags": []
      |  },
      |  "nodeMoved": {
      |    "before": {
      |      "latitude": "1",
      |      "longitude": "2"
      |    },
      |    "after": {
      |      "latitude": "3",
      |      "longitude": "4"
      |    },
      |    "distance": 2
      |  },
      |  "addedToRoute": [{
      |    "id": 11,
      |    "name": "01-02"
      |  }],
      |  "removedFromRoute": [{
      |    "id": 12,
      |    "name": "01-03"
      |  }],
      |  "addedToNetwork": [{
      |    "id": 1,
      |    "name": "network-1"
      |  }],
      |  "removedFromNetwork": [{
      |    "id": 1,
      |    "name": "network-2"
      |  }],
      |  "factDiffs": {
      |    "resolved": [],
      |    "introduced": [],
      |    "remaining": []
      |  },
      |  "facts": ["Added", "WasOrphan"],
      |  "extra": "bla"
      |}
      |""".stripMargin
  }

  private def serializeAndDeserialize(nodeChange: NodeChange): Unit = {
    val json = NodeChangeFormat.write(nodeChange)
    val after = NodeChangeFormat.read(json)
    after should equal(nodeChange)
  }
}
