package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.NodeUtil
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteNameMissing

/**
  * Analyzes the route name.
  *
  * The route name is found in the route relation 'note' tag.
  *
  * Any characters after ";" in the 'note' tag are ignored.
  *
  * The regular expected pattern is two times 2 digits separated with a dash. The first two digits
  * represent the start node, the second 2 digits the end node. The start node is expected to have
  * a lower value than the end node.
  *
  * If the start node has a higher value than the end node, the route name considered to be 'reversed'.
  */
object RouteNameAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNameAnalyzer(context).analyze
  }
}

class RouteNameAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val noteRouteNameAnalysis = analyze(context.loadedRoute.relation.tags("note"))
    val routeNameAnalysis = if (noteRouteNameAnalysis.startNodeName.nonEmpty || noteRouteNameAnalysis.endNodeName.nonEmpty) {
      noteRouteNameAnalysis
    }
    else {
      val refRouteNameAnalysis = analyze(context.loadedRoute.relation.tags("ref"))
      if (refRouteNameAnalysis.startNodeName.nonEmpty || refRouteNameAnalysis.endNodeName.nonEmpty) {
        refRouteNameAnalysis
      }
      else {
        noteRouteNameAnalysis
      }
    }
    context.copy(routeNameAnalysis = Some(routeNameAnalysis)).withFact(routeNameAnalysis.name.isEmpty, RouteNameMissing)
  }

  private def analyze(nameOption: Option[String]): RouteNameAnalysis = {

    nameOption match {

      case None => RouteNameAnalysis()

      case Some(name) =>

        val routeName = cleanRouteName(name)

        val namePattern = """(.*)-(.*)""".r

        val nodeNames: (Option[String], Option[String]) = namePattern.findFirstMatchIn(routeName) match {
          case None => (None, None)
          case Some(m) =>
            if (m.group(1).contains("-") || m.group(2).contains("-")) {
              (None, None)
            }
            else if (m.group(1).endsWith("-")) {
              (Some(m.group(2)), None)
            }
            else {
              val n1 = if (m.group(1).nonEmpty) Some(m.group(1)) else None
              val n2 = if (m.group(2).nonEmpty) Some(m.group(2)) else None
              (n1, n2)
            }
        }

        val number1 = nodeNames._1 match {
          case Some(nodeName) => NodeUtil.toNumber(nodeName)
          case _ => None
        }

        val number2 = nodeNames._2 match {
          case Some(nodeName) => NodeUtil.toNumber(nodeName)
          case _ => None
        }

        val reversed: Boolean = if (number1.isDefined && number2.isDefined) {
          // only compare if both node names consist of digits only
          number1.get > number2.get
        }
        else {
          false
        }

        val startNodeName: Option[String] = if (reversed) nodeNames._2 else nodeNames._1

        val endNodeName: Option[String] = if (reversed) nodeNames._1 else nodeNames._2

        RouteNameAnalysis(Some(routeName), startNodeName, endNodeName, reversed)
    }
  }

  private def cleanRouteName(name: String): String = {
    if (name.contains(";")) {
      name.split(";").head
    }
    else {
      name
    }
  }
}
