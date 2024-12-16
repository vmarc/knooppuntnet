package kpn.core.tools.next.support

import kpn.api.custom.Relation
import kpn.core.doc.RouteRelation
import kpn.core.tools.analysis.AnalysisStartConfiguration
import kpn.core.tools.analysis.AnalysisStartToolOptions
import kpn.core.tools.next.support.RouteAnalysisTool.law9
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteDetailDocBuilder
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.analysis.route.structure.DependencySorter
import kpn.server.analyzer.engine.analysis.route.structure.RouteDependency

object RouteAnalysisTool {
  def main(args: Array[String]): Unit = {
    val configuration = new AnalysisStartConfiguration(AnalysisStartToolOptions("kpn-next"))
    new RouteAnalysisTool(configuration).analyze()
  }

  val essenOkRouteIds: Seq[Long] = Seq(
    13844575L, // 01-02
    3294187L, // 01-08
    13844576L, // 01-36
    3665081L, // 02-92
    13844574L, // 02-11
  )

  val law9: Seq[Long] = Seq(
    7973533L, // LAW-9 super route containing other super routes
    312993L, // Pieterpad deel 1 - Pieterburen-Vorden
    8831649L,
    8832176L,
    8832222L,
    8832221L,
    8832220L,
    8832392L,
    8832391L,
    8832709L,
    8832708L,
    8832707L,
    8832706L,
    8832705L,
    8832704L,
    156951L, // Pieterpad deel 2 - Vorden-Maastricht Pietersberg
    8834446L,
    8834445L,
    8835026L,
    8835025L,
    8835024L,
    8835656L,
    8835655L,
    8835654L,
    8835653L,
    8835652L,
    8835651L,
    8835650L,
    8835649L,
  )
}

class RouteAnalysisTool(config: AnalysisStartConfiguration) {

  private val log = Log(classOf[RouteAnalysisTool])

  def analyze(): Unit = {
    log.info("Fetching all route ids")
    //    val routeIds = config.nextRepository.allRouteIds()
    //    log.info(s"found ${routeIds.size} routeIds")
    //    analyzeRoutes(routeIds)
    //    analyzeRoutes(Seq(16068584L) ++ essenOkRouteIds) // 16068584 Wandelpad Calmeyn
    // analyzeRoutes(Seq(8312991L)) // GR145 Arras to Reims
    // analyzeRoutes(Seq(8618)) // ok route with start tenticle
    // analyzeRoutes(Seq(5491)) // ok route with 2 start tenticles
    // analyzeRoutes(essenOkRouteIds)
    analyzeRoutes(law9)
    // analyzeRoutes(Seq(13844575L))
    // analyzeRoutes(Seq(5880L)) // exception during structure analysis
    // analyzeRoutes(Seq(3952592)) // broken route
    // analyzeRoutes(Seq(3963819)) // route with roundabout
    log.info(s"Done")
  }

  private def analyzeRoutes(routeIds: Seq[Long]): Unit = {
    val dependencies = analyzeRouteDetails(routeIds)
    analyzeRoutesMain(dependencies)
  }

  private def analyzeRouteDetails(routeIds: Seq[Long]): Seq[RouteDependency] = {
    val routeIdsSize = routeIds.size
    log.info(s"analyzing $routeIdsSize route relation details")
    routeIds.zipWithIndex.flatMap { case (routeId, index) =>
      Log.context(s"${index + 1}/$routeIdsSize route=$routeId") {
        log.info("analyze detail")
        try {
          config.nextRepository.nextRouteRelation(routeId) match {
            case Some(nextRouteRelation) => analyzeRouteDetail(nextRouteRelation.relation, nextRouteRelation.structure)
            case None =>
              log.error(s"route $routeId not found in route-relations")
              Seq.empty
          }
        } catch {
          case e: Exception =>
            log.error(s"Error analyzing detail route $routeId", e)
            Seq.empty
        }
      }
    }
  }

  private def analyzeRoutesMain(dependencies: Seq[RouteDependency]): Unit = {
    log.info(s"analyzing main ${dependencies.size} route relations")
    val sortedRouteIds = DependencySorter.sort(dependencies)
    val sortedRouteIdsSize = sortedRouteIds.size
    sortedRouteIds.zipWithIndex.foreach { case (relationId, index) =>
      Log.context(s"${index + 1}/$sortedRouteIdsSize route=$relationId") {
        try {
          config.routeDetailRepository.findById(relationId) match {
            case None => log.error(s"could not find route details")
            case Some(routeDetailDoc) =>
              config.routeMainAnalyzer.analyze(routeDetailDoc) match {
                case Some(routeDoc) => config.routeRepository.saveRoute(routeDoc)
                case None =>
              }
          }
        } catch {
          case e: Exception =>
            log.error(s"Error analyzing main route $relationId", e)
            Seq.empty
        }
      }
    }
  }

  private def analyzeRouteDetail(relation: Relation, hierarchy: Option[RouteRelation]): Seq[RouteDependency] = {
    config.routeDetailMainAnalyzer.analyze(relation, hierarchy) match {
      case None => Seq.empty
      case Some(context) =>
        val routeDetailDoc = new RouteDetailDocBuilder(context).build()
        config.routeDetailRepository.save(routeDetailDoc)
        context.tileDatas.foreach { tileData =>
          val doc = RouteTileDoc(
            _id = s"${tileData.name}-${context.relation.id}",
            routeId = context.relation.id,
            routeName = context.routeNameAnalysis.name.getOrElse("no-name"), // TODO redesign tiles - can do better?
            networkTypes = context.networkTypes,
            z = tileData.z,
            x = tileData.x,
            y = tileData.y,
            layer = tileData.layer,
            scope = tileData.scope,
            survey = tileData.survey,
            error = tileData.error,
            segments = tileData.segments
          )
          config.routeRepository.saveRouteTile(doc)
        }

        routeDetailDoc.hierarchy match {
          case None =>
            config.routeMainAnalyzer.analyze(routeDetailDoc) match {
              case Some(routeDoc) =>
                config.routeRepository.saveRoute(routeDoc)
                Seq.empty
              case None => Seq.empty
            }
          case Some(hierarchyValue) =>
            // further analysis should go in second pass
            hierarchyValue.relations.map(subrelation =>
              RouteDependency(relation.id, subrelation.relationId)
            )
        }

      // TODO saveRouteChange(routeAnalysis)
    }
  }
}
