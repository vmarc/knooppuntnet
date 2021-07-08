package kpn.server.analyzer.engine.context

import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class AnalysisContextTest extends UnitTest with SharedTestObjects {

  test("isReferencedNetworkNode rwn") {

    def isReferencedNetworkNode(tags: Tags): Boolean = {
      val context = new AnalysisContext()
      val node = newRawNode(tags = tags)
      context.isReferencedNetworkNode(ScopedNetworkType.rwn, node)
    }

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_name" -> "name")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "name")))

    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_name" -> "name")))
    assert(!isReferencedNetworkNode(Tags.from("rwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("rwn_name" -> "name")))

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01", "lwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01", "rwn_name" -> "name")))
  }

  test("isReferencedNetworkNode lwn") {

    def isReferencedNetworkNode(tags: Tags): Boolean = {
      val context = new AnalysisContext()
      val node = newRawNode(tags = tags)
      context.isReferencedNetworkNode(ScopedNetworkType(NetworkScope.local, NetworkType.hiking), node)
    }

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_name" -> "name")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_name" -> "name")))

    assert(!isReferencedNetworkNode(Tags.from("lwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("lwn_name" -> "name")))

    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_name" -> "name")))

    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01", "rwn_ref" -> "01")))
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01", "lwn_name" -> "name")))
  }

  test("isReferencedNetworkNode old tagging") {

    def isReferencedNetworkNode(tags: Tags): Boolean = {
      val context = new AnalysisContext(oldTagging = true)
      val node = newRawNode(tags = tags)
      context.isReferencedNetworkNode(ScopedNetworkType.rwn, node)
    }

    assert(isReferencedNetworkNode(Tags.from("rwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("lwn_ref" -> "01")))

    // tag "rwn_name" is ignored:
    assert(!isReferencedNetworkNode(Tags.from("rwn_name" -> "name")))

    // tag "network:type" is ignored:
    assert(isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(!isReferencedNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
  }

  test("isValidNetworkNode") {

    def isValidNetworkNode(tags: Tags): Boolean = {
      val context = new AnalysisContext()
      val node = newRawNode(tags = tags)
      context.isValidNetworkNode(NetworkType.hiking, node)
    }

    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "lwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_name" -> "name")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "lwn_name" -> "name")))

    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_ref" -> "01")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "name")))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "proposed:lwn_name" -> "name")))

    assert(!isValidNetworkNode(Tags.from("rwn_ref" -> "01")))
    assert(!isValidNetworkNode(Tags.from("lwn_ref" -> "01")))
    assert(!isValidNetworkNode(Tags.from("rwn_name" -> "name")))
    assert(!isValidNetworkNode(Tags.from("lwn_name" -> "name")))
  }

  test("isValidNetworkNode old tagging") {

    def isValidNetworkNode(tags: Tags): Boolean = {
      val context = new AnalysisContext(oldTagging = true)
      val node = newRawNode(tags = tags)
      context.isValidNetworkNode(NetworkType.hiking, node)
    }

    assert(isValidNetworkNode(Tags.from("rwn_ref" -> "01")))
    assert(!isValidNetworkNode(Tags.from("lwn_ref" -> "01"))) // only regional
    assert(!isValidNetworkNode(Tags.from("rwn_name" -> "name"))) // r?n_name tags not supported yet
    assert(!isValidNetworkNode(Tags.from("proposed:rwn_ref" -> "01"))) // proposed not supported yet
    assert(!isValidNetworkNode(Tags.from("proposed:rwn_name" -> "name"))) // proposed not supported yet

    // ignore "network:type" tag
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))
  }

  test("isValidNetworkNode beforeNetworkTypeTaggingStart") {

    def isValidNetworkNode(tags: Tags, knownNode: Boolean): Boolean = {
      val context = new AnalysisContext(beforeNetworkTypeTaggingStart = true)
      val node = newRawNode(tags = tags)
      if (knownNode) {
        context.addKnownNode(node.id)
      }
      context.isValidNetworkNode(NetworkType.hiking, node)
    }

    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), knownNode = true))
    assert(isValidNetworkNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"), knownNode = false))
    assert(isValidNetworkNode(Tags.from("rwn_ref" -> "01"), knownNode = true))
    assert(!isValidNetworkNode(Tags.from("lwn_ref" -> "01"), knownNode = true)) // only regional
    assert(!isValidNetworkNode(Tags.from("rwn_name" -> "name"), knownNode = true)) // r?n_name tags not supported yet
    assert(!isValidNetworkNode(Tags.from("rwn_ref" -> "01"), knownNode = false)) // ignore if not a known node
  }

  test("unexpectedNode") {

    def isUnexpectedNode(tags: Tags): Boolean = {
      val context = new AnalysisContext()
      val node = newNode(1L, tags = tags)
      context.isUnexpectedNode(ScopedNetworkType.rwn, node)
    }

    // map
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "map")))
    assert(!isUnexpectedNode(Tags.from("tourism" -> "information", "information" -> "guidepost")))

    // actual node
    assert(!isUnexpectedNode(Tags.from("network:type" -> "node_network", "rwn_ref" -> "01")))

    // unexpected
    assert(isUnexpectedNode(Tags.empty))
  }
}
