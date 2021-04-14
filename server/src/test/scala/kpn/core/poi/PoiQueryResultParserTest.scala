package kpn.core.poi

import kpn.api.common.Poi
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

import scala.xml.XML

class PoiQueryResultParserTest extends UnitTest {

  test("parse nodes") {

    val xml =
      """
        |<osm version="0.6" generator="Overpass API">
        |
        |  <node id="24347225" lat="52.9596832" lon="0.7344434">
        |    <tag k="man_made" v="windmill"/>
        |    <tag k="name" v="one"/>
        |  </node>
        |  <node id="27233617" lat="52.7053088" lon="9.5934963">
        |    <tag k="man_made" v="windmill"/>
        |    <tag k="name" v="two"/>
        |  </node>
        |
        |</osm>
      """.stripMargin

    val node = XML.loadString(xml)
    val pois = new PoiQueryResultParser().parse("windmill", node)

    pois should matchTo(
      Seq(
        Poi("node", 24347225L, "52.9596832", "0.7344434", Seq("windmill"), Tags.from("man_made" -> "windmill", "name" -> "one")),
        Poi("node", 27233617L, "52.7053088", "9.5934963", Seq("windmill"), Tags.from("man_made" -> "windmill", "name" -> "two"))
      )
    )
  }

  test("parse ways") {

    val xml =
      """
        |<osm version="0.6" generator="Overpass API">
        |
        |  <way id="23218347">
        |    <center lat="52.4041477" lon="13.0356714"/>
        |    <nd ref="251151091"/>
        |    <nd ref="669883821"/>
        |    <nd ref="251151092"/>
        |    <tag k="man_made" v="windmill"/>
        |    <tag k="name" v="one"/>
        |  </way>
        |
        |</osm>
      """.stripMargin

    val node = XML.loadString(xml)
    val pois = new PoiQueryResultParser().parse("windmill", node)

    pois should matchTo(
      Seq(
        Poi("way", 23218347L, "52.4041477", "13.0356714", Seq("windmill"), Tags.from("man_made" -> "windmill", "name" -> "one"))
      )
    )
  }

  test("parse relations") {

    val xml =
      """
        |<osm version="0.6" generator="Overpass API">
        |
        |  <relation id="8152175">
        |    <center lat="51.9915194" lon="6.9159526"/>
        |    <member type="way" ref="108826502" role="outer"/>
        |    <member type="way" ref="260690393" role="outer"/>
        |    <tag k="amenity" v="bank"/>
        |    <tag k="atm" v="yes"/>
        |  </relation>
        |
        |</osm>
      """.stripMargin

    val node = XML.loadString(xml)
    val pois = new PoiQueryResultParser().parse("bank", node)

    pois should matchTo(
      Seq(
        Poi("relation", 8152175L, "51.9915194", "6.9159526", Seq("bank"), Tags.from("amenity" -> "bank", "atm" -> "yes"))
      )
    )
  }
}
