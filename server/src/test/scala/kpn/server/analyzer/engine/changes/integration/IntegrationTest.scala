package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSummary
import kpn.api.common.OrphanNodeInfo
import kpn.api.common.Relation
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawElement
import kpn.api.custom.Change
import kpn.api.custom.Subset
import kpn.core.doc.BaseNetworkDoc
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NodeDoc
import kpn.core.doc.OrphanRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.test.MongoTest
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.Timestamps
import kpn.database.actions.nodes.MongoQueryOrphanNodes
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.context.Watched

class IntegrationTest extends MongoTest {

  private var contextOption: Option[IntegrationTestContext] = None

  def simulate(
    dataBefore: OverpassData,
    dataAfter: OverpassData,
    keepDatabaseAfterTest: Boolean = false
  )(f: => Unit): Unit = {
    doTestIntegration(
      dataBefore,
      dataAfter,
      LocationAnalyzerTest.locationAnalyzer,
      keepDatabaseAfterTest
    )(f)
  }

  def testIntegration(
    dataBefore: OverpassData,
    dataAfter: OverpassData,
    keepDatabaseAfterTest: Boolean = false
  )(f: => Unit): Unit = {
    val locationAnalyzer = new LocationAnalyzerMock()
    doTestIntegration(dataBefore, dataAfter, locationAnalyzer, keepDatabaseAfterTest)(f)
  }

  private def doTestIntegration(
    dataBefore: OverpassData,
    dataAfter: OverpassData,
    locationAnalyzer: LocationAnalyzer,
    keepDatabaseAfterTest: Boolean = false
  )(f: => Unit): Unit = {
    contextOption = Some(new IntegrationTestContext(database, dataBefore, dataAfter, locationAnalyzer))
    try {
      context.mainFullAnalyzer.analyze(Timestamps.before, None)
      context.analysisDataInitializer.load()
      f
    }
    finally {
      contextOption = None
    }
  }

  def context: IntegrationTestContext = {
    contextOption.get
  }

  def watched: Watched = {
    context.analysisContext.watched
  }

  def process(action: ChangeAction, elements: RawElement*): Unit = {
    val changes = Seq(Change(action, elements))
    process(changes)
  }

  def process(changes: Seq[Change]): Unit = {
    val changeSet = newChangeSet(changes = changes)
    processChangeSet(1, changeSet)
  }

  def processChangeSet(replicationNumber: Long, changeSet: ChangeSet): Unit = {
    val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
    val changeSetContext = ChangeSetContext(
      ReplicationId(replicationNumber),
      changeSet,
      elementIds
    )
    context.changeProcessorPipeline.process(changeSetContext)
    context.postProcessor.process()
  }

  def findRouteById(routeId: Long): RouteDoc = {
    database.routes.findById(routeId).map(_.copy(stamp = None)).getOrElse {
      val ids = database.routes.ids()
      if (ids.isEmpty) {
        fail(s"Could not find route $routeId, no routes in database")
      }
      else {
        fail(s"Could not find route $routeId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findBaseRouteById(routeId: Long): BaseRouteDoc = {
    database.baseRoutes.findById(routeId).getOrElse {
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
    database.nodes.findById(nodeId).map(_.copy(stamp = None)).getOrElse {
      val ids = database.nodes.ids()
      if (ids.isEmpty) {
        fail(s"Could not find node $nodeId, no nodes in database")
      }
      else {
        fail(s"Could not find node $nodeId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findOrphanNode(subset: Subset, nodeId: Long): OrphanNodeInfo = {
    val orphanNodeInfos = new MongoQuerySubsetOrphanNodes(database).execute(subset)
    orphanNodeInfos.find(_.id == nodeId).get
  }

  def findOrphanNodes(): Seq[OrphanNodeInfo] = {
    new MongoQueryOrphanNodes(database).execute()
  }

  def findBaseNodeById(nodeId: Long): BaseNodeDoc = {
    database.baseNodes.findById(nodeId).getOrElse {
      val ids = database.nodes.ids()
      if (ids.isEmpty) {
        fail(s"Could not find base node $nodeId, no base nodes in database")
      }
      else {
        fail(s"Could not find base node $nodeId (but found: ${ids.mkString(", ")})")
      }
    }
  }

  def findBaseNetworkById(networkId: Long): BaseNetworkDoc = {
    database.baseNetworks.findById(networkId).getOrElse {
      val ids = database.networks.ids()
      if (ids.isEmpty) {
        fail(s"Could not find BaseNetworkDoc $networkId, no networks in database")
      }
      else {
        fail(s"Could not find BaseNetworkDoc $networkId (but found: ${ids.mkString(", ")})")
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

  def findBaseRouteChangeById(id: String): BaseRouteChange = {
    database.baseRouteChanges.findByStringId(id).getOrElse {
      val ids = database.routeChanges.stringIds()
      if (ids.isEmpty) {
        fail(s"Could not find BaseRouteChange $id, no base route changes in database")
      }
      else {
        fail(s"Could not find BaseRouteChange $id (but found: ${ids.mkString(", ")})")
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
    if (database.nodeChanges.findAll().exists(_.id == nodeId)) {
      fail(s"unexpected node changes for node $nodeId")
    }
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
