package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.db.json.JsonFormats.elementIdsFormat
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ElementIdMapTest extends FunSuite with Matchers {

  test("elementIds to json and back") {

    val elementIds = ElementIds(
      nodeIds = Set(1001, 1002, 1003),
      wayIds = Set(101, 102, 102),
      relationIds = Set(11, 12, 13)
    )

    val json = elementIdsFormat.write(elementIds)
    val readElementIds = elementIdsFormat.read(json)

    readElementIds should equal(elementIds)
  }
}
