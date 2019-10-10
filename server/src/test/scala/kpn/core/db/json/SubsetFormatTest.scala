package kpn.core.db.json

import kpn.core.db.json.JsonFormats.subsetFormat
import kpn.shared.Subset
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json._

class SubsetFormatTest extends FunSuite with Matchers {

  test("Subset") {
    assertSubset(Subset.nlHiking, "nl:rwn")
    assertSubset(Subset.beBicycle, "be:rcn")
  }

  private def assertSubset(subset: Subset, expectedJsonString: String): Unit = {
    val json = subset.toJson
    json.toString should equal(s""""$expectedJsonString"""")
    json.convertTo[Subset] should equal(subset)
  }
}
