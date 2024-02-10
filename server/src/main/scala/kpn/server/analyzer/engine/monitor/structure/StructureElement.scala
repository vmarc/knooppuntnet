package kpn.server.analyzer.engine.monitor.structure

object StructureElement {
  def from(fragments: Seq[StructureFragment], direction: Option[ElementDirection.Value]): StructureElement = {
    StructureElement(0, fragments, direction)
  }
}

case class StructureElement(
  id: Long,
  fragments: Seq[StructureFragment],
  direction: Option[ElementDirection.Value]
) {

  def forwardStartNodeId: Long = {
    fragments.head.forwardStartNodeId
  }

  def forwardEndNodeId: Long = {
    fragments.last.forwardEndNodeId
  }

  def backwardStartNodeId: Long = {
    fragments.head.backwardStartNodeId
  }

  def backwardEndNodeId: Long = {
    fragments.last.backwardEndNodeId
  }

  def isLoop: Boolean = {
    forwardStartNodeId == forwardEndNodeId
  }

  def nodeIds: Seq[Long] = {
    fragments.zipWithIndex.flatMap { case (fragment, index) =>
      if (index == 0) {
        fragment.nodeIds
      }
      else {
        fragment.nodeIds.tail
      }
    }
  }

  def string: String = {

    direction match {
      case Some(ElementDirection.Backward) =>
        val endNodeIds = fragments.map(_.backwardEndNodeId)
        val nodeString = backwardStartNodeId.toString + endNodeIds.mkString(">", ">", "")
        val directionString = direction match {
          case None => ""
          case Some(string) => s" ($string)"
        }
        nodeString + directionString
      case _ =>
        val endNodeIds = fragments.map(_.forwardEndNodeId)
        val nodeString = forwardStartNodeId.toString + endNodeIds.mkString(">", ">", "")
        val directionString = direction match {
          case None => ""
          case Some(string) => s" ($string)"
        }
        nodeString + directionString
    }
  }
}
