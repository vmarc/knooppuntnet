package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.Redesign
import kpn.server.analyzer.engine.analysis.route.RouteSegmentData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.Structure
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementGroup
import kpn.server.analyzer.engine.analysis.route.structure.StructurePath

object RouteContextAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteContextAnalyzer(context).analyze
    context
  }
}

class RouteContextAnalyzer(context: RouteAnalysisContext) {

  def analyze: Unit = {
    if (Redesign.enableDebugPrinting) {
      printReferenceStructure()
      context.segmentAnalysis.foreach { segmentAnalysis =>
        printElementGroups(segmentAnalysis.elementGroups)
        printRouteSegments(segmentAnalysis.routeSegments)
        printStructure(segmentAnalysis.structure)
      }
      printFacts()
    }
  }

  private def printReferenceStructure(): Unit = {
    context.referenceStructure match {
      case Some(referenceStructure) =>
        println("ReferenceStructure")
        referenceStructure.reportStrings.foreach(s => println(s"  $s"))
        println("---")
    }
  }

  private def printElementGroups(groups: Seq[StructureElementGroup]): Unit = {
    println("elementGroups")
    groups.foreach { group =>
      println("  StructureElementGroup")
      group.elements.foreach { element =>
        println("    StructureElement")
        element.fragments.foreach { fragment =>
          println(s"""      StructureFragment way=${fragment.way.id} bidirectional=${fragment.bidirectional}, nodeIds=${fragment.nodeIds.mkString(",")}""")
        }
      }
    }
  }

  private def printRouteSegments(routeSegments: Seq[RouteSegmentData]): Unit = {
    println("routeSegments")
    routeSegments.foreach { routeSegment =>
      println(s"  id=${routeSegment.id}, id=${routeSegment.segment.id}, startNodeId=${routeSegment.segment.startNodeId}, endNodeId=${routeSegment.segment.endNodeId}")
    }
  }

  private def printStructure(structure: Structure): Unit = {
    println(s"Structure")
    structure.forwardPath.foreach { path =>
      printStructurePath("forwardPath", path)
    }
    structure.backwardPath.foreach { path =>
      printStructurePath("backwardPath", path)
    }
    structure.otherPaths.foreach { path =>
      printStructurePath("otherPath", path)
    }
  }

  private def printStructurePath(name: String, path: StructurePath): Unit = {
    println(s"  StructurePath $name startNodeId=${path.startNodeId}, endNodeId=${path.endNodeId}")
    path.elements.foreach { pathElement =>
      println(s"    StructurePathElement reversed=${pathElement.reversed}")
      pathElement.element.fragments.foreach { fragment =>
        println(s"""      way=${fragment.way.id} bidirectional=${fragment.bidirectional}, nodeIds=${fragment.nodeIds.mkString(",")}""")
      }
    }
  }

  private def printFacts(): Unit = {
    if (context.facts != context.oldFacts) {
      println("FACTS DO NOT MATCH")
      println(s"  oldFacts=${context.oldFacts.map(_.name).mkString(", ")}")
      println(s"  newFacts=${context.facts.map(_.name).mkString(", ")}")
    }
    else {
      println(s"facts=${context.facts.map(_.name).mkString(", ")}")
    }
  }
}
