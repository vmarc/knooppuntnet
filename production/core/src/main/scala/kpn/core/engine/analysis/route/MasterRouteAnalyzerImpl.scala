package kpn.core.engine.analysis.route

import kpn.core.analysis.LinkType
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberNode
import kpn.core.analysis.RouteMemberWay
import kpn.core.changes.RelationAnalyzer
import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.analysis.WayAnalyzer
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzer
import kpn.core.engine.analysis.route.analyzers.BigGermanBicycleRouteWithoutNameRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.BigGermanBicycleRouteWithoutNetworkNodesAnalyzer
import kpn.core.engine.analysis.route.analyzers.ExpectedNameRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.FixmeIncompleteRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.FixmeTodoRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.ForeignCountryRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.GermanHikingRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.IngoredRouteAnalysisBuilder
import kpn.core.engine.analysis.route.analyzers.NetworkTaggedAsRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.OverlappingWaysRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteAnalysisBuilder
import kpn.core.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteFragmentAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteMapAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteMemberAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteNameAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteNodeAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteStructureAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteTagRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.SuspiciousWaysRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.UnexpectedNodeRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.UnexpectedRelationRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.WithoutWaysRouteAnalyzer
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.FragmentAnalyzer
import kpn.core.engine.analysis.route.segment.Segment
import kpn.core.engine.analysis.route.segment.SegmentAnalyzer
import kpn.core.engine.analysis.route.segment.SegmentBuilder
import kpn.core.engine.analysis.route.segment.SegmentFinderAbort
import kpn.core.engine.analysis.route.segment.ZzzzObsoleteSuspiciousWayAnalyzer
import kpn.core.engine.analysis.route.segment._OldOverlapAnalyzer
import kpn.core.load.data.LoadedRoute
import kpn.core.obsolete.OldLinkBuilder
import kpn.core.util.Log
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.Fact._
import kpn.shared.NetworkType
import kpn.shared.RouteSummary
import kpn.shared.Timestamp
import kpn.shared.common.MapBounds
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.data.Element
import kpn.shared.data.Member
import kpn.shared.data.Node
import kpn.shared.data.NodeMember
import kpn.shared.data.Tags
import kpn.shared.data.Way
import kpn.shared.data.WayMember
import kpn.shared.route.Both
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.route.WayDirection

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

class MasterRouteAnalyzerImpl(accessibilityAnalyzer: AccessibilityAnalyzer) extends MasterRouteAnalyzer {

  private val log = Log(classOf[MasterRouteAnalyzerImpl])

  override def analyze(networkNodes: Map[Long, NetworkNode], loadedRoute: LoadedRoute, orphan: Boolean): RouteAnalysis = {
    Log.context("route=%07d".format(loadedRoute.id)) {
      val routeAnalysis = new MasterRouteAnalyzerInstance(accessibilityAnalyzer, networkNodes, loadedRoute, orphan).analyze()
      val newRouteAnalysis = newAnalyze(networkNodes, loadedRoute, orphan)

      if (routeAnalysis != newRouteAnalysis) {
        log.error("MISMATCH bewteen old and new analysis")
      }

      newRouteAnalysis
    }
  }

  override def newAnalyze(networkNodes: Map[Long, NetworkNode], loadedRoute: LoadedRoute, orphan: Boolean): RouteAnalysis = {
    Log.context("route=%07d".format(loadedRoute.id)) {

      val context = RouteAnalysisContext(
        networkNodes,
        loadedRoute,
        orphan,
        interpreter = new Interpreter(loadedRoute.networkType)
      )

      val analyzers: List[RouteAnalyzer] = List(
        ForeignCountryRouteAnalyzer,
        GermanHikingRouteAnalyzer,
        BigGermanBicycleRouteWithoutNameRouteAnalyzer,
        RouteTagRouteAnalyzer,
        WithoutWaysRouteAnalyzer,
        FixmeIncompleteRouteAnalyzer,
        FixmeTodoRouteAnalyzer,
        UnexpectedNodeRouteAnalyzer,
        UnexpectedRelationRouteAnalyzer,
        RouteNameAnalyzer,
        RouteNodeAnalyzer,
        BigGermanBicycleRouteWithoutNetworkNodesAnalyzer,
        NetworkTaggedAsRouteAnalyzer,
        ExpectedNameRouteAnalyzer,
        SuspiciousWaysRouteAnalyzer,
        OverlappingWaysRouteAnalyzer,
        RouteFragmentAnalyzer,
        RouteStructureAnalyzer,
        RouteMemberAnalyzer,
        RouteMapAnalyzer
      )

      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteAnalyzer], context: RouteAnalysisContext): RouteAnalysis = {
    if (analyzers.isEmpty) {
      new RouteAnalysisBuilder(context).build
    }
    else {
      val newContext = analyzers.head.analyze(context)
      if (newContext.ignore) {
        new IngoredRouteAnalysisBuilder().build(newContext)
      }
      else {
        doAnalyze(analyzers.tail, newContext)
      }
    }
  }

}

