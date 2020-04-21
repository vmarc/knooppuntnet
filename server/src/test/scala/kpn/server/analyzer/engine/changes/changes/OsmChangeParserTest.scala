package kpn.server.analyzer.engine.changes.changes

import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.api.common.data.raw.RawNode
import org.scalatest.matchers.should.Matchers
import org.scalatest.FunSuite
import kpn.api.common.changes.ChangeAction._

import scala.xml.XML

class OsmChangeParserTest extends FunSuite with Matchers {

  test("parse") {

    val xml =
      """
        |<osmChange version="0.6" generator="Osmosis 0.43.1">
        |  <modify>
        |    <node id="1111" version="3" timestamp="2015-06-16T08:59:54Z" uid="123" user="username" changeset="10001" lat="36.9728669" lon="27.3003405">
        |      <tag k="source" v="survey"/>
        |    </node>
        |  </modify>
        |  <delete>
        |    <node id="2222" version="3" timestamp="2015-06-16T08:59:47Z" uid="123" user="username" changeset="10001" lat="53.9531729" lon="11.2208911"/>
        |  </delete>
        |  <modify>
        |    <node id="3333" version="2" timestamp="2015-06-16T08:59:32Z" uid="123" user="username" changeset="10002" lat="55.6156364" lon="13.0011819"/>
        |  </modify>
        |  <create>
        |    <node id="4444" version="1" timestamp="2015-06-16T08:59:38Z" uid="123" user="username" changeset="10002" lat="47.3420318" lon="-0.231803">
        |      <tag k="highway" v="track"/>
        |    </node>
        |    <way id="5555" version="1" timestamp="2015-06-16T08:59:54Z" uid="123" user="username" changeset="10002">
        |      <nd ref="1101"/>
        |      <nd ref="1102"/>
        |      <nd ref="1103"/>
        |    </way>
        |  </create>
        |  <modify>
        |    <relation id="6666" version="6" timestamp="2015-06-16T08:59:30Z" uid="123" user="username" changeset="10003">
        |      <member type="way" ref="2201" role="outer"/>
        |      <member type="way" ref="2201" role="inner"/>
        |      <tag k="landuse" v="meadow"/>
        |      <tag k="type" v="multipolygon"/>
        |    </relation>
        |  </modify>
        |</osmChange>
      """.stripMargin

    val node = XML.loadString(xml)
    val osmChange = new OsmChangeParser().parse(node)

    osmChange.actions.map(_.action) should equal(Seq(Modify, Delete, Modify, Create, Modify))
    osmChange.actions.head.elements should equal(Seq(RawNode(1111, "36.9728669", "27.3003405", 3, Timestamp(2015, 6, 16, 8, 59, 54), 10001, Tags.from("source" -> "survey"))))
  }
}
