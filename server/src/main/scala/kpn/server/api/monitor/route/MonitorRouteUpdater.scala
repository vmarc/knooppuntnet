package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor._
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepository
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.springframework.stereotype.Component

object MonitorRouteUpdater {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
      val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
      val monitorRouteAnalyzer = new MonitorRouteAnalyzerImpl(monitorRouteRepository, overpassQueryExecutor)
      val updater = new MonitorRouteUpdater(
        monitorGroupRepository,
        monitorRouteRepository,
        monitorRouteRelationRepository,
        monitorRouteAnalyzer
      )
      updater.analyze("fr-iwn-Camino", "Voie-Toulouse")
    }
  }
}

@Component
class MonitorRouteUpdater(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteAnalyzer: MonitorRouteAnalyzer,
) {

  def add(user: String, groupName: String, properties: MonitorRouteProperties): MonitorRouteSaveResult = {
    Log.context(Seq("add-route", s"group=$groupName", s"route=${properties.name}")) {
      val group = findGroup(groupName)
      assertNewRoute(group, properties.name)

      val routeId = ObjectId()

      var context = MonitorUpdateContext(group, properties.referenceType)

      if (properties.referenceType.contains("osm")) {
        properties.relationId match {
          case None => None // TODO add error in MonitorRouteSaveResult ???
          case Some(relationId) =>
            properties.referenceDay match {
              case None => // TODO add error in MonitorRouteSaveResult ???
              case Some(referenceDay) =>
                monitorRouteRelationRepository.load(Some(Timestamp(referenceDay)), relationId) match {
                  case None => None // TODO add error in MonitorRouteSaveResult !!
                  case Some(relation) =>
                    val relations: Seq[Relation] = MonitorFilter.relationsInRelation(relation)
                    val references = relations.map { routeRelation =>

                      val wayMembers = MonitorFilter.filterWayMembers(routeRelation.wayMembers)
                      val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
                      val analysis = new MonitorRouteOsmSegmentAnalyzerImpl().analyze(wayMembers)

                      val geomFactory = new GeometryFactory
                      val geometryCollection = new GeometryCollection(analysis.routeSegments.map(_.lineString).toArray, geomFactory)
                      val geoJsonWriter = new GeoJsonWriter()
                      geoJsonWriter.setEncodeCRS(false)
                      val geometry = geoJsonWriter.write(geometryCollection)

                      MonitorRouteReference(
                        ObjectId(),
                        routeId,
                        Some(routeRelation.id),
                        Time.now,
                        user,
                        bounds,
                        "osm",
                        properties.referenceDay.get,
                        analysis.osmDistance,
                        analysis.routeSegments.size,
                        None,
                        geometry
                      )
                    }

                    context = context.copy(
                      newReferences = references
                    )
                }
            }
        }
      }

      val monitorRouteRelationData = properties.relationId match {
        case None => None
        case Some(relationId) =>
          monitorRouteRelationRepository.load(None, relationId) match {
            case None => None // TODO add error in MonitorRouteSaveResult !!
            case Some(relation) =>
              Some(
                new MonitorRouteRelationBuilder().build(relation, None)
              )
          }
      }


      val route = MonitorRoute(
        ObjectId(),
        group._id,
        properties.name,
        properties.description,
        properties.comment,
        properties.relationId,
        user,
        referenceType = properties.referenceType,
        referenceDay = properties.referenceDay,
        referenceFilename = properties.referenceFilename,
        referenceDistance = 0,
        deviationDistance = 0,
        deviationCount = 0,
        osmWayCount = 0,
        osmDistance = 0,
        osmSegmentCount = 0,
        happy = false,
        relation = monitorRouteRelationData.map(_.relation)
      )

      monitorRouteRepository.saveRoute(route)

      MonitorRouteSaveResult()
    }
  }

  def update(user: String, groupName: String, routeName: String, properties: MonitorRouteProperties): MonitorRouteSaveResult = {
    Log.context(Seq("route-update", s"group=$groupName", s"route=$routeName")) {
      val group = findGroup(groupName)
      val route = findRoute(group._id, routeName)
      val reference = findRouteReference(route._id)

      val updatedRoute = if (isRouteChanged(group, route, properties)) {
        updateRoute(group, route, properties)
      }
      else {
        route
      }

      if (properties.referenceType == "osm") {
        if (isOsmReferenceChanged(reference, properties) || isRelationIdChanged(route, properties)) {
          val updatedRoute = if (isRelationIdChanged(route, properties)) {
            route.copy(
              relationId = properties.relationId
            )
          }
          else {
            route
          }
          updateOsmReference(user, updatedRoute, properties)
          MonitorRouteSaveResult(analyzed = true)
        }
        else {
          MonitorRouteSaveResult()
        }
      }
      else if (properties.referenceType == "gpx") {
        if (properties.referenceFileChanged) {
          // reference has changed, but details will arrive in next api call
          // re-analyze only after reference has been updated
          MonitorRouteSaveResult()
        }
        else if (isRelationIdChanged(route, properties)) {
          // reference does not change, but we have re-analyze because the relationId has changed
          monitorRouteAnalyzer.analyze(updatedRoute, reference)
          MonitorRouteSaveResult(analyzed = true)
        }
        else {
          MonitorRouteSaveResult()
        }
      }
      else {
        MonitorRouteSaveResult()
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

  private def updateRoute(group: MonitorGroup, route: MonitorRoute, properties: MonitorRouteProperties): MonitorRoute = {
    val groupId = if (group.name != properties.groupName) {
      val newGroup = findGroup(properties.groupName)
      newGroup._id
    }
    else {
      route.groupId
    }
    val updatedRoute = route.copy(
      groupId = groupId,
      name = properties.name,
      description = properties.description,
      relationId = properties.relationId,
      referenceType = properties.referenceType,
      referenceDay = properties.referenceDay,
      referenceFilename = properties.referenceFilename,
      comment = properties.comment
    )

    monitorRouteRepository.saveRoute(updatedRoute)
    updatedRoute
  }

  private def updateOsmReference(user: String, route: MonitorRoute, properties: MonitorRouteProperties): MonitorRouteSaveResult = {

    properties.relationId match {
      case None =>

        val distance = 0L // TODO pick up distance from detailed data

        val reference = MonitorRouteReference(
          ObjectId(),
          routeId = route._id,
          relationId = None,
          created = Time.now,
          user = user,
          bounds = Bounds(),
          referenceType = "osm",
          referenceDay = properties.referenceDay.get,
          distance = distance,
          segmentCount = 0,
          filename = None,
          geometry = ""
        )
        monitorRouteRepository.saveRouteReference(reference)
        MonitorRouteSaveResult(errors = Seq("no-relation-id"))

      case Some(relationId) =>

        val referenceDay = findReferenceDay(properties)

        monitorRouteRelationRepository.load(Some(Timestamp(referenceDay)), relationId) match {
          case None =>
            val reference = MonitorRouteReference(
              ObjectId(),
              routeId = route._id,
              relationId = properties.relationId,
              created = Time.now,
              user = user,
              bounds = Bounds(),
              referenceType = "osm",
              referenceDay = properties.referenceDay.get,
              distance = 0, // TODO set proper value
              segmentCount = 0, // TODO set proper value
              filename = None,
              geometry = ""
            )
            monitorRouteRepository.saveRouteReference(reference)
            MonitorRouteSaveResult(errors = Seq("osm-relation-not-found"))

          case Some(relation) =>
            val wayMembers = MonitorRouteAnalysisSupport.filteredWayMembers(relation)
            val analysis = new MonitorRouteOsmSegmentAnalyzerImpl().analyze(wayMembers)
            val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
            val geomFactory = new GeometryFactory
            val geometryCollection = new GeometryCollection(analysis.routeSegments.map(_.lineString).toArray, geomFactory)
            val geoJsonWriter = new GeoJsonWriter()
            geoJsonWriter.setEncodeCRS(false)
            val geometry = geoJsonWriter.write(geometryCollection)

            val distance = Math.round(geometryCollection.getLength)

            val reference = MonitorRouteReference(
              ObjectId(),
              routeId = route._id,
              relationId = properties.relationId,
              created = Time.now,
              user = user,
              bounds = bounds,
              referenceType = "osm",
              referenceDay = properties.referenceDay.get,
              distance = distance,
              segmentCount = analysis.routeSegments.size,
              filename = None,
              geometry = geometry
            )
            monitorRouteRepository.saveRouteReference(reference)
            monitorRouteAnalyzer.analyze(route, reference)
            MonitorRouteSaveResult(analyzed = true)
        }
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
    monitorRouteRepository.routeReferenceRouteWithId(routeId).getOrElse {
      throw new IllegalArgumentException(
        s"""${Log.contextString} Could not find reference for route with id "$routeId""""
      )
    }
  }

  private def findRelationId(properties: MonitorRouteProperties): Long = {
    properties.relationId.getOrElse {
      throw new IllegalArgumentException(s"""${Log.contextString} relationId is required when add route with referenceType="osm"""")
    }
  }

  private def findReferenceDay(properties: MonitorRouteProperties): Day = {
    properties.referenceDay.getOrElse {
      throw new IllegalArgumentException(s"""${Log.contextString} referenceDay is required in route with referenceType="osm"""")
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

  private def isRouteChanged(group: MonitorGroup, route: MonitorRoute, properties: MonitorRouteProperties): Boolean = {
    route.name != properties.name ||
      route.description != properties.description ||
      route.relationId != properties.relationId ||
      route.referenceType != properties.referenceType ||
      route.referenceDay != properties.referenceDay ||
      route.referenceFilename != properties.referenceFilename ||
      route.comment != properties.comment ||
      group.name != properties.groupName
  }

  private def isOsmReferenceChanged(reference: MonitorRouteReference, properties: MonitorRouteProperties): Boolean = {
    reference.referenceType != properties.referenceType ||
      reference.relationId != properties.relationId ||
      !properties.referenceDay.contains(reference.referenceDay)
  }

  private def isRelationIdChanged(route: MonitorRoute, properties: MonitorRouteProperties): Boolean = {
    route.relationId != properties.relationId
  }

}
