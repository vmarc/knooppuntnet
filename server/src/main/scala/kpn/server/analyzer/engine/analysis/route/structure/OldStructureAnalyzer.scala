package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.route.RouteNodes

class OldStructureAnalyzer(traceEnabled: Boolean = false) {

  def analyze(routeNodeAnalysis: RouteNodes, elementGroups: Seq[StructureElementGroup]): OldStructure = {

    val mainStartNode = routeNodeAnalysis.startNode.lastOption
    val mainEndNode = routeNodeAnalysis.endNode.headOption

    if (elementGroups.size != 1) {
      val otherPaths: Seq[OldStructurePath] = {
        elementGroups.map { elementGroup =>
          val pathElements = elementGroup.elements.map { element =>
            OldStructurePathElement(
              element,
              reversed = false
            )
          }
          val startNodeId = pathElements.head.nodeIds.head
          val endNodeId = pathElements.last.nodeIds.last
          OldStructurePath(
            startNodeId,
            endNodeId,
            pathElements
          )
        }
      }

      OldStructure(
        None,
        None,
        otherPaths
      )
    }
    else {
      val forwardPath = analyzeForwardPath(elementGroups)
      val backwardPath = analyzeBackwardPath(elementGroups)
      OldStructure(
        forwardPath,
        backwardPath,
        Seq.empty
      )
    }
  }

  private def analyzeForwardPath(elementGroups: Seq[StructureElementGroup]): Option[OldStructurePath] = {
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
                OldStructurePathElement(element, reversed = false)
              }
              Some(
                OldStructurePath(
                  firstElement.forwardStartNodeId,
                  lastElement.forwardEndNodeId,
                  structurePathElements
                )
              )
          }
      }
    }
  }

  private def analyzeBackwardPath(elementGroups: Seq[StructureElementGroup]): Option[OldStructurePath] = {
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
          OldStructurePathElement(element, reversed)
        }

        val hasNoGaps = structurePathElements.size == 1 || allPathsConnected(structurePathElements)
        if (hasNoGaps) {
          val startNodeId = structurePathElements.head.nodeIds.head
          val endNodeId = structurePathElements.last.nodeIds.last
          Some(
            OldStructurePath(
              startNodeId,
              endNodeId,
              structurePathElements
            )
          )
        }
        else {
          None
        }
      }
    }
  }

  private def allPathsConnected(structurePathElements: Seq[OldStructurePathElement]): Boolean = {
    structurePathElements.sliding(2).forall { case Seq(a, b) =>
      a.endNodeId == b.startNodeId
    }
  }
}
