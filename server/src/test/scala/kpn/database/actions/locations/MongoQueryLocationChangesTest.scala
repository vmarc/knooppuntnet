package kpn.database.actions.locations

import kpn.api.common.LocationChangeSet
import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.filter.ChangesParameters
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryLocationChangesTest extends UnitTest with SharedTestObjects {

  test("all") {
    withDatabase() { database =>
      val setup = new MongoQueryLocationChangesTestSetup(database)

      val change1a = setup.locationChanges()
      val change1b = setup.locationChanges()
      val change1c = setup.locationChanges()
      val change2 = setup.locationChanges()
      val change3 = setup.locationChanges()

      setup.changeSetSummary1(locationChanges = Seq(change1a, change1b, change1c))
      setup.changeSetSummary2(locationChanges = Seq(change2))
      setup.changeSetSummary3(locationChanges = Seq(change3))

      setup.count() should equal(3)
      assertEqual(
        setup.changes(),
        Seq(
          LocationChangeSet(
            _id = "301:30",
            key = setup.key3,
            locationChanges = Seq(change3)
          ),
          LocationChangeSet(
            _id = "201:20",
            key = setup.key2,
            locationChanges = Seq(change2)
          ),
          LocationChangeSet(
            _id = "101:10",
            key = setup.key1,
            locationChanges = Seq(change1a, change1b, change1c)
          ),
        )
      )
    }
  }

  test("query location") {
    withDatabase() { database =>
      val setup = new MongoQueryLocationChangesTestSetup(database)

      val change1a = setup.locationChanges(locationNames = Seq("be", "be-1", "be-1-a"))
      val change1b = setup.locationChanges(locationNames = Seq("be", "be-1", "be-1-a"))
      val change1c = setup.locationChanges(locationNames = Seq("be", "be-1", "be-1-b"))
      val change2 = setup.locationChanges(locationNames = Seq("be", "be-1", "be-1-a"))
      val change3 = setup.locationChanges(locationNames = Seq("be", "be-1", "be-1-b"))

      setup.changeSetSummary1(
        locationChanges = Seq(change1a, change1b, change1c),
        locations = Seq("be", "be-1", "be-1-a", "be-1-b"),
      )

      setup.changeSetSummary2(
        locationChanges = Seq(change2),
        locations = Seq("be", "be-1", "be-1-a"),
      )

      setup.changeSetSummary3(
        locationChanges = Seq(change3),
        locations = Seq("be", "be-1", "be-1-b"),
      )

      setup.count() should equal(2)
      assertEqual(
        setup.changes(),
        Seq(
          LocationChangeSet(
            _id = "201:20",
            key = setup.key2,
            locationChanges = Seq(change2)
          ),
          LocationChangeSet(
            _id = "101:10",
            key = setup.key1,
            locationChanges = Seq(change1a, change1b)
          )
        )
      )

      setup.count(locationName = "be-1-b") should equal(2)
      assertEqual(
        setup.changes(locationName = "be-1-b"),
        Seq(
          LocationChangeSet(
            _id = "301:30",
            key = setup.key3,
            locationChanges = Seq(change3)
          ),
          LocationChangeSet(
            _id = "101:10",
            key = setup.key1,
            locationChanges = Seq(change1c)
          )
        )
      )
    }
  }

  test("impact") {
    withDatabase() { database =>
      val setup = new MongoQueryLocationChangesTestSetup(database)

      val change1a = setup.locationChanges(happy = true)
      val change1b = setup.locationChanges(investigate = true)
      val change1c = setup.locationChanges(/* no impact*/)
      val change2 = setup.locationChanges(happy = true)
      val change3 = setup.locationChanges(/* no impact*/)

      setup.changeSetSummary1(
        locationChanges = Seq(
          change1a,
          change1b,
          change1c
        )
      )

      setup.changeSetSummary2(
        locationChanges = Seq(
          change2,
        ),
      )

      setup.changeSetSummary3(
        locationChanges = Seq(
          change3,
        ),
      )

      setup.count(parameters = ChangesParameters(impact = true)) should equal(2)
      val impactedChanges = setup.changes(parameters = ChangesParameters(impact = true))

      assertEqual(
        impactedChanges,
        Seq(
          LocationChangeSet(
            _id = "201:20",
            key = setup.key2,
            locationChanges = Seq(change2)
          ),
          LocationChangeSet(
            _id = "101:10",
            key = setup.key1,
            locationChanges = Seq(change1a, change1b)
          ),
        )
      )
    }
  }

  // TODO test specific timerange
  //  parameters.year.map(year => equal("key.time.year", year.toInt)),
  //  parameters.month.map(month => equal("key.time.month", month.toInt)),
  //  parameters.day.map(day => equal("key.time.day", day.toInt))

  test("networkType") {

    withDatabase() { database =>
      val setup = new MongoQueryLocationChangesTestSetup(database)

      val change1a = setup.locationChanges()
      val change1b = setup.locationChanges()
      val change1c = setup.locationChanges(networkType = NetworkType.cycling)
      val change2 = setup.locationChanges()
      val change3 = setup.locationChanges(networkType = NetworkType.cycling)

      setup.changeSetSummary1(locationChanges = Seq(change1a, change1b, change1c))
      setup.changeSetSummary2(locationChanges = Seq(change2))
      setup.changeSetSummary3(locationChanges = Seq(change3))

      setup.count(NetworkType.hiking) should equal(2)
      assertEqual(
        setup.changes(NetworkType.hiking),
        Seq(
          LocationChangeSet(
            _id = "201:20",
            key = setup.key2,
            locationChanges = Seq(change2)
          ),
          LocationChangeSet(
            _id = "101:10",
            key = setup.key1,
            locationChanges = Seq(change1a, change1b)
          )
        )
      )

      setup.count(NetworkType.cycling) should equal(2)
      assertEqual(
        setup.changes(NetworkType.cycling),
        Seq(
          LocationChangeSet(
            _id = "301:30",
            key = setup.key3,
            locationChanges = Seq(change3)
          ),
          LocationChangeSet(
            _id = "101:10",
            key = setup.key1,
            locationChanges = Seq(change1c)
          )
        )
      )
    }
  }
}
