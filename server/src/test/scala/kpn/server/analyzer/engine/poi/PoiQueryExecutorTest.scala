package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLonImpl
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory

class PoiQueryExecutorTest extends UnitTest with MockFactory {

  test("pick up way center") {

    val queryResult =
      """
        |<osm version="0.6" generator="Overpass API 0.7.54.13 ff15392f">
        |<note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>
        |<meta osm_base="2019-12-18T11:56:02Z"/>
        |
        |  <way id="179212052">
        |    <center lat="47.7402732" lon="8.9747377"/>
        |    <nd ref="3752513588"/>
        |    <nd ref="262512798"/>
        |    <nd ref="262512795"/>
        |    <nd ref="1315433939"/>
        |    <nd ref="7024037175"/>
        |    <nd ref="7024037177"/>
        |    <nd ref="7024037178"/>
        |    <nd ref="7024037176"/>
        |    <nd ref="262512801"/>
        |    <nd ref="262512796"/>
        |    <nd ref="1315433932"/>
        |    <nd ref="3752513591"/>
        |    <nd ref="3752513588"/>
        |    <tag k="amenity" v="parking"/>
        |    <tag k="fee" v="yes"/>
        |    <tag k="name" v="Messeplatz"/>
        |    <tag k="parking" v="surface"/>
        |    <tag k="surface" v="asphalt"/>
        |    <tag k="wheelchair" v="yes"/>
        |  </way>
        |
        |</osm>
        |""".stripMargin

    val overpassQueryExecutor: OverpassQueryExecutor = stub[OverpassQueryExecutor]
    (overpassQueryExecutor.executeQuery _).when(*, *).returns(queryResult)

    val poiQueryExecutor: PoiQueryExecutor = new PoiQueryExecutorImpl(overpassQueryExecutor)

    poiQueryExecutor.center(PoiRef("way", 179212052)) should equal(Some(LatLonImpl("47.7402732", "8.9747377")))
  }

  test("'None' when way not found in database") {

    val queryResult =
      """
        |<osm version="0.6" generator="Overpass API 0.7.54.13 ff15392f">
        |<note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>
        |<meta osm_base="2019-12-18T11:56:02Z"/>
        |</osm>
        |""".stripMargin

    val overpassQueryExecutor: OverpassQueryExecutor = stub[OverpassQueryExecutor]
    (overpassQueryExecutor.executeQuery _).when(*, *).returns(queryResult)

    val poiQueryExecutor: PoiQueryExecutor = new PoiQueryExecutorImpl(overpassQueryExecutor)

    poiQueryExecutor.center(PoiRef("way", 179212052)) should equal(None)
  }

}
