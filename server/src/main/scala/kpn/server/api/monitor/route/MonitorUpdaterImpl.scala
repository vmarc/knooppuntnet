package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

import scala.xml.Elem

@Component
class MonitorUpdaterImpl(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateRoute: MonitorUpdateRoute,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorUpdateReference: MonitorUpdateReference,
  monitorUpdateAnalyzer: MonitorUpdateAnalyzer,
  saver: MonitorUpdateSaver,
  xxx: XxxImpl,
) extends MonitorUpdater {

  def add(
    user: String,
    groupName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {

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

  def update(
    user: String,
    groupName: String,
    routeName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {

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

  override def upload(
    user: String,
    route: MonitorRoute,
    relationId: Long,
    referenceDay: Day,
    filename: String,
    xml: Elem
  ): MonitorRouteSaveResult = {

    val now = Time.now
    val geometryCollection = new MonitorRouteGpxReader().read(xml)
    val bounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
    val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

    // TODO should delete already existing reference here?

    val distance = Math.round(geometryCollection.getLength)
    val segmentCount = geometryCollection.getNumGeometries

    val reference = MonitorRouteReference(
      ObjectId(),
      routeId = route._id,
      relationId = Some(relationId),
      created = now,
      user = user,
      bounds = bounds,
      referenceType = "gpx", // "osm" | "gpx"
      referenceDay = referenceDay,
      distance = distance,
      segmentCount = segmentCount,
      filename = Some(filename),
      geometry = geoJson
    )

    monitorRouteRepository.saveRouteReference(reference)


    route.referenceType match {
      case "gpx" =>
        val gpxDistance = {
          val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
          Math.round(toMeters(referenceLineStrings.map(_.getLength).sum))
        }

        val updatedRoute = route.copy(
          referenceFilename = reference.filename,
          referenceDistance = gpxDistance,
        )
        monitorRouteRepository.saveRoute(updatedRoute)

        MonitorRouteSaveResult()

      case "multi-gpx" =>

        // TODO perform analysis of the sub-relation !!

        val state = xxx.analyzeReference(route._id, reference)

//        var context = MonitorUpdateContext(group)
//        context = monitorUpdateRoute.update(context, ObjectId(), user, properties)
//        context = monitorUpdateStructure.update(context)
//        context = monitorUpdateReference.update(context)
//        context = monitorUpdateAnalyzer.analyze(context)
//        context = saver.save(context)
//        context.saveResult


        val referenceDistance = monitorRouteRepository.superRouteSummary(route._id) match {
          case Some(distance) => distance
          case None => 0
        }

        val updatedRoute = route.copy(
          referenceDistance = referenceDistance,
        )
        monitorRouteRepository.saveRoute(updatedRoute)

        MonitorRouteSaveResult()


      case _ =>
        MonitorRouteSaveResult()

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
