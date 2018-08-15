package kpn.core.replicate

import java.io.File

import kpn.shared.ReplicationId
import kpn.shared.Timestamp
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ReplicationStateRepositoryTest extends FunSuite with Matchers {

  test("write state file and read timestamp") {

    val state =
      """#Mon Dec 21 10:55:05 UTC 2015
        |sequenceNumber=1712505
        |txnMaxQueried=757730742
        |txnReadyList=
        |timestamp=2015-12-21T10\:55\:01Z
        |txnMax=757730742
        |txnActiveList=757730680,75773073
      """.stripMargin

    testTimestamp(state)
  }

  test("write state file and read timestamp at other position") {

    val state =
      """#Mon Dec 21 10:55:05 UTC 2015
        |sequenceNumber=1712505
        |timestamp=2015-12-21T10\:55\:01Z
        |txnMaxQueried=757730742
        |txnReadyList=
        |txnMax=757730742
        |txnActiveList=757730680,75773073
      """.stripMargin

    testTimestamp(state)
  }

  private def testTimestamp(state: String): Unit = {
    val repo = new ReplicationStateRepositoryImpl(new File("/tmp"))
    val replicationId = ReplicationId(1712505)
    repo.write(replicationId, state)
    repo.read(replicationId) should equal(Timestamp(2015, 12, 21, 10, 55, 1))
  }
}
