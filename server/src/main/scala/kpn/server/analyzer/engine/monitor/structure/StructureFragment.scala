package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Way

case class StructureFragment(way: Way, bidirectional: Boolean, nodeIds: Seq[Long]) {

  def forwardStartNodeId: Long = {
    nodeIds.head
  }

  def forwardEndNodeId: Long = {
    nodeIds.last
  }

  def backwardStartNodeId: Long = {
    if (bidirectional) {
      nodeIds.last
    }
    else {
      nodeIds.head
    }
  }

  def backwardEndNodeId: Long = {
    if (bidirectional) {
      nodeIds.head
    }
    else {
      nodeIds.last
    }
  }

  def string: String = {
    s"(${way.id}) $forwardStartNodeId${if (bidirectional) "<>" else ">"}$forwardEndNodeId"
  }
}
