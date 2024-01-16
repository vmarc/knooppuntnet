package kpn.server.analyzer.engine.monitor

import kpn.api.custom.Relation

class MonitorRouteStructureAnalyzer {
  private val memberAnalyzer = new MonitorRouteMemberAnalyzer

  def analyze(relation: Relation): Unit = {
    memberAnalyzer.analyzeRoute(relation).map { monitorRouteMemberGroup =>
      val elementGroups = MonitorRouteElementAnalyzer.analyze(monitorRouteMemberGroup.members)
      val forwardPathOption = analyzeForwardPath(elementGroups)
      val backwardPathOption = analyzeBackwardPath(elementGroups)

      println("structure")
      forwardPathOption match {
        case None => println("  no forward path")
        case Some(path) =>
          println("  forward path")
          path.elements.foreach { element =>
            element.fragments.foreach { fragment =>
              println(s"    fragment nodeIds=${fragment.nodeIds}")
            }
          }
      }

      backwardPathOption match {
        case None => println("  no backward path")
        case Some(path) =>
          println("  backward path")
          path.elements.foreach { element =>
            element.fragments.foreach { fragment =>
              println(s"    fragment nodeIds=${fragment.nodeIds}")
            }
          }
      }
    }
  }

  private def analyzeForwardPath(elementGroups: Seq[MonitorRouteElementGroup]): Option[RoutePath] = {
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
                RoutePath(
                  firstElement.startNodeId,
                  lastElement.endNodeId,
                  elements
                )
              )
          }
      }
    }
  }

  private def analyzeBackwardPath(elementGroups: Seq[MonitorRouteElementGroup]): Option[RoutePath] = {
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
                RoutePath(
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
