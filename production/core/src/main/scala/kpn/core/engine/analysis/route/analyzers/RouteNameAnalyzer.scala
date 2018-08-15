package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.NodeUtil
import kpn.core.engine.analysis.route.RouteNameAnalysis
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.shared.Fact.RouteNameMissing

object RouteNameAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNameAnalyzer(context).analyze
  }
}

class RouteNameAnalyzer(context: RouteAnalysisContext) {

  private val ignoredSuffixes = Seq(
    "; canoe",
    ";canoe",
    "; motorboat",
    ";motorboat",
    "; (incompleet)",
    ";(incompleet)",
    " (incompleet)",
    "(incompleet)"
  )

  def analyze: RouteAnalysisContext = {
    val routeNameAnalysis = analyze(context.loadedRoute.relation.tags("note"))
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
    ignoredSuffixes.find(suffix => name.endsWith(suffix)) match {
      case Some(s) => name.dropRight(s.length)
      case None => name
    }
  }
}