class MasterRouteAnalyzerInstance(accessibilityAnalyzer: AccessibilityAnalyzer, networkNodes: Map[Long, NetworkNode], loadedRoute: LoadedRoute, orphan: Boolean) {

  private val facts: ListBuffer[Fact] = ListBuffer[Fact]()
  private val interpreter = new Interpreter(loadedRoute.networkType)

  def analyze(): RouteAnalysis = {

    if (isForeignCountryRoute) { // DONE ForeignCountryRouteAnalyzer
      return ignoredRouteAnalysis(Fact.IgnoreForeignCountry)
    }

    if (isGermanHikingRoute) {
      return ignoredRouteAnalysis(Fact.IgnoreUnsupportedSubset)
    }

    if (isBigGermanBicycleRouteWithoutName) {
      return ignoredRouteAnalysis(Fact.IgnoreNotNodeNetwork)
    }

    analyzeRouteTag()
    analyzeRouteWithoutWays()
    analyzeFixmeIncomplete()
    analyzeFixmeTodo()

    val unexpectedNodeIds = analyzeUnexpectedNodeIds()
    val unexpectedRelationIds = analyzeUnexpectedRelationIds()

    val routeNameAnalysis = analyzeRouteName()

    val routeNodeAnalysis = analyzeNodes(routeNameAnalysis)

    if (isBigGermanBicycleRouteWithoutNetworkNodes(routeNodeAnalysis)) {
      return ignoredRouteAnalysis(Fact.IgnoreNotNodeNetwork)
    }

    if (isNetworkTaggedAsRoute(routeNodeAnalysis)) {
      return ignoredRouteAnalysis(Fact.IgnoreNetworkTaggedAsRoute)
    }

    val expectedName = determineExpectedName(routeNameAnalysis, routeNodeAnalysis)

    val title: String = routeNameAnalysis.name match {
      case Some(routeName) => routeName
      case _ => "no-name"
    }

    analyzeSuspiciousWays()
    analyzeOverlappingWays()

    val fragments = determineRouteFragments(routeNodeAnalysis)
    val structure = analyzeStructure(routeNodeAnalysis, fragments)
    analyzeStructure2(routeNodeAnalysis, structure, fragments)

    val routeMembers = analyzeRouteMembers(routeNodeAnalysis)

    if (routeMembers.exists(!_.accessible)) {
      facts += RouteUnaccessible
    }

    /// building routeMap

    val ways: Seq[Way] = routeMembers.flatMap {
      case w: RouteMemberWay => Some(w.way)
      case _ => None
    }

    val allWayNodes = ways.flatMap(w => w.nodes)

    val bounds = MapBounds(allWayNodes ++ routeNodeAnalysis.routeNodes.map(_.node))

    val routeMap = buildRouteMap(routeNodeAnalysis, structure, bounds)

    if (facts.exists(_.isError)) {
      facts += RouteBroken
    }

    val display: Boolean = analyzeDisplay(title, routeNodeAnalysis)

    val route = buildRoute(title, routeMembers, ways, routeMap, unexpectedNodeIds, expectedName, structure, routeNodeAnalysis, display)

    RouteAnalysis(
      loadedRoute.relation,
      route = route,
      structure = structure,
      routeNodes = routeNodeAnalysis, // TODO rename in RouteAnalysis
      routeMembers = routeMembers,
      ways = ways,
      startNodes = routeMap.startNodes,
      endNodes = routeMap.endNodes,
      startTentacleNodes = routeMap.startTentacleNodes,
      endTentacleNodes = routeMap.endTentacleNodes,
      allWayNodes = allWayNodes,
      bounds = bounds
    )
  }

