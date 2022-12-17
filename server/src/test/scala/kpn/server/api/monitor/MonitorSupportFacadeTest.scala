package kpn.server.api.monitor

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.util.UnitTest
import org.apache.commons.io.IOUtils
import org.scalamock.scalatest.MockFactory

class MonitorSupportFacadeTest extends UnitTest with MockFactory {

  test("read superroute structure") {
    val routeRelationId = 312993L
    val filename = s"/case-studies/$routeRelationId.xml"
    val xmlString = IOUtils.toString(getClass.getResourceAsStream(filename), "UTF-8")
    val overpassQueryExecutor = stub[OverpassQueryExecutor]
    (overpassQueryExecutor.executeQuery _).when(*, *).returns(xmlString)
    val facade = new MonitorSupportFacadeImpl(overpassQueryExecutor)
    val structure = facade.routeStructure(routeRelationId)

    val relation = structure.get
    relation.name should equal(Some("Pieterpad deel 1 - Pieterburen-Vorden"))
    relation.relations.flatMap(_.name) should equal(
      Seq(
        "Pieterpad deel 1 - 01",
        "Pieterpad deel 1 - 02",
        "Pieterpad deel 1 - 03",
        "Pieterpad deel 1 - 04",
        "Pieterpad deel 1 - 05",
        "Pieterpad deel 1 - 06",
        "Pieterpad deel 1 - 07",
        "Pieterpad deel 1 - 08",
        "Pieterpad deel 1 - 09",
        "Pieterpad deel 1 - 10",
        "Pieterpad deel 1 - 11",
        "Pieterpad deel 1 - 12",
        "Pieterpad deel 1 - 13",
      )
    )
  }
}
