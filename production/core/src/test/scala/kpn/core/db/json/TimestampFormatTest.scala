package kpn.core.db.json

import kpn.core.db.json.JsonFormats._
import kpn.shared.Timestamp
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json._

class TimestampFormatTest extends FunSuite with Matchers {

  test("TimestampFormat") {
    val timestamp = Timestamp(2015, 8, 11, 12, 34, 56)
    val json = timestamp.toJson
    json.convertTo[Timestamp] should equal(timestamp)
  }
}
