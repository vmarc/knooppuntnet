package kpn.server.analyzer.engine.context

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class AnalysisContextTest extends UnitTest with SharedTestObjects {

  test("isReferencedNetworkNode") {

    def doTest(tags: Tags, expected: Boolean) {
      val context = new AnalysisContext()
      val node = newRawNode(tags = tags)
      context.isReferencedNetworkNode(ScopedNetworkType.rwn, node) should equal(expected)
    }

    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "rwn_name" -> "name"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01"), expected = false)
    doTest(Tags.from("network:type" -> "node_network", "lwn_name" -> "name"), expected = false)
    doTest(Tags.from("rwn_ref" -> "01"), expected = false)
    doTest(Tags.from("rwn_name" -> "name"), expected = false)

    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01", "lwn_ref" -> "01"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01", "rwn_name" -> "name"), expected = true)
  }

  test("isReferencedNetworkNode old tagging") {

    def doTest(tags: Tags, expected: Boolean) {
      val context = new AnalysisContext(oldTagging = true)
      val node = newRawNode(tags = tags)
      context.isReferencedNetworkNode(ScopedNetworkType.rwn, node) should equal(expected)
    }

    doTest(Tags.from("rwn_ref" -> "01"), expected = true)
    doTest(Tags.from("lwn_ref" -> "01"), expected = false)

    // tag "rwn_name" is ignored:
    doTest(Tags.from("rwn_name" -> "name"), expected = false)

    // tag "network:type" is ignored:
    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01"), expected = false)
  }

  test("isValidNetworkNode") {

    def doTest(tags: Tags, expected: Boolean) {
      val context = new AnalysisContext()
      val node = newRawNode(tags = tags)
      context.isValidNetworkNode(NetworkType.hiking, node) should equal(expected)
    }

    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "rwn_name" -> "name"), expected = true)
    doTest(Tags.from("network:type" -> "node_network", "lwn_name" -> "name"), expected = true)


    doTest(Tags.from("rwn_ref" -> "01"), expected = false)
    doTest(Tags.from("lwn_ref" -> "01"), expected = false)
    doTest(Tags.from("rwn_name" -> "name"), expected = false)
    doTest(Tags.from("lwn_name" -> "name"), expected = false)
  }

  test("isValidNetworkNode old tagging") {

    def doTest(tags: Tags, expected: Boolean) {
      val context = new AnalysisContext(oldTagging = true)
      val node = newRawNode(tags = tags)
      context.isValidNetworkNode(NetworkType.hiking, node) should equal(expected)
    }

    doTest(Tags.from("rwn_ref" -> "01"), expected = true)
    doTest(Tags.from("lwn_ref" -> "01"), expected = false) // only regional
    doTest(Tags.from("rwn_name" -> "name"), expected = false) // r?n_name tags not supported yet

    // ignore "network:type" tag
    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), expected = true)
  }

  test("isValidNetworkNode beforeNetworkTypeTaggingStart") {

    def doTest(tags: Tags, knownNode: Boolean, expected: Boolean) {
      val context = new AnalysisContext(beforeNetworkTypeTaggingStart = true)
      val node = newRawNode(tags = tags)
      if (knownNode) {
        context.addKnownNode(node.id)
      }
      context.isValidNetworkNode(NetworkType.hiking, node) should equal(expected)
    }

    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), knownNode = true, expected = true)
    doTest(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), knownNode = false, expected = true)
    doTest(Tags.from("rwn_ref" -> "01"), knownNode = true, expected = true)
    doTest(Tags.from("lwn_ref" -> "01"), knownNode = true, expected = false) // only regional
    doTest(Tags.from("rwn_name" -> "name"), knownNode = true, expected = false) // r?n_name tags not supported yet
    doTest(Tags.from("rwn_ref" -> "01"), knownNode = false, expected = false) // ignore if not a known node
  }

}