  private def calculatedRouteLength: Int = {
    loadedRoute.relation.wayMembers.map(_.way.length).sum
  }

  private def ignoredRouteAnalysis(fact: Fact): RouteAnalysis = {

    val summary = RouteSummary(
      loadedRoute.relation.id,
      loadedRoute.country,
      loadedRoute.networkType,
      loadedRoute.name,
      calculatedRouteLength,
      isBroken = false,
      loadedRoute.relation.wayMembers.size,
      loadedRoute.relation.timestamp,
      Seq(),
      loadedRoute.relation.tags
    )

    val lastUpdated = RelationAnalyzer.lastUpdated(loadedRoute.relation)

    val route = RouteInfo(
      summary,
      active = true,
      display = false,
      ignored = true,
      orphan = true,
      loadedRoute.relation.version,
      loadedRoute.relation.changeSetId,
      lastUpdated,
      loadedRoute.relation.tags,
      Seq(fact),
      None
    )

    RouteAnalysis(
      loadedRoute.relation,
      route = route
    )
  }


  private def connection: Boolean = loadedRoute.relation.tags.has("state", "connection")

  private def hasFact(expectedFacts: Fact*): Boolean = {
    expectedFacts.exists(f => facts.contains(f))
  }

  private def isForeignCountryRoute: Boolean = { // DONE ForeignCountryRouteAnalyzer
    orphan && loadedRoute.country.isEmpty
  }

  private def isGermanHikingRoute: Boolean = {
    loadedRoute.country.contains(Country.de) && loadedRoute.networkType == NetworkType.hiking
  }

  private def isBigGermanBicycleRouteWithoutName: Boolean = {
    val length100km = 100 * 1000
    orphan &&
      loadedRoute.country.contains(Country.de) &&
      loadedRoute.networkType == NetworkType.bicycle &&
      calculatedRouteLength > length100km &&
      !loadedRoute.relation.tags.has("note")
  }

  private def isBigGermanBicycleRouteWithoutNetworkNodes(routeNodeAnalysis: RouteNodeAnalysis): Boolean = {
    val length100km = 100 * 1000
    orphan &&
      loadedRoute.country.contains(Country.de) &&
      loadedRoute.networkType == NetworkType.bicycle &&
      calculatedRouteLength > length100km &&
      routeNodeAnalysis.routeNodes.isEmpty
  }

  private def isNetworkTaggedAsRoute(routeNodeAnalysis: RouteNodeAnalysis): Boolean = {
    orphan && routeNodeAnalysis.redundantNodes.size > 5
  }

  private def analyzeUnexpectedNodeIds(): Seq[Long] = {
    val unexpectedNodeIds: Seq[Long] = loadedRoute.relation.nodeMembers.map(_.node).filter(interpreter.isUnexpectedNode).map(_.id)
    if (unexpectedNodeIds.nonEmpty) {
      facts += RouteUnexpectedNode
    }
    unexpectedNodeIds
  }

  private def analyzeUnexpectedRelationIds(): Seq[Long] = {
    val unexpectedRelationIds: Seq[Long] = loadedRoute.relation.relationMembers.map(_.relation.id)
    if (unexpectedRelationIds.nonEmpty) {
      facts += RouteUnexpectedRelation
    }
    unexpectedRelationIds
  }

  private def analyzeRouteName(): RouteNameAnalysis = {
    val routeNameAnalysis = new ZzzzObsoleteRouteNameAnalyzer().analyze(loadedRoute.relation.tags("note"))
    if (routeNameAnalysis.name.isEmpty) {
      facts += RouteNameMissing
    }
    routeNameAnalysis
  }

