package kpn.core.engine.changes.data

import kpn.core.db.json.JsonFormats.analysisDataNetworkCollectionsFormat
import org.scalatest.FunSuite
import org.scalatest.Matchers

class AnalysisDataNetworkCollectionsTest extends FunSuite with Matchers {

  test("convert to json and back") {
    val data = new AnalysisDataNetworkCollections()
    data.add(1)
    data.add(2)
    data.add(3)

    val json = analysisDataNetworkCollectionsFormat.write(data)
    val fromJson = analysisDataNetworkCollectionsFormat.read(json)

    fromJson.ids.toSet should equal(data.ids.toSet)
  }
}
