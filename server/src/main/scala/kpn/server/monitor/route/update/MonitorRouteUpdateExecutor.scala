package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.data.WayMember
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Relation
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.core.util.ValidationException
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentBuilder
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.json.Json
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorGroup
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
import org.xml.sax.SAXParseException

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

  private val context = new MonitorContext()

  def execute(originalContext: MonitorUpdateContext): Unit = {
    context.set(
      originalContext.copy(
        referenceType = Some(originalContext.update.referenceType),
        analysisStartMillis = Some(System.currentTimeMillis())
      )
    )
    try {
      if (context.value.isActionAdd) {
        add()
      }
      else if (context.value.isActionUpdate) {
        update()
      }
      else if (context.value.isActionGpxUpload) {
        gpxUpload()
      }
      else if (context.value.isActionGpxDelete) {
        gpxDelete()
      }
    }
    catch {
      case e: RuntimeException =>
        val update = Json.string(context.value.update.printable())
        e match {
          case ve: ValidationException => log.info(s"ValidationException(${ve.getMessage}) $update")
          case _ => log.error(s"Could not update route: $update", e)
        }
        context.value.reporter.report(
          MonitorRouteUpdateStatusMessage(
            exception = Some(e.getMessage)
          )
        )
    }
    finally {
      Time.clear()
    }
  }

  def updateAnalysis(group: MonitorGroup, oldRoute: MonitorRoute): Unit = {

    Log.context(s"${group.name}, ${oldRoute.name}") {
      log.infoElapsed {

        val reporter = new MonitorUpdateReporterLogger()
        context.set(
          MonitorUpdateContext(
            user = "analyzer",
            reporter = null,
            update = null, //update,
            referenceType = Some(oldRoute.referenceType),
            group = Some(group),
            newRoute = Some(oldRoute),
            analysisStartMillis = Some(System.currentTimeMillis()),
          )
        )

        context.set(
          monitorUpdateStructure.update(context.value)
        )

        val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
        context.set(
          context.value.copy(
            oldStateIds = oldStateIds
          )
        )

        removeObsoleteStates()

        if (oldRoute.referenceType == "multi-gpx") {
          analyzeMultiGpx(oldRoute)
        }
        else if (oldRoute.referenceType == "gpx") {
          analyzeGpx(oldRoute)
        }
        else {
          analyzeOsm(oldRoute)
        }

        save()
        ("analysis completed", ())
      }
    }
  }

  private def add(): Unit = {

    context.value.reporter.report(
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

    val referenceTimestamp = if (context.value.update.referenceNow.contains(true)) {
      Some(Time.now)
    }
    else {
      context.value.update.referenceTimestamp
    }

    context.set(
      context.value.copy(
        newRoute = Some(
          MonitorRoute(
            ObjectId(),
            context.value.group.get._id,
            context.value.update.routeName,
            context.value.update.description.getOrElse(""),
            context.value.update.comment,
            context.value.update.relationId,
            context.value.user,
            Time.now,
            None,
            None,
            None,
            referenceType = context.value.update.referenceType,
            referenceTimestamp = referenceTimestamp,
            referenceFilename = context.value.update.referenceFilename,
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
    )

    context.value.reporter.stepActive("analyze-route-structure")
    context.set(monitorUpdateStructure.update(context.value))

    if (context.value.isReferenceTypeGpx) {
      updateRouteWithGpxReference()
    }
    else if (context.value.isReferenceTypeMultiGpx) {
      addRouteWithMultiGpxReference()
    }
    else {
      updateSubRelationOsmReferences()
    }

    context.value.reporter.stepActive("save")
    save()
    context.value.reporter.stepDone("save")
  }

  private def update(): Unit = {

    context.value.reporter.report(
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

    context.value.reporter.stepActive("analyze-route-structure")
    context.set(monitorUpdateStructure.update(context.value))

    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(context.value.routeId)
    val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
    context.set(
      context.value.copy(
        oldReferenceIds = oldReferenceIds,
        oldStateIds = oldStateIds
      )
    )

    context.set(removeObsoleteReferences())
    removeObsoleteStates()

    if (context.value.isReferenceTypeGpx) {
      updateRouteWithGpxReference()
    }
    else {
      if (context.value.isReferenceTypeOsm && context.value.isReferenceChanged) {
        updateSubRelationOsmReferences()
      }
    }

    context.value.reporter.stepActive("save")
    save()
    context.value.reporter.stepDone("save")
  }

  private def gpxUpload(): Unit = {

    val commands = Seq(
      MonitorRouteUpdateStatusCommand(
        "step-add",
        "upload",
      ),
      MonitorRouteUpdateStatusCommand(
        "step-add",
        "save"
      ),
      MonitorRouteUpdateStatusCommand(
        "step-active",
        "upload",
      ),
    )

    context.value.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = commands
      )
    )

    findGroup()
    findRoute()

    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(context.value.routeId)
    val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
    context.set(
      context.value.copy(
        oldReferenceIds = oldReferenceIds,
        oldStateIds = oldStateIds
      )
    )

    val now = Time.now
    val referenceTimestamp = context.value.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp missing in update"))
    val relationId = context.value.update.relationId.getOrElse(throw new RuntimeException("relationId missing in update"))

    val geometryCollection: GeometryCollection = context.value.update.migrationGeojson match {
      case Some(migrationGeojson) =>
        val geometryFactory = new GeometryFactory
        new GeoJsonReader(geometryFactory).read(migrationGeojson).asInstanceOf[GeometryCollection]
      case None =>
        val referenceGpx = context.value.update.referenceGpx.getOrElse(throw new RuntimeException("reference gpx missing in update"))
        val xml = try {
          XML.loadString(referenceGpx)
        }
        catch {
          case e: SAXParseException =>
            throw new ValidationException("invalid-reference-file")
        }

        new MonitorRouteGpxReader().read(xml)
    }

    val bounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
    val geoJson = context.value.update.migrationGeojson match {
      case Some(migrationGeojson) => migrationGeojson
      case None => MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)
    }

    val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
    val distance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
    val segmentCount = geometryCollection.getNumGeometries

    val objectId = context.value.oldReferenceIds.filter(_.relationId.contains(relationId)).map(_._id).headOption.getOrElse(ObjectId())

    val reference = MonitorRouteReference(
      objectId,
      routeId = context.value.routeId,
      relationId = Some(relationId),
      timestamp = now,
      user = context.value.user,
      referenceBounds = bounds,
      referenceType = "gpx",
      referenceTimestamp = referenceTimestamp,
      referenceDistance = distance,
      referenceSegmentCount = segmentCount,
      referenceFilename = context.value.update.referenceFilename,
      referenceGeoJson = geoJson
    )

    monitorRouteRepository.saveRouteReference(reference)

    context.set(
      context.value.copy(
        newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
      )
    )

    if (context.value.isReferenceTypeMultiGpx) { // TODO referenceType will always be "multi-gpx" ?
      context.set(
        context.value.copy(
          newRoute = context.value.oldRoute
        )
      )
    }
    else {
      context.set(
        context.value.copy(
          newRoute = Some(
            context.value.oldRoute.get.copy(
              referenceDistance = reference.referenceDistance
            )
          )
        )
      )
    }

    monitorRouteRelationRepository.loadTopLevel(None, relationId) match {
      case None =>
      case Some(relation) =>
        analyzeReference(reference, Some(relation)) match {
          case None =>
          case Some(state) =>
            monitorRouteRepository.saveRouteState(state)
            context.set(
              context.value.copy(
                stateChanged = true
              )
            )
            context.value.reporter.stepActive("save")
            save()
            context.value.reporter.stepDone("save")
        }
    }
  }

  private def gpxDelete(): Unit = {

    val commands = Seq(
      MonitorRouteUpdateStatusCommand(
        "step-add",
        "delete",
      ),
      MonitorRouteUpdateStatusCommand(
        "step-add",
        "save"
      ),
      MonitorRouteUpdateStatusCommand(
        "step-active",
        "delete",
      ),
    )

    context.value.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = commands
      )
    )

    findGroup()
    findRoute()

    val relationId = context.value.update.relationId.getOrElse(throw new RuntimeException("subrelation id needed for gpx-delete"))
    monitorRouteRepository.deleteRouteReference(context.value.routeId, relationId)
    monitorRouteRepository.routeState(context.value.routeId, relationId) match {
      case None =>
      case Some(state) =>
        val updatedState = state.copy(
          matchesGeometry = None,
          deviations = Seq.empty,
          happy = false
        )
        monitorRouteRepository.saveRouteState(updatedState)
        context.set(
          context.value.copy(
            stateChanged = true
          )
        )
    }

    context.value.reporter.stepActive("save")
    save()
    context.value.reporter.stepDone("save")
  }

  private def addRouteWithMultiGpxReference(): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = composeProcessList(monitorRouteRelation)
            context.value.reporter.processList(processList)

            val processListSize = processList.size
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/$processListSize ${mrr.relationId}") {
                context.value.reporter.stepActive(mrr.relationId.toString)
                val updateSingleRelationRoute = index == 0 && processList.sizeIs == 1

                monitorRouteRelationRepository.loadTopLevel(None, mrr.relationId).map { relation =>

                  val wayMembers = MonitorFilter.filterWayMembers(relation.wayMembers)
                  if (wayMembers.nonEmpty) {
                    val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)
                    val bounds = Util.mergeBounds(osmSegmentAnalysis.routeSegments.map(_.segment.bounds))

                    val id = if (context.value.isActionUpdate || context.value.isActionGpxUpload) {
                      context.value.oldStateIds.find(_.relationId == mrr.relationId) match {
                        case Some(oldStateId) => oldStateId._id
                        case None => ObjectId()
                      }
                    }
                    else {
                      ObjectId()
                    }

                    val state = MonitorRouteState(
                      id,
                      routeId = context.value.routeId,
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
                    context.set(
                      context.value.copy(
                        stateChanged = true
                      )
                    )
                  }
                }
              }
            }
        }
    }
  }

  private def analyzeMultiGpx(route: MonitorRoute): Unit = {
    route.relation match {
      case None =>
      case Some(rootMonitorRouteRelation) =>
        val monitorRouteRelations = composeProcessList(rootMonitorRouteRelation)
        val monitorRouteRelationsSize = monitorRouteRelations.size
        monitorRouteRelations.zipWithIndex.foreach { case (monitorRouteRelation, index) =>
          Log.context(s"${index + 1}/$monitorRouteRelationsSize ${monitorRouteRelation.relationId}") {
            if (monitorRouteRelation.referenceTimestamp.nonEmpty && monitorRouteRelation.referenceFilename.nonEmpty) {
              monitorRouteRepository.routeReference(route._id, Some(monitorRouteRelation.relationId)) match {
                case None => log.error("could not find reference")
                case Some(reference) =>
                  analyzeReference(reference, None) match {
                    case None => log.error("could not analyze")
                    case Some(newState) =>
                      val shouldUpdate = monitorRouteRepository.routeState(route._id, monitorRouteRelation.relationId) match {
                        case None => true
                        case Some(oldState) =>
                          newState.copy(timestamp = null) != oldState.copy(timestamp = null)
                      }

                      if (shouldUpdate) {
                        monitorRouteRepository.saveRouteState(newState)
                        context.set(
                          context.value.copy(
                            stateChanged = true
                          )
                        )
                      }
                  }
              }
            }
          }
        }
    }
  }

  private def analyzeGpx(route: MonitorRoute): Unit = {
    monitorRouteRepository.routeReference(route._id, route.relationId) match {
      case None => log.error("reference not found")
      case Some(reference) =>
        analyzeReference(reference, None) match {
          case None => log.error("could not analyze")
          case Some(newState) =>

            val shouldUpdate = route.relationId match {
              case None => false
              case Some(relationId) =>
                monitorRouteRepository.routeState(route._id, relationId) match {
                  case None => true
                  case Some(oldState) =>
                    newState.copy(timestamp = null) != oldState.copy(timestamp = null)
                }
            }

            if (shouldUpdate) {
              monitorRouteRepository.saveRouteState(newState)
              context.set(
                context.value.copy(
                  stateChanged = true
                )
              )
            }
        }
    }
  }

  private def analyzeOsm(route: MonitorRoute): Unit = {
    route.relation match {
      case None =>
      case Some(rootMonitorRouteRelation) =>
        val monitorRouteRelations = composeProcessList(rootMonitorRouteRelation)
        val monitorRouteRelationsSize = monitorRouteRelations.size
        monitorRouteRelations.zipWithIndex.foreach { case (monitorRouteRelation, index) =>
          Log.context(s"${index + 1}/$monitorRouteRelationsSize ${monitorRouteRelation.relationId}") {
            monitorRouteRepository.routeReference(route._id, Some(monitorRouteRelation.relationId)) match {
              case None =>
                log.info("could not find reference") // TODO ???
              case Some(reference) =>
                analyzeReference(reference, None) match {
                  case None =>
                    log.error("could not analyze")
                  case Some(newState) =>

                    val shouldUpdate = monitorRouteRepository.routeState(route._id, monitorRouteRelation.relationId) match {
                      case None =>
                        true
                      case Some(oldState) =>
                        newState.copy(timestamp = null) != oldState.copy(timestamp = null)
                    }

                    if (shouldUpdate) {
                      monitorRouteRepository.saveRouteState(newState)
                      context.set(
                        context.value.copy(
                          stateChanged = true
                        )
                      )
                    }
                }
            }
          }
        }
    }
  }

  private def updateSubRelationOsmReferences(): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = composeProcessList(monitorRouteRelation)
            val processListSize = processList.size
            context.value.reporter.processList(processList)
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/$processListSize ${mrr.relationId}") {
                context.value.reporter.stepActive(mrr.relationId.toString)
                val updateSingleRelationRoute = index == 0 && processList.sizeIs == 1
                updateSubRelationOsmReference(mrr, updateSingleRelationRoute)
              }
            }
        }
    }
  }

  private def updateSubRelationOsmReference(
    monitorRouteRelation: MonitorRouteRelation,
    updateSingleRelationRoute: Boolean
  ): Unit = {
    val referenceTimestamp = context.value.newRoute.get.referenceTimestamp
    val subs = monitorRouteRelation.relations.map(_.relationId).mkString("(", ",", ")")
    log.info(s"${monitorRouteRelation.name}    $subs")
    monitorRouteRelationRepository.loadTopLevel(referenceTimestamp, monitorRouteRelation.relationId) match {
      case None =>
        val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.map(_.yyyymmddhhmmss).getOrElse(Time.now.yyyymmddhhmmss)}"
        context.value.reporter.report(
          MonitorRouteUpdateStatusMessage(
            errors = Some(Seq(error))
          )
        )
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

          val geomFactory = new GeometryFactory
          val geometryCollection = new GeometryCollection(analysis.routeSegments.flatMap(_.lineStrings).toArray, geomFactory)
          val geoJsonWriter = new GeoJsonWriter()
          geoJsonWriter.setEncodeCRS(false)
          val geometry = geoJsonWriter.write(geometryCollection)

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
            "osm",
            context.value.newRoute.get.referenceTimestamp.get,
            analysis.osmDistance,
            analysis.routeSegments.size,
            None,
            geometry
          )

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

          analyzeReference(ref, currentRelation) match {
            case Some(state) =>
              monitorRouteRepository.saveRouteState(state)
              context.set(
                context.value.copy(
                  stateChanged = true
                )
              )

            case None =>
              val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.get.yyyymmddhhmmss}"
              context.value.reporter.report(
                MonitorRouteUpdateStatusMessage(
                  errors = Some(Seq(error))
                )
              )
          }
        }
    }
  }

  private def analyzeSubRelations(): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = composeProcessList(monitorRouteRelation)
            val processListSize = processList.size
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/$processListSize ${mrr.relationId}") {
                analyzeSubRelation(mrr)
              }
            }
        }
    }
  }

  private def analyzeSubRelation(monitorRouteRelation: MonitorRouteRelation): Unit = {
    monitorRouteRelationRepository.loadTopLevel(None, monitorRouteRelation.relationId) match {
      case None =>
        val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${Time.now.yyyymmddhhmmss}"
        monitorRouteRepository.deleteRouteState(context.value.routeId, monitorRouteRelation.relationId)

      case Some(subRelation) =>
        monitorRouteRepository.routeReference(context.value.routeId, Some(monitorRouteRelation.relationId)) match {
          case None =>
          case Some(reference) =>
            analyzeReference(reference, Some(subRelation)) match {
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

            val reference = MonitorRouteReference(
              ObjectId(),
              routeId = context.value.routeId,
              relationId = context.value.relationId,
              timestamp = Time.now,
              user = context.value.user,
              referenceBounds = referenceBounds,
              referenceType = "gpx",
              referenceTimestamp = referenceTimestamp,
              referenceDistance = referenceDistance,
              referenceSegmentCount = referenceSegmentCount,
              referenceFilename = context.value.update.referenceFilename,
              referenceGeoJson = referenceGeoJson
            )

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

            analyze(reference)

          case None =>
            monitorRouteRepository.routeReference(context.value.routeId, None) match {
              case None =>
              case Some(reference) =>
                if (reference.relationId != context.value.update.relationId) {
                  context.value.reporter.report(
                    MonitorRouteUpdateStatusMessage(
                      commands = Seq(
                        MonitorRouteUpdateStatusCommand("step-add", "analyze"),
                      )
                    )
                  )
                  val updatedReference = reference.copy(
                    relationId = context.value.update.relationId
                  )
                  monitorRouteRepository.saveRouteReference(updatedReference)
                  context.set(
                    context.value.copy(
                      newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(updatedReference),
                    )
                  )
                  analyze(updatedReference)
                }
            }
        }

      case Some(referenceGpx) =>

        context.value.reporter.report(
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

        val objectId = context.value.oldReferenceIds.filter(_.relationId == context.value.relationId).map(_._id).headOption.getOrElse(ObjectId())

        val reference = MonitorRouteReference(
          objectId,
          routeId = context.value.routeId,
          relationId = context.value.relationId,
          timestamp = now,
          user = context.value.user,
          referenceBounds = referenceBounds,
          referenceType = "gpx",
          referenceTimestamp = referenceTimestamp,
          referenceDistance = referenceDistance,
          referenceSegmentCount = referenceSegmentCount,
          referenceFilename = context.value.update.referenceFilename,
          referenceGeoJson = referenceGeoJson
        )

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

        analyze(reference)
    }
  }

  private def analyze(reference: MonitorRouteReference): Unit = {
    context.value.reporter.stepActive("analyze")
    analyzeReference(reference, None) match {
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

  private def findGroup(): Unit = {
    val groupName = context.value.update.groupName
    val group = monitorGroupRepository.groupByName(groupName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find group with name "$groupName""""
      )
    }
    context.set(
      context.value.copy(
        group = Some(group)
      )
    )
  }

  private def findRoute(): MonitorRoute = {
    val routeName = context.value.update.routeName
    val route = monitorRouteRepository.routeByName(context.value.group.get._id, routeName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find route with name "$routeName" in group "${context.value.group.get.name}""""
      )
    }
    context.set(
      context.value.copy(
        oldRoute = Some(route)
      )
    )
    route
  }

  private def assertNewRoute(): Unit = {
    val group = context.value.group.get
    val routeName = context.value.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None => // OK: no route with this name yet
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }

  private def save(): Unit = {

    if (context.value.newReferenceSummaries.nonEmpty) {
      monitorRouteRepository.superRouteReferenceSummary(context.value.routeId) match {
        case None =>
        case Some(referenceDistance) =>
          context.value.newRoute match {
            case None =>
              context.value.oldRoute match {
                case None =>
                case Some(oldRoute) =>

                  val updatedRelation = if (context.value.isReferenceTypeMultiGpx) {
                    oldRoute.relation.map { monitorRouteRelation =>
                      udpateMonitorRouteRelation(context.value, monitorRouteRelation)
                    }
                  }
                  else {
                    oldRoute.relation
                  }

                  val updatedRoute = oldRoute.copy(
                    referenceDistance = if (context.value.isReferenceTypeOsm) referenceDistance else 0,
                    relation = updatedRelation
                  )

                  context.set(
                    context.value.copy(
                      newRoute = Some(updatedRoute)
                    )
                  )
              }

            case Some(newRoute) =>
              val updatedRelation = if (context.value.isReferenceTypeMultiGpx) {
                newRoute.relation.map { monitorRouteRelation =>
                  udpateMonitorRouteRelation(context.value, monitorRouteRelation)
                }
              }
              else {
                newRoute.relation
              }
              val updatedRoute = newRoute.copy(
                referenceDistance = referenceDistance,
                relation = updatedRelation
              )
              context.set(
                context.value.copy(
                  newRoute = Some(updatedRoute)
                )
              )
          }
      }
    }

    if (context.value.structureChanged || context.value.stateChanged) {

      val stateSummaries = monitorRouteRepository.routeStateSummaries(context.value.routeId)
      val relation = context.value.route.relation.map(relation => updatedMonitorRouteRelation(relation, stateSummaries))
      val relationWithDistances = relation.map(updatedMonitorRouteRelationCumulativeDistance)

      val monitorRouteSegmentInfos = monitorRouteRepository.routeStateSegments(context.value.routeId)
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
      val deviationCount = stateSummaries.map(_.deviationCount).sum
      val deviationDistance = stateSummaries.map(_.deviationDistance).sum

      context.set(
        context.value.copy(
          newRoute = Some(
            context.value.route.copy(
              symbol = symbol,
              relation = relationWithGaps,
              osmWayCount = osmWayCount,
              osmDistance = osmDistance,
              deviationCount = deviationCount,
              deviationDistance = deviationDistance
            )
          )
        )
      )

      val happy = superRouteSuperSegments.sizeIs == 1 &&
        context.value.newRoute.map(_.deviationCount).sum == 0 &&
        context.value.newRoute.get.relation.exists(_.happy)

      val updatedRoute = context.value.route.copy(
        osmSegments = superRouteSuperSegments,
        osmSegmentCount = superRouteSuperSegments.size,
        happy = happy
      )
      context.set(
        context.value.copy(
          newRoute = Some(updatedRoute)
        )
      )
    }

    val analysisDuration = System.currentTimeMillis() - context.value.analysisStartMillis.get

    val savedRoute = context.value.route.copy(
      analysisTimestamp = Some(Time.now),
      analysisDuration = Some(analysisDuration)
    )
    monitorRouteRepository.saveRoute(savedRoute)
  }

  private def updatedMonitorRouteRelation(monitorRouteRelation: MonitorRouteRelation, stateSummaries: Seq[MonitorRouteStateSummary]): MonitorRouteRelation = {

    val updatedWithState = if (context.value.isReferenceTypeGpx) {
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

    if (context.value.isActionGpxDelete && context.value.update.relationId.get == monitorRouteRelation.relationId) {
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

  private def analyzeReference(reference: MonitorRouteReference, currentRelation: Option[Relation]): Option[MonitorRouteState] = {
    reference.relationId.flatMap { relationId =>
      val relationOption = if (currentRelation.nonEmpty) {
        currentRelation
      }
      else if (context.value.isReferenceTypeGpx) {
        monitorRouteRelationRepository.load(None, relationId)
      }
      else {
        monitorRouteRelationRepository.loadTopLevel(None, relationId)
      }
      relationOption.flatMap { relation =>
        if (context.value.isReferenceTypeGpx) {
          updateSubRelationOsmInfo(relation)
        }

        val allWayMembers = if (context.value.isReferenceTypeGpx) {
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
          routeAnalysis.osmSegments.sizeIs == 1

        val id = if (context.value.isActionAnalyze || context.value.isActionUpdate || context.value.isActionGpxUpload) {
          context.value.oldStateIds.find(_.relationId == relationId) match {
            case Some(oldStateId) => oldStateId._id
            case None => ObjectId()
          }
        }
        else {
          ObjectId()
        }

        Some(
          MonitorRouteState(
            id,
            context.value.routeId,
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
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        val updatedRelation = newRoute.relation.map { monitorRouteRelation =>
          updateSubRelationOsmInfo(relation, monitorRouteRelation)
        }
        context.set(
          context.value.copy(
            newRoute = Some(
              newRoute.copy(
                relation = updatedRelation
              )
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

  private def removeObsoleteReferences(): MonitorUpdateContext = {
    context.value.newRoute match {
      case None => context.value
      case Some(newRoute) =>
        val oldReferenceType = context.value.oldRoute.map(_.referenceType)
        if (newRoute.referenceType == "multi-gpx" && !oldReferenceType.contains("multi-gpx")) {
          context.value.oldReferenceIds.foreach { referenceId =>
            monitorRouteRepository.deleteRouteReferenceById(referenceId._id)
          }
          context.value.copy(
            newRoute = Some(newRoute.copy(referenceDistance = 0))
          )
        }
        else {
          if (newRoute.referenceType == "osm") {
            val allRelationIds = newRoute.relationId.toSeq ++ MonitorUtil.subRelationsIn(newRoute).map(_.relationId)
            if (allRelationIds.isEmpty) {
              monitorRouteRepository.deleteRouteReferences(newRoute._id)
            }
            else {
              val obsoleteReferenceIds = context.value.oldReferenceIds.filter { oldReferenceId =>
                oldReferenceId.relationId match {
                  case Some(relationId) => !allRelationIds.contains(relationId)
                  case None => true
                }
              }
              obsoleteReferenceIds.foreach { referenceId =>
                monitorRouteRepository.deleteRouteReferenceById(referenceId._id)
              }
            }
          }
          context.value
        }
    }
  }

  private def removeObsoleteStates(): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        val allRelationIds = newRoute.relationId.toSeq ++ MonitorUtil.subRelationsIn(newRoute).map(_.relationId)
        if (allRelationIds.isEmpty) {
          monitorRouteRepository.deleteRouteStates(newRoute._id)
        }
        else {
          val obsoleteStateIds = context.value.oldStateIds.filterNot(id => allRelationIds.contains(id.relationId))
          obsoleteStateIds.foreach { stateId =>
            monitorRouteRepository.deleteRouteStateById(stateId._id)
          }
        }
    }
  }
}
