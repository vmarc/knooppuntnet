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
      case Some(route) => Some(build(group, route, relationId))
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
            throw new IllegalStateException("Requested relationId does not match route relationId")
          }
          buildSimpleRoutePage(group, route, relationId)
        }
    }
  }

  private def buildPageWithoutRelationIdSpecified(group: MonitorGroup, route: MonitorRoute): MonitorRouteMapPage = {

    route.relation match {
      case None =>
        // route structure not known (osm id for route not filled in yet)
        if (route.referenceType == "gpx") {
          // include reference in page (there will be no state yet)

          val references = monitorRouteRepository.routeReferences(route._id) // TODO improve repository method
          val referenceInfo = if (references.size == 1) {
            val reference = references.head
            Some(
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
          }
          else {
            // TODO throw exception?
            None
          }

          referenceInfo match {
            case None => // TODO throw exception?
              MonitorRouteMapPage(
                relationId = None,
                routeName = route.name,
                routeDescription = route.description,
                groupName = group.name,
                groupDescription = group.description,
                bounds = None,
                prevSubRelation = None,
                nextSubRelation = None,
                osmSegments = Seq.empty,
                matchesGeoJson = None,
                deviations = Seq.empty,
                reference = None,
                subRelations = Seq.empty
              )


            case Some(reference) =>
              MonitorRouteMapPage(
                relationId = None,
                routeName = route.name,
                routeDescription = route.description,
                groupName = group.name,
                groupDescription = group.description,
                bounds = Some(reference.bounds),
                prevSubRelation = None,
                nextSubRelation = None,
                osmSegments = Seq.empty,
                matchesGeoJson = None,
                deviations = Seq.empty,
                reference = referenceInfo,
                subRelations = Seq.empty
              )
          }
        }
        else {
          MonitorRouteMapPage(
            relationId = None,
            routeName = route.name,
            routeDescription = route.description,
            groupName = group.name,
            groupDescription = group.description,
            bounds = None,
            prevSubRelation = None,
            nextSubRelation = None,
            osmSegments = Seq.empty,
            matchesGeoJson = None,
            deviations = Seq.empty,
            reference = None,
            subRelations = Seq.empty
          )
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

  private def buildSimpleRoutePage(group: MonitorGroup, route: MonitorRoute, relationId: Long): MonitorRouteMapPage = {

    monitorRouteRepository.routeRelationReference(route._id, relationId) match {
      case None =>
      case Some(reference) =>
    }

    val references = monitorRouteRepository.routeReferences(route._id) // TODO improve repository method
    val referenceInfo = if (references.size == 1) {
      val reference = references.head
      Some(
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
    }
    else {
      None
    }

    val stateOption = route.relationId.flatMap(relId => monitorRouteRepository.routeState(route._id, relId))
    val bounds = stateOption match {
      case None => referenceInfo.map(_.bounds)
      case Some(state) =>
        referenceInfo match {
          case None => Some(state.bounds)
          case Some(ref) =>
            if (state.bounds == Bounds()) {
              Some(ref.bounds)
            }
            else {
              Some(
                Util.mergeBounds(Seq(state.bounds, ref.bounds))
              )
            }
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
      stateOption.toSeq.flatMap(_.osmSegments),
      stateOption.flatMap(_.matchesGeometry),
      stateOption.toSeq.flatMap(_.deviations),
      referenceInfo,
      Seq.empty
    )
  }


  private def buildSuperRoutePage(
    group: MonitorGroup,
    route: MonitorRoute,
    relationId: Long
  ): MonitorRouteMapPage = {

    val subRelations = MonitorUtil.subRelationsIn(route)

    val (prevSubRelation, nextSubRelation) =
      if (subRelations.nonEmpty) {
        Triplet.slide[MonitorRouteSubRelation](subRelations).find(_.current.relationId == relationId) match {
          case None => (None, None)
          case Some(triplet) => (triplet.previous, triplet.next)
        }
      }
      else {
        (None, None)
      }

    monitorRouteRepository.routeRelationReference(route._id, relationId) match {
      case None =>

        val stateOption = monitorRouteRepository.routeState(route._id, relationId)
        val bounds = stateOption.map(_.bounds)

        MonitorRouteMapPage(
          route.relationId,
          route.name,
          route.description,
          group.name,
          group.description,
          bounds,
          prevSubRelation,
          nextSubRelation,
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
          case None => referenceInfo.bounds
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
          route.description,
          group.name,
          group.description,
          Some(bounds),
          prevSubRelation,
          nextSubRelation,
          stateOption.toSeq.flatMap(_.osmSegments),
          stateOption.flatMap(_.matchesGeometry),
          stateOption.toSeq.flatMap(_.deviations),
          Some(referenceInfo),
          subRelations
        )
    }
  }
}
