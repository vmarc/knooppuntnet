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
  monitorRouteRelationAnalyzer: MonitorRouteRelationAnalyzer,
) extends MonitorUpdater {

  private val log = Log(classOf[MonitorUpdaterImpl])

  def add(
    user: String,
    groupName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {

    Log.context(Seq("route-add", s"group=$groupName", s"route=${properties.name}")) {
      var context = MonitorUpdateContext(user)
      context = findGroup(context, groupName)
      if (!context.abort) {
        context = assertNewRoute(context, properties.name)
        context = context.copy(
          referenceType = Some(properties.referenceType)
        )
      }
      if (!context.abort) {
        context = monitorUpdateRoute.update(context, user, properties)
      }
      if (!context.abort) {
        context = monitorUpdateStructure.update(context)
      }
      if (!context.abort) {
        context = monitorUpdateReference.update(context)
      }
      if (!context.abort) {
        context = monitorUpdateAnalyzer.analyze(context)
      }
      if (!context.abort) {
        context = saver.save(context)
      }
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

      var context = MonitorUpdateContext(user)
      context = findGroup(context, groupName)
      if (!context.abort) {
        context = findRoute(context, routeName)
        context = context.copy(
          referenceType = Some(properties.referenceType)
        )
      }

      if (!context.abort) {
        context = monitorUpdateRoute.update(context, user, properties)
      }
      if (!context.abort) {
        context = monitorUpdateStructure.update(context)
      }
      if (!context.abort) {
        context = monitorUpdateReference.update(context)
      }
      if (!context.abort) {
        context = monitorUpdateAnalyzer.analyze(context)
      }
      if (!context.abort) {
        context = saver.save(context)
      }
      context.saveResult
    }
  }

  override def upload(
    user: String,
    groupName: String,
    routeName: String,
    relationId: Long,
    referenceDay: Day,
    filename: String,
    xml: Elem
  ): MonitorRouteSaveResult = {

    Log.context(Seq("route-upload", s"group=$groupName", s"route=$routeName")) {
      var context = MonitorUpdateContext(user)
      context = findGroup(context, groupName)
      if (!context.abort) {
        context = findRoute(context, routeName)
      }
      if (!context.abort) {
        context = context.copy(
          referenceType = Some(context.oldRoute.get.referenceType)
        )
      }

      if (!context.abort) {

        val now = Time.now
        val geometryCollection = new MonitorRouteGpxReader().read(xml)
        val bounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
        val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

        // TODO should delete already existing reference here?

        val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
        val distance = Math.round(toMeters(referenceLineStrings.map(_.getLength).sum))

        val segmentCount = geometryCollection.getNumGeometries

        val reference = MonitorRouteReference(
          ObjectId(),
          routeId = context.routeId,
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

        context = context.copy(
          newReferences = context.newReferences :+ reference
        )

        context.referenceType match {
          case Some("gpx") =>
            val referenceDistance = {
              val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
              Math.round(toMeters(referenceLineStrings.map(_.getLength).sum))
            }

            val updatedRoute = context.oldRoute.get.copy(
              referenceDay = Some(referenceDay),
              referenceFilename = reference.filename,
              referenceDistance = referenceDistance,
            )

            context = context.copy(
              newRoute = Some(updatedRoute)
            )

            context = monitorUpdateAnalyzer.analyze(context)
            context = saver.save(context)

          case Some("multi-gpx") =>

            // TODO perform analysis of the sub-relation !!

            monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference) match {
              case None =>
              case Some(state) =>
                context = context.copy(
                  newStates = context.newStates :+ state,
                  saveResult = context.saveResult.copy(
                    analyzed = true
                  )
                )
            }

            context = saver.save(context)

          case _ =>
        }
      }
      context.saveResult
    }
  }

  override def analyzeAll(routeId: ObjectId): Unit = {

    monitorRouteRepository.routeById(routeId) match {
      case None => log.error(s"Route ${routeId.oid} not found")
      case Some(route) =>

        // TODO load all references
        val oldReferences: Seq[MonitorRouteReference] = Seq.empty

        var context = MonitorUpdateContext("").copy(
          oldRoute = Some(route),
          referenceType = Some(route.referenceType),
          oldReferences = oldReferences
        )

        if (!context.abort) {
          context = monitorUpdateAnalyzer.analyze(context)
        }
        if (!context.abort) {
          context = saver.save(context)
        }
    }
  }

  override def analyzeRelation(routeId: ObjectId, relationId: Long): Unit = {
    monitorRouteRepository.routeById(routeId) match {
      case None => log.error(s"Route ${routeId.oid} not found")
      case Some(route) =>
        monitorRouteRepository.routeRelationReference(routeId, relationId) match {
          case None => log.error(s"Route ${route.name}, reference for relation $relationId not found")
          case Some(reference) =>
            var context = MonitorUpdateContext("").copy(
              oldRoute = Some(route),
              referenceType = Some(route.referenceType),
              oldReferences = Seq(reference)
            )
            if (!context.abort) {
              context = monitorUpdateAnalyzer.analyze(context)
            }
            if (!context.abort) {
              context = saver.save(context)
            }
        }
    }
  }

  private def findGroup(context: MonitorUpdateContext, groupName: String): MonitorUpdateContext = {
    monitorGroupRepository.groupByName(groupName) match {
      case None =>
        val exception = s"""Could not find group with name "$groupName""""
        log.error(exception)
        context.copy(
          abort = true,
          saveResult = context.saveResult.copy(
            exception = Some(exception)
          )
        )
      case Some(group) =>
        context.copy(
          group = Some(group)
        )
    }
  }

  private def findRoute(context: MonitorUpdateContext, routeName: String): MonitorUpdateContext = {
    context.group match {
      case None => context
      case Some(group) =>
        monitorRouteRepository.routeByName(group._id, routeName) match {
          case None =>
            val exception = s"""Could not find route with name "$routeName" in group "${group.name}""""
            log.error(exception)
            context.copy(
              abort = true,
              saveResult = context.saveResult.copy(
                exception = Some(exception)
              )
            )
          case Some(route) =>
            context.copy(
              oldRoute = Some(route)
            )
        }
    }
  }

  private def assertNewRoute(context: MonitorUpdateContext, routeName: String): MonitorUpdateContext = {
    context.group match {
      case None => context
      case Some(group) =>
        monitorRouteRepository.routeByName(group._id, routeName) match {
          case None => context
          case Some(route) =>
            val exception = s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
            log.error(exception)
            context.copy(
              abort = true,
              saveResult = context.saveResult.copy(
                exception = Some(exception)
              )
            )
        }
    }
  }
}
