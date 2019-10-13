package kpn.server.analyzer.engine.changes.changes

import org.scalatest.FunSuite
import org.scalatest.Matchers


import kpn.shared.Timestamp

import scala.xml.XML

class ChangeSetInfoParserTest extends FunSuite with Matchers {

  test("parse") {

    val xml =
      """
        |<osm version="0.6" generator="OpenStreetMap server" copyright="OpenStreetMap and contributors" attribution="http://www.openstreetmap.org/copyright"
        |	 license="http://opendatacommons.org/licenses/odbl/1-0/">
        |	<changeset id="32016913" user="Jakka" uid="2403313" created_at="2015-06-16T20:51:52Z" closed_at="2015-06-16T20:51:58Z" open="false" comments_count="0">
        |		<tag k="source" v="org.openstreetmap.josm.gui.tagging.TagModel@defc73"/>
        |		<tag k="created_by" v="JOSM/1.5 (8339 nl)"/>
        |		<tag k="comment" v="rcn 41-49 volgorde bijgewerkt"/>
        |	</changeset>
        |</osm>
      """.stripMargin

    val node = XML.loadString(xml)
    val changeSetInfo = new ChangeSetInfoParser().parse(node)

    changeSetInfo.id should equal(32016913)
    changeSetInfo.createdAt should equal(Timestamp(2015, 6, 16, 20, 51, 52))
    changeSetInfo.closedAt should equal(Some(Timestamp(2015, 6, 16, 20, 51, 58)))
    changeSetInfo.open should equal(false)
    changeSetInfo.commentsCount should equal(0)
    changeSetInfo.tags("comment") should equal(Some("rcn 41-49 volgorde bijgewerkt"))
  }

  test("parse open changeset") {

    val xml =
      """
        |<osm version="0.6" generator="CGImap 0.5.8 (4104 thorn-03.openstreetmap.org)" copyright="OpenStreetMap and contributors" attribution="http://www.openstreetmap.org/copyright" license="http://opendatacommons.org/licenses/odbl/1-0/">
        | <changeset id="46854348" created_at="2017-03-14T22:02:18Z" open="true" user="paulbe" uid="48448" min_lat="52.4291449" min_lon="4.9126077" max_lat="52.4329023" max_lon="4.9198340" comments_count="0">
        |  <tag k="created_by" v="Potlatch 2"/>
        |  <tag k="comment" v="issues with POI's in Landsmeer"/>
        |  <tag k="version" v="2.4"/>
        |  <tag k="build" v="2.4-46-gea38865"/>
        | </changeset>
        |</osm>
      """.stripMargin

    val node = XML.loadString(xml)
    val changeSetInfo = new ChangeSetInfoParser().parse(node)

    changeSetInfo.id should equal(46854348)
    changeSetInfo.createdAt should equal(Timestamp(2017, 3, 14, 22, 2, 18))
    changeSetInfo.closedAt should equal(None)
    changeSetInfo.open should equal(true)
    changeSetInfo.commentsCount should equal(0)
    changeSetInfo.tags("comment") should equal(Some("issues with POI's in Landsmeer"))
  }

}
