package kpn.server.repository

import kpn.api.common.FactCount
import kpn.api.common.subset.SubsetInfo
import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.database.actions.statistics.MongoQueryStatistics
import kpn.database.actions.subsets.MongoQuerySubsetInfo
import kpn.database.base.Database
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetMapNetworks
import org.springframework.stereotype.Component

@Component
class SubsetRepositoryImpl(database: Database) extends SubsetRepository {

  private val log = Log(classOf[SubsetRepositoryImpl])

  def subsetInfo(subset: Subset): SubsetInfo = {
    new MongoQuerySubsetInfo(database).execute(subset, log)
  }

  def subsetFactCounts(subset: Subset): Seq[FactCount] = {
    val statisticValuess = new MongoQueryStatistics(database).execute()
    Fact.reportedFacts.flatMap { fact =>
      statisticValuess.find(_._id == (fact.name + "Count")) match {
        case None => Seq.empty
        case Some(statisticValues) =>
          statisticValues.values.filter(_.isSubset(subset)).map { sv =>
            FactCount(fact, sv.value)
          }
      }
    }
  }

  def subsetMapNetworks(subset: Subset): Seq[SubsetMapNetwork] = {
    new MongoQuerySubsetMapNetworks(database).execute(subset, log)
  }
}
