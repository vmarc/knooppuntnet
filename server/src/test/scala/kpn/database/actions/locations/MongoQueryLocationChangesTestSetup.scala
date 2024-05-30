package kpn.database.actions.locations

import kpn.api.common.LocationChangeSet
import kpn.api.common.LocationChanges
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationSubset

class MongoQueryLocationChangesTestSetup(database: Database) extends SharedTestObjects {

  val timestamp1: Timestamp = Timestamp(2015, 8, 11)
  val timestamp2: Timestamp = Timestamp(2015, 8, 12)
  val timestamp3: Timestamp = Timestamp(2015, 8, 13)
  val timestamp4: Timestamp = Timestamp(2015, 8, 14)
  val timestamp5: Timestamp = Timestamp(2015, 8, 15)

  val key1: ChangeKey = newChangeKey(replicationNumber = 10, timestamp = timestamp1, changeSetId = 101)
  val key2: ChangeKey = newChangeKey(replicationNumber = 20, timestamp = timestamp2, changeSetId = 201)
  val key3: ChangeKey = newChangeKey(replicationNumber = 30, timestamp = timestamp3, changeSetId = 301)
  val key4: ChangeKey = newChangeKey(replicationNumber = 40, timestamp = timestamp4, changeSetId = 401)
  val key5: ChangeKey = newChangeKey(replicationNumber = 50, timestamp = timestamp5, changeSetId = 501)

  def locationChanges(
    networkType: NetworkType = NetworkType.hiking,
    locationNames: Seq[String] = Seq("be", "be-1", "be-1-a"),
    happy: Boolean = false,
    investigate: Boolean = false
  ): LocationChanges = {
    newLocationChanges(
      networkType = networkType,
      locationNames = locationNames,
      happy = happy,
      investigate = investigate
    )
  }

  def changeSetSummary1(
    locationChanges: Seq[LocationChanges],
    locations: Seq[String] = Seq("be", "be-1", "be-1-a")
  ): Unit = {
    changeSetSummary(
      key = key1,
      timestamp = timestamp1,
      locationChanges = locationChanges,
      locations = locations
    )
  }

  def changeSetSummary2(
    locationChanges: Seq[LocationChanges],
    locations: Seq[String] = Seq("be", "be-1", "be-1-a")
  ): Unit = {
    changeSetSummary(
      key = key2,
      timestamp = timestamp2,
      locationChanges = locationChanges,
      locations = locations
    )
  }

  def changeSetSummary3(
    locationChanges: Seq[LocationChanges],
    locations: Seq[String] = Seq("be", "be-1", "be-1-a")
  ): Unit = {
    changeSetSummary(
      key = key3,
      timestamp = timestamp3,
      locationChanges = locationChanges,
      locations = locations,
    )
  }

  def changeSetSummary(
    key: ChangeKey,
    timestamp: Timestamp,
    locationChanges: Seq[LocationChanges],
    locations: Seq[String]
  ): Unit = {
    val changeSetSummary = newChangeSetSummary(
      key = key,
      subsets = Seq(
        Subset.beHiking,
      ),
      timestampFrom = timestamp,
      timestampUntil = timestamp,
      locationChanges = locationChanges,
      locations = locations,
      happy = locationChanges.exists(_.happy),
      investigate = locationChanges.exists(_.investigate),
    )
    database.changes.save(changeSetSummary)
  }

  def count(
    networkType: NetworkType = NetworkType.hiking,
    locationName: String = "be-1-a",
    parameters: ChangesParameters = ChangesParameters()
  ): Long = {
    val subset = LocationSubset("", networkType, Seq(locationName))
    new MongoQueryLocationChanges(database).executeCount(subset, parameters)
  }

  def changes(
    networkType: NetworkType = NetworkType.hiking,
    locationName: String = "be-1-a",
    parameters: ChangesParameters = ChangesParameters()
  ): Seq[LocationChangeSet] = {
    val subset = LocationSubset("", networkType, Seq(locationName))
    new MongoQueryLocationChanges(database).execute(subset, parameters)
  }
}
