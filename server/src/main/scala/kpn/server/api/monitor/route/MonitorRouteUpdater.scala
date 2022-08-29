package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdateResult
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.geotools.geojson.geom.GeometryJSON
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

import java.io.ByteArrayOutputStream

@Component
class MonitorRouteUpdater(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteAnalyzer: MonitorRouteAnalyzer
) {

  def add(user: String, groupName: String, properties: MonitorRouteProperties): Unit = {
    Log.context(Seq("add-route", s"group=$groupName", s"route=${properties.name}")) {
      val group = findGroup(groupName)
      assertNewRoute(group, properties.name)
      val relationId = properties.relationId.map(_.toLong)
      val route = MonitorRoute(
        ObjectId(),
        group._id,
        properties.name,
        properties.description,
        relationId,
      )
      monitorRouteRepository.saveRoute(route)

      if (properties.referenceType == "osm") {
        updateOsmReference(user, route, properties)
      }
      else if (properties.referenceType == "gpx") {
        // the route reference will be created at the time the gpxfile is uploaded
        // the route analysis will be done in a separate call, after the gpx file has been uploaded
      }
    }
  }

  def update(user: String, groupName: String, routeName: String, properties: MonitorRouteProperties): MonitorRouteUpdateResult = {
    Log.context(Seq("route-update", s"group=$groupName", s"route=$routeName")) {
      val group = findGroup(groupName)
      val route = findRoute(group._id, routeName)
      val reference = findRouteReference(route._id)

      if (isRouteChanged(route, properties)) {
        monitorRouteRepository.saveRoute(
          route.copy(
            name = properties.name,
            description = properties.description,
            relationId = properties.relationId.map(_.toLong)
          )
        )
      }

      if (properties.referenceType == "osm") {
        if (isOsmReferenceChanged(reference, properties) || isRelationIdChanged(route, properties)) {
          val updatedRoute = if (isRelationIdChanged(route, properties)) {
            route.copy(
              relationId = properties.relationId.map(_.toLong)
            )
          }
          else {
            route
          }
          updateOsmReference(user, updatedRoute, properties)
          MonitorRouteUpdateResult(reAnalyzed = true)
        }
        else {
          MonitorRouteUpdateResult(reAnalyzed = false)
        }
      }
      else if (properties.referenceType == "gpx") {
        if (properties.gpxFileChanged) {
          // reference has changed, but details will arrive in next api call
          // re-analyze only after reference has been updated
          MonitorRouteUpdateResult(reAnalyzed = false)
        }
        else if (isRelationIdChanged(route, properties)) {
          // reference does not change, but we have re-analyze because the relationId has changed
          monitorRouteAnalyzer.analyze(route, reference)
          MonitorRouteUpdateResult(reAnalyzed = true)
        }
        else {
          MonitorRouteUpdateResult(reAnalyzed = false)
        }
      }
      else {
        MonitorRouteUpdateResult(reAnalyzed = false)
      }
    }
  }

  def analyze(groupName: String, routeName: String): Unit = {
    Log.context(Seq("route-analyze", s"group=$groupName", s"route=$routeName")) {
      val group = findGroup(groupName)
      val route = findRoute(group._id, routeName)
      val reference = findRouteReference(route._id)
      monitorRouteAnalyzer.analyze(route, reference)
    }
  }

  private def updateOsmReference(user: String, route: MonitorRoute, properties: MonitorRouteProperties): Unit = {

    val relationId = findRelationId(properties)
    val osmReferenceDay = findOsmReferenceDay(properties)

    monitorRouteRelationRepository.load(Timestamp(osmReferenceDay), relationId) match {
      case None => // TODO MON return result with clear error message that says that relation did not exist at specified time!
      case Some(relation) =>
        val nodes = relation.wayMembers.flatMap(_.way.nodes)
        val bounds = Bounds.from(nodes)
        val routeSegments = MonitorRouteAnalysisSupport.toRouteSegments(relation)
        val geomFactory = new GeometryFactory
        val geometryCollection = new GeometryCollection(routeSegments.map(_.lineString).toArray, geomFactory)
        val baos = new ByteArrayOutputStream()
        val g = new GeometryJSON()
        g.write(geometryCollection, baos)
        val geometry = baos.toString

        val reference = MonitorRouteReference(
          ObjectId(),
          routeId = route._id,
          relationId = properties.relationId.map(_.toLong),
          key = "", // TODO MON remove???
          created = Time.now,
          user = user,
          bounds = bounds,
          referenceType = "osm",
          osmReferenceDay = properties.osmReferenceDay,
          segmentCount = routeSegments.size,
          filename = None,
          geometry = geometry
        )
        monitorRouteRepository.saveRouteReference(reference)
        monitorRouteAnalyzer.analyze(route, reference)
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

  private def findRouteReference(routeId: ObjectId): MonitorRouteReference = {
    monitorRouteRepository.currentRouteReference(routeId).getOrElse {
      throw new IllegalArgumentException(
        s"""${Log.contextString} Could not find reference for route with id "$routeId""""
      )
    }
  }

  private def findRelationId(properties: MonitorRouteProperties): Long = {
    properties.relationId.map(_.toLong).getOrElse {
      throw new IllegalArgumentException(s"""${Log.contextString} relationId is required when add route with referenceType="osm"""")
    }
  }

  private def findOsmReferenceDay(properties: MonitorRouteProperties): Day = {
    properties.osmReferenceDay.getOrElse {
      throw new IllegalArgumentException(s"""${Log.contextString} osmReferenceDay is required in route with referenceType="osm"""")
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

  private def isRouteChanged(route: MonitorRoute, properties: MonitorRouteProperties): Boolean = {
    route.name != properties.name ||
      route.description != properties.description ||
      route.relationId != properties.relationId.map(_.toLong)
    /* || TODO MON || route.groupId != properties.groupId */
  }

  private def isOsmReferenceChanged(reference: MonitorRouteReference, properties: MonitorRouteProperties): Boolean = {
    reference.referenceType != properties.referenceType ||
      reference.relationId != properties.relationId.map(_.toLong) ||
      reference.osmReferenceDay != properties.osmReferenceDay
  }

  private def isRelationIdChanged(route: MonitorRoute, properties: MonitorRouteProperties): Boolean = {
    route.relationId != properties.relationId.map(_.toLong)
  }
}
