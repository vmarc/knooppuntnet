package kpn.server.analyzer.engine.analysis.route.structure

object StructureUtil {

  def closedLoopNodeIds(startNodeId: Long, nodeIds: Seq[Long]): Option[Seq[Long]] = {
    val reducedNodeIds = if (nodeIds.head == nodeIds.last) {
      nodeIds.dropRight(1)
    }
    else {
      throw new Exception("a closed loop is expected to have identical start and end node")
    }

    val startIndex = reducedNodeIds.indexOf(startNodeId)
    if (startIndex >= 0) {
      Some(reducedNodeIds.drop(startIndex) ++ reducedNodeIds.takeWhile(_ != startNodeId) :+ startNodeId)
    }
    else {
      None
    }
  }

  def closedLoopNodeIds(startNodeId: Long, endNodeId: Long, nodeIds: Seq[Long]): Option[Seq[Long]] = {
    val reducedNodeIds = if (nodeIds.head == nodeIds.last) {
      nodeIds.dropRight(1)
    }
    else {
      throw new Exception("a closed loop is expected to have identical start and end node")
    }
    val startIndex = reducedNodeIds.indexOf(startNodeId)
    if (startIndex >= 0) {
      val endIndex = reducedNodeIds.indexOf(endNodeId)
      if (endIndex >= 0) {
        if (endIndex > startIndex) {
          Some(reducedNodeIds.drop(startIndex).take(endIndex - startIndex + 1))
        }
        else if (endIndex < startIndex) {
          Some(reducedNodeIds.drop(startIndex) ++ reducedNodeIds.take(endIndex + 1))
        }
        else {
          closedLoopNodeIds(startNodeId, nodeIds)
        }
      }
      else {
        None
      }
    }
    else {
      None
    }
  }

  def split(nodeIds: Seq[Long], nodeNetworkNodeIds: Seq[Long]): Seq[Seq[Long]] = {
    val splitNodeIdIndexes = {
      if (nodeIds.sizeIs > 2) {
        (1 until nodeIds.size - 1).filter { index =>
          val nodeId = nodeIds(index)
          nodeNetworkNodeIds.contains(nodeId)
        }
      }
      else {
        Seq.empty
      }
    }
    if (splitNodeIdIndexes.nonEmpty) {
      val splits = 0 +: splitNodeIdIndexes :+ nodeIds.size
      splits.sliding(2).map { case Seq(from, to) =>
        nodeIds.slice(from, to + 1)
      }.toSeq
    }
    else {
      Seq(
        nodeIds
      )
    }
  }
}
