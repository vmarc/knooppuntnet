package kpn.server.repository

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.db.couch.Couch
import kpn.core.gpx.GpxFile
import kpn.core.test.TestSupport.withDatabase
import kpn.api.common.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkRepositoryTest extends FunSuite with Matchers with SharedTestObjects {

  test("network - get network by id") {
    withDatabase { database =>
      val repository = new NetworkRepositoryImpl(database)
      repository.network(1, Couch.uiTimeout) should equal(None)

      val testNetwork = newNetwork(1, Some(Country.nl), NetworkType.cycling, "name")
      repository.save(testNetwork)
      repository.network(1, Couch.uiTimeout) should equal(Some(testNetwork))
    }
  }

  test("save network - returns false if saving same network without change") {
    withDatabase { database =>
      val repository = new NetworkRepositoryImpl(database)
      repository.save(newNetwork(1, Some(Country.nl), NetworkType.cycling, "name")) should equal(true)
      repository.save(newNetwork(1, Some(Country.nl), NetworkType.cycling, "name")) should equal(false)
      repository.save(newNetwork(1, Some(Country.nl), NetworkType.cycling, "changed-name")) should equal(true)
    }
  }

  test("gpx - get gpx file by network id") {
    withDatabase { database =>
      val repository = new NetworkRepositoryImpl(database)
      repository.gpx(1, Couch.uiTimeout) should equal(None)

      val gpxFile = GpxFile(1, "filename", Seq(), Seq())
      repository.saveGpxFile(gpxFile)
      repository.gpx(1, Couch.uiTimeout) should equal(Some(gpxFile))
    }
  }

  test("save gpxFile - returns false if saving same gpxFile without change") {
    withDatabase { database =>
      val repository = new NetworkRepositoryImpl(database)
      repository.saveGpxFile(GpxFile(1, "filename1", Seq(), Seq())) should equal(true)
      repository.saveGpxFile(GpxFile(1, "filename1", Seq(), Seq())) should equal(false)
      repository.saveGpxFile(GpxFile(1, "filename2", Seq(), Seq())) should equal(true)
    }
  }

  test("networks - find attributes of networks for given country and network type") {

    withDatabase { database =>

      val repository = new NetworkRepositoryImpl(database)

      // sorting order different from 'by network name'
      repository.save(newNetwork(1, Some(Country.nl), NetworkType.cycling, "nl-rcn-2"))
      repository.save(newNetwork(2, Some(Country.be), NetworkType.hiking, "be-rwn-2"))
      repository.save(newNetwork(3, Some(Country.be), NetworkType.hiking, "be-rwn-1"))
      repository.save(newNetwork(4, Some(Country.nl), NetworkType.cycling, "nl-rcn-1"))

      repository.networks(Subset.nlBicycle, Couch.uiTimeout, stale = false) should equal(
        Seq(
          newNetworkAttributes(4, Some(Country.nl), NetworkType.cycling, "nl-rcn-1"),
          newNetworkAttributes(1, Some(Country.nl), NetworkType.cycling, "nl-rcn-2")
        )
      )

      repository.networks(Subset.beHiking, Couch.uiTimeout, stale = false) should equal(
        Seq(
          newNetworkAttributes(3, Some(Country.be), NetworkType.hiking, "be-rwn-1"),
          newNetworkAttributes(2, Some(Country.be), NetworkType.hiking, "be-rwn-2")
        )
      )
    }
  }
}
