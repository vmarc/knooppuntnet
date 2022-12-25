package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdaterImpl(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateRoute: MonitorUpdateRoute,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorUpdateReference: MonitorUpdateReference,
  monitorUpdateAnalyzer: MonitorUpdateAnalyzer,
  saver: MonitorUpdateSaver
) extends MonitorUpdater {

  def add(user: String, groupName: String, properties: MonitorRouteProperties): MonitorRouteSaveResult = {
    Log.context(Seq("add-route", s"group=$groupName", s"route=${properties.name}")) {
      val group = findGroup(groupName)
      assertNewRoute(group, properties.name)
      var context = MonitorUpdateContext(group)
      context = monitorUpdateRoute.update(context, ObjectId(), user, properties)
      context = monitorUpdateStructure.update(context)
      context = monitorUpdateReference.update(context)
      context = monitorUpdateAnalyzer.analyze(context)
      context = saver.save(context)
      context.saveResult
    }
  }

  def update(user: String, groupName: String, routeName: String, properties: MonitorRouteProperties): MonitorRouteSaveResult = {
    Log.context(Seq("route-update", s"group=$groupName", s"route=$routeName")) {
      val group = findGroup(groupName)
      val oldRoute = findRoute(group._id, routeName)
      var context = MonitorUpdateContext(group, oldRoute = Some(oldRoute))
      context = monitorUpdateRoute.update(context, oldRoute._id, user, properties)
      context = monitorUpdateStructure.update(context)
      context = monitorUpdateReference.update(context)
      context = monitorUpdateAnalyzer.analyze(context)
      context = saver.save(context)
      context.saveResult
    }
  }

  private def findGroup(groupName: String): MonitorGroup = {
    monitorGroupRepository.groupByName(groupName).getOrElse {
      throw new IllegalArgumentException(
        s"""${Log.contextString} Could not find group with name "$groupName""""
      )
    }
  }

  private def findRoute(groupId: ObjectId, routeName: String): MonitorRoute = {
    monitorRouteRepository.routeByName(groupId, routeName).getOrElse {
      throw new IllegalArgumentException(
        s"""${Log.contextString} Could not find route with name "$routeName" in group "${groupId.oid}""""
      )
    }
  }

  private def assertNewRoute(group: MonitorGroup, routeName: String): Unit = {
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None =>
      case Some(route) =>
        throw new IllegalArgumentException(
          s"""${Log.contextString} Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }
}
