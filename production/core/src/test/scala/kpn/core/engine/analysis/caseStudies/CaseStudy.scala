package kpn.core.engine.analysis.caseStudies

import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.engine.analysis.NetworkNodeBuilder
import kpn.core.engine.analysis.country.CountryAnalyzerNoop
import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.load.data.LoadedRoute
import kpn.core.loadOld.Parser
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.data.Relation

import scala.xml.InputSource
import scala.xml.XML

object CaseStudy {

  private val countryAnalyzer = new CountryAnalyzerNoop()

  def routeAnalysis(name: String): RouteAnalysis = {
    val filename = s"/case-studies/$name.xml"
    val (data, routeRelation) = load(filename)
    val networkNodes = new NetworkNodeBuilder(data, countryAnalyzer).networkNodes
    val routeAnalyzer = new MasterRouteAnalyzerImpl()
    routeAnalyzer.analyze(networkNodes, LoadedRoute(Some(Country.nl), data.networkType, "", data, routeRelation), orphan = false)
  }

  private def load(filename: String): (Data, Relation) = {

    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser().parse(xml)
    if (rawData.relations.isEmpty) {
      throw new IllegalArgumentException(s"No route relation found in file $filename")
    }

    if (rawData.relations.size > 1) {
      throw new IllegalArgumentException(s"Multiple relations found in file $filename (expected 1 single relation only)")
    }

    val rawRouteRelation = rawData.relations.head

    if (!rawRouteRelation.tags.has("type", "route")) {
      throw new IllegalArgumentException(s"Relation does not have expected tag type=route in file $filename")
    }

    val networkType = rawRouteRelation.tags("network") match {
      case Some("rcn") => NetworkType.bicycle
      case Some("rwn") => NetworkType.hiking
      case _ => throw new IllegalArgumentException("Network type not found in file " + filename)
    }

    val data = new DataBuilder(networkType, rawData).data
    val routeRelation = data.relations(rawRouteRelation.id)
    (data, routeRelation)
  }
}
