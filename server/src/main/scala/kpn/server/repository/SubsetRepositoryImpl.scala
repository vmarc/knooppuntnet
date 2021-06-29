package kpn.server.repository

import kpn.api.common.subset.SubsetInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetInfo
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class SubsetRepositoryImpl(database: Database) extends SubsetRepository {

  private val log = Log(classOf[SubsetRepositoryImpl])

  def subsetInfo(subset: Subset): SubsetInfo = {
    new MongoQuerySubsetInfo(database).execute(subset, log)
  }
}
