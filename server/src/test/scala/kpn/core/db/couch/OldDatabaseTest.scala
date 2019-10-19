package kpn.core.db.couch

import kpn.core.db.StringValueDoc
import kpn.core.db.json.JsonFormats.stringValueDocFormat
import kpn.core.test.TestSupport.withOldDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OldDatabaseTest extends FunSuite with Matchers {

  private val timeout = Couch.uiTimeout

  test("currentRevision") {
    withOldDatabase { database =>
      database.currentRevision("bla", timeout) should equal(None)
      val value = StringValueDoc("id", "value")
      database.save("id", stringValueDocFormat.write(value))
      database.currentRevision("id", timeout).isDefined should equal(true)
    }
  }

  test("save same document multiple times") {
    withOldDatabase { database =>
      database.save("id1", stringValueDocFormat.write(StringValueDoc("id1", "value1")))
      database.save("id2", stringValueDocFormat.write(StringValueDoc("id2", "value2")))

      database.optionGet("id1", timeout).isDefined should equal(true)
      database.optionGet("id2", timeout).isDefined should equal(true)

      database.deleteDocs(Seq("id1", "id2"))

      database.optionGet("id1", timeout) should equal(None)
      database.optionGet("id2", timeout) should equal(None)
    }
  }

  test("deleteDocs of objects that do not exist") {
    withOldDatabase { database =>
      database.deleteDocs(Seq("id1", "id2"))
    }
  }

  test("deleteDocs") {
    withOldDatabase { database =>
      database.save("id1", stringValueDocFormat.write(StringValueDoc("id1", "value1")))
      database.save("id2", stringValueDocFormat.write(StringValueDoc("id2", "value2")))

      database.optionGet("id1", timeout).isDefined should equal(true)
      database.optionGet("id2", timeout).isDefined should equal(true)

      database.deleteDocs(Seq("id1", "id2"))

      database.optionGet("id1", timeout) should equal(None)
      database.optionGet("id2", timeout) should equal(None)
    }
  }

  test("delete") {
    withOldDatabase { database =>
      database.delete("bla")

      val value = StringValueDoc("id", "value")
      database.save("id", stringValueDocFormat.write(value))

      database.optionGet("id", timeout).isDefined should equal(true)
      database.delete("id")

      database.optionGet("id", timeout) should equal(None)
    }
  }

  test("getJsonString") {
    withOldDatabase { database =>
      val value = StringValueDoc("id", "value")
      database.save("id", stringValueDocFormat.write(value))
      val jsonString = database.getJsonString("id")
      jsonString.contains(""""_id":"id"""") should equal(true)
      jsonString.contains(""""value":"value"""") should equal(true)
    }
  }

  test("bulk save") {
    withOldDatabase { database =>

      val docs = Seq(
        stringValueDocFormat.write(StringValueDoc("id1", "value1")),
        stringValueDocFormat.write(StringValueDoc("id2", "value2"))
      )

      database.bulkSave(docs)

      database.optionGet("id1", timeout).isDefined should equal(true)
      database.optionGet("id2", timeout).isDefined should equal(true)

      database.deleteDocs(Seq("id1", "id2"))
    }
  }

}
