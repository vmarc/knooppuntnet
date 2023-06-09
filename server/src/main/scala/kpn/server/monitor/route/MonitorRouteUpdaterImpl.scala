package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.common.monitor.MonitorRouteUpdateStep
import kpn.api.common.Bounds
import kpn.api.custom.ApiResponse
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.springframework.stereotype.Component

import scala.xml.Elem

@Component
class MonitorRouteUpdaterImpl(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateRoute: MonitorUpdateRoute,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorUpdateReference: MonitorUpdateReference,
  monitorUpdateAnalyzer: MonitorUpdateAnalyzer,
  saver: MonitorUpdateSaver,
  monitorRouteRelationAnalyzer: MonitorRouteRelationAnalyzer,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer
) extends MonitorRouteUpdater {

  private val log = Log(classOf[MonitorRouteUpdaterImpl])

  def update(
    user: String,
    update: MonitorRouteUpdate,
    reporter: MonitorUpdateReporter
  ): Unit = {
    Log.context(Seq("route-update", s"group=${update.groupName}", s"route=${update.routeName}")) {
      var context = MonitorUpdateContext(
        user,
        reporter,
        update.referenceType,
        update.referenceFilename,
        update.referenceGpx
      )
      try {
        if (update.action == "add") {
          val steps = if (update.referenceType == "gpx") {
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
              MonitorRouteUpdateStep("upload"),
              MonitorRouteUpdateStep("analyze"),
            )
          }
          else {
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
            )
          }

          context = context.withStatus(
            context.status.copy(steps = steps)
          )

          context = context.copy(
            group = Some(findGroup(update.groupName))
          )

          context = assertNewRoute(context, update.routeName)
          context = monitorUpdateRoute.update(context, user, update)
          context = monitorUpdateStructure.update(context)

          context = updateSubRelations(context)

          // context = monitorUpdateReference.update(context)
          // context = monitorUpdateAnalyzer.analyze(context)
          context = saver.save(context)
        }
        else if (update.action == "update") {
          val steps = if (update.referenceType == "gpx") {
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
              MonitorRouteUpdateStep("upload"),
              MonitorRouteUpdateStep("analyze"),
            )
          }
          else {
            Seq(
              MonitorRouteUpdateStep("definition", "busy"),
            )
          }

          context = context.withStatus(
            context.status.copy(steps = steps)
          )

          context = context.copy(
            group = Some(findGroup(update.groupName))
          )

          context = findRoute(context, update.routeName)
          context = monitorUpdateRoute.update(context, user, update)
          context = monitorUpdateStructure.update(context)

          // TODO pick up information about existing state and references, and delete the ones that are not the structure anymore

          context = updateSubRelations(context)

          // context = monitorUpdateReference.update(context)
          // context = monitorUpdateAnalyzer.analyze(context)
          context = saver.save(context)

          if (context.saveResult.errors.nonEmpty) {
            context.withStatus(
              context.status.copy(errors = context.saveResult.errors)
            )
          }
        }
      }
      catch {
        case e: RuntimeException =>
          log.error(s"Could not update route", e)
          context.withStatus(
            context.status.copy(exception = Some(e.getMessage))
          )
      }
    }
  }

  private def updateSubRelations(originalContext: MonitorUpdateContext): MonitorUpdateContext = {
    var context = originalContext
    context.newRoute match {
      case None =>
      case Some(newRoute) =>
        newRoute.relation match {
          case None =>
          case Some(monitorRouteRelation) =>
            val processList = composeProcessList(monitorRouteRelation)
            processList.zipWithIndex.foreach { case (mrr, index) =>
              Log.context(s"${index + 1}/${processList.size} ${mrr.relationId}") {
                val updateSingleRelationRoute = index == 0 && processList.size == 1
                context = updateSubRelation(context, mrr, updateSingleRelationRoute)
              }
            }
        }
    }
    context
  }

  private def updateSubRelation(
    originalContext: MonitorUpdateContext,
    monitorRouteRelation: MonitorRouteRelation,
    updateSingleRelationRoute: Boolean
  ): MonitorUpdateContext = {
    var context = originalContext

    val referenceTimestamp = context.newRoute.get.referenceTimestamp
    val subs = monitorRouteRelation.relations.map(_.relationId).mkString("(", ",", ")")
    log.info(s"${monitorRouteRelation.name}    $subs")
    val referenceOption = log.infoElapsed {

      val rrr = monitorRouteRelationRepository.loadTopLevel(referenceTimestamp, monitorRouteRelation.relationId) match {
        case None =>
          val error = s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.map(_.yyyymmddhhmmss).getOrElse(Time.now.yyyymmddhhmmss)}"
          context.withStatus(
            context.status.copy(errors = context.status.errors :+ error)
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

            val ref = MonitorRouteReference(
              ObjectId(),
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

            if (updateSingleRelationRoute) {
              context = context.copy(
                newRoute = Some(
                  context.newRoute.get.copy(
                    referenceDistance = ref.distance
                  )
                )
              )
            }

            Some(ref)
          }
      }
      ("build reference", rrr)
    }

    referenceOption.foreach { reference =>
      monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference) match {
        case Some(state) =>
          monitorRouteRepository.saveRouteState(state)
          context = context.copy(
            newStates = context.newStates :+ state
          )

        case None =>
          context = context.copy(
            saveResult = context.saveResult.copy(
              errors = context.saveResult.errors :+ s"Could not load relation ${monitorRouteRelation.relationId} at ${referenceTimestamp.get.yyyymmddhhmmss}"
            )
          )
      }
    }

    context
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

  def add(
    user: String,
    groupName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {
    throw new RuntimeException("deprecated")
    /*
        Log.context(Seq("route-add", s"group=$groupName", s"route=${properties.name}")) {
          try {
            var context = MonitorUpdateContext(user, findGroup(groupName), new MonitorUpdateReporterLogger())
            context = assertNewRoute(context, properties.name)
            context = context.copy(
              referenceType = Some(properties.referenceType)
            )
            context = monitorUpdateRoute.update(context, user, properties)
            context = monitorUpdateStructure.update(context)
            context = monitorUpdateReference.update(context)
            context = monitorUpdateAnalyzer.analyze(context)
            context = saver.save(context)
            context.saveResult
          }
          catch {
            case e: RuntimeException =>
              log.error(s"Could not add route", e)
              MonitorRouteSaveResult(
                exception = Some(e.getMessage)
              )
          }
        }
    */
  }

  def oldUpdate(
    user: String,
    groupName: String,
    routeName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult = {
    throw new RuntimeException("deprecated")
    /*
        Log.context(Seq("route-update", s"group=$groupName", s"route=$routeName")) {
          try {
            var context = MonitorUpdateContext(user, findGroup(groupName), new MonitorUpdateReporterLogger())
            context = findRoute(context, routeName)
            context = context.copy(
              referenceType = Some(properties.referenceType)
            )
            context = monitorUpdateRoute.update(context, user, properties)
            context = monitorUpdateStructure.update(context)
            context = monitorUpdateReference.update(context)
            context = monitorUpdateAnalyzer.analyze(context)
            context = saver.save(context)
            context.saveResult
          }
          catch {
            case e: RuntimeException =>
              log.error(s"Could not update route", e)
              MonitorRouteSaveResult(
                exception = Some(e.getMessage)
              )
          }
        }

     */
  }

  override def upload(
    user: String,
    groupName: String,
    routeName: String,
    relationId: Option[Long],
    referenceTimestamp: Timestamp,
    filename: String,
    xml: Elem
  ): MonitorRouteSaveResult = {
    throw new RuntimeException("deprecated")
    /*
        Log.context(Seq("route-upload", s"group=$groupName", s"route=$routeName")) {
          try {
            var context = MonitorUpdateContext(user, findGroup(groupName), new MonitorUpdateReporterLogger())
            context = findRoute(context, routeName)
            context = context.copy(
              referenceType = Some(context.oldRoute.get.referenceType)
            )
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
              relationId = relationId,
              timestamp = now,
              user = user,
              bounds = bounds,
              referenceType = "gpx",
              referenceTimestamp = referenceTimestamp,
              distance = distance,
              segmentCount = segmentCount,
              filename = Some(filename),
              geoJson = geoJson
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
                  referenceTimestamp = Some(referenceTimestamp),
                  referenceFilename = reference.filename,
                  referenceDistance = referenceDistance,
                )

                context = context.copy(
                  newRoute = Some(updatedRoute)
                )

                context = monitorUpdateAnalyzer.analyze(context)

                val monitorRouteRelationOption = context.oldRoute.get.relation.map { oldMonitorRouteRelation =>
                  val state = context.newStates.head
                  oldMonitorRouteRelation.copy(
                    referenceTimestamp = Some(referenceTimestamp),
                    referenceFilename = Some(filename),
                    referenceDistance = referenceDistance,
                    deviationDistance = state.deviations.map(_.distance).sum,
                    deviationCount = state.deviations.size,
                    osmWayCount = state.wayCount,
                    osmDistance = state.osmDistance,
                    osmSegmentCount = state.osmSegments.size,
                    happy = state.happy,
                    relations = Seq.empty
                  )
                }

                val updatedRoute2 = context.oldRoute.get.copy(
                  relation = monitorRouteRelationOption,
                )

                context = context.copy(
                  newRoute = Some(updatedRoute2)
                )

                context = saver.save(context)

              case Some("multi-gpx") =>

                // TODO perform analysis of the sub-relation !!

                val stateOption = monitorRouteRelationAnalyzer.analyzeReference(context.routeId, reference)
                stateOption match {
                  case None =>
                  case Some(state) =>
                    context = context.copy(
                      newStates = context.newStates :+ state,
                      saveResult = context.saveResult.copy(
                        analyzed = true
                      )
                    )
                }

                val monitorRouteRelationOption = context.oldRoute.get.relation.map { oldMonitorRouteRelation =>
                  updateMonitorRouteRelation(oldMonitorRouteRelation, reference, stateOption)
                }

                val updatedRoute2 = context.oldRoute.get.copy(
                  relation = monitorRouteRelationOption,
                )

                context = context.copy(
                  newRoute = Some(updatedRoute2)
                )

                context = saver.save(context)

              case _ =>
            }
            context.saveResult
          }
          catch {
            case e: RuntimeException =>
              log.error(s"Could not upload reference", e)
              MonitorRouteSaveResult(
                exception = Some(e.getMessage)
              )
          }
        }
     */
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
            referenceFilename = reference.filename,
            referenceDistance = reference.distance,
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

  override def resetSubRelationGpxReference(groupName: String, routeName: String, subRelationId: Long): Unit = {
    throw new RuntimeException("deprecated")
    /*
    monitorGroupRepository.groupByName(groupName).foreach { group =>
      monitorRouteRepository.routeByName(group._id, routeName).foreach { route =>
        monitorRouteRepository.deleteRouteReference(route._id, subRelationId)
        monitorRouteRepository.deleteRouteState(route._id, subRelationId)
        val updatedRoute = route.copy(
          relation = route.relation.map(rel => resetReference(rel, subRelationId))
        )
        monitorRouteRepository.saveRoute(updatedRoute)
      }
    }
   */
  }

  private def resetReference(relation: MonitorRouteRelation, subRelationId: Long): MonitorRouteRelation = {
    if (relation.relationId == subRelationId) {
      relation.copy(
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
      )
    }
    else {
      relation.copy(
        relations = relation.relations.map(rel => resetReference(rel, subRelationId))
      )
    }
  }

  def analyze(groupName: String, routeName: String): ApiResponse[MonitorRouteSaveResult] = {
    throw new RuntimeException("deprecated")
    null
  }

  override def analyzeAll(group: MonitorGroup, routeId: ObjectId): Unit = {
    throw new RuntimeException("deprecated")
    /*
        monitorRouteRepository.routeById(routeId) match {
          case None => log.error(s"Route ${routeId.oid} not found")
          case Some(route) =>

            try {
              val oldReferences = monitorRouteRepository.routeReferences(routeId)

              var context = MonitorUpdateContext("", group, new MonitorUpdateReporterLogger()).copy(
                oldRoute = Some(route),
                referenceType = Some(route.referenceType),
                oldReferences = oldReferences
              )
              context = monitorUpdateAnalyzer.analyze(context)
              context = saver.save(context)
            }
            catch {
              case e: RuntimeException =>
                log.error(s"Could not analyzeAll(routeId=${routeId.oid})", e)
            }
        }
     */
  }

  override def analyzeRelation(group: MonitorGroup, routeId: ObjectId, relationId: Long): Unit = {
    throw new RuntimeException("deprecated")
    /*
        monitorRouteRepository.routeById(routeId) match {
          case None => log.error(s"Route ${routeId.oid} not found")
          case Some(route) =>
            monitorRouteRepository.routeRelationReference(routeId, relationId) match {
              case None => log.error(s"Route ${route.name}, reference for relation $relationId not found")
              case Some(reference) =>
                var context = MonitorUpdateContext("", group, new MonitorUpdateReporterLogger()).copy(
                  oldRoute = Some(route),
                  referenceType = Some(route.referenceType),
                  oldReferences = Seq(reference)
                )
                context = monitorUpdateAnalyzer.analyze(context)
                context = saver.save(context)
            }
        }
     */
  }

  private def findGroup(groupName: String): MonitorGroup = {
    monitorGroupRepository.groupByName(groupName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find group with name "$groupName""""
      )
    }
  }

  private def findRoute(context: MonitorUpdateContext, routeName: String): MonitorUpdateContext = {
    monitorRouteRepository.routeByName(context.group.get._id, routeName) match {
      case Some(route) => context.copy(oldRoute = Some(route))
      case None =>
        throw new IllegalArgumentException(
          s"""Could not find route with name "$routeName" in group "${context.group.get.name}""""
        )
    }
  }

  private def assertNewRoute(context: MonitorUpdateContext, routeName: String): MonitorUpdateContext = {
    monitorRouteRepository.routeByName(context.group.get._id, routeName) match {
      case None => context
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${context.group.get.name}""""
        )
    }
  }
}
