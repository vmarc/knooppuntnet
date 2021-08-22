package kpn.server.analyzer.engine.tiles

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.mongo.doc.NodeDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

class TileDataNodeBuilderTest extends UnitTest with SharedTestObjects {

  test("rwn_ref") {


    val node = newNodeDoc( // TODO TileInfoNode
      id = 1001,
      labels = Seq("active"),
      names = Seq(
        newNodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "01"
        )
      ),
      latitude = "1",
      longitude = "2"
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.id should equal(1001)
    tileDataNode.layer should equal("node")
    tileDataNode.ref should equal(Some("01"))
    tileDataNode.name should equal(None)
    tileDataNode.latitude should equal("1")
    tileDataNode.longitude should equal("2")
    tileDataNode.surveyDate should equal(None)
  }

  test("proposed") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from("proposed:rwn_ref" -> "01")
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(Some("01"))
  }

  test("rwn_ref = 'o'") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from("rwn_ref" -> "o")
    )

    tileDataNodeBuilder.build(NetworkType.hiking, null /*node*/) should equal(None)
  }

  test("proposed:rwn_ref = 'o'") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from("proposed:rwn_ref" -> "o")
    )

    tileDataNodeBuilder.build(NetworkType.hiking, null /*node*/) should equal(None)
  }

  test("rwn_ref and rwn_name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "rwn_ref" -> "01",
        "rwn_name" -> "name"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(Some("01"))
    tileDataNode.name should equal(Some("name"))
  }

  test("rwn_name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "rwn_name" -> "name"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("proposed:rwn_name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "proposed:rwn_name" -> "name"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("rwn:name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "rwn:name" -> "name"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("proposed:rwn:name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "proposed:rwn:name" -> "name"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("rwn_name length less than or equal to 3 characters") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "rwn_name" -> "123"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(Some("123"))
    tileDataNode.name should equal(None)
  }

  test("lwn_ref") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from("lwn_ref" -> "01")
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(Some("01"))
    tileDataNode.name should equal(None)
  }

  test("prefer rwn_ref over lwn_ref") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "lwn_ref" -> "01",
        "rwn_ref" -> "02"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(Some("02"))
    tileDataNode.name should equal(None)
  }

  test("lwn_name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from("lwn_name" -> "name")
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("lwn:name") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from("lwn:name" -> "name")
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("survey date") {

    val node = newNodeDoc(
      id = 1001,
      labels = Seq("active"),
      tags = Tags.from(
        "rwn_ref" -> "01",
        "survey:date" -> "2020-08-11"
      )
    )

    val tileDataNode = buildTileDataNode(node)
    tileDataNode.surveyDate should equal(Some(Day(2020, 8, Some(11))))
  }


  private def buildTileDataNode(node: NodeDoc): TileDataNode = {
    pending
    tileDataNodeBuilder.build(NetworkType.hiking, null /*node*/).get
  }

  private def tileDataNodeBuilder: TileDataNodeBuilder = {
    new TileDataNodeBuilderImpl()
  }
}
