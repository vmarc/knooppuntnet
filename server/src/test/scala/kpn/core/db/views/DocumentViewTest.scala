package kpn.core.db.views

import kpn.core.db.StringValueDoc
import kpn.core.db.couch.Couch
import kpn.core.db.views.DocumentView.DocumentCount
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DocumentViewTest extends FunSuite with Matchers {

  test("document view") {

    withDatabase { database =>

      database.save(StringValueDoc("prefix1:suffix1", "bla"))
      database.save(StringValueDoc("prefix1:suffix2", "bla"))
      database.save(StringValueDoc("prefix2:suffix1", "bla"))

      val rows = database.old.groupQuery(1, AnalyzerDesign, DocumentView, Couch.uiTimeout, stale = false)().map(DocumentView.convert)

      rows should equal(
        Seq(
          DocumentCount("prefix1", 2),
          DocumentCount("prefix2", 1)
        )
      )
    }
  }
}
