package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawElement
import kpn.api.custom.Change
import kpn.api.custom.Relation
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.OrphanNodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.network.NetworkChange
import kpn.server.analyzer.engine.context.Watched
import org.scalamock.scalatest.MockFactory

class IntegrationTest extends UnitTest with MockFactory with SharedTestObjects {

  private var contextOption: Option[IntegrationTestContext] = None

  def simulate(dataBefore: OverpassData, dataAfter: OverpassData, keepDatabaseAfterTest: Boolean = false)(f: => Unit): Unit = {
    doTestIntegration(
      dataBefore,
      dataAfter,
      LocationAnalyzerTest.locationAnalyzer,
      keepDatabaseAfterTest
    )(f)
  }

  def testIntegration(dataBefore: OverpassData, dataAfter: OverpassData)(f: => Unit): Unit = {
    val locationAnalyzer = new LocationAnalyzerMock()
    doTestIntegration(dataBefore, dataAfter, locationAnalyzer)(f)
  }

  private def doTestIntegration(
    dataBefore: OverpassData,
    dataAfter: OverpassData,
    locationAnalyzer: LocationAnalyzer,
    keepDatabaseAfterTest: Boolean = false
  )(f: => Unit): Unit = {
    withDatabase(keepDatabaseAfterTest) { database =>
      contextOption = Some(new IntegrationTestContext(database, dataBefore, dataAfter, locationAnalyzer))
      try {
        context.fullAnalyzer.analyze(timestampBeforeValue)
        context.analysisDataInitializer.load()
        f
      }
      finally {
        contextOption = None
      }
    }
  }

  def context: IntegrationTestContext = {
    contextOption.get
  }

  def database: Database = {
    context.database
  }

  def watched: Watched = {
    context.analysisContext.watched
  }

  def process(action: ChangeAction, element: RawElement): Unit = {
    val changes = Seq(Change(action, Seq(element)))
    process(changes)
  }

  def process(changes: Seq[Change]): Unit = {
    val changeSet = newChangeSet(changes = changes)
    val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
    val changeSetContext = ChangeSetContext(
      ReplicationId(1),
      changeSet,
      elementIds
    )
    context.changeProcessor.process(changeSetContext)
    context.postProcessor.processPhase2()
  }

  def findRouteById(routeId: Long): RouteDoc = {
    database.routes.findById(routeId).getOrElse {
      val ids = database.routes.ids()
      if (ids.isEmpty) {
        fail(s"Could not find route $routeId, no routes in database")
      }
      else {
        fail(s"Could not find route $routeId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findOrphanRouteById(routeId: Long): OrphanRouteDoc = {
    database.orphanRoutes.findById(routeId).getOrElse {
      val ids = database.orphanRoutes.ids()
      if (ids.isEmpty) {
        fail(s"Could not find orphan route $routeId, no orphan routes in database")
      }
      else {
        fail(s"Could not find orphan route $routeId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findNodeById(nodeId: Long): NodeDoc = {
    database.nodes.findById(nodeId).getOrElse {
      val ids = database.nodes.ids()
      if (ids.isEmpty) {
        fail(s"Could not find node $nodeId, no nodes in database")
      }
      else {
        fail(s"Could not find route $nodeId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findOrphanNodeById(_id: String): OrphanNodeDoc = {
    database.orphanNodes.findByStringId(_id).getOrElse {
      val ids = database.orphanNodes.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find orphan node ${_id}, no orphan nodes in database")
      }
      else {
        fail(s"Could not find orphan node ${_id} (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findNetworkById(networkId: Long): NetworkDoc = {
    database.networks.findById(networkId).getOrElse {
      val ids = database.networks.ids()
      if (ids.isEmpty) {
        fail(s"Could not find NetworkDoc $networkId, no networks in database")
      }
      else {
        fail(s"Could not find NetworkDoc $networkId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findNetworkInfoById(networkId: Long): NetworkInfoDoc = {
    database.networkInfos.findById(networkId).getOrElse {
      val ids = database.networkInfos.ids()
      if (ids.isEmpty) {
        fail(s"Could not find NetworkInfoDoc $networkId, no networks in database")
      }
      else {
        fail(s"Could not find NetworkInfoDoc $networkId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findChangeSetSummaryById(id: String): ChangeSetSummary = {
    database.changes.findByStringId(id).getOrElse {
      val ids = database.changes.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find changeSetSummary $id, no changeSetSummaries in database")
      }
      else {
        fail(s"Could not find changeSetSummary $id (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findNetworkChangeById(id: String): NetworkChange = {
    database.networkChanges.findByStringId(id).getOrElse {
      val ids = database.networkChanges.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find NetworkChange $id, no network changes in database")
      }
      else {
        fail(s"Could not find NetworkChange $id (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findNetworkInfoChangeById(id: String): NetworkInfoChange = {
    database.networkInfoChanges.findByStringId(id).getOrElse {
      val ids = database.networkInfoChanges.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find NetworkInfoChange $id, no network info changes in database")
      }
      else {
        fail(s"Could not find NetworkInfoChange $id (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findRouteChangeById(id: String): RouteChange = {
    database.routeChanges.findByStringId(id).getOrElse {
      val ids = database.routeChanges.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find RouteChange $id, no route changes in database")
      }
      else {
        fail(s"Could not find RouteChange $id (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findNodeChangeById(id: String): NodeChange = {
    database.nodeChanges.findByStringId(id).getOrElse {
      val ids = database.nodeChanges.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find NodeChange $id, no node changes in database")
      }
      else {
        fail(s"Could not find NodeChange $id (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def assertNoNodeChange(nodeId: Long): Unit = {
    !database.nodeChanges.findAll().exists(_.id == nodeId)
  }

  def beforeNodeWithId(nodeId: Long): Node = {
    context.before.nodes.getOrElse(
      nodeId,
      throw new IllegalArgumentException(s"No node with id $nodeId in test data")
    )
  }

  def beforeRelationWithId(relationId: Long): Relation = {
    context.before.relations.getOrElse(
      relationId,
      throw new IllegalArgumentException(s"No relation with id $relationId in before test data")
    )
  }

  def afterRelationWithId(relationId: Long): Relation = {
    context.after.relations.getOrElse(
      relationId,
      throw new IllegalArgumentException(s"No relation with id $relationId in after test data")
    )
  }
}