  private def analyzeNodes(routeNameAnalysis: RouteNameAnalysis): RouteNodeAnalysis = {
    val routeNodeAnalysis = new ZzzzObsoleteRouteNodeAnalyzer(interpreter, routeNameAnalysis, loadedRoute.relation).analysis
    //  private val routeEndNodeMismatch = {
    //    // TODO implement, derive from RouteNodes? or better: include in RouteNodeAnalysis
    //    val factsRouteEndNodeMismatch = ListBuffer[RouteEndNodeMismatch]()
    //    if(factsRouteEndNodeMismatch.nonEmpty) Some(factsRouteEndNodeMismatch.toList.toSeq) else None
    //  }

    if (!connection || routeNodeAnalysis.hasStartAndEndNode) {
      if (!hasFact(RouteWithoutWays, RouteIncomplete)) {
        // do not report this fact if route has no ways or is known to be incomplete

        if (!routeNodeAnalysis.startNodes.exists(_.definedInWay) ||
          !routeNodeAnalysis.endNodes.exists(_.definedInWay)) {
          facts += RouteNodeMissingInWays
        }

        if (routeNodeAnalysis.redundantNodes.nonEmpty) {
          facts += RouteRedundantNodes
        }

        if (routeNodeAnalysis.reversed && loadedRoute.relation.wayMembers.size > 1) {
          facts += RouteReversed
        }
      }
    }

    routeNodeAnalysis
  }

  private def determineExpectedName(routeNameAnalysis: RouteNameAnalysis, routeNodeAnalysis: RouteNodeAnalysis): String = {
    if (routeNameAnalysis.name.isDefined && routeNodeAnalysis.startNodes.nonEmpty && routeNodeAnalysis.endNodes.nonEmpty) {
      val expected = routeNodeAnalysis.startNodes.head.name + "-" + routeNodeAnalysis.endNodes.head.name
      if (!routeNameAnalysis.name.get.equals(expected)) {
        facts += RouteNodeNameMismatch // TODO return fact from route*Analysis classes ??
      }
      expected
    }
    else {
      ""
    }
  }

  private def analyzeRouteTag(): Unit = {
    if (loadedRoute.relation.tags.has("route")) {
      val routeTagValue = loadedRoute.relation.tags("route").get
      if (!interpreter.networkType.routeTagValues.contains(routeTagValue)) {
        facts += RouteTagInvalid
      }
    }
    else {
      facts += RouteTagMissing
    }
  }

  private def analyzeRouteWithoutWays(): Unit = {
    if (!loadedRoute.relation.members.exists(_.isWay)) {
      facts += RouteWithoutWays
    }
  }

  private def analyzeFixmeIncomplete(): Unit = {
    if (loadedRoute.relation.tags.has("fixme", "incomplete")) {
      facts += RouteIncomplete
    }
  }

  private def analyzeFixmeTodo(): Unit = {
    if (loadedRoute.relation.tags.has("fixmetodo")) {
      facts += RouteFixmetodo
    }
  }

  private def analyzeSuspiciousWays(): Unit = {
    val suspiciousWayIds = new ZzzzObsoleteSuspiciousWayAnalyzer(loadedRoute.relation.wayMembers).suspiciousWayIds
    if (suspiciousWayIds.nonEmpty) {
      facts += RouteSuspiciousWays
    }
  }

  private def analyzeOverlappingWays(): Unit = {
    val overlappingWays = new _OldOverlapAnalyzer(loadedRoute.relation.wayMembers).overlappingWays
    if (overlappingWays.nonEmpty) {
      facts += RouteOverlappingWays
    }
  }

  private def determineRouteFragments(routeNodeAnalysis: RouteNodeAnalysis): Seq[Fragment] = {
    new FragmentAnalyzer(routeNodeAnalysis.usedNodes, loadedRoute.relation.wayMembers).fragments
  }

  private def analyzeStructure(routeNodeAnalysis: RouteNodeAnalysis, fragments: Seq[Fragment]): RouteStructure = {
    if (connection && !routeNodeAnalysis.hasStartAndEndNode) {
      RouteStructure(
        unusedSegments = new SegmentBuilder().segments(fragments)
      )
    }
    else if (hasFact(RouteNodeMissingInWays, RouteOverlappingWays)) {
      RouteStructure(
        unusedSegments = new SegmentBuilder().segments(fragments)
      )
    }
    else if (routeNodeAnalysis.redundantNodes.size > 3) {
      RouteStructure(
        unusedSegments = new SegmentBuilder().segments(fragments)
      )
    }
    else {
      try {
        new SegmentAnalyzer(
          interpreter.networkType,
          loadedRoute.relation.id,
          fragments,
          routeNodeAnalysis
        ).structure
      }
      catch {
        case e: SegmentFinderAbort =>

          facts += RouteAnalysisFailed

          RouteStructure()
      }
    }
  }


