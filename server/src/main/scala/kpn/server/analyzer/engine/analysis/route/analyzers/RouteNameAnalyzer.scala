package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteNameMissing
import kpn.api.custom.Tags
import kpn.core.util.NaturalSorting
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

/**
 * Analyzes the route name.
 *
 * The route name can be found (in following order of precedence):
 * <ol>
 * <li>in the 'ref' tag
 * <li>in the 'name' tag
 * <li>in the 'note' tag (any characters after ";" in the 'note' tag are ignored)
 * <li>in the 'from' and 'to' tags
 * </ol>
 */
object RouteNameAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNameAnalyzer(context).analyze
  }
}

class RouteNameAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val tags = context.relation.tags
    val routeNameAnalysis = tags("ref") match {
      case Some(ref) => analyzeRouteName(ref)
      case None =>
        tags("name") match {
          case Some(name) =>
            val routeNameAnalysis = analyzeRouteName(name)
            if (routeNameAnalysis.hasStandardNodeNames) {
              routeNameAnalysis
            }
            else {
              // verify is name in note tag is better, and if so: use that instead
              tags("note") match {
                case None => routeNameAnalysis
                case Some(note) =>
                  val noteRouteNameAnalysis = analyzeRouteName(note)
                  if (noteRouteNameAnalysis.hasStandardNodeNames) {
                    noteRouteNameAnalysis
                  }
                  else {
                    routeNameAnalysis
                  }
              }
            }

          case None =>
            tags("note") match {
              case Some(note) => analyzeRouteName(note)
              case None => analyzeToFromTags(tags)
            }
        }
    }
    context.copy(routeNameAnalysis = Some(routeNameAnalysis)).withFact(routeNameAnalysis.name.isEmpty, RouteNameMissing)
  }

  private def analyzeRouteName(fullRouteName: String): RouteNameAnalysis = {
    val routeName = withoutComment(fullRouteName)
    if (routeName.contains("-")) {
      analyzeRouteNameNodes(routeName)
    }
    else {
      RouteNameAnalysis(
        Some(routeName),
        None,
        None
      )
    }
  }

  private def analyzeRouteNameNodes(routeName: String): RouteNameAnalysis = {

    val (startNodeNameOption, endNodeNameOption) = splitRouteName(routeName)

    val startNodeName = startNodeNameOption.getOrElse("")
    val endNodeName = endNodeNameOption.getOrElse("")

    val normalizedRouteName = if (useDashSpaces(routeName, startNodeName, endNodeName)) {
      s"$startNodeName - $endNodeName"
    }
    else {
      s"$startNodeName-$endNodeName"
    }

    if (startNodeName.nonEmpty && endNodeNameOption.nonEmpty) {
      toRouteNameAnalysisFromNodeNames(normalizedRouteName, startNodeName, endNodeName)
    }
    else {
      RouteNameAnalysis(
        Some(normalizedRouteName),
        startNodeNameOption,
        endNodeNameOption
      )
    }
  }

  private def splitRouteName(routeName: String): (Option[String], Option[String]) = {
    val splitAt = if (routeName.count(_ == '-') > 1) " - " else "-"
    val nameParts = routeName.split(splitAt)
    if (nameParts.isEmpty) {
      (None, None)
    }
    else {
      val firstPart = nameParts.head.trim
      val startNodeNameOption = if (firstPart.nonEmpty) Some(NodeUtil.normalize(firstPart)) else None
      val endNodeNameOption = if (nameParts.size > 1) {
        val secondPart = nameParts(1).trim
        if (secondPart.nonEmpty) Some(NodeUtil.normalize(secondPart)) else None
      }
      else {
        None
      }
      (startNodeNameOption, endNodeNameOption)
    }
  }

  private def analyzeToFromTags(tags: Tags): RouteNameAnalysis = {
    tags("from") match {
      case None =>
        tags("to") match {
          case None => RouteNameAnalysis()
          case Some(toNodeName) =>
            val to = NodeUtil.normalize(toNodeName)
            RouteNameAnalysis(
              Some("-" + to),
              None,
              Some(to)
            )
        }

      case Some(fromNodeName) =>
        tags("to") match {
          case None =>
            val from = NodeUtil.normalize(fromNodeName)
            RouteNameAnalysis(
              Some(from + "-"),
              Some(from),
              None
            )
          case Some(toNodeName) =>
            val to = NodeUtil.normalize(toNodeName)
            val from = NodeUtil.normalize(fromNodeName)
            toRouteNameAnalysisFromNodeNames(from + "-" + to, from, to)
        }
    }
  }

  private def toRouteNameAnalysisFromNodeNames(routeName: String, startNodeName: String, endNodeName: String): RouteNameAnalysis = {
    if (isRouteReversed(startNodeName, endNodeName)) {
      RouteNameAnalysis(
        Some(routeName),
        Some(endNodeName),
        Some(startNodeName),
        reversed = true
      )
    }
    else {
      RouteNameAnalysis(
        Some(routeName),
        Some(startNodeName),
        Some(endNodeName)
      )
    }
  }

  private def withoutComment(tagValue: String): String = {
    if (tagValue.contains(";")) {
      tagValue.split(";").head
    }
    else {
      tagValue
    }
  }

  private def isRouteReversed(startNodeName: String, endNodeName: String): Boolean = {
    if (Util.isDigits(startNodeName) && Util.isDigits(endNodeName)) {
      if (startNodeName.toInt > endNodeName.toInt) {
        true
      }
      else {
        false
      }
    }
    else {
      if (Util.hasDigits(startNodeName) && Util.hasDigits(endNodeName)) {
        val originalOrder = Seq(startNodeName, endNodeName)
        val sortedOrder = NaturalSorting.sort(originalOrder)
        sortedOrder != originalOrder
      }
      else {
        false
      }
    }
  }

  private def useDashSpaces(routeName: String, startNodeName: String, endNodeName: String): Boolean = {
    routeName.count(_ == '-') > 1 || startNodeName.length > 3 || endNodeName.length > 3
  }
}
