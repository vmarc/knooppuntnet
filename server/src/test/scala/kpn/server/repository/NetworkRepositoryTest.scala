package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.core.gpx.GpxFile
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class NetworkRepositoryTest extends UnitTest with SharedTestObjects {

  test("gpx - get gpx file by network id") {
    withCouchDatabase { database =>
      val repository = new NetworkRepositoryImpl(null, database, false)
      repository.gpx(1) should equal(None)

      val gpxFile = GpxFile(1, 1, "filename", Seq.empty, Seq.empty)
      repository.saveGpxFile(gpxFile)
      repository.gpx(1) should equal(Some(gpxFile))
    }
  }
}
