package kpn.core.db.couch.implementation

import kpn.core.TestObjects
import kpn.core.db.NodeDoc
import kpn.core.db.couch.ViewResult
import kpn.core.db.couch.ViewResultRow
import kpn.core.db.views.LocationDesign
import kpn.core.db.views.LocationView
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.DesignRepositoryImpl
import kpn.shared.Location
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class DatabaseQueryyTest extends FunSuite with Matchers with TestObjects {

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

      val result = database.query(LocationDesign, LocationView, classOf[ViewResult], stale = false)("node")

      result should equal(
        ViewResult(
          2,
          0,
          Seq(
            ViewResultRow(
              "node:1001",
              Seq("node", "cycling", "country", "province", "municipality"),
              Seq("01", "1001")
            ),
            ViewResultRow(
              "node:1002",
              Seq("node", "cycling", "country", "province", "municipality"),
              Seq("01", "1002")
            )
          )
        )
      )
    });
  }

}
