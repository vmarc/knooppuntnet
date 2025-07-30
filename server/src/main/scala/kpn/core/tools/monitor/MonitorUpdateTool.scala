package kpn.core.tools.monitor

import kpn.core.tools.monitor.MonitorUpdateTool.log
import kpn.core.tools.monitor.support.MonitorTileTool
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.index.IndexConfiguration
import kpn.database.index.Indexer
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import org.apache.commons.io.FileUtils

import java.io.File

object MonitorUpdateTool extends Tool[MonitorUpdateToolOptions] {
  private val log = Log(classOf[MonitorUpdateTool])

  override def options: Options[MonitorUpdateToolOptions] = MonitorUpdateToolOptions

  override def execute(options: MonitorUpdateToolOptions): Unit = {
    log.infoElapsed {
      Mongo.executeIn(options.databaseName) { database =>

        removeStateDocsWhileUpdateLogicNotReadyYet(database)

        val tool = new MonitorUpdateTool(new MonitorUpdateToolConfiguration(database))
        // tool.update()
        tool.testUpdate("eu-icn-EV", "EV1")

        regenerateTiles(database)
      }
      ("update completed", ())
    }
  }

  private def regenerateTiles(database: Database): Unit = {
    FileUtils.cleanDirectory(new File("/Users/marc/kpn/tiles/monitor"))
    val tileTool = new MonitorTileTool(database)
    tileTool.generate()
  }

  private def removeStateDocsWhileUpdateLogicNotReadyYet(database: Database): Unit = {
    database.monitorStates.drop()
    database.monitorStateTiles.drop()
    val indexer = new Indexer(database)
    new IndexConfiguration(database).stateIndexes.foreach(indexer.createIndex)
  }
}

class MonitorUpdateTool(configuration: MonitorUpdateToolConfiguration) {

  def testUpdate(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => log.error(s"group not found: $groupName")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => log.error(s"route not found: $groupName, $routeName")
          case Some(route) => updateAnalysis(group, route)
        }
    }
  }

  def update(): Unit = {
    val groups = configuration.monitorGroupRepository.groups().sortBy(_.name)
    groups.foreach { group =>
      configuration.monitorGroupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        updateAnalysis(group, route)
      }
    }
  }

  private def updateAnalysis(group: MonitorGroup, route: MonitorRoute): Unit = {
    Log.context(s"${group.name}, ${route.name}") {
      log.infoElapsed {
        try {
          configuration.monitorUpdateAnalysis.updateAnalysis(route)
        }
        catch {
          case e: Exception =>
            log.error(e.getMessage, e)
            throw new RuntimeException(s"Error analyzing route ${group.name}/${route.name}", e)
        }
        ("analysis completed", ())
      }
    }
  }
}
