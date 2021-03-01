package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteNameMissing
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

/**
 * Analyzes the route name.
 *
 * The route name can be found (in following order of precedence):
 * 1) in the 'ref' tag.
 * 2) in the 'name' tag.
 * 3) in the 'note' tag (any characters after ";" in the 'note' tag are ignored)
 * 4) in the 'from' and 'to' tags.
 */
object RouteNameAnalyzer extends RouteAnalyzer {
  def  analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteNameAnalyzer(context).analyze
  }
}

class RouteNameAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val tags = context.loadedRoute.relation.tags
    val routeName: Option[String] = tags("ref") match {
      case Some(ref) => Some(ref)
      case None =>
        tags("name") match {
          case Some(name) => Some(name)
          case None =>
            tags("note") match {
              case Some(note) =>

                if (note.contains(";")) {
                  Some(note.split(";").head)
                }
                else {
                  Some(note)
                }

              case None =>

                tags("from") match {
                  case None => None
                  case Some(from) =>
                    tags("to") match {
                      case Some(to) => Some(from + "-" + to)
                      case None => None
                    }
                }
            }
        }
    }
    val routeNameAnalysis = RouteNameAnalysis(routeName)
    context.copy(routeNameAnalysis = Some(routeNameAnalysis)).withFact(routeNameAnalysis.name.isEmpty, RouteNameMissing)
  }

}
