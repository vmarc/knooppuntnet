package kpn.core.engine.analysis.caseStudies

import kpn.core.data.DataBuilder
import kpn.core.engine.analysis.NetworkShapeAnalyzer
import kpn.core.loadOld.Parser
import kpn.shared.NetworkType
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.xml.InputSource
import scala.xml.XML

class ConcaveHullTest extends FunSuite with Matchers {

  test("reproduce the problem with the concave hull calculation (currently commented out)") {

    val stream = getClass.getResourceAsStream("/case-studies/network-4257206.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser().parse(xml)

    val data = new DataBuilder(NetworkType.bicycle, rawData).data
    val networkRelation = data.relations(4257206)

    new NetworkShapeAnalyzer(networkRelation).shape

  }

}
