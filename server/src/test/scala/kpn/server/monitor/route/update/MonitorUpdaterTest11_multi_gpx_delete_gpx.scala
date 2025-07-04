package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorAction
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

class MonitorUpdaterTest11_multi_gpx_delete_gpx extends MonitorUpdateTest {

  test("add route with gpx references per subrelation - delete subrelation gpx reference") {

    val (group, route, reference11, reference112, state11, state111, state112, reporter) = setup()

    executeGpxDelete(group, reporter)

    database.monitorRoutes.countDocuments() should equal(1)
    database.monitorRouteReferences.countDocuments() should equal(2)
    database.monitorRouteStates.countDocuments() should equal(3)

    val updatedRoute = configuration.monitorRouteRepository.routeByName(group._id, "route-name").get
    val subrelation111 = updatedRoute.relation.get.relations.head.relations.head

    subrelation111.referenceTimestamp should equal(None)
    subrelation111.referenceFilename should equal(None)
    subrelation111.deviationDistance should equal(0)
    subrelation111.deviationCount should equal(0)

    val subrelation112 = updatedRoute.relation.get.relations.head.relations(1)

    subrelation112.referenceTimestamp should equal(Some(Timestamp(2022, 8, 11)))
    subrelation112.referenceFilename should equal(Some("filename-112"))
    subrelation112.deviationDistance should equal(0)
    subrelation112.deviationCount should equal(0)

    configuration.monitorRouteRepository.routeReference(route._id, Some(11)) should equal(Some(reference11))
    configuration.monitorRouteRepository.routeState(route._id, 11) should equal(Some(state11))

    configuration.monitorRouteRepository.routeReference(route._id, Some(111)) should equal(None)
    configuration.monitorRouteRepository.routeState(route._id, 111) should equal(
      Some(
        state111.copy(
          matchesGeometry = None,
          deviations = Seq.empty,
        )
      )
    )

    configuration.monitorRouteRepository.routeReference(route._id, Some(112)) should equal(Some(reference112))
    configuration.monitorRouteRepository.routeState(route._id, 112) should equal(Some(state112))
  }

  private def executeGpxDelete(group: MonitorGroup, reporter: MonitorUpdateReporterMock): Unit = {
    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        "user",
        reporter,
        MonitorRouteUpdate(
          action = MonitorAction.gpxDelete,
          groupName = group.name,
          routeName = "route-name",
          referenceType = MonitorReferenceType.multiGpx,
          relationId = Some(111),
        )
      )
    )
  }

  private def setup() = {

    val group = newMonitorGroup("group")
    val route = setupRoute(group)
    val reference11 = setupReference11(route)
    val reference111 = setupReference111(route)
    val reference112 = setupReference112(route)

    val state11 = setupState11(route)
    val state111 = setupState111(route)
    val state112 = setupState112(route)

    configuration.monitorGroupRepository.saveGroup(group)
    configuration.monitorRouteRepository.saveRoute(route)
    configuration.monitorRouteRepository.saveRouteReference(reference11)
    configuration.monitorRouteRepository.saveRouteReference(reference111)
    configuration.monitorRouteRepository.saveRouteReference(reference112)
    configuration.monitorRouteRepository.saveRouteState(state11)
    configuration.monitorRouteRepository.saveRouteState(state111)
    configuration.monitorRouteRepository.saveRouteState(state112)

    Time.set(CurrentTimestamp)
    val reporter = new MonitorUpdateReporterMock()
    (group, route, reference11, reference112, state11, state111, state112, reporter)
  }

  private def setupRoute(group: MonitorGroup): MonitorRoute = {
    newMonitorRoute(
      group._id,
      name = "route-name",
      relationId = Some(1),
      user = "user",
      referenceType = MonitorReferenceType.osm,
      referenceTimestamp = Some(Timestamp(2022, 8, 11)),
      referenceFilename = None,
      relation = Some(
        newMonitorRouteRelation(
          relationId = 1,
          name = "super-route",
          happy = true,
          relations = Seq(
            newMonitorRouteRelation(
              relationId = 11,
              name = "sub-route-11",
              referenceTimestamp = Some(Timestamp(2022, 8, 11)),
              referenceFileName = Some("filename-11"),
              referenceDistance = 11,
              happy = true,
              relations = Seq(
                newMonitorRouteRelation(
                  relationId = 111,
                  name = "sub-route-111",
                  referenceTimestamp = Some(Timestamp(2022, 8, 11)),
                  referenceFileName = Some("filename-111"),
                  referenceDistance = 1110,
                  deviationDistance = 111,
                  deviationCount = 3,
                  happy = true,
                ),
                newMonitorRouteRelation(
                  relationId = 112,
                  name = "sub-route-112",
                  referenceTimestamp = Some(Timestamp(2022, 8, 11)),
                  referenceFileName = Some("filename-112"),
                  referenceDistance = 1120,
                  deviationDistance = 112,
                  deviationCount = 5,
                  happy = true,
                )
              )
            )
          )
        )
      )
    )
  }

  private def setupReference11(route: MonitorRoute): MonitorRouteReference = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(11),
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Timestamp(2022, 8, 11),
      filename = Some("filename-11"),
    )
  }

  private def setupReference111(route: MonitorRoute): MonitorRouteReference = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(111),
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Timestamp(2022, 8, 11),
      filename = Some("filename-111"),
    )
  }

  private def setupReference112(route: MonitorRoute): MonitorRouteReference = {
    newMonitorRouteReference(
      routeId = route._id,
      relationId = Some(112),
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = Timestamp(2022, 8, 11),
      filename = Some("filename-112"),
    )
  }

  private def setupState11(route: MonitorRoute): MonitorRouteState = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = 11,
      timestamp = Timestamp(2022, 8, 11),
    )
  }

  private def setupState111(route: MonitorRoute): MonitorRouteState = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = 111,
      timestamp = Timestamp(2022, 8, 11),
      matchesGeometry = Some("matches"),
      deviations = Seq(
        MonitorRouteDeviation(
          id = 1,
          meters = 100,
          distance = 12,
          bounds = Bounds(1, 1, 1, 1),
          geoJson = "geoJson"
        )
      ),
    )
  }

  private def setupState112(route: MonitorRoute): MonitorRouteState = {
    newMonitorRouteState(
      routeId = route._id,
      relationId = 112,
      timestamp = Timestamp(2022, 8, 11),
    )
  }
}
