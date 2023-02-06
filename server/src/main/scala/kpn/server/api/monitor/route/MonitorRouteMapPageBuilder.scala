package kpn.server.api.monitor.route

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteReferenceInfo
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.core.util.Log
import kpn.core.util.Triplet
import kpn.core.util.Util
import kpn.server.api.monitor.MonitorUtil
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
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
    relationId: Option[Long],
    log: Log = MonitorRouteMapPageBuilder.log
  ): Option[MonitorRouteMapPage] = {

    Log.context(s"$groupName/$routeName/${relationId.getOrElse("-")}") {
      monitorGroupRepository.groupByName(groupName) match {
        case Some(group) => build(group, routeName, relationId, log)
        case None =>
          log.error(s"""Group "$groupName" does not exist""")
          None
      }
    }
  }

  private def build(
    group: MonitorGroup,
    routeName: String,
    relationId: Option[Long],
    log: Log
  ): Option[MonitorRouteMapPage] = {

    monitorRouteRepository.routeByName(group._id, routeName) match {
      case Some(route) =>
        Some(
          build(group, route, relationId)
        )

      case None =>
        log.error(s"""Route "$routeName" does not exist""")
        None
    }
  }

  private def build(group: MonitorGroup, route: MonitorRoute, relationId: Option[Long]): MonitorRouteMapPage = {
    relationId match {
      case Some(relId) => buildPageForRelationId(group, route, relId)
      case None => buildPageWithoutRelationIdSpecified(group, route)
    }
  }

  private def buildPageForRelationId(
    group: MonitorGroup,
    route: MonitorRoute,
    relationId: Long
  ): MonitorRouteMapPage = {

    route.relation match {
      case None => throw new IllegalStateException("Mandatory route structure (route.relation) not found")
      case Some(relation) =>
        if (relation.relations.nonEmpty) {
          buildSuperRoutePage(group, route, relationId)
        }
        else {
          if (relationId != relation.relationId) {
            throw new IllegalStateException(
              s"""Requested relationId "$relationId" does not match route relationId "${relation.relationId}""""
            )
          }
          buildSimpleRoutePage(group, route, relationId)
        }
    }
  }

  private def buildPageWithoutRelationIdSpecified(group: MonitorGroup, route: MonitorRoute): MonitorRouteMapPage = {

    route.relation match {
      case None =>
        if (route.referenceType == "gpx") {
          monitorRouteRepository.routeRelationReference(route._id, 0) match {
            case Some(reference) => buildPageWithGpxReferenceOnly(group, route, reference)
            case None => buildEmptyPage(group, route) // TODO or throw exception?
          }
        }
        else {
          buildEmptyPage(group, route)
        }

      case Some(relation) =>
        if (relation.relations.nonEmpty) {
          // build page with first sub-relation
          buildSuperRoutePage(group, route, relation.relations.head.relationId)
        }
        else {
          buildSimpleRoutePage(group, route, relation.relationId)
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
      bounds = None,
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
      reference.bounds,
      reference.distance,
      reference.referenceType,
      reference.referenceDay,
      reference.segmentCount,
      reference.filename,
      reference.geoJson
    )

    MonitorRouteMapPage(
      relationId = None,
      routeName = route.name,
      routeDescription = route.description,
      groupName = group.name,
      groupDescription = group.description,
      bounds = Some(reference.bounds),
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

    monitorRouteRepository.routeRelationReference(route._id, relationId) match {
      case None => buildEmptyPage(group, route) // TODO or throw exception?
      case Some(reference) =>

        val referenceInfo = Some(
          MonitorRouteReferenceInfo(
            reference.timestamp,
            reference.user,
            reference.bounds,
            reference.distance,
            reference.referenceType,
            reference.referenceDay,
            reference.segmentCount,
            reference.filename,
            reference.geoJson
          )
        )

        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val bounds = stateOption match {
          case None => Some(reference.bounds)
          case Some(state) =>
            if (state.bounds == Bounds()) {
              Some(reference.bounds)
            }
            else {
              Some(
                Util.mergeBounds(Seq(state.bounds, reference.bounds))
              )
            }
        }

        MonitorRouteMapPage(
          route.relationId,
          route.name,
          route.description,
          group.name,
          group.description,
          bounds,
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
    relationId: Long
  ): MonitorRouteMapPage = {

    val subRelations = MonitorUtil.subRelationsIn(route)

    val subRelationPages = Triplet.slide[MonitorRouteSubRelation](subRelations).find(_.current.relationId == relationId)


    val routeDescription = subRelationPages.map(_.current).map {
      subRelation =>
        val index = subRelations.zipWithIndex.find { case (subRelation, index) =>
          subRelation.relationId == relationId
        }.map(_._2).getOrElse(0)
        s"(${index + 1}/${subRelations.size}) ${subRelation.name}"
    }.getOrElse("?")

    monitorRouteRepository.routeRelationReference(route._id, relationId) match {
      case None =>

        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val bounds = stateOption.map(_.bounds)
        MonitorRouteMapPage(
          route.relationId,
          route.name,
          routeDescription,
          group.name,
          group.description,
          bounds,
          subRelationPages.map(_.current),
          subRelationPages.flatMap(_.previous),
          subRelationPages.flatMap(_.next),
          stateOption.toSeq.flatMap(_.osmSegments),
          stateOption.flatMap(_.matchesGeometry),
          stateOption.toSeq.flatMap(_.deviations),
          None,
          subRelations
        )

      case Some(reference) =>

        val referenceInfo = MonitorRouteReferenceInfo(
          reference.timestamp,
          reference.user,
          reference.bounds,
          reference.distance,
          reference.referenceType,
          reference.referenceDay,
          reference.segmentCount,
          reference.filename,
          reference.geoJson
        )
        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val bounds = stateOption match {
          case None => reference.bounds
          case Some(state) =>
            if (state.bounds == Bounds()) { // TODO this is not possible?
              reference.bounds
            }
            else {
              Util.mergeBounds(Seq(state.bounds, referenceInfo.bounds))
            }
        }

        MonitorRouteMapPage(
          route.relationId,
          route.name,
          routeDescription,
          group.name,
          group.description,
          Some(bounds),
          subRelationPages.map(_.current),
          subRelationPages.flatMap(_.previous),
          subRelationPages.flatMap(_.next),
          stateOption.toSeq.flatMap(_.osmSegments),
          stateOption.flatMap(_.matchesGeometry),
          stateOption.toSeq.flatMap(_.deviations),
          Some(referenceInfo),
          subRelations
        )
    }
  }
}
