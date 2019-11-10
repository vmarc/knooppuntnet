package kpn.core.tools.status

import org.scalatest.FunSuite
import org.scalatest.Matchers
import java.io.File

import kpn.core.tools.config.Dirs
import kpn.api.common.ReplicationId

class StatusRepositoryTest extends FunSuite with Matchers {

  test("write and read status") {
    val file = new File("/tmp/kpn")
    file.mkdir()
    try {
      val dirs = new Dirs(file)

      val repository = new StatusRepositoryImpl(dirs)

      val replicationId = ReplicationId(1, 2, 3)

      repository.writeReplicationStatus(replicationId)

      repository.replicatorStatus should equal(Some(replicationId))
    }
    finally {
      file.delete()
      ()
    }
  }

  test("status file not available") {
    val file = new File("/tmp/kpn")
    file.mkdir()
    try {
      val dirs = new Dirs(file)

      val repository = new StatusRepositoryImpl(dirs)

      repository.analysisStatus1 should equal(None)
    }
    finally {
      file.delete()
      ()
    }
  }

}
