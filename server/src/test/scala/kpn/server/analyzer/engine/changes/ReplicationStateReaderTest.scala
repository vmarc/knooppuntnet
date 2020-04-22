package kpn.server.analyzer.engine.changes

import java.io.File

import kpn.api.common.ReplicationId
import kpn.core.util.UnitTest
import org.apache.commons.io.FileUtils

class ReplicationStateReaderTest extends UnitTest {

  test("read timestamp") {

    val contents =
      """#Wed Jun 17 18:19:04 UTC 2015
        |sequenceNumber=1443999
        |txnMaxQueried=699414186
        |txnReadyList=
        |timestamp=2015-06-17T18\:19\:02Z
        |txnMax=699414186
        |txnActiveList=""".stripMargin

    new File("/tmp/001/002").mkdirs()
    val file = new File("/tmp/001/002/003.state.txt")
    FileUtils.writeStringToFile(file, contents)

    read(ReplicationId(1, 2, 3)) should equal(Some("2015-06-17T18:19:02Z"))
  }

  test("state file absent") {
    new File("/tmp/001/002").mkdirs()
    val file = new File("/tmp/001/002/003.state.txt")
    file.delete()

    read(ReplicationId(1, 2, 3)) should equal(None)
  }

  test("state file with invalid contents") {
    new File("/tmp/001/002").mkdirs()
    val file = new File("/tmp/001/002/003.state.txt")
    FileUtils.writeStringToFile(file, "brol")

    read(ReplicationId(1, 2, 3)) should equal(None)
  }

  private def read(replicationId: ReplicationId): Option[String] = {
    new ReplicationStateReader(new File("/tmp")).readTimestamp(replicationId).map(_.iso)
  }
}
