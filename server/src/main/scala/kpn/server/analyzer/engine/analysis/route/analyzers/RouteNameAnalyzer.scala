package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteNameMissing
import kpn.api.custom.Tags
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

/**
 * Analyzes the route name.
 *
 * The route name can be found (in following order of precedence):
 * 1) in the 'ref' tag
 * 2) in the 'name' tag
 * 3) in the 'note' tag (any characters after ";" in the 'note' tag are ignored)
 * 4) in the 'from' and 'to' tags
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
      case Some(ref) => Some(toRouteNameAnalysis(ref))
      case None =>
        tags("name") match {
          case Some(name) => Some(toRouteNameAnalysis(name))
          case None =>
            tags("note") match {
              case Some(note) => Some(toRouteNameAnalysis(noteWithoutComment(note)))
              case None => fromToTagsAnalysis(tags)
            }
        }
    }

    context.copy(routeNameAnalysis = routeNameAnalysis).withFact(routeNameAnalysis.isEmpty, RouteNameMissing)
  }

  private def fromToTagsAnalysis(tags: Tags): Option[RouteNameAnalysis] = {
    tags("from") match {
      case None =>
        tags("to") match {
          case None => None
          case Some(name2) =>
            Some(
              RouteNameAnalysis(
                Some("-" + name2),
                None,
                Some(name2)
              )
            )
        }

      case Some(name1) =>
        tags("to") match {
          case None =>
            Some(
              RouteNameAnalysis(
                Some(name1 + "-"),
                Some(name1),
                None
              )
            )
          case Some(name2) =>
            Some(
              toRouteNameAnalysisFromNodeNames(name1 + "-" + name2, name1, name2)
            )
        }
    }
  }

  private def toRouteNameAnalysis(routeName: String): RouteNameAnalysis = {
    if (routeName.contains("-")) {
      val nameParts = routeName.split("-")
      val startNodeName = nameParts.head
      val endNodeName = nameParts(1)
      toRouteNameAnalysisFromNodeNames(routeName, startNodeName, endNodeName)
    }
    else {
      RouteNameAnalysis(
        Some(routeName),
        None,
        None
      )
    }
  }

  private def toRouteNameAnalysisFromNodeNames(routeName: String, startNodeName: String, endNodeName: String): RouteNameAnalysis = {
    if (Util.isDigits(startNodeName) && Util.isDigits(endNodeName)) {
      if (startNodeName.toInt > endNodeName.toInt) {
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

}
