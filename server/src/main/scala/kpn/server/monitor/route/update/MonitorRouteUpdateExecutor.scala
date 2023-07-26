package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.common.Bounds
import kpn.api.common.data.WayMember
import kpn.api.custom.ApiResponse
import kpn.api.custom.Relation
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentBuilder
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.json.Json
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteReferenceSummary
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteStateSummary
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.collection.immutable.Seq
import scala.xml.XML

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class MonitorRouteUpdateExecutor(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteGapAnalyzer: MonitorRouteGapAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer
) {

  private val log = Log(classOf[MonitorRouteUpdateExecutor])

  private var context: MonitorUpdateContext = null

  def execute(originalContext: MonitorUpdateContext): Unit = {
    context = originalContext
    try {
      if (context.update.action == "add") {
        add()
      }
      else if (context.update.action == "update") {
        update()
      }
      else if (context.update.action == "gpx-upload") {
        gpxUpload()
      }
      else if (context.update.action == "gpx-delete") {
        gpxDelete()
      }
    }
    catch {
      case e: RuntimeException =>
        log.error(s"Could not update route\nupdate= ${Json.string(context.update)}", e)
        context.reporter.report(
          MonitorRouteUpdateStatusMessage(
            exception = Some(e.getMessage)
          )
        )
    }
  }

  private def add(): Unit = {

    context.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )

    findGroup()
    assertNewRoute()

    val referenceTimestamp = if (context.update.referenceNow) {
      Some(Time.now)
    }
    else {
      context.update.referenceTimestamp
    }

    context = context.copy(
      newRoute = Some(
        MonitorRoute(
          ObjectId(),
          context.group.get._id,
          context.update.routeName,
          context.update.description.getOrElse(""),
          context.update.comment,
          context.update.relationId,
          context.user,
          Time.now,
          None,
          referenceType = context.update.referenceType,
          referenceTimestamp = referenceTimestamp,
          referenceFilename = context.update.referenceFilename,
          referenceDistance = 0,
          deviationDistance = 0,
          deviationCount = 0,
          osmWayCount = 0,
          osmSegmentCount = 0,
          osmDistance = 0,
          happy = false,
          osmSegments = Seq.empty,
          relation = None
        )
      )
    )

    reportStepActive("analyze-route-structure")
    context = monitorUpdateStructure.update(context)

    if (context.update.referenceType == "gpx") {
      updateRouteWithGpxReference()
    }
    else if (context.update.referenceType == "multi-gpx") {
      addRouteWithMultiGpxReference()
    }
    else {
      updateSubRelations()
    }

    save()
  }

  private def update(): Unit = {

    context.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )

    findGroup()
    val oldRoute = findRoute()

    if (context.isRouteChanged()) {

      val groupId = context.update.newGroupName match {
        case None => context.group.get._id
        case Some(newGroupName) =>
          monitorGroupRepository.groupByName(newGroupName).map(_._id) match {
            case Some(id) => id
            case None =>
              throw new IllegalArgumentException(
                s"""Could not find group with name "${newGroupName}""""
              )
          }
      }

      val referenceTimestamp = if (context.update.referenceNow) {
        Some(Time.now)
      }
      else {
        context.update.referenceTimestamp
      }

      context = context.copy(
        newRoute = Some(
          oldRoute.copy(
            groupId = groupId,
            name = context.update.newRouteName.getOrElse(oldRoute.name),
            description = context.update.description.get, // TODO make more safe!!
            comment = context.update.comment,
            relationId = context.update.relationId,
            user = context.user,
            timestamp = Time.now,
            referenceType = context.update.referenceType,
            referenceTimestamp = referenceTimestamp,
            referenceFilename = context.update.referenceFilename,
          )
        )
      )
    }

    reportStepActive("analyze-route-structure")
    context = monitorUpdateStructure.update(context)

    // TODO pick up information about existing state and references, and delete the ones that are not the structure anymore

    if (context.update.referenceType == "gpx") {
      updateRouteWithGpxReference()
    }
    else {
      if (context.isReferenceChanged()) {
        updateSubRelations()
      }
    }

    save()
  }

  private def gpxUpload(): Unit = {

    // TODO communicate expected steps

    findGroup()
    findRoute()

    val now = Time.now
    val referenceTimestamp = context.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp missing in update"))
    val relationId = context.update.relationId.getOrElse(throw new RuntimeException("relationId missing in update"))

    val geometryCollection: GeometryCollection = context.update.migrationGeojson match {
      case Some(migrationGeojson) =>
        val geometryFactory = new GeometryFactory
        new GeoJsonReader(geometryFactory).read(migrationGeojson).asInstanceOf[GeometryCollection]
      case None =>
        val referenceGpx = context.update.referenceGpx.getOrElse(throw new RuntimeException("reference gpx missing in update"))
        val xml = XML.loadString(referenceGpx)
        new MonitorRouteGpxReader().read(xml)
    }

    val bounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
    val geoJson = context.update.migrationGeojson match {
      case Some(migrationGeojson) => migrationGeojson
      case None => MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)
    }
    // TODO should delete already existing reference here?

    val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
    val distance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
    val segmentCount = geometryCollection.getNumGeometries

    val reference = MonitorRouteReference(
      ObjectId(),
      routeId = context.routeId,
      relationId = Some(relationId),
      timestamp = now,
      user = context.user,
      referenceBounds = bounds,
      referenceType = "gpx",
      referenceTimestamp = referenceTimestamp,
      referenceDistance = distance,
      referenceSegmentCount = segmentCount,
      referenceFilename = context.update.referenceFilename,
      referenceGeoJson = geoJson
    )

    monitorRouteRepository.saveRouteReference(reference)

    context = context.copy(
      newReferenceSummaries = context.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
    )

    if (context.update.referenceType == "multi-gpx") {
      context = context.copy(
        newRoute = context.oldRoute
      )
    }
    else {
      context = context.copy(
        newRoute = Some(
          context.oldRoute.get.copy(
            referenceDistance = reference.referenceDistance
          )
        )
      )
    }

    analyzeReference(reference, None) match {
      case None =>
      case Some(state) =>
        // TODO improve performance by picking up _id only?
        val stateToSave = monitorRouteRepository.routeState(context.routeId, state.relationId) match {
          case Some(oldState) => state.copy(_id = oldState._id)
          case None => state
        }
        monitorRouteRepository.saveRouteState(stateToSave)
        context = context.copy(
          stateChanged = true
        )
        save()
    }
  }

  private def gpxDelete(): Unit = {

    // TODO communicate expected steps

    findGroup()
    findRoute()

    val relationId = context.update.relationId.getOrElse(throw new RuntimeException("subrelation id needed for gpx-delete"))
    monitorRouteRepository.deleteRouteReference(context.routeId, relationId)
    monitorRouteRepository.routeState(context.routeId, relationId) match {
      case None =>
      case Some(state) =>
        val updatedState = state.copy(
          matchesGeometry = None,
          deviations = Seq.empty,
          happy = false
        )
        monitorRouteRepository.saveRouteState(updatedState)
        context = context.copy(
          stateChanged = true
        )
    }
    save()
  }

  private def addRouteWithMultiGpxReference() = {
    context.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = composeProcessList(monitorRouteRelation)
            reportProcessList(processList)

            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/${processList.size} ${mrr.relationId}") {
                reportStepActive(mrr.relationId.toString)
                val updateSingleRelationRoute = index == 0 && processList.size == 1

                monitorRouteRelationRepository.loadTopLevel(None, mrr.relationId) match {
                  case None => None
                  case Some(relation) =>

                    val wayMembers = MonitorFilter.filterWayMembers(relation.wayMembers)
                    if (wayMembers.nonEmpty) {
                      val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)
                      val bounds = Util.mergeBounds(osmSegmentAnalysis.routeSegments.map(_.segment.bounds))

                      val id = if (context.update.action == "update" || context.update.action == "gpx-upload") {
                        monitorRouteRepository.routeStateId(context.routeId, mrr.relationId) match {
                          case Some(id) => id
                          case None => ObjectId()
                        }
                      }
                      else {
                        ObjectId()
                      }

                      val state = MonitorRouteState(
                        id,
                        routeId = context.routeId,
                        relationId = mrr.relationId,
                        timestamp = Time.now,
                        wayCount = wayMembers.size,
                        startNodeId = osmSegmentAnalysis.startNodeId,
                        endNodeId = osmSegmentAnalysis.endNodeId,
                        osmDistance = osmSegmentAnalysis.osmDistance,
                        bounds = bounds,
                        osmSegments = osmSegmentAnalysis.routeSegments.map(_.segment),
                        matchesGeometry = None,
                        deviations = Seq.empty,
                        happy = false,
                      )

                      monitorRouteRepository.saveRouteState(state)
                      context = context.copy(
                        stateChanged = true
                      )
                    }
                }
              }
            }
        }
    }
  }

  private def updateSubRelations(): Unit = {
    context.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = composeProcessList(monitorRouteRelation)
            reportProcessList(processList)
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/${processList.size} ${mrr.relationId}") {
                reportStepActive(mrr.relationId.toString)
                val updateSingleRelationRoute = index == 0 && processList.size == 1
                updateSubRelation(mrr, updateSingleRelationRoute)
              }
            }
        }
    }
  }

  private def updateSubRelation(
    monitorRouteRelation: MonitorRouteRelation,
    updateSingleRelationRoute: Boolean
  ): Unit = {
    val referenceTimestamp = context.newRoute.get.referenceTimestamp
    val subs = monitorRouteRelation.relations.map(_.relationId).mkString("(", ",", ")")
    log.info(s"${monitorRouteRelation.name}    $subs")
    monitorRouteRelationRepository.loadTopLevel(referenceTimestamp, monitorRouteRelation.relationId) match {
      case None =>
        val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.map(_.yyyymmddhhmmss).getOrElse(Time.now.yyyymmddhhmmss)}"
        context.reporter.report(
          MonitorRouteUpdateStatusMessage(
            errors = Some(Seq(error))
          )
        )
        monitorRouteRepository.deleteRouteReference(context.routeId, monitorRouteRelation.relationId)
        monitorRouteRepository.deleteRouteState(context.routeId, monitorRouteRelation.relationId)
        None

      case Some(subRelation) =>
        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        if (wayMembers.isEmpty) {
          None
        }
        else {
          val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
          val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

          val geomFactory = new GeometryFactory
          val geometryCollection = new GeometryCollection(analysis.routeSegments.map(_.lineString).toArray, geomFactory)
          val geoJsonWriter = new GeoJsonWriter()
          geoJsonWriter.setEncodeCRS(false)
          val geometry = geoJsonWriter.write(geometryCollection)

          val id = if (context.update.action == "update" || context.update.action == "gpx-upload") {
            monitorRouteRepository.routeRelationReferenceId(context.routeId, subRelation.id) match {
              case Some(id) => id
              case None => ObjectId()
            }
          }
          else {
            ObjectId()
          }

          val ref = MonitorRouteReference(
            id,
            context.newRoute.get._id,
            Some(subRelation.id),
            Time.now,
            context.user,
            bounds,
            "osm",
            context.newRoute.get.referenceTimestamp.get,
            analysis.osmDistance,
            analysis.routeSegments.size,
            None,
            geometry
          )

          monitorRouteRepository.saveRouteReference(ref)
          context = context.copy(
            newReferenceSummaries = context.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(ref),
          )

          if (updateSingleRelationRoute) {
            context = context.copy(
              newRoute = Some(
                context.newRoute.get.copy(
                  referenceDistance = ref.referenceDistance
                )
              )
            )
          }

          val currentRelation = if (context.update.referenceNow) {
            Some(subRelation)
          }
          else {
            None
          }

          analyzeReference(ref, currentRelation) match {
            case Some(state) =>
              monitorRouteRepository.saveRouteState(state)
              context = context.copy(
                stateChanged = true
              )

            case None =>
              val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.get.yyyymmddhhmmss}"
              context.reporter.report(
                MonitorRouteUpdateStatusMessage(
                  errors = Some(Seq(error))
                )
              )
          }
        }
    }
  }

  private def composeProcessList(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteRelation] = {
    if (monitorRouteRelation.relations.isEmpty) {
      Seq(monitorRouteRelation)
    }
    else {
      val subs = monitorRouteRelation.relations.flatMap { subMonitorRouteRelation =>
        composeProcessList(subMonitorRouteRelation)
      }
      subs :+ monitorRouteRelation
    }
  }

  private def updateRouteWithGpxReference(): Unit = {

    context.update.referenceGpx match {
      case None =>

        context.update.migrationGeojson match {
          case Some(referenceGeoJson) =>

            val referenceTimestamp = context.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp not found"))

            val now = Time.now
            val geometryFactory = new GeometryFactory

            val geometryCollection = new GeoJsonReader(geometryFactory).read(referenceGeoJson)
            val referenceBounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)

            // TODO should delete already existing reference here?

            val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
            val referenceDistance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
            val referenceSegmentCount = geometryCollection.getNumGeometries

            val reference = MonitorRouteReference(
              ObjectId(),
              routeId = context.routeId,
              relationId = context.relationId,
              timestamp = now,
              user = context.user,
              referenceBounds = referenceBounds,
              referenceType = "gpx",
              referenceTimestamp = referenceTimestamp,
              referenceDistance = referenceDistance,
              referenceSegmentCount = referenceSegmentCount,
              referenceFilename = context.update.referenceFilename,
              referenceGeoJson = referenceGeoJson
            )

            monitorRouteRepository.saveRouteReference(reference)

            val updatedNewRoute = context.newRoute.get.copy(
              referenceDistance = referenceDistance
            )

            context = context.copy(
              newReferenceSummaries = context.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
              newRoute = Some(updatedNewRoute)
            )

            analyze(reference)

          case None =>
            monitorRouteRepository.routeReference(context.routeId) match {
              case None =>
              case Some(reference) =>
                if (reference.relationId != context.update.relationId) {
                  context.reporter.report(
                    MonitorRouteUpdateStatusMessage(
                      commands = Seq(
                        MonitorRouteUpdateStatusCommand("step-add", "analyze"),
                      )
                    )
                  )
                  val updatedReference = reference.copy(
                    relationId = context.update.relationId
                  )
                  monitorRouteRepository.saveRouteReference(updatedReference)
                  context = context.copy(
                    newReferenceSummaries = context.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(updatedReference),
                  )
                  analyze(updatedReference)
                }
            }
        }

      case Some(referenceGpx) =>

        context.reporter.report(
          MonitorRouteUpdateStatusMessage(
            commands = Seq(
              MonitorRouteUpdateStatusCommand("step-add", "load-gpx"),
              MonitorRouteUpdateStatusCommand("step-add", "analyze"),
              MonitorRouteUpdateStatusCommand("step-active", "load-gpx"),
            )
          )
        )

        val referenceTimestamp = context.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp not found"))

        val now = Time.now
        val xml = XML.loadString(referenceGpx)
        val geometryCollection = new MonitorRouteGpxReader().read(xml)
        val referenceBounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
        val referenceGeoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

        // TODO should delete already existing reference here?

        val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
        val referenceDistance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
        val referenceSegmentCount = geometryCollection.getNumGeometries

        val reference = MonitorRouteReference(
          ObjectId(),
          routeId = context.routeId,
          relationId = context.relationId,
          timestamp = now,
          user = context.user,
          referenceBounds = referenceBounds,
          referenceType = "gpx",
          referenceTimestamp = referenceTimestamp,
          referenceDistance = referenceDistance,
          referenceSegmentCount = referenceSegmentCount,
          referenceFilename = context.update.referenceFilename,
          referenceGeoJson = referenceGeoJson
        )

        monitorRouteRepository.saveRouteReference(reference)

        val updatedNewRoute = context.newRoute.get.copy(
          referenceDistance = referenceDistance
        )

        context = context.copy(
          newReferenceSummaries = context.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
          newRoute = Some(updatedNewRoute)
        )

        analyze(reference)
    }
  }

  private def analyze(reference: MonitorRouteReference): Unit = {
    reportStepActive("analyze")
    analyzeReference(reference, None) match {
      case None =>
      case Some(state) =>
        monitorRouteRepository.saveRouteState(state)
        context = context.copy(
          stateChanged = true
        )
    }
  }

  private def updateMonitorRouteRelation(
    monitorRouteRelation: MonitorRouteRelation,
    reference: MonitorRouteReference,
    stateOption: Option[MonitorRouteState]
  ): MonitorRouteRelation = {

    reference.relationId match {
      case None => monitorRouteRelation
      case Some(referenceRelationId) =>

        if (referenceRelationId == monitorRouteRelation.relationId) {

          val deviationDistance = stateOption match {
            case None => 0
            case Some(state) => state.deviations.map(_.distance).sum
          }
          val deviationCount = stateOption match {
            case None => 0
            case Some(state) => state.deviations.size
          }
          val happy = stateOption match {
            case None => false
            case Some(state) => state.happy
          }

          monitorRouteRelation.copy(
            referenceTimestamp = Some(reference.referenceTimestamp),
            referenceFilename = reference.referenceFilename,
            referenceDistance = reference.referenceDistance,
            deviationDistance = deviationDistance,
            deviationCount = deviationCount,
            // TODO update happy, taking into account subrelations
            happy = happy
          )
        }
        else {
          val relations = monitorRouteRelation.relations.map { subRelation =>
            updateMonitorRouteRelation(subRelation, reference, stateOption)
          }
          monitorRouteRelation.copy(
            relations = relations
            // TODO update happy, taking into account subrelations
          )
        }
    }
  }

  private def resetReference(monitorRouteRelation: MonitorRouteRelation, subRelationId: Long): MonitorRouteRelation = {
    if (monitorRouteRelation.relationId == subRelationId) {
      monitorRouteRelation.copy(
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
      )
    }
    else {
      monitorRouteRelation.copy(
        relations = monitorRouteRelation.relations.map(rel => resetReference(rel, subRelationId))
      )
    }
  }

  def analyze(groupName: String, routeName: String): ApiResponse[MonitorRouteSaveResult] = {
    throw new RuntimeException("deprecated")
    null
  }

  private def findGroup(): Unit = {
    val groupName = context.update.groupName
    val group = monitorGroupRepository.groupByName(groupName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find group with name "$groupName""""
      )
    }
    context = context.copy(
      group = Some(group)
    )
  }

  private def findRoute(): MonitorRoute = {
    val routeName = context.update.routeName
    val route = monitorRouteRepository.routeByName(context.group.get._id, routeName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find route with name "$routeName" in group "${context.group.get.name}""""
      )
    }
    context = context.copy(
      oldRoute = Some(route)
    )
    route
  }

  private def assertNewRoute(): Unit = {
    val group = context.group.get
    val routeName = context.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None => // OK: no route with this name yet
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }

  private def save(): Unit = {

    reportStepActive("save")

    if (context.newReferenceSummaries.nonEmpty) {
      monitorRouteRepository.superRouteReferenceSummary(context.routeId) match {
        case None =>
        case Some(referenceDistance) =>
          context.newRoute match {
            case None =>
              context.oldRoute match {
                case None =>
                case Some(oldRoute) =>

                  val updatedRelation = if (context.update.referenceType.contains("multi-gpx")) {
                    oldRoute.relation.map { monitorRouteRelation =>
                      udpateMonitorRouteRelation(context, monitorRouteRelation)
                    }
                  }
                  else {
                    oldRoute.relation
                  }

                  val updatedRoute = oldRoute.copy(
                    referenceDistance = if (context.update.referenceType == "osm") referenceDistance else 0,
                    relation = updatedRelation
                  )

                  context = context.copy(
                    newRoute = Some(updatedRoute)
                  )
              }

            case Some(newRoute) =>
              val updatedRelation = if (context.update.referenceType.contains("multi-gpx")) {
                newRoute.relation.map { monitorRouteRelation =>
                  udpateMonitorRouteRelation(context, monitorRouteRelation)
                }
              }
              else {
                newRoute.relation
              }
              val updatedRoute = newRoute.copy(
                referenceDistance = if (context.update.referenceType != "multi-gpx") referenceDistance else 0,
                relation = updatedRelation
              )
              context = context.copy(
                newRoute = Some(updatedRoute)
              )
          }
      }
    }

    if (context.stateChanged) {

      val stateSummaries = monitorRouteRepository.routeStateSummaries(context.routeId)
      val relation = context.route.relation.map(relation => updatedMonitorRouteRelation(relation, stateSummaries))
      val relationWithDistances = relation.map(updatedMonitorRouteRelationCumulativeDistance)

      val monitorRouteSegmentInfos = monitorRouteRepository.routeStateSegments(context.routeId)
      val superRouteSuperSegments = MonitorRouteOsmSegmentBuilder.build(monitorRouteSegmentInfos)

      val relationWithGaps = relationWithDistances.map { monitorRouteRelation =>
        monitorRouteGapAnalyzer.calculate(
          monitorRouteSegmentInfos,
          superRouteSuperSegments,
          monitorRouteRelation
        )
      }

      val symbol = relationWithGaps.flatMap(_.symbol)
      val osmWayCount = stateSummaries.map(_.osmWayCount).sum
      val osmDistance = stateSummaries.map(_.osmDistance).sum

      context = context.copy(
        newRoute = Some(
          context.route.copy(
            symbol = symbol,
            relation = relationWithGaps,
            osmWayCount = osmWayCount,
            osmDistance = osmDistance
          )
        )
      )

      val happy = superRouteSuperSegments.size == 1 &&
        context.newRoute.map(_.deviationCount).sum == 0 &&
        (context.newRoute.get.relation.map(_.happy).getOrElse(false))

      val updatedRoute = context.route.copy(
        osmSegments = superRouteSuperSegments,
        osmSegmentCount = superRouteSuperSegments.size,
        happy = happy
      )
      context = context.copy(
        newRoute = Some(updatedRoute)
      )
    }

    context.newRoute match {
      case Some(route) => monitorRouteRepository.saveRoute(route)
      case None =>
    }
    reportStepDone("save")
  }

  private def updatedMonitorRouteRelation(monitorRouteRelation: MonitorRouteRelation, stateSummaries: Seq[MonitorRouteStateSummary]): MonitorRouteRelation = {

    val updatedWithState = if (context.update.referenceType == "gpx") {
      stateSummaries.headOption match {
        case None => monitorRouteRelation
        case Some(stateSummary) =>
          monitorRouteRelation.copy(
            deviationDistance = stateSummary.deviationDistance,
            deviationCount = stateSummary.deviationCount,
            osmWayCount = stateSummary.osmWayCount,
            osmSegmentCount = stateSummary.osmSegmentCount,
            osmDistanceSubRelations = 0,
            happy = stateSummary.happy,
          )
      }
    }
    else {
      val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelation(r, stateSummaries))
      val subRelationsHappy = updatedRelations.forall(_.happy)
      stateSummaries.find(_.relationId == monitorRouteRelation.relationId) match {
        case None =>
          monitorRouteRelation.copy(
            relations = updatedRelations,
            happy = subRelationsHappy
          )

        case Some(state) =>

          monitorRouteRelation.copy(
            deviationDistance = state.deviationDistance,
            deviationCount = state.deviationCount,
            osmWayCount = state.osmWayCount,
            osmSegmentCount = state.osmSegmentCount,
            osmDistance = state.osmDistance,
            happy = state.happy && subRelationsHappy,
            relations = updatedRelations
          )
      }
    }

    if (context.update.action == "gpx-delete" && context.update.relationId.get == monitorRouteRelation.relationId) {
      updatedWithState.copy(
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0
      )
    }
    else {
      updatedWithState
    }
  }

  private def updatedMonitorRouteRelationCumulativeDistance(monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
    val updatedRelations = monitorRouteRelation.relations.map(r => updatedMonitorRouteRelationCumulativeDistance(r))
    val osmDistanceSubRelations = monitorRouteRelation.relations.flatMap(monitorRouteRelationSubRelations).map(_.osmDistance).sum
    monitorRouteRelation.copy(
      osmDistanceSubRelations = osmDistanceSubRelations,
      relations = updatedRelations
    )
  }

  private def monitorRouteRelationSubRelations(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteRelation] = {
    monitorRouteRelation +: monitorRouteRelation.relations.flatMap(r => monitorRouteRelationSubRelations(r))
  }

  private def udpateMonitorRouteRelation(context: MonitorUpdateContext, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
    if (context.newReferenceSummaries.nonEmpty) {
      val relations = monitorRouteRelation.relations.map(r => udpateMonitorRouteRelation(context, r))
      context.newReferenceSummaries.find(_.relationId.get == monitorRouteRelation.relationId) match {
        case None =>
          monitorRouteRelation.copy(
            relations = relations
          )
        case Some(reference) =>
          monitorRouteRelation.copy(
            referenceTimestamp = Some(reference.referenceTimestamp),
            referenceFilename = reference.referenceFilename,
            referenceDistance = reference.referenceDistance,
            relations = relations
          )
      }
    }
    else {
      monitorRouteRelation
    }
  }

  private def reportProcessList(processList: Seq[MonitorRouteRelation]): Unit = {
    val commands = processList.zipWithIndex.map { case (monitorRouteRelation, index) =>
      val description = s"${index + 1}/${processList.size} ${monitorRouteRelation.name}"
      MonitorRouteUpdateStatusCommand(
        "step-add",
        monitorRouteRelation.relationId.toString,
        Some(description)
      )
    } :+ MonitorRouteUpdateStatusCommand(
      "step-add",
      "save"
    )

    context.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = commands
      )
    )
  }

  private def reportStepActive(stepId: String): Unit = {
    context.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-active", stepId)
        )
      )
    )
  }

  private def reportStepDone(stepId: String): Unit = {
    context.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-done", stepId)
        )
      )
    )
  }

  private def analyzeReference(reference: MonitorRouteReference, currentRelation: Option[Relation]): Option[MonitorRouteState] = {
    reference.relationId.flatMap { relationId =>
      val relationOption = if (currentRelation.nonEmpty) {
        currentRelation
      }
      else if (context.update.referenceType == "gpx") {
        monitorRouteRelationRepository.load(None, relationId)
      }
      else {
        monitorRouteRelationRepository.loadTopLevel(None, relationId)
      }
      relationOption match {
        case None => None
        case Some(relation) =>
          if (context.update.referenceType == "gpx") {
            updateSubRelationOsmInfo(relation)
          }

          val allWayMembers = if (context.update.referenceType == "gpx") {
            collectAllWayMembers(relation)
          }
          else {
            relation.wayMembers
          }
          val wayMembers = MonitorFilter.filterWayMembers(allWayMembers)
          val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)
          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(wayMembers.map(_.way), reference.referenceGeoJson)
          val bounds = Util.mergeBounds(osmSegmentAnalysis.routeSegments.map(_.segment.bounds) ++ deviationAnalysis.deviations.map(_.bounds))
          val routeAnalysis = MonitorRouteAnalysis(
            relation,
            wayMembers.size,
            osmSegmentAnalysis.startNodeId,
            osmSegmentAnalysis.endNodeId,
            osmSegmentAnalysis.osmDistance,
            deviationAnalysis.referenceDistance,
            bounds,
            osmSegmentAnalysis.routeSegments.map(_.segment),
            Some(deviationAnalysis.referenceGeometry),
            deviationAnalysis.matchesGeometry,
            deviationAnalysis.deviations,
            relations = Seq.empty
          )

          val happy = routeAnalysis.gpxDistance > 0 &&
            routeAnalysis.deviations.isEmpty &&
            routeAnalysis.osmSegments.size == 1

          val id = if (context.update.action == "update" || context.update.action == "gpx-upload") {
            monitorRouteRepository.routeStateId(context.routeId, relationId) match {
              case Some(id) => id
              case None => ObjectId()
            }
          }
          else {
            ObjectId()
          }

          Some(
            MonitorRouteState(
              id,
              context.routeId,
              relationId,
              Time.now,
              routeAnalysis.wayCount,
              routeAnalysis.startNodeId,
              routeAnalysis.endNodeId,
              routeAnalysis.osmDistance,
              routeAnalysis.bounds,
              routeAnalysis.osmSegments,
              routeAnalysis.matchesGeometry,
              routeAnalysis.deviations,
              happy,
            )
          )
      }
    }
  }

  private def collectAllWayMembers(relation: Relation): Seq[WayMember] = {
    val wayMembers = relation.wayMembers
    val subRelationWayMembers = relation.relationMembers.flatMap { relationMember =>
      collectAllWayMembers(relationMember.relation)
    }
    wayMembers ++ subRelationWayMembers
  }

  private def updateSubRelationOsmInfo(relation: Relation): Unit = {
    context.newRoute match {
      case None =>
      case Some(newRoute) =>
        val updatedRelation = newRoute.relation.map { monitorRouteRelation =>
          updateSubRelationOsmInfo(relation, monitorRouteRelation)
        }
        context = context.copy(
          newRoute = Some(
            newRoute.copy(
              relation = updatedRelation
            )
          )
        )
    }
  }

  private def updateSubRelationOsmInfo(relation: Relation, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {

    findSubRelation(relation, monitorRouteRelation.relationId) match {
      case None => monitorRouteRelation
      case Some(subRelation) =>

        val updatedRelations = monitorRouteRelation.relations.map { subMonitorRouteRelation =>
          updateSubRelationOsmInfo(subRelation, subMonitorRouteRelation)
        }

        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        val osmWayCount = wayMembers.size
        val osmDistance = wayMembers.map(_.way.length).sum
        val osmDistanceSubRelations = updatedRelations.map { monitorRouteRelation =>
          monitorRouteRelation.osmDistance + monitorRouteRelation.osmDistanceSubRelations
        }.sum

        monitorRouteRelation.copy(
          osmWayCount = osmWayCount,
          osmDistance = osmDistance,
          osmDistanceSubRelations = osmDistanceSubRelations,
          relations = updatedRelations
        )
    }
  }

  private def findSubRelation(relation: Relation, relationId: Long): Option[Relation] = {
    if (relation.id == relationId) {
      Some(relation)
    }
    else {
      relation.relationMembers.flatMap { subRelationMember =>
        findSubRelation(subRelationMember.relation, relationId)
      }.headOption
    }
  }
}
