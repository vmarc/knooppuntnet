package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteNameDeprecatedNoteTag
import kpn.api.common.Fact.RouteNameMissing
import kpn.core.util.NaturalSorting
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.node.NodeUtil
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

/**
 * Analyzes the route name.
 *
 * The name of a network node route can be found (in following order of precedence):
 * <ol>
 * <li>in the 'ref' tag
 * <li>in the 'name' tag
 * <li>in the 'note' tag (any characters after ";" in the 'note' tag are ignored)
 * <li>in the 'from' and 'to' tags
 * </ol>
 *
 * If none of the above results in a route name, we try to make up a route name
 * from the nodes that are found in the route ways (requires less than 3 different
 * node names).
 *
 * The name of a non network node route can be found (in following order of precedence):
 * <ol>
 * <li>in the 'name' tag
 * <li>in the 'ref' tag
 * <li>in the 'from' and 'to' tags
 * </ol>
 */
object RouteNameAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteNameAnalyzer(context).analyze
  }
}

class RouteNameAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val routeNameAnalysis = if (context.nodeNetwork) {
      analyzeNetworkNodeRoute()
    }
    else {
      analyzeNonNetworkNodeRouteName()
    }

    context
      .copy(_routeNameAnalysis = Some(routeNameAnalysis))
      .withFact(routeNameAnalysis.name.isEmpty, RouteNameMissing)
      .withFact(routeNameAnalysis.derivedFromDeprecatedNoteTag, RouteNameDeprecatedNoteTag)
  }

  private def analyzeNetworkNodeRoute(): RouteNameAnalysis = {
    routeNameFromRefTag().getOrElse {
      routeNameFromNameTag().getOrElse {
        routeNameFromNoteTag().getOrElse {
          routeNameFromToAndFromTags().getOrElse {
            routeNameFromNodesInWays().getOrElse(
              RouteNameAnalysis()
            )
          }
        }
      }
    }
  }

  private def analyzeNonNetworkNodeRouteName(): RouteNameAnalysis = {
    context.relation.tagValue("name") match {
      case Some(name) => RouteNameAnalysis(Some(name))
      case None =>
        context.relation.tagValue("ref") match {
          case Some(ref) => RouteNameAnalysis(Some(ref))
          case None =>
            context.relation.tagValue("from") match {
              case Some(from) =>
                context.relation.tagValue("to") match {
                  case Some(to) => RouteNameAnalysis(Some(s"$from-$to"))
                  case None => RouteNameAnalysis(Some(s"${context.relation.id}"))
                }
              case None => RouteNameAnalysis(Some(s"${context.relation.id}"))
            }
        }
    }
  }

  private def routeNameFromRefTag(): Option[RouteNameAnalysis] = {
    context.relation.tagValue("ref").flatMap { ref =>
      routeNameFromTagValue(pureValue(ref))
    }
  }

  private def routeNameFromNameTag(): Option[RouteNameAnalysis] = {
    context.relation.tagValue("name").flatMap { name =>
      routeNameFromTagValue(pureValue(name)).map { routeNameAnalysis =>
        if (routeNameAnalysis.hasStandardNodeNames) {
          routeNameAnalysis
        }
        else {
          // verify is name in note tag is better, and if so: use that instead
          routeNameFromNoteTag() match {
            case None => routeNameAnalysis
            case Some(noteRouteNameAnalysis) =>
              if (noteRouteNameAnalysis.hasStandardNodeNames) {
                noteRouteNameAnalysis
              }
              else {
                routeNameAnalysis
              }
          }
        }
      }
    }
  }

  private def routeNameFromNoteTag(): Option[RouteNameAnalysis] = {
    context.relation.tagValue("note").flatMap { note =>
      val value = pureValue(note)
      if (NoteTagAnalyzer.isDeprecatedNoteTag(value)) {
        routeNameFromTagValue(value).map(_.copy(derivedFromDeprecatedNoteTag = true))
      }
      else {
        None
      }
    }
  }

  private def routeNameFromTagValue(tagValue: String): Option[RouteNameAnalysis] = {
    if (tagValue.contains("-")) {
      analyzeRouteNameNodes(tagValue)
    }
    else {
      Some(
        RouteNameAnalysis(
          Some(tagValue),
          None,
          None
        )
      )
    }
  }

  private def analyzeRouteNameNodes(routeName: String): Option[RouteNameAnalysis] = {

    val (startNodeNameOption, endNodeNameOption) = splitRouteName(routeName)

    val startNodeName = startNodeNameOption.getOrElse("")
    val endNodeName = endNodeNameOption.getOrElse("")

    val normalizedRouteName = normalizeRouteName(routeName, startNodeName, endNodeName)

    if (startNodeName.nonEmpty && endNodeNameOption.nonEmpty) {
      toRouteNameAnalysisFromNodeNames(normalizedRouteName, startNodeName, endNodeName)
    }
    else {
      Some(
        RouteNameAnalysis(
          Some(normalizedRouteName),
          startNodeNameOption,
          endNodeNameOption
        )
      )
    }
  }

  private def splitRouteName(routeName: String): (Option[String], Option[String]) = {
    val splitAt = if (routeName.count(_ == '-') > 1) " - " else "-"
    val nameParts = routeName.split(splitAt)
    if (nameParts.isEmpty) {
      (None, None)
    }
    else {
      val firstPart = nameParts.head.trim
      val startNodeNameOption = if (firstPart.nonEmpty) Some(NodeUtil.normalize(firstPart)) else None
      val endNodeNameOption = if (nameParts.size > 1) {
        val secondPart = nameParts(1).trim
        if (secondPart.nonEmpty) Some(NodeUtil.normalize(secondPart)) else None
      }
      else {
        None
      }
      (startNodeNameOption, endNodeNameOption)
    }
  }

  private def routeNameFromToAndFromTags(): Option[RouteNameAnalysis] = {
    context.relation.tagValue("from") match {
      case None =>
        context.relation.tagValue("to").flatMap { toNodeName =>
          val to = NodeUtil.normalize(toNodeName)
          Some(
            RouteNameAnalysis(
              Some(s"-$to"),
              None,
              Some(to)
            )
          )
        }

      case Some(fromNodeName) =>
        context.relation.tagValue("to") match {
          case None =>
            val from = NodeUtil.normalize(fromNodeName)
            Some(
              RouteNameAnalysis(
                Some(s"$from-"),
                Some(from),
                None
              )
            )
          case Some(toNodeName) =>
            val to = NodeUtil.normalize(toNodeName)
            val from = NodeUtil.normalize(fromNodeName)
            toRouteNameAnalysisFromNodeNames(s"$from-$to", from, to)
        }
    }
  }

  private def toRouteNameAnalysisFromNodeNames(routeName: String, startNodeName: String, endNodeName: String): Option[RouteNameAnalysis] = {
    Some(
      if (isRouteReversed(startNodeName, endNodeName)) {
        RouteNameAnalysis(
          Some(routeName),
          Some(endNodeName),
          Some(startNodeName),
          reversed = true
        )
      }
      else {
        RouteNameAnalysis(
          Some(routeName),
          Some(startNodeName),
          Some(endNodeName)
        )
      }
    )
  }

  private def withoutComment(tagValue: String): String = {
    if (tagValue.contains(";")) {
      tagValue.split(";").head
    }
    else {
      tagValue
    }
  }

  private def isRouteReversed(startNodeName: String, endNodeName: String): Boolean = {
    if (Util.isDigits(startNodeName) && Util.isDigits(endNodeName)) {
      if (startNodeName.toInt > endNodeName.toInt) {
        true
      }
      else {
        false
      }
    }
    else {
      if (Util.hasDigits(startNodeName) && Util.hasDigits(endNodeName)) {
        val originalOrder = Seq(startNodeName, endNodeName)
        val sortedOrder = NaturalSorting.sort(originalOrder)
        sortedOrder != originalOrder
      }
      else {
        false
      }
    }
  }

  private def normalizeRouteName(routeName: String, startNodeName: String, endNodeName: String): String = {
    if (useDashSpaces(routeName, startNodeName, endNodeName)) {
      s"$startNodeName - $endNodeName"
    }
    else {
      s"$startNodeName-$endNodeName"
    }
  }

  private def useDashSpaces(routeName: String, startNodeName: String, endNodeName: String): Boolean = {
    routeName.count(_ == '-') > 1 || startNodeName.length > 3 || endNodeName.length > 3
  }

  private def routeNameFromNodesInWays(): Option[RouteNameAnalysis] = {
    val nodeNames = context.routeNodeInfos.values.map(_.name).toSeq.distinct
    if (nodeNames.sizeIs == 1) {
      val startNodeName = nodeNames.head
      val endNodeName = startNodeName
      val routeName = s"$startNodeName-$endNodeName"
      val normalizedRouteName = normalizeRouteName(routeName, startNodeName, endNodeName)
      Some(
        RouteNameAnalysis(
          name = Some(normalizedRouteName),
          startNodeName = Some(startNodeName),
          endNodeName = Some(endNodeName),
          derivedFromNodes = true
        )
      )
    }
    else if (nodeNames.sizeIs == 2) {
      val startNodeName = nodeNames.head
      val endNodeName = nodeNames(1)
      val routeName = s"$startNodeName-$endNodeName"
      val normalizedRouteName = normalizeRouteName(routeName, startNodeName, endNodeName)
      Some(
        RouteNameAnalysis(
          name = Some(normalizedRouteName),
          startNodeName = Some(startNodeName),
          endNodeName = Some(endNodeName),
          derivedFromNodes = true
        )
      )
    }
    else {
      None
    }
  }

  private def pureValue(tagValue: String): String = {
    val tagValueWithoutComment = withoutComment(tagValue)
    tagValueWithoutComment.replaceAll("–", "-")
  }
}
