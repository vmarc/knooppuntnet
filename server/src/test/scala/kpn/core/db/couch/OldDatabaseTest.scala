package kpn.core.db.couch

import kpn.core.database.doc.StringValueDoc
import kpn.core.db.json.JsonFormats.stringValueDocFormat
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OldDatabaseTest extends FunSuite with Matchers {

  private val timeout = Couch.uiTimeout

  test("bulk save") {
    withDatabase { database =>

      val docs = Seq(
        stringValueDocFormat.write(StringValueDoc("id1", "value1")),
        stringValueDocFormat.write(StringValueDoc("id2", "value2"))
      )

      database.old.bulkSave(docs)

      database.docWithId("id1", classOf[StringValueDoc]).map(_.value) should equal(Some("value1"))
      database.docWithId("id2", classOf[StringValueDoc]).map(_.value) should equal(Some("value2"))
    }
  }

}
