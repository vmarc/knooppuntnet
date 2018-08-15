package kpn.core.engine.changes.data

import kpn.core.db.json.JsonFormats.orphanNodesDataFormat
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanNodesDataTest extends FunSuite with Matchers {

  test("test methods") {

    val data = new OrphanNodesData()

    data.size should equal(0)
    data.contains(1001) should equal(false)
    data.ids.isEmpty should equal(true)
    data.delete(1001) // does no fail

    data.add(1001)

    data.size should equal(1)
    data.contains(1001) should equal(true)
    data.ids should equal(Set(1001))
    data.delete(1001)

    data.size should equal(0)
  }

  test("convert to json and back") {

    val data = new OrphanNodesData()
    data.add(1002)
    data.add(1003)

    val json = orphanNodesDataFormat.write(data)

    orphanNodesDataFormat.read(json).ids.toSet should equal(data.ids.toSet)
  }
}
