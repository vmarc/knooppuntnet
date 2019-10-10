package kpn.core.db.views

import kpn.core.db.StringValueDoc
import kpn.core.db.couch.Couch
import kpn.core.db.json.JsonFormats
import kpn.core.db.views.DocumentView.DocumentCount
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DocumentViewTest extends FunSuite with Matchers {

  test("document view") {

    withDatabase { database =>

      def createDoc(key: String): Unit = {
        database.save(key, JsonFormats.stringValueDocFormat.write(StringValueDoc(key, "bla")))
      }

      createDoc("prefix1:suffix1")
      createDoc("prefix1:suffix2")
      createDoc("prefix2:suffix1")

      val rows = database.groupQuery(1, AnalyzerDesign, DocumentView, Couch.uiTimeout, stale = false)().map(DocumentView.convert)

      rows should equal(
        Seq(
          DocumentCount("prefix1", 2),
          DocumentCount("prefix2", 1)
        )
      )
    }
  }
}
