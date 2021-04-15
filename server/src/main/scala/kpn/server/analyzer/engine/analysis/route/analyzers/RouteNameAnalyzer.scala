package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteNameMissing
import kpn.api.custom.Tags
import kpn.core.util.NaturalSorting
import kpn.core.util.Util
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
    val tags = context.loadedRoute.relation.tags
    val routeNameAnalysis = tags("ref") match {
      case Some(ref) => Some(analyzeName(ref))
      case None =>
        tags("name") match {
          case Some(name) => Some(analyzeName(name))
          case None =>
            tags("note") match {
              case Some(note) => Some(analyzeName(noteWithoutComment(note)))
              case None => analyzeToFromTags(tags)
            }
        }
    }
    context.copy(routeNameAnalysis = routeNameAnalysis).withFact(routeNameAnalysis.isEmpty, RouteNameMissing)
  }

  private def analyzeName(routeName: String): RouteNameAnalysis = {
    if (routeName.contains("-")) {
      val nameParts = routeName.split("-")
      val firstPart = nameParts.head.trim
      val startNodeNameOption = if (firstPart.nonEmpty) Some(NodeNameAnalyzer.normalize(firstPart)) else None
      val endNodeNameOption = if (nameParts.size > 1) {
        val secondPart = nameParts(1).trim
        if (secondPart.nonEmpty) Some(NodeNameAnalyzer.normalize(secondPart)) else None
      }
      else {
        None
      }

      val startNodeName = startNodeNameOption.getOrElse("")
      val endNodeName = endNodeNameOption.getOrElse("")

      val normalizedRouteName = s"$startNodeName-$endNodeName"

      if (startNodeNameOption.nonEmpty && endNodeNameOption.nonEmpty) {
        toRouteNameAnalysisFromNodeNames(normalizedRouteName, startNodeNameOption.get, endNodeNameOption.get)
      }
      else {
        RouteNameAnalysis(
          Some(normalizedRouteName),
          startNodeNameOption,
          endNodeNameOption
        )
      }
    }
    else {
      RouteNameAnalysis(
        Some(routeName),
        None,
        None
      )
    }
  }

  private def analyzeToFromTags(tags: Tags): Option[RouteNameAnalysis] = {
    tags("from") match {
      case None =>
        tags("to") match {
          case None => None
          case Some(toNodeName) =>
            val to = NodeNameAnalyzer.normalize(toNodeName)
            Some(
              RouteNameAnalysis(
                Some("-" + to),
                None,
                Some(to)
              )
            )
        }

      case Some(fromNodeName) =>
        tags("to") match {
          case None =>
            val from = NodeNameAnalyzer.normalize(fromNodeName)
            Some(
              RouteNameAnalysis(
                Some(from + "-"),
                Some(from),
                None
              )
            )
          case Some(toNodeName) =>
            val to = NodeNameAnalyzer.normalize(toNodeName)
            val from = NodeNameAnalyzer.normalize(fromNodeName)
            Some(
              toRouteNameAnalysisFromNodeNames(from + "-" + to, from, to)
            )
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

  private def noteWithoutComment(note: String): String = {
    if (note.contains(";")) {
      note.split(";").head
    }
    else {
      note
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
      val originalOrder = Seq(startNodeName, endNodeName)
      val sortedOrder = NaturalSorting.sort(originalOrder)
      sortedOrder != originalOrder
    }
  }
}
