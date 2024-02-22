package kpn.server.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.core.util.Log
import kpn.core.util.Triplet
import kpn.core.util.Util
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

object MonitorRouteMapPageBuilder {
  val log: Log = Log(classOf[MonitorRouteMapPageBuilder])
}

@Component
class MonitorRouteMapPageBuilder(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(
    groupName: String,
    routeName: String,
    subRelationIndex: Option[Int],
    log: Log = MonitorRouteMapPageBuilder.log
  ): Option[MonitorRouteMapPage] = {

    Log.context(s"$groupName/$routeName/${subRelationIndex.getOrElse("-")}") {
      monitorGroupRepository.groupByName(groupName) match {
        case Some(group) => build(group, routeName, subRelationIndex, log)
        case None =>
          log.error(s"""Group "$groupName" does not exist""")
          None
      }
    }
  }

  private def build(
    group: MonitorGroup,
    routeName: String,
    subRelationIndex: Option[Int],
    log: Log
  ): Option[MonitorRouteMapPage] = {

    monitorRouteRepository.routeByName(group._id, routeName) match {
      case Some(route) =>
        Some(
          build(group, route, subRelationIndex)
        )

      case None =>
        log.error(s"""Route "$routeName" does not exist""")
        None
    }
  }

  private def build(group: MonitorGroup, route: MonitorRoute, subRelationIndex: Option[Int]): MonitorRouteMapPage = {
    subRelationIndex match {
      case Some(index) => buildPageForSubRelationIndex(group, route, index)
      case None => buildPageWithoutRelationIdSpecified(group, route)
    }
  }

  private def buildPageForSubRelationIndex(
    group: MonitorGroup,
    route: MonitorRoute,
    subRelationIndex: Int
  ): MonitorRouteMapPage = {

    route.relation match {
      case None => throw new IllegalStateException("Mandatory route structure (route.relation) not found")
      case Some(relation) =>
        if (relation.relations.nonEmpty) {
          buildSuperRoutePage(group, route, subRelationIndex)
        }
        else {
          buildSimpleRoutePage(group, route, relation.relationId)
        }
    }
  }

  private def buildPageWithoutRelationIdSpecified(group: MonitorGroup, route: MonitorRoute): MonitorRouteMapPage = {

    route.relation match {
      case None =>
        if (route.referenceType == "gpx") {
          monitorRouteRepository.routeReference(route._id, None) match {
            case Some(reference) => buildPageWithGpxReferenceOnly(group, route, reference)
            case None => buildEmptyPage(group, route) // TODO or throw exception?
          }
        }
        else {
          buildEmptyPage(group, route)
        }

      case Some(relation) =>
        if (relation.relations.isEmpty || route.referenceType == "gpx") {
          // build page with first sub-relation
          buildSimpleRoutePage(group, route, relation.relationId)
        }
        else {
          val subRelationsWithWays = MonitorUtil.allRelationsIn(route).filter(_.wayCount > 0)
          if (subRelationsWithWays.isEmpty) {
            buildSimpleRoutePage(group, route, relation.relationId)
          }
          else {
            buildSuperRoutePage(group, route, 0)
          }
        }
    }
  }

  private def buildEmptyPage(group: MonitorGroup, route: MonitorRoute): MonitorRouteMapPage = {
    MonitorRouteMapPage(
      relationId = None,
      routeName = route.name,
      routeDescription = route.description,
      groupName = group.name,
      groupDescription = group.description,
      referenceType = route.referenceType,
      bounds = None,
      route.analysisTimestamp,
      currentSubRelation = None,
      previousSubRelation = None,
      nextSubRelation = None,
      osmSegments = Seq.empty,
      matchesGeoJson = None,
      deviations = Seq.empty,
      reference = None,
      subRelations = Seq.empty
    )
  }

  private def buildPageWithGpxReferenceOnly(
    group: MonitorGroup,
    route: MonitorRoute,
    reference: MonitorRouteReference
  ): MonitorRouteMapPage = {

    val referenceInfo = MonitorRouteReferenceInfo(
      reference.timestamp,
      reference.user,
      reference.referenceBounds,
      reference.referenceDistance,
      reference.referenceType,
      reference.referenceTimestamp,
      reference.referenceSegmentCount,
      reference.referenceFilename,
      reference.referenceGeoJson
    )

    MonitorRouteMapPage(
      relationId = None,
      routeName = route.name,
      routeDescription = route.description,
      groupName = group.name,
      groupDescription = group.description,
      referenceType = route.referenceType,
      bounds = Some(reference.referenceBounds),
      route.analysisTimestamp,
      currentSubRelation = None,
      previousSubRelation = None,
      nextSubRelation = None,
      osmSegments = Seq.empty,
      matchesGeoJson = None,
      deviations = Seq.empty,
      reference = Some(referenceInfo),
      subRelations = Seq.empty
    )
  }

  private def buildSimpleRoutePage(group: MonitorGroup, route: MonitorRoute, relationId: Long): MonitorRouteMapPage = {

    monitorRouteRepository.routeReference(route._id, Some(relationId)) match {
      case None => buildEmptyPage(group, route) // TODO or throw exception?
      case Some(reference) =>

        val referenceInfo = Some(
          MonitorRouteReferenceInfo(
            reference.timestamp,
            reference.user,
            reference.referenceBounds,
            reference.referenceDistance,
            reference.referenceType,
            reference.referenceTimestamp,
            reference.referenceSegmentCount,
            reference.referenceFilename,
            reference.referenceGeoJson
          )
        )

        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val bounds = stateOption match {
          case None => Some(reference.referenceBounds)
          case Some(state) =>
            if (state.bounds == Bounds()) {
              Some(reference.referenceBounds)
            }
            else {
              Some(
                Util.mergeBounds(Seq(state.bounds, reference.referenceBounds))
              )
            }
        }

        MonitorRouteMapPage(
          route.relationId,
          route.name,
          route.description,
          group.name,
          group.description,
          route.referenceType,
          bounds,
          route.analysisTimestamp,
          None,
          None,
          None,
          stateOption.toSeq.flatMap(_.osmSegments),
          stateOption.flatMap(_.matchesGeometry),
          stateOption.toSeq.flatMap(_.deviations),
          referenceInfo,
          Seq.empty
        )
    }
  }

  private def buildSuperRoutePage(
    group: MonitorGroup,
    route: MonitorRoute,
    subRelationIndex: Int
  ): MonitorRouteMapPage = {

    val subRelations = MonitorUtil.allRelationsIn(route).filter(_.wayCount > 0)
      .zipWithIndex.map { case (subRelation, index) => subRelation.copy(subRelationIndex = Some(index.toLong)) }

    val triplet = {
      val index = if (subRelationIndex < subRelations.size) {
        subRelationIndex
      }
      else {
        0
      }
      val current = subRelations(index)
      val previous = if (index > 0) {
        Some(subRelations(index - 1))
      }
      else {
        None
      }
      val next = if (index < subRelations.size - 1) {
        Some(subRelations(index + 1))
      }
      else {
        None
      }
      Triplet(previous, current, next)
    }

    val routeDescription = s"(${subRelationIndex + 1}/${subRelations.size}) ${triplet.current.name}"

    val relationId = triplet.current.relationId

    monitorRouteRepository.routeReference(route._id, Some(relationId)) match {
      case None =>

        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val referenceOption = monitorRouteRepository.routeReference(route._id, Some(relationId)).map { reference =>
          MonitorRouteReferenceInfo(
            reference.timestamp,
            reference.user,
            reference.referenceBounds,
            reference.referenceDistance,
            reference.referenceType,
            reference.referenceTimestamp,
            reference.referenceSegmentCount,
            reference.referenceFilename,
            reference.referenceGeoJson
          )
        }

        val bounds = stateOption.map(_.bounds)
        MonitorRouteMapPage(
          route.relationId,
          route.name,
          routeDescription,
          group.name,
          group.description,
          route.referenceType,
          bounds,
          route.analysisTimestamp,
          Some(triplet.current),
          triplet.previous,
          triplet.next,
          stateOption.toSeq.flatMap(_.osmSegments),
          stateOption.flatMap(_.matchesGeometry),
          stateOption.toSeq.flatMap(_.deviations),
          referenceOption,
          subRelations
        )

      case Some(reference) =>

        val referenceInfo = MonitorRouteReferenceInfo(
          reference.timestamp,
          reference.user,
          reference.referenceBounds,
          reference.referenceDistance,
          reference.referenceType,
          reference.referenceTimestamp,
          reference.referenceSegmentCount,
          reference.referenceFilename,
          reference.referenceGeoJson
        )
        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val bounds = stateOption match {
          case None => reference.referenceBounds
          case Some(state) =>
            if (state.bounds == Bounds()) { // TODO this is not possible?
              reference.referenceBounds
            }
            else {
              Util.mergeBounds(Seq(state.bounds, referenceInfo.referenceBounds))
            }
        }

        MonitorRouteMapPage(
          route.relationId,
          route.name,
          routeDescription,
          group.name,
          group.description,
          route.referenceType,
          Some(bounds),
          route.analysisTimestamp,
          Some(triplet.current),
          triplet.previous,
          triplet.next,
          stateOption.toSeq.flatMap(_.osmSegments),
          stateOption.flatMap(_.matchesGeometry),
          stateOption.toSeq.flatMap(_.deviations),
          Some(referenceInfo),
          subRelations
        )
    }
  }
}
