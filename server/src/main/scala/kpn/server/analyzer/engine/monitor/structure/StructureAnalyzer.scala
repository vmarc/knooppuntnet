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
          case Some(ElementDirection.Down) => true
          case Some(ElementDirection.Up) => false
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
                  firstElement.startNodeId,
                  lastElement.endNodeId,
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
          case Some(ElementDirection.Up) => true
          case Some(ElementDirection.Down) => false
          case _ => true
        }
      }
      if (elements.isEmpty) {
        None
      }
      else {
        val structurePathElements = elements.map { element =>
          StructurePathElement(element, reversed = true)
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