  private def analyzeStructure2(routeNodeAnalysis: RouteNodeAnalysis, structure: RouteStructure, fragments: Seq[Fragment]): Unit = {
    if (!hasFact(RouteAnalysisFailed, RouteOverlappingWays)) {
      if (!connection || routeNodeAnalysis.hasStartAndEndNode) {
        if (!hasFact(RouteWithoutWays)) {
          // do not report this fact if route has no ways or is known to be incomplete

          val oneWayRouteForward = loadedRoute.relation.tags.has("direction", "forward")
          val oneWayRouteBackward = loadedRoute.relation.tags.has("direction", "backward")

          val oneWayRoute = loadedRoute.relation.tags.tags.exists { tag =>
            (tag.key == "comment" && tag.value.contains("to be used in one direction")) ||
              (tag.key == "oneway" && tag.value == "yes")
          }

          val routeForward = structure.forwardSegment.nonEmpty && !structure.forwardSegment.get.broken
          val routeBackward = structure.backwardSegment.nonEmpty && !structure.backwardSegment.get.broken

          if (routeForward) {
            if (routeBackward) {
              if (oneWayRoute || oneWayRouteForward || oneWayRouteBackward) {
                facts += RouteNotOneWay
              }
            }
            else {
              if (oneWayRoute || oneWayRouteForward) {
                facts += RouteOneWay
              }
              else {
                facts += RouteNotBackward
              }
            }
          }
          else if (routeBackward) {
            if (oneWayRoute || oneWayRouteBackward) {
              facts += RouteOneWay
            }
            else {
              facts += RouteNotForward
            }
          }
          else {
            if (oneWayRoute || oneWayRouteForward || oneWayRouteBackward) {
              facts += RouteNotOneWay
            }
            facts += RouteNotForward
            facts += RouteNotBackward
          }
        }

        if (!hasFact(RouteNodeMissingInWays, RouteWithoutWays, RouteIncomplete, RouteOneWay)) {
          if (structure.forwardSegment.isEmpty || structure.forwardSegment.get.broken ||
            structure.backwardSegment.isEmpty || structure.backwardSegment.get.broken) {
            facts += RouteNotContinious
          }
        }

        if (!hasFact(RouteWithoutWays, RouteIncomplete, RouteNotForward, RouteNotBackward)) {
          if (structure.unusedSegments.nonEmpty) {
            facts += RouteUnusedSegments
          }

          val routeSortingOrderAnalysis = new RouteSortingOrderAnalyzer(fragments, structure).analysis
          if (!routeSortingOrderAnalysis.ok) {
            facts += RouteInvalidSortingOrder
          }
        }
      }
    }
  }

