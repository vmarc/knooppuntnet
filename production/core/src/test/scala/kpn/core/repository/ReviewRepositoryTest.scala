package kpn.core.repository

import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

import kpn.shared.ReplicationId
import kpn.shared.Timestamp
import kpn.shared.changes.Review

class ReviewRepositoryTest extends FunSuite with Matchers {

  test("save and get reviews") {
    withDatabase { database =>
      val repository: ReviewRepository = new ReviewRepositoryImpl(database)

      val replicationId = ReplicationId(1, 2, 3)
      val changeSetId = 12345L
      val timestamp1 = Timestamp(2015, 8, 11, 12, 34, 56)
      val timestamp2 = Timestamp(2015, 8, 11, 13, 14, 15)
      repository.save(changeSetId, replicationId, Review("user1", timestamp1, "status1", "comment1"))

      repository.get(changeSetId, replicationId) should equal(
        Seq(
          Review("user1", timestamp1, "status1", "comment1")
        )
      )

      repository.save(changeSetId, replicationId, Review("user2", timestamp2, "status2", "comment2"))

      repository.get(changeSetId, replicationId) should equal(
        Seq(
          Review("user1", timestamp1, "status1", "comment1"),
          Review("user2", timestamp2, "status2", "comment2")
        )
      )
    }
  }
}
