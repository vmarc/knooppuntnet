package kpn.core.tools.next.support

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.doc.RouteRelation
import kpn.core.tools.analysis.AnalysisStartConfiguration
import kpn.core.tools.analysis.AnalysisStartToolOptions
import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc

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

  private val timestamp = Timestamp(2025, 1, 1)
  private val log = Log(classOf[RouteAnalysisTool])

  def analyze(): Unit = {
    log.info("Fetching all node ids")
    val nodeIds = config.rawDataRepository.nodeIds(timestamp)
    analyzeBaseNodes(nodeIds)

    log.info("Fetching all network ids")
    val networkIds = config.rawDataRepository.networkIds(timestamp)
    analyzeBaseNetworks(networkIds)

    log.info("Fetching all route ids")
    val routeIds = config.rawDataRepository.routeIds(timestamp)
    log.info(s"found ${routeIds.size} routeIds")
    analyzeBaseRoutes(routeIds)

    analyzeNodes(nodeIds)

    analyzeRoutesMain(routeIds)

    log.info(s"Done")
  }

  private def analyzeBaseNodes(nodeIds: Seq[Long]): Unit = {
    val batchSize = 500
    Log.context("base-nodes") {
      val nodeCount = nodeIds.size
      log.info(s"Analyzing $nodeCount base nodes")
      log.infoElapsed {
        nodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.foreach { case (nodeIdsBatch, index) =>
          log.infoElapsed {
            val rawNodes = config.rawDataRepository.nodes(timestamp, nodeIdsBatch)
            rawNodes.foreach { rawNode =>
              config.baseNodeMainAnalyzer.analyze(rawNode) match {
                case None => log.error(s"Could not analyze node ${rawNode.id}")
                case Some(baseNodeDoc) =>
                  config.baseNodeRepository.saveBaseNode(baseNodeDoc)
              }
            }
            (s"Analyzed ${batchSize * (index + 1)}/$nodeCount nodes", ())
          }
        }
        (s"Analyzed $nodeCount nodes", ())
      }
    }
  }

  private def analyzeNodes(nodeIds: Seq[Long]): Unit = {
    Log.context("nodes") {
      val nodeCount = nodeIds.size
      log.info(s"Analyzing $nodeCount nodes")
      log.infoElapsed {
        nodeIds.zipWithIndex.foreach { case (nodeId, index) =>
          if (((index + 1) % 100) == 0) {
            log.info(s"${index + 1}/$nodeCount")
          }
          config.nodeRepository.baseNodeWithId(nodeId) match {
            case None => log.error(s"Could not find base node $nodeId")
            case Some(baseNodeDoc) =>
              config.nodeMainAnalyzer.analyze(baseNodeDoc) match {
                case None => log.error(s"Could not analyze node $nodeId")
                case Some(nodeDoc) =>
                  config.nodeRepository.save(nodeDoc)
              }
          }
        }
        (s"Analyzed $nodeCount nodes", ())
      }
    }
  }

  private def analyzeBaseNetworks(networkIds: Seq[Long]): Unit = {
    val batchSize = 25
    Log.context("base-networks") {
      log.info("Analyzing base networks")
      val networkCount = networkIds.size
      log.info(s"Analyzing $networkCount networks")
      log.infoElapsed {
        networkIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.foreach { case (networkIdsBatch, index) =>
          log.infoElapsed {
            val rawNetworks = config.rawDataRepository.networks(timestamp, networkIdsBatch)
            rawNetworks.foreach { rawNetwork =>
              config.baseNetworkMainAnalyzer.analyze(rawNetwork) match {
                case None =>
                case Some(baseNetworkDoc) =>
                  config.networkRepository.saveBaseNetwork(baseNetworkDoc)
              }
            }
            (s"Analyzed ${batchSize * (index + 1)}/$networkCount networks", ())
          }
        }
        (s"Analyzed $networkCount networks", ())
      }
    }
  }

  private def analyzeBaseRoutes(routeIds: Seq[Long]): Unit = {
    Log.context("base-routes") {
      val routeCount = routeIds.size
      log.info(s"analyzing $routeCount base routes")
      val context = Log.contextMessages
      log.infoElapsed {
        ThreadExecutor.execute(10, routeIds) { (index, count, routeId) =>
          Log.context(context) {
            Log.context(s"$index/$count $routeId") {
              log.infoElapsed {
                try {
                  config.rawDataRepository.route(timestamp, routeId) match {
                    case Some(rawRouteDoc) =>
                      analyzeBaseRoute(rawRouteDoc.relation, rawRouteDoc.structure)
                    case None =>
                      log.error(s"route $routeId not found in route-relations")
                  }
                } catch {
                  case e: Exception =>
                    log.error(s"Error analyzing detail route $routeId", e)
                }
                (s"Analyzed route $routeId", ())
              }
            }
          }
        }
        (s"Analyzed $routeCount routes", ())
      }
    }
  }

  private def analyzeRoutesMain(routeIds: Seq[Long]): Unit = {
    val routeIdsSize = routeIds.size
    routeIds.zipWithIndex.foreach { case (relationId, index) =>
      Log.context(s"${index + 1}/$routeIdsSize route=$relationId") {
        try {
          log.info("analyze main")
          config.baseRouteRepository.findById(relationId) match {
            case None => log.error(s"could not find route details")
            case Some(baseRouteDoc) =>
              config.routeMainAnalyzer.analyze(baseRouteDoc) match {
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

  private def analyzeBaseRoute(relation: Relation, hierarchy: Option[RouteRelation]): Unit = {
    config.baseRouteMainAnalyzer.analyze(relation, hierarchy) match {
      case None =>
      case Some(context) =>
        val baseRouteDoc = new BaseRouteDocBuilder(context).build()
        config.baseRouteRepository.save(baseRouteDoc)
        context.tileDatas.foreach { tileData =>
          val doc = RouteTileDoc(
            _id = s"${tileData.name}-${context.relation.id}",
            routeId = context.relation.id,
            routeName = context.routeNameAnalysis.name.getOrElse("no-name"), // TODO redesign tiles - can do better?
            routeTypes = context.routeTypes,
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
      // TODO saveRouteChange(routeAnalysis)
    }
  }
}
