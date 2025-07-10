package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.CoordinateUtil
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteReferenceSummary
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorUpdate(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorUpdateAnalyzeReference: MonitorUpdateAnalyzeReference,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave,
  monitorOsmUpdate: MonitorOsmUpdate,
  monitorGpxUpdate: MonitorGpxUpdate,
) {

  private val log = Log(classOf[MonitorUpdate])

  def execute(context: MonitorContext): Unit = {

    {
      val args = MonitorUpdateArgs(
        context.value.user,
        context.value.reporter,
        context.value.update,
      )

      //      if (args.update.referenceType == MonitorReferenceType.multiGpx) {
      //        monitorAddMultiGpx.execute(args)
      //        return
      //      }
      //      if (args.update.referenceType == MonitorReferenceType.osm && args.update.referenceNow.contains(true)) {
      //        monitorUpdateOsmNow.execute(args)
      //        return
      //      }
      if (args.update.referenceType == MonitorReferenceType.osm) {
        monitorOsmUpdate.execute(args)
        return
      }
      if (args.update.referenceType == MonitorReferenceType.gpx) {
        monitorGpxUpdate.execute(args)
        return
      }
    }

    initReporter(context)

    monitorUpdateCommon.oldFindGroup(context)
    val oldRoute = monitorUpdateCommon.oldFindRoute(context)

    if (context.value.isRouteChanged) {

      val groupId = context.value.update.newGroupName match {
        case None => context.value.group.get._id
        case Some(newGroupName) =>
          monitorGroupRepository.groupByName(newGroupName).map(_._id) match {
            case Some(id) => id
            case None =>
              throw new IllegalArgumentException(
                s"""Could not find group with name "$newGroupName""""
              )
          }
      }

      val referenceTimestamp = if (context.value.update.referenceNow.contains(true)) {
        Some(Time.now)
      }
      else {
        context.value.update.referenceTimestamp
      }

      context.set(
        context.value.copy(
          newRoute = Some(
            oldRoute.copy(
              groupId = groupId,
              name = context.value.update.newRouteName.getOrElse(oldRoute.name),
              description = context.value.update.description.getOrElse(""),
              comment = context.value.update.comment,
              relationId = context.value.update.relationId,
              user = context.value.user,
              timestamp = Time.now,
              referenceType = context.value.update.referenceType,
              referenceTimestamp = referenceTimestamp,
              referenceFilename = context.value.update.referenceFilename,
            )
          )
        )
      )
    }

    context.stepActive("analyze-route-structure")
    context.set(monitorUpdateStructure.update(context.value))

    val oldReferences = monitorRouteRepository.routeReferences(context.value.routeId)
    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(context.value.routeId)
    val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
    context.set(
      context.value.copy(
        oldReferenceIds = oldReferenceIds,
        oldStateIds = oldStateIds,
        references = oldReferences,
      )
    )

    context.set(monitorUpdateCommon.removeObsoleteReferences(context))
    monitorUpdateCommon.removeObsoleteStates(context)

    if (context.value.isReferenceTypeGpx) {
      updateRouteWithGpxReference(context)
    }
    else {
      if (context.value.isReferenceTypeOsm && context.value.isReferenceChanged) {
        updateSubRelationOsmReferences(context)
      }
    }

    context.stepActive("save")
    monitorUpdateSave.save(context)
    context.stepDone("save")
  }

  private def initReporter(context: MonitorContext): Unit = {
    context.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )
  }

  def updateRouteWithGpxReference(context: MonitorContext): Unit = {

    context.value.update.referenceGpx match {
      case None =>

        context.value.update.migrationGeojson match {
          case Some(referenceGeoJson) =>

            val referenceTimestamp = context.value.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp not found"))

            val geometryFactory = new GeometryFactory

            val geometryCollection = new GeoJsonReader(geometryFactory).read(referenceGeoJson)
            val referenceBounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)

            val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
            val referenceDistance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
            val referenceSegmentCount = geometryCollection.getNumGeometries
            val referenceLines = referenceLineStrings.map(CoordinateUtil.lineStringToCoordinates)

            val reference = MonitorRouteReference(
              ObjectId(),
              routeId = context.value.routeId,
              relationId = context.value.relationId,
              timestamp = Time.now,
              user = context.value.user,
              referenceBounds = referenceBounds,
              referenceType = MonitorReferenceType.gpx,
              referenceTimestamp = referenceTimestamp,
              referenceDistance = referenceDistance,
              referenceSegmentCount = referenceSegmentCount,
              referenceFilename = context.value.update.referenceFilename,
              referenceLines = referenceLines
            )

            context.upsertRouteReference(reference)
            monitorRouteRepository.saveRouteReference(reference)

            val updatedNewRoute = context.value.newRoute.get.copy(
              referenceDistance = referenceDistance
            )

            context.set(
              context.value.copy(
                newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
                newRoute = Some(updatedNewRoute)
              )
            )

            analyze(context, reference)

          case None =>
            monitorRouteRepository.routeReference(context.value.routeId, None) match {
              case None =>
              case Some(reference) =>
                if (reference.relationId != context.value.update.relationId) {
                  context.report(
                    MonitorRouteUpdateStatusMessage(
                      commands = Seq(
                        MonitorRouteUpdateStatusCommand("step-add", "analyze"),
                      )
                    )
                  )
                  val updatedReference = reference.copy(
                    relationId = context.value.update.relationId
                  )
                  context.upsertRouteReference(updatedReference)
                  monitorRouteRepository.saveRouteReference(updatedReference)
                  context.set(
                    context.value.copy(
                      newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(updatedReference),
                    )
                  )
                  analyze(context, updatedReference)
                }
            }
        }

      case Some(referenceGpx) =>

        context.report(
          MonitorRouteUpdateStatusMessage(
            commands = Seq(
              MonitorRouteUpdateStatusCommand("step-add", "load-gpx"),
              MonitorRouteUpdateStatusCommand("step-add", "analyze"),
              MonitorRouteUpdateStatusCommand("step-active", "load-gpx"),
            )
          )
        )

        val referenceTimestamp = context.value.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp not found"))

        val now = Time.now
        val xml = XML.loadString(referenceGpx)
        val geometryCollection = new MonitorRouteGpxReader().read(xml)
        val referenceBounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
        val referenceGeoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

        val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
        val referenceDistance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
        val referenceSegmentCount = geometryCollection.getNumGeometries
        val referenceLines = referenceLineStrings.map(CoordinateUtil.lineStringToCoordinates)
        val objectId = context.value.oldReferenceIds.filter(_.relationId == context.value.relationId).map(_._id).headOption.getOrElse(ObjectId())

        val reference = MonitorRouteReference(
          objectId,
          routeId = context.value.routeId,
          relationId = context.value.relationId,
          timestamp = now,
          user = context.value.user,
          referenceBounds = referenceBounds,
          referenceType = MonitorReferenceType.gpx,
          referenceTimestamp = referenceTimestamp,
          referenceDistance = referenceDistance,
          referenceSegmentCount = referenceSegmentCount,
          referenceFilename = context.value.update.referenceFilename,
          referenceLines = referenceLines
        )

        context.upsertRouteReference(reference)
        monitorRouteRepository.saveRouteReference(reference)

        val updatedNewRoute = context.value.newRoute.get.copy(
          referenceDistance = referenceDistance
        )

        context.set(
          context.value.copy(
            newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
            newRoute = Some(updatedNewRoute)
          )
        )

        analyze(context, reference)
    }
  }

  private def analyze(context: MonitorContext, reference: MonitorRouteReference): Unit = {
    context.stepActive("analyze")
    monitorUpdateAnalyzeReference.analyzeReference(context, reference, None) match {
      case None =>
      case Some(state) =>
        monitorRouteRepository.saveRouteState(state)
        context.set(
          context.value.copy(
            stateChanged = true
          )
        )
    }
  }

  def updateSubRelationOsmReferences(context: MonitorContext): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = monitorUpdateCommon.composeProcessList(monitorRouteRelation)
            val processListSize = processList.size
            context.value.reporter.processList(processList)
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/$processListSize ${mrr.relationId}") {
                context.stepActive(mrr.relationId.toString)
                val updateSingleRelationRoute = index == 0 && processList.sizeIs == 1
                updateSubRelationOsmReference(context, mrr, updateSingleRelationRoute)
              }
            }
        }
    }
  }

  private def updateSubRelationOsmReference(
    context: MonitorContext,
    monitorRouteRelation: MonitorRouteRelation,
    updateSingleRelationRoute: Boolean
  ): Unit = {
    val referenceTimestamp = context.value.newRoute.get.referenceTimestamp
    val subs = monitorRouteRelation.relations.map(_.relationId).mkString("(", ",", ")")
    log.info(s"${monitorRouteRelation.name}    $subs")
    monitorRouteRelationRepository.loadTopLevel(referenceTimestamp, monitorRouteRelation.relationId) match {
      case None =>
        val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.map(_.yyyymmddhhmmss).getOrElse(Time.now.yyyymmddhhmmss)}"
        context.report(
          MonitorRouteUpdateStatusMessage(
            errors = Some(Seq(error))
          )
        )
        context.deleteRouteReference(context.value.routeId, Some(monitorRouteRelation.relationId))
        monitorRouteRepository.deleteRouteReference(context.value.routeId, monitorRouteRelation.relationId)
        monitorRouteRepository.deleteRouteState(context.value.routeId, monitorRouteRelation.relationId)
        context.set(
          context.value.copy(
            stateChanged = true
          )
        )

      case Some(subRelation) =>
        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        if (wayMembers.nonEmpty) {
          val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
          val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

          val referenceLines = analysis.routeSegments.flatMap(_.lineStrings).map(CoordinateUtil.lineStringToCoordinates)

          val id = if (context.value.isActionUpdate || context.value.isActionGpxUpload) {
            monitorRouteRepository.routeRelationReferenceId(context.value.routeId, Some(subRelation.id)) match {
              case Some(referenceId) => referenceId
              case None => ObjectId()
            }
          }
          else {
            ObjectId()
          }

          val ref = MonitorRouteReference(
            id,
            context.value.newRoute.get._id,
            Some(subRelation.id),
            Time.now,
            context.value.user,
            bounds,
            MonitorReferenceType.osm,
            context.value.newRoute.get.referenceTimestamp.get,
            analysis.osmDistance,
            analysis.routeSegments.size,
            None,
            referenceLines
          )

          context.upsertRouteReference(ref)
          monitorRouteRepository.saveRouteReference(ref)
          context.set(
            context.value.copy(
              newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(ref),
            )
          )

          if (updateSingleRelationRoute) {
            context.set(
              context.value.copy(
                newRoute = Some(
                  context.value.newRoute.get.copy(
                    referenceDistance = ref.referenceDistance
                  )
                )
              )
            )
          }

          val currentRelation = Option.when(context.value.update.referenceNow.contains(true)) {
            subRelation
          }

          monitorUpdateAnalyzeReference.analyzeReference(context, ref, currentRelation) match {
            case Some(state) =>
              monitorRouteRepository.saveRouteState(state)
              context.set(
                context.value.copy(
                  stateChanged = true
                )
              )

            case None =>
              val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.get.yyyymmddhhmmss}"
              context.report(
                MonitorRouteUpdateStatusMessage(
                  errors = Some(Seq(error))
                )
              )
          }
        }
    }
  }
}
