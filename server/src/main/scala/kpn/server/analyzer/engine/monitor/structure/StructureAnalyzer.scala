package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Relation

class StructureAnalyzer(traceEnabled: Boolean = false) {

  def analyze(relation: Relation): Structure = {
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled)
    if (elementGroups.size != 1) {
      Structure(
        None,
        None,
        Seq.empty
      )
    }
    else {
      val forwardPath = analyzeForwardPath(elementGroups)
      val backwardPath = analyzeBackwardPath(elementGroups)
      Structure(
        forwardPath,
        backwardPath,
        Seq.empty
      )
    }
  }

  private def analyzeForwardPath(elementGroups: Seq[StructureElementGroup]): Option[StructurePath] = {
    elementGroups.headOption.flatMap { firstElementGroup =>
      val elements = firstElementGroup.elements.filter { element =>
        element.direction match {
          case Some(ElementDirection.Forward) => true
          case Some(ElementDirection.Backward) => false
          case _ => true
        }
      }
      elements.headOption match {
        case None => None
        case Some(firstElement) =>
          elements.lastOption match {
            case None => None
            case Some(lastElement) =>
              val structurePathElements = elements.map { element =>
                StructurePathElement(element, reversed = false)
              }
              Some(
                StructurePath(
                  firstElement.forwardStartNodeId,
                  lastElement.forwardEndNodeId,
                  structurePathElements
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
          case Some(ElementDirection.Backward) => true
          case Some(ElementDirection.Forward) => false
          case _ => true
        }
      }
      if (elements.isEmpty) {
        None
      }
      else {
        val structurePathElements = elements.map { element =>
          val reversed = element.direction.isEmpty
          StructurePathElement(element, reversed)
        }
        val startNodeId = structurePathElements.head.nodeIds.head
        val endNodeId = structurePathElements.last.nodeIds.last
        Some(
          StructurePath(
            startNodeId,
            endNodeId,
            structurePathElements
          )
        )
      }
    }
  }
}