  private def analyzeRouteMembers(routeNodeAnalysis: RouteNodeAnalysis): Seq[RouteMember] = {
    // map with key Node.id and value node number
    val nodeMap: scala.collection.mutable.Map[Long, Int] = scala.collection.mutable.Map()
    val nodeNumberIterator = (1 to 10000).iterator
    val validRouteMembers: Seq[Member] = loadedRoute.relation.members.filter(interpreter.isValidNetworkMember)

    val wayRelationMembers = validRouteMembers.flatMap {
      case member: WayMember => Some(kpn.core.josm.RelationMember(member.role.getOrElse(""), member.way))
      case _ => None
    }

    val oldLinks = new OldLinkBuilder(wayRelationMembers).links

    //links.zip(relationMembers).toSeq.map { case(link, w) => LinkInfo(link, w)}

    val linkIterator = oldLinks.iterator
    //    val wayMemberIterator = validRouteMembers.filter(_.isWay).iterator
    //    val nodeMemberIterator = validRouteMembers.filter(_.isNode).iterator
    validRouteMembers.map {
      case nodeMember: NodeMember =>

        val node = nodeMember.node

        val name = networkNodes.get(node.id) match {
          case Some(networkNode) => networkNode.name
          case None => ""
        }

        val number = if (nodeMap.isDefinedAt(node.id)) {
          nodeMap(node.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(node.id) = n
          n
        }

        val alternateName = routeNodeAnalysis.routeNodes.find(rn => rn.id == node.id).map(_.alternateName) match {
          case Some(aname) => aname
          case _ => name
        }

        RouteMemberNode(name, alternateName, number.toString, nodeMember.role, node)


      case wayMember: WayMember =>

        // relationMember.isWay)
        val link = linkIterator.next()
        val way = wayMember.way
        val wayNetworkNodes = way.nodes.filter(n => interpreter.isNetworkNode(n.raw)).flatMap(n => routeNodeAnalysis.routeNodes.find(_.id == n.id))
        val name = way.tags("name").getOrElse("")

        val fromNode = if (link.linkType == LinkType.FORWARD) way.nodes.head else way.nodes.last
        val toNode = if (link.linkType == LinkType.FORWARD) way.nodes.last else way.nodes.head

        val from = if (nodeMap.isDefinedAt(fromNode.id)) {
          nodeMap(fromNode.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(fromNode.id) = n
          n
        }

        val to = if (nodeMap.isDefinedAt(toNode.id)) {
          nodeMap(toNode.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(toNode.id) = n
          n
        }

        val accessible = accessibilityAnalyzer.accessible(interpreter.networkType, way)

        // way.tags.has("route", "ferry") TODO draw boat icon?

        // some ways have <tag k="route" v="bicycle"/>; Is this enough to decide that this is ok ???

        RouteMemberWay(name, link, wayMember.role, way, fromNode, toNode, from.toString, to.toString, accessible, wayNetworkNodes)
    }
  }

  private def buildRouteMap(routeNodeAnalysis: RouteNodeAnalysis, structure: RouteStructure, bounds: MapBounds): RouteMap = {
    val forwardBreakPoint = {
      structure.forwardSegment match {
        case Some(segment) if segment.broken => Some(RouteAnalyzerFunctions.toTrackPoint(segment.nodes.last))
        case _ => None
      }
    }

    val backwardBreakPoint = {
      structure.backwardSegment match {
        case Some(segment) if segment.broken => Some(RouteAnalyzerFunctions.toTrackPoint(segment.nodes.last))
        case _ => None
      }
    }

    RouteMap(
      bounds,
      forwardSegments = structure.forwardSegment.toSeq.map(RouteAnalyzerFunctions.toTrackSegment),
      backwardSegments = structure.backwardSegment.toSeq.map(RouteAnalyzerFunctions.toTrackSegment),
      unusedSegments = structure.unusedSegments.map(RouteAnalyzerFunctions.toTrackSegment),
      startTentacles = structure.tentacles.map(RouteAnalyzerFunctions.toTrackSegment),
      //endTentacles = structure.endTentacles.map(toTrackSegment),
      forwardBreakPoint = forwardBreakPoint,
      backwardBreakPoint = backwardBreakPoint,
      startNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.startNodes.head)),
      endNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.endNodes.head)),
      startTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.size <= 1) Seq() else routeNodeAnalysis.startNodes.tail),
      endTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.size <= 1) Seq() else routeNodeAnalysis.endNodes.tail),
      redundantNodes = RouteAnalyzerFunctions.toInfos(routeNodeAnalysis.redundantNodes)
      // halfWayPoints Seq()
    )
  }


  private def buildRoute(
    title: String,
    routeMembers: Seq[RouteMember],
    ways: Seq[Way],
    routeMap: RouteMap,
    unexpectedNodeIds: Seq[Long],
    expectedName: String,
    structure: RouteStructure,
    routeNodeAnalysis: RouteNodeAnalysis,
    display: Boolean
  ): RouteInfo = {

    val members: Seq[RouteMemberInfo] = routeMembers.map { member =>

      RouteMemberInfo(
        member.id,
        member.memberType,
        member.memberType == "way",
        member.nodes,
        member.linkName,
        member.from: String,
        member.fromNode.id,
        member.to,
        member.toNode.id,
        member.role.getOrElse(""),
        member.element.timestamp,
        member.accessible, // TODO rename
        member.length,
        member.nodeCount,
        member.description,
        RouteAnalyzerFunctions.oneWay(member),
        RouteAnalyzerFunctions.oneWayTags(member)
      )
    }

    val length: Int = ways.map(_.length).sum

    val routeWays: Seq[Way] = {
      routeMembers.flatMap {
        case w: RouteMemberWay => Some(w.way)
        case _ => None
      }
    }

    def routeMemberWays: Seq[RouteMemberWay] = {
      routeMembers.flatMap {
        case w: RouteMemberWay => Some(w)
        case _ => None
      }
    }

    val accessible: Boolean = ways.size == routeMemberWays.count(_.accessible)

    val routeAnalysis = RouteInfoAnalysis(// TODO include this information in Route
      routeMap.startNodes, // TODO duplication in routeMap member
      routeMap.endNodes, // TODO duplication in routeMap member
      routeMap.startTentacleNodes, // TODO duplication in routeMap member
      routeMap.endTentacleNodes, // TODO duplication in routeMap member
      unexpectedNodeIds,
      members,
      expectedName,
      routeMap,
      new RouteStructureFormatter(structure).strings
    )

    val lastUpdatedElement: Element = {
      val elements: Seq[Element] = Seq(loadedRoute.relation) ++ routeWays ++ routeNodeAnalysis.routeNodes.map(rn => rn.node)
      elements.reduceLeft((a, b) => if (a.timestamp > b.timestamp) a else b)
    }

    val lastUpdated: Timestamp = lastUpdatedElement.timestamp

    val nodeNames = routeAnalysis.startNodes.map(_.name) ++ routeAnalysis.endNodes.map(_.name)

    val summary = RouteSummary(
      loadedRoute.relation.id,
      loadedRoute.country,
      interpreter.networkType,
      title,
      length,
      facts.contains(Fact.RouteBroken),
      routeWays.size,
      loadedRoute.relation.timestamp,
      nodeNames,
      loadedRoute.relation.tags
    )

    RouteInfo(
      summary,
      active = true,
      display = display,
      ignored = false,
      orphan = orphan,
      loadedRoute.relation.version,
      loadedRoute.relation.changeSetId,
      lastUpdated,
      loadedRoute.relation.tags,
      facts,
      Some(routeAnalysis)
    )
  }

  /*
    The property 'display' indicates whether the route is to be included in the
    analysis reports on screen.

    Display is false for orphan routes that we want to keep watching, but not show in screens:
    - do not display orphan routes when "no-name"
    - do not display orphan routes when no network nodes at all
   */
  private def analyzeDisplay(title: String, routeNodeAnalysis: RouteNodeAnalysis): Boolean = {
    if (orphan) {
      if (title == "no-name") {
        false
      }
      else {
        routeNodeAnalysis.routeNodes.nonEmpty
      }
    }
    else {
      true
    }
  }

}

object RouteAnalyzerFunctions {

  def toInfos(nodes: Seq[RouteNode]): Seq[RouteNetworkNodeInfo] = {
    nodes.map {
      routeNode =>
        RouteNetworkNodeInfo(
          routeNode.id,
          routeNode.name,
          routeNode.alternateName,
          routeNode.lat,
          routeNode.lon
        )
    }
  }

  def toTrackSegment(segment: Segment): TrackSegment = {
    val trackPoints = segment.segmentFragments.flatMap(_.nodes).map(toTrackPoint)
    TrackSegment(segment.surface, trackPoints)
  }

  def toTrackPoint(node: Node): TrackPoint = {
    TrackPoint(node.latitude.toString, node.longitude.toString)
  }


  def oneWay(member: RouteMember): WayDirection = {
    member match {
      case routeMemberWay: RouteMemberWay => WayAnalyzer.oneWay(routeMemberWay.way)
      case _ => Both
    }
  }

  def oneWayTags(member: RouteMember): Tags = {
    member match {
      case routeMemberWay: RouteMemberWay => WayAnalyzer.oneWayTags(routeMemberWay.way)
      case _ => Tags.empty
    }
  }
}
