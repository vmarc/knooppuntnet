package kpn.core.database.implementation

import kpn.api.common.location.Location
import kpn.api.custom.Tags
import kpn.core.TestObjects
import kpn.core.database.doc.NodeDoc
import kpn.core.database.query.Query
import kpn.core.database.views.location.LocationDesign
import kpn.core.database.views.location.LocationView
import kpn.core.db.couch.ViewResult
import kpn.core.db.couch.ViewResultRow
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.DesignRepositoryImpl
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class DatabaseQueryTest extends AnyFunSuite with Matchers with TestObjects {

  test("query") {
    withDatabase(database => {
      new DesignRepositoryImpl(database).save(LocationDesign)

      database.save(
        NodeDoc(
          "node:1001",
          newNodeInfo(
            1001,
            tags = Tags.from("rcn_ref" -> "01"),
            location = Some(
              Location(Seq("country", "province", "municipality"))
            )
          ),
          None
        )
      )

      database.save(
        NodeDoc(
          "node:1002",
          newNodeInfo(
            1002,
            tags = Tags.from("rcn_ref" -> "01"),
            location = Some(
              Location(Seq("country", "province", "municipality"))
            )
          ),
          None
        )
      )

      val query = Query(LocationDesign, LocationView, classOf[ViewResult]).stale(false).reduce(false).keyStartsWith("node")
      val result = database.execute(query)

      result should equal(
        ViewResult(
          6,
          0,
          Seq(
            ViewResultRow(
              "node:1001",
              Seq("node", "cycling", "country"),
              Seq("01", "1001")
            ),
            ViewResultRow(
              "node:1002",
              Seq("node", "cycling", "country"),
              Seq("01", "1002")
            ),
            ViewResultRow(
              "node:1001",
              Seq("node", "cycling", "municipality"),
              Seq("01", "1001")
            ),
            ViewResultRow(
              "node:1002",
              Seq("node", "cycling", "municipality"),
              Seq("01", "1002")
            ),
            ViewResultRow(
              "node:1001",
              Seq("node", "cycling", "province"),
              Seq("01", "1001")
            ),
            ViewResultRow(
              "node:1002",
              Seq("node", "cycling", "province"),
              Seq("01", "1002")
            )
          )
        )
      )
    })
  }

}
