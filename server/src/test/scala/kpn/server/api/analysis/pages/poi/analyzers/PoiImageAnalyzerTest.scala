package kpn.server.api.analysis.pages.poi.analyzers

import kpn.api.common.PoiAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.api.analysis.pages.poi.PoiAnalysisContext

class PoiImageAnalyzerTest extends UnitTest with SharedTestObjects {

  test("image urls") {

    imageTagValueToUrl("FILE:Stolperstein_Siegen_Rosenberg_Louis.jpeg") should equal(
      Some("https://upload.wikimedia.org/wikipedia/commons/f/ff/Stolperstein_Siegen_Rosenberg_Louis.jpeg")
    )

    imageTagValueToUrl("de.wikipedia.org/wiki/Datei:Eiche_am_So._30.04.2017.jpg") should equal(
      Some("https://upload.wikimedia.org/wikipedia/commons/9/91/Eiche_am_So._30.04.2017.jpg")
    )
  }

  private def imageTagValueToUrl(tagValue: String): Option[String] = {

    val poi = newPoi(
      "node",
      1000L,
      "",
      "",
      tags = Tags.from(
        "image" -> tagValue
      )
    )

    val context = PoiAnalysisContext(
      poi,
      processedTagKeys = Seq.empty,
      ignoredTagKeys = Seq.empty,
      ignoredTagKeyValues = Seq.empty,
      PoiAnalysis()
    )

    val newContext = PoiImageAnalyzer.analyze(context)

    newContext.analysis.image
  }
}
