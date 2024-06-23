package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.analysis.RouteMember
import kpn.server.analyzer.engine.analysis.route.RouteSegmentData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.Structure
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementGroup
import kpn.server.analyzer.engine.analysis.route.structure.StructurePath

import java.io.File
import java.io.PrintWriter

object RouteAnalysisContextReport {
  def report(context: RouteAnalysisContext): Unit = {
    new RouteAnalysisContextReport(context).report()
  }
}

class RouteAnalysisContextReport(context: RouteAnalysisContext) {

  private val out = {
    val dir = "/Users/marc/tmp/node-network-analysis"
    new File(dir).mkdirs
    new PrintWriter(s"$dir/${context.relation.id}.html")
  }

  def report(): Unit = {
    reportStart()
    printSummary()
    printFacts()
    printReferenceStructure()
    reportRouteMembers()
    context.segmentAnalysis.foreach { segmentAnalysis =>
      printElementGroups(segmentAnalysis.elementGroups)
      printRouteSegments(segmentAnalysis.routeSegments)
      printStructure(segmentAnalysis.structure)
    }
    reportEnd()
  }

  private def reportStart(): Unit = {
    out.println("<html>")

    out.println("<head>")
    out.println("""  <meta http-equiv="content-type" content="text/html; charset=UTF-8">""")
    out.println("""  <link href="styles.css" rel="stylesheet" type="text/css">""")
    out.println(s"  <title>${context.relation.id} route</title>")
    out.println("  <style>")
    out.println("  body {")
    out.println("    font-family: monospace;")
    out.println("  }")
    out.println("  table, pre {")
    out.println("    margin: 30;")
    out.println("  }")
    out.println("  table, th, td {")
    out.println("    border: 1px solid gray;")
    out.println("    border-collapse: collapse;")
    out.println("  }")
    out.println("  td {")
    out.println("    padding: .5em;")
    out.println("  }")
    out.println("  ")
    out.println("  </style>")
    out.println("</head>")

    out.println("<body>")
  }

  private def reportEnd(): Unit = {
    out.println("</body>")
    out.println("</html>")
    out.close()
  }

  private def printSummary(): Unit = {
    out.println("<table>")
    row("superRoute", yes(context.superRoute))
    row("nodeNetwork", yes(context.nodeNetwork))
    row("proposed", yes(context.proposed))
    row("networkType", s"""${context.networkType.map(_.name).getOrElse("")}""")
    row("scopedNetworkType", s"""${context.scopedNetworkTypeOption.map(_.key).getOrElse("")}""")
    row("country", s"""${context.country.map(_.domain).getOrElse("")}""")
    out.println("")
    out.println("")
    out.println("")
    out.println("")
    out.println("</table>")
  }

  private def row(key: String, value: String): Unit = {
    out.println(s"<tr><td>$key</td><td>$value</td></tr>")
  }

  private def reportRouteMembers(): Unit = {
    out.println("""<table>""")
    reportRouteMembersHeader()
    context.routeMembers.get.foreach { routeMember =>
      reportRouteMember(routeMember)
    }
    out.println("</table>")
  }

  private def reportRouteMembersHeader(): Unit = {
    out.println("<tr>")
    out.println("<td></td>")
    out.println("<td>id</td>")
    out.println("<td>name</td>")
    out.println("<td>role</td>")
    out.println("<td>accessible</td>")
    out.println("</tr>")
  }

  private def reportRouteMember(routeMember: RouteMember): Unit = {
    out.println("<tr>")
    out.println("""<td style="padding:0">""")
    out.println(s"""<img src="images/${routeMember.linkName}.png"/>""")
    out.println("</td>")
    out.println("<td>")
    out.println(s"""<a href="https://www.openstreetmap.org/${routeMember.memberType}/${routeMember.id}">${routeMember.id}</a>""")
    out.println("</td>")
    out.println("<td>")
    out.println(routeMember.name)
    out.println("</td>")
    out.println("<td>")
    out.println(routeMember.role.getOrElse(""))
    out.println("</td>")
    out.println("<td>")
    out.println(if (routeMember.accessible) "" else "no")
    out.println("</td>")
    out.println("</tr>")
  }

  private def printReferenceStructure(): Unit = {
    context.referenceStructure match {
      case Some(referenceStructure) =>
        out.println("<pre>")
        out.println("ReferenceStructure")
        referenceStructure.reportStrings.foreach(s => out.println(s"  $s"))
        out.println("---")
        out.println("</pre>")
    }
  }

  private def printElementGroups(groups: Seq[StructureElementGroup]): Unit = {
    out.println("<pre>")
    out.println("elementGroups")
    groups.foreach { group =>
      out.println("  StructureElementGroup")
      group.elements.foreach { element =>
        out.println("    StructureElement")
        element.fragments.foreach { fragment =>
          out.println(s"""      StructureFragment way=${fragment.way.id} bidirectional=${fragment.bidirectional}, nodeIds=${fragment.nodeIds.mkString(",")}""")
        }
      }
    }
    out.println("</pre>")
  }

  private def printRouteSegments(routeSegments: Seq[RouteSegmentData]): Unit = {
    out.println("<pre>")
    out.println("routeSegments")
    routeSegments.foreach { routeSegment =>
      out.println(s"  id=${routeSegment.id}, id=${routeSegment.segment.id}, startNodeId=${routeSegment.segment.startNodeId}, endNodeId=${routeSegment.segment.endNodeId}")
    }
    out.println("</pre>")
  }

  private def printStructure(structure: Structure): Unit = {
    out.println("<pre>")
    out.println(s"Structure")
    structure.forwardPath.foreach { path =>
      printStructurePath("forwardPath", path)
    }
    structure.backwardPath.foreach { path =>
      printStructurePath("backwardPath", path)
    }
    structure.otherPaths.foreach { path =>
      printStructurePath("otherPath", path)
    }
    out.println("</pre>")
  }

  private def printStructurePath(name: String, path: StructurePath): Unit = {
    out.println(s"  StructurePath $name startNodeId=${path.startNodeId}, endNodeId=${path.endNodeId}")
    path.elements.foreach { pathElement =>
      out.println(s"    StructurePathElement reversed=${pathElement.reversed}")
      pathElement.element.fragments.foreach { fragment =>
        out.println(s"""      way=${fragment.way.id} bidirectional=${fragment.bidirectional}, nodeIds=${fragment.nodeIds.mkString(",")}""")
      }
    }
  }

  private def printFacts(): Unit = {
    out.println("<pre>")
    if (context.facts != context.oldFacts) {
      out.println("FACTS DO NOT MATCH")
      out.println(s"  oldFacts=${context.oldFacts.map(_.name).mkString(", ")}")
      out.println(s"  newFacts=${context.facts.map(_.name).mkString(", ")}")
    }
    else {
      out.println(s"facts=${context.facts.map(_.name).mkString(", ")}")
    }
    out.println("</pre>")
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
