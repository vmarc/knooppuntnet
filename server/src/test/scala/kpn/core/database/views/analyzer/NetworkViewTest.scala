package kpn.core.database.views.analyzer

import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.NetworkRepositoryImpl
import kpn.shared.NetworkType.bicycle
import kpn.shared.NetworkType.hiking
import kpn.shared.Country
import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.http.Uri

class NetworkViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("view row values") {

    withDatabase { database =>

      val repository = new NetworkRepositoryImpl(database)

      // sorting order different from 'by network name'
      repository.save(newNetwork(1, Some(Country.nl), bicycle, "nl-rcn-2"))
      repository.save(newNetwork(2, Some(Country.be), hiking, "be-rwn-2"))
      repository.save(newNetwork(3, Some(Country.be), hiking, "be-rwn-1"))
      repository.save(newNetwork(4, Some(Country.nl), bicycle, "nl-rcn-1"))

      val uri = Uri("_design/AnalyzerDesign/_view/NetworkView")

      database.old.getRows(uri.toString(), Couch.uiTimeout).map(NetworkView.convert) should equal(
        Seq(
          newNetworkAttributes(3, Some(Country.be), hiking, "be-rwn-1"),
          newNetworkAttributes(2, Some(Country.be), hiking, "be-rwn-2"),
          newNetworkAttributes(4, Some(Country.nl), bicycle, "nl-rcn-1"),
          newNetworkAttributes(1, Some(Country.nl), bicycle, "nl-rcn-2")
        )
      )
    }
  }

  test("non-active networks are not included") {

    withDatabase { database =>

      val repository = new NetworkRepositoryImpl(database)

      // sorting order different from 'by network name'
      repository.save(newNetwork(1, Some(Country.nl), bicycle, "nl-rcn-2"))
      repository.save(newNetwork(2, Some(Country.be), hiking, "be-rwn-2", active = false))

      val uri = Uri("_design/AnalyzerDesign/_view/NetworkView")

      database.old.getRows(uri.toString(), Couch.uiTimeout).map(NetworkView.convert) should equal(
        Seq(
          newNetworkAttributes(1, Some(Country.nl), bicycle, "nl-rcn-2")
        )
      )
    }
  }
}
