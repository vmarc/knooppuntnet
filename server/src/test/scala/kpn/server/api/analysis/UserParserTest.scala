package kpn.server.api.analysis

import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.xml.XML

class UserParserTest extends FunSuite with Matchers {

  test("parse") {

    val xml =
      """
        |<osm generator="OpenStreetMap server" version="0.6">
        |  <user account_created="2001-01-01T00:00:00Z" display_name="vmarc" id="0">
        |    <description/>
        |    <contributor-terms pd="false" agreed="true"/>
        |    <roles>
        |    </roles>
        |    <changesets count="0"/>
        |    <traces count="0"/>
        |    <blocks>
        |      <received active="0" count="0"/>
        |    </blocks>
        |    <home zoom="0" lon="0" lat="0"/>
        |    <languages>
        |      <lang>en_US</lang>
        |    </languages>
        |    <messages>
        |      <received unread="0" count="0"/>
        |      <sent count="0"/>
        |    </messages>
        |  </user>
        |</osm>
      """.stripMargin

    val node = XML.loadString(xml)
    val userOption = new UserParser().parse(node)

    userOption should equal(Some("vmarc"))
  }

}
