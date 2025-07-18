package kpn.core.tools.monitor

import kpn.core.tools.monitor.MonitorUpdateTool.log
import kpn.core.util.Log
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute

object MonitorUpdateTool extends Tool[MonitorUpdateToolOptions] {
  private val log = Log(classOf[MonitorUpdateTool])

  override def options: Options[MonitorUpdateToolOptions] = MonitorUpdateToolOptions

  override def execute(options: MonitorUpdateToolOptions): Unit = {
    log.infoElapsed {
      Mongo.executeIn(options.databaseName) { database =>
        val tool = new MonitorUpdateTool(new MonitorUpdateToolConfiguration(database))
        tool.update()
        // tool.testUpdate("BE-GRV", "p12")
      }
      ("update completed", ())
    }
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
