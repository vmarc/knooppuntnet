package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest

import scala.xml.InputSource
import scala.xml.XML

class IssueRouteDistance extends UnitTest {

  test("route distance") {
    val routes = Seq(
      "VPSM01" -> 15656714L,
      "VPSM02" -> 15656715L,
      "VPSM03" -> 15656716L,
      "VPSM04" -> 12921288L,
      "VPSM05" -> 12921427L,
      "VPSM06" -> 13014492L,
      "VPSM07" -> 13014677L,
      "VPSM08" -> 13014771L,
      "VPSM09" -> 12955973L,
      "VPSM10" -> 12964641L,
      "VPSM11" -> 12966181L,
      "VPSM12" -> 12966645L,
      "VPSM13" -> 12966812L,
      "VPSM14" -> 12867780L,
      "VPSM15" -> 12882087L,
      "VPSM16" -> 12882250L,
      "VPSM17" -> 12887722L,
      "VPSM18" -> 12859714L,
      "VPSM19" -> 12863114L,
      "VPSM20" -> 12864030L,
      "VPSM21" -> 12865974L,
    )

    routes.foreach { case (name, relationId) =>
      evaluateRouteDistance(name, relationId)
    }
  }

  private def evaluateRouteDistance(name: String, relationId: Long): Unit = {

    val filename = s"/case-studies/$relationId.xml"

    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser(full = false).parse(xml)

    val relation = new DataBuilder(rawData).data.relations(relationId)
    val length = relation.wayMembers.map(_.way.length).sum.toDouble / 1000f

    val osmDistance = MonitorRouteRelation.from(relation, None).osmDistance

    println("|%s|%.1f|%.1f|".format(name, length, osmDistance.toDouble / 1000))
  }
}
