package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Relation

class StructureAnalyzer(traceEnabled: Boolean = false) {

  def analyze(relation: Relation): Structure = {
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled)
    val forwardPath = analyzeForwardPath(elementGroups)
    val backwardPath = analyzeBackwardPath(elementGroups)

    Structure(
      forwardPath,
      backwardPath,
      Seq.empty
    )
  }

  private def analyzeForwardPath(elementGroups: Seq[StructureElementGroup]): Option[StructurePath] = {
    elementGroups.headOption.flatMap { firstElementGroup =>
      val elements = firstElementGroup.elements.filter { element =>
        element.direction match {
          case None => true
          case Some(ElementDirection.Down) => true
          case Some(ElementDirection.Up) => false
        }
      }
      elements.headOption match {
        case None => None
        case Some(firstElement) =>
          elements.lastOption match {
            case None => None
            case Some(lastElement) =>
              Some(
                StructurePath(
                  firstElement.startNodeId,
                  lastElement.endNodeId,
                  elements
                )
              )
          }
      }
    }
  }

  private def analyzeBackwardPath(elementGroups: Seq[StructureElementGroup]): Option[StructurePath] = {
    elementGroups.lastOption.flatMap { lastElementGroup =>
      val elements = lastElementGroup.elements.reverse.filter { element =>
        element.direction match {
          case None => true
          case Some(ElementDirection.Up) => true
          case Some(ElementDirection.Down) => false
        }
      }
      elements.lastOption match {
        case None => None
        case Some(firstElement) =>
          elements.lastOption match {
            case None => None
            case Some(lastElement) =>
              Some(
                StructurePath(
                  firstElement.endNodeId,
                  lastElement.startNodeId,
                  elements.reverse
                )
              )
          }
      }
    }
  }
}
