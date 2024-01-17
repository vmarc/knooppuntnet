package kpn.server.analyzer.engine.monitor.structure

import kpn.api.custom.Relation

class StructureAnalyzer(traceEnabled: Boolean = false) {
  private val memberAnalyzer = new StructureMemberAnalyzer()

  def analyze(relation: Relation): Unit = {
    memberAnalyzer.analyzeRoute(relation).map { monitorRouteMemberGroup =>
      val elementGroups = StructureElementAnalyzer.analyze(monitorRouteMemberGroup.members)
      val forwardPathOption = analyzeForwardPath(elementGroups)
      val backwardPathOption = analyzeBackwardPath(elementGroups)

      if (traceEnabled) println("structure")
      forwardPathOption match {
        case None => if (traceEnabled) println("  no forward path")
        case Some(path) =>
          if (traceEnabled) println("  forward path")
          path.elements.foreach { element =>
            element.fragments.foreach { fragment =>
              if (traceEnabled) println(s"    fragment nodeIds=${fragment.nodeIds}")
            }
          }
      }

      backwardPathOption match {
        case None => if (traceEnabled) println("  no backward path")
        case Some(path) =>
          if (traceEnabled) println("  backward path")
          path.elements.foreach { element =>
            element.fragments.foreach { fragment =>
              if (traceEnabled) println(s"    fragment nodeIds=${fragment.nodeIds}")
            }
          }
      }
    }
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
      val elements = lastElementGroup.elements.filter { element =>
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
                  lastElement.endNodeId,
                  firstElement.startNodeId,
                  elements
                )
              )
          }
      }
    }
  }
}
