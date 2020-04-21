package kpn.core.database.implementation

import kpn.core.TestObjects
import kpn.core.database.doc.NodeDoc
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class DatabaseRevisionTest extends FunSuite with Matchers with TestObjects {

  test("revision - get document revision without having to load the entire document") {

    withDatabase(database => {

      val doc = {
        val nodeInfo = newNodeInfo(123)
        NodeDoc("123", nodeInfo, None)
      }

      database.save(doc)

      val rev = database.docWithId(doc._id, classOf[NodeDoc]).flatMap(_._rev)

      database.revision(doc._id) should equal(rev)

    });
  }

  test("revision - None if non-existing document") {
    withDatabase(database => {
      database.revision("bla") should equal(None)
    });
  }

}
