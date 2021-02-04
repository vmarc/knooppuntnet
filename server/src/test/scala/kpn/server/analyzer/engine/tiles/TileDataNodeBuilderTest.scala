package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Day
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class TileDataNodeBuilderTest extends UnitTest with SharedTestObjects {

  test("rwn_ref") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from("rwn_ref" -> "01"),
      latitude = "1",
      longitude = "2"
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.id should equal(1001)
    tileDataNode.layer should equal("node")
    tileDataNode.ref should equal(Some("01"))
    tileDataNode.name should equal(None)
    tileDataNode.latitude should equal("1")
    tileDataNode.longitude should equal("2")
    tileDataNode.surveyDate should equal(None)
  }

  test("rwn_ref = 'o'") {

    val node: NodeInfo = newNodeInfo(
      id = 1001,
      tags = Tags.from("rwn_ref" -> "o")
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(None)
  }

  test("rwn_ref and rwn_name") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from(
        "rwn_ref" -> "01",
        "rwn_name" -> "name"
      )
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(Some("01"))
    tileDataNode.name should equal(Some("name"))
  }

  test("rwn_name") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from(
        "rwn_name" -> "name"
      )
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("rwn:name") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from(
        "rwn:name" -> "name"
      )
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("rwn_name length less than or equal to 3 characters") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from(
        "rwn_name" -> "123"
      )
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(Some("123"))
    tileDataNode.name should equal(None)
  }

  test("lwn_ref") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from("lwn_ref" -> "01")
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(Some("01"))
    tileDataNode.name should equal(None)
  }

  test("prefer rwn_ref over lwn_ref") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from(
        "lwn_ref" -> "01",
        "rwn_ref" -> "02"
      )
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(Some("02"))
    tileDataNode.name should equal(None)
  }

  test("lwn_name") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from("lwn_name" -> "name")
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("lwn:name") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from("lwn:name" -> "name")
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.ref should equal(None)
    tileDataNode.name should equal(Some("name"))
  }

  test("survey date") {

    val node = newNodeInfo(
      id = 1001,
      tags = Tags.from(
        "rwn_ref" -> "01",
        "survey:date" -> "2020-08-11"
      )
    )

    val tileDataNode = new TileDataNodeBuilder().build(NetworkType.hiking, node)
    tileDataNode.surveyDate should equal(Some(Day(2020, 8, Some(11))))
  }

}
