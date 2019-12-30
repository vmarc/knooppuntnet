package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiAddressAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiAddressAnalyzer(context).analyze
  }
}

class PoiAddressAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val city = context.poi.tags("addr:city")
    val postcode = context.poi.tags("addr:postcode")
    val street = context.poi.tags("addr:street")
    val housenumber = context.poi.tags("addr:housenumber")

    val addressLine1: Option[String] = street match {
      case Some(s) =>
        housenumber match {
          case Some(n) => Some(s + " " + n)
          case None => street
        }
      case None => None
    }

    val addressLine2: Option[String] = postcode match {
      case Some(p) =>
        city match {
          case Some(c) => Some(p + " " + c)
          case None => postcode
        }
      case None => city
    }

    context.copy(
      analysis = context.analysis.copy(
        addressLine1 = addressLine1,
        addressLine2 = addressLine2
      ),
      ignoredTagKeys = context.ignoredTagKeys :+ "addr:housename",
      processedTagKeys = context.processedTagKeys ++ Seq(
        "addr:city",
        "addr:postcode",
        "addr:street",
        "addr:housenumber"
      )
    )
  }

}
