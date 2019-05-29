// TODO migrate to Angular
package kpn.client.components.route

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.WayTagFilter
import kpn.client.common.map.UiSmallMap
import kpn.client.components.changeset.UiMetaInfo
import kpn.client.components.changeset.diff.UiRouteDiff
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiDetail
import kpn.client.components.common.UiLevel4
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiTagDiffs
import kpn.client.components.common.UiTagsTable
import kpn.client.components.common.UiThin
import kpn.shared.common.KnownElements
import kpn.shared.data.raw.RawNode
import kpn.shared.diff.WayInfo
import kpn.shared.diff.WayUpdate
import kpn.shared.node.NodeNameAnalyzer
import kpn.shared.route.RouteChangeInfo

object UiRouteChangeDetail {

  private case class Props(context: Context, key: String, routeChangeInfo: RouteChangeInfo)

  private val component = ScalaComponent.builder[Props]("route-change")
    .render_P { props =>
      new Renderer(props.key, props.routeChangeInfo)(props.context).render()
    }
    .build

  def apply(key: String, routeChangeInfo: RouteChangeInfo)(implicit context: Context): VdomElement = {
    component(Props(context, key, routeChangeInfo))
  }

  private class Renderer(key: String, routeChangeInfo: RouteChangeInfo)(implicit context: Context) {

    def render(): VdomElement = {

      <.div(
        UiRouteDiff(routeChangeInfo.diffs, KnownElements()),

        renderNodes(routeChangeInfo, "Added network node", "Toegevoegd knooppunt", routeChangeInfo.addedNodes),
        renderNodes(routeChangeInfo, "Deleted network node", "Verwijderd knooppunt", routeChangeInfo.deletedNodes),
        renderNodes(routeChangeInfo, "Unchanged network node", "Onveranderd knooppunt", routeChangeInfo.commonNodes),

        TagMod.when(routeChangeInfo.addedWayIds.nonEmpty) {
          UiDetail(nls("Added ways", "Toegevoegde wegen") + "=" + routeChangeInfo.addedWayIds.mkString(", "))
        },
        TagMod.when(routeChangeInfo.deletedWayIds.nonEmpty) {
          UiDetail(nls("Deleted ways", "Verwijderde wegen") + "=" + routeChangeInfo.deletedWayIds.mkString(", "))
        },
        TagMod.when(routeChangeInfo.geometryDiff.isEmpty) {
          UiDetail(nls("No geometry change", "Geometrie niet veranderd"))
        },
        routeChangeInfo.geometryDiff.whenDefined { geometryDiff =>
          UiDetail(
            UiSmallMap(
              new RouteHistoryMap(
                "map-" + key,
                routeChangeInfo.nodes,
                routeChangeInfo.bounds,
                geometryDiff
              )
            )
          )
        },
        routeRemovedWays(routeChangeInfo.removedWays),
        routeAddedWays(routeChangeInfo.addedWays),
        routeUpdatedWays(routeChangeInfo.updatedWays)
      )
    }

    private def version: VdomElement = {
      UiDetail(
        nls("Version", "Versie") + " " + routeChangeInfo.version
      )
    }

    private def renderNodes(entry: RouteChangeInfo, en: String, nl: String, nodeIds: Seq[Long]): TagMod = {
      nodeIds.flatMap { nodeId =>
        entry.nodes.filter(_.id == nodeId).map { node =>
          nodeLine(en, nl, node)
        }
      }.toTagMod
    }

    private def nodeLine(en: String, nl: String, node: RawNode): VdomElement = {
      val nodeName = NodeNameAnalyzer.name(node.tags)
      UiDetail(
        UiLine(
          <.span(
            nls(
              en,
              nl
            ) + ":"
          ),
          context.gotoNode(node.id, nodeName),
          <.span(
            "v" + node.version
          )
        )
      )
    }

    private def routeRemovedWays(removedWays: Seq[WayInfo]): TagMod = {
      removedWays.toTagMod { way =>
        UiLevel4(
          <.div(
            nls("Removed way", "Verwijderde weg"),
            " ",
            UiOsmLink.osmWay(way.id)
          ),
          <.div(
            UiDetail(
              UiThin(UiMetaInfo(way))
            ),
            UiDetail(
              UiTagsTable(WayTagFilter(way))
            )
          )
        )
      }
    }

    private def routeAddedWays(ways: Seq[WayInfo]): TagMod = {
      ways.toTagMod { way =>
        UiLevel4(
          routeAddedWayHeader(way),
          routeAddedWayBody(way)
        )
      }
    }

    private def routeAddedWayHeader(way: WayInfo): TagMod = {
      <.div(
        nls("Added way", "Toegevoegde weg"),
        " ",
        UiOsmLink.osmWay(way.id)
      )
    }

    private def routeAddedWayBody(way: WayInfo): TagMod = {
      <.div(
        UiDetail(
          if (way.changeSetId == routeChangeInfo.changeKey.changeSetId) {
            UiThin(
              <.span(
                s"[ v${way.version}",
                <.i(
                  nls(
                    "this changeset",
                    "deze changeset"
                  )
                ),
                "]"
              )
            )
          } else {
            UiThin(UiMetaInfo(way))
          }
        ),
        UiDetail(
          UiTagsTable(WayTagFilter(way))
        )
      )
    }

    private def routeUpdatedWays(wayUpdates: Seq[WayUpdate]): TagMod = {
      wayUpdates.toTagMod { wayUpdate =>
        UiLevel4(
          routeUpdatedWayHeader(wayUpdate),
          routeUpdatedWayBody(wayUpdate)
        )
      }
    }

    private def routeUpdatedWayHeader(wayUpdate: WayUpdate): TagMod = {
      <.div(
        nls("Updated way", "Gewijzigde weg"),
        " ",
        UiOsmLink.osmWay(wayUpdate.id)
      )
    }

    private def routeUpdatedWayBody(wayUpdate: WayUpdate): TagMod = {
      <.div(
        routeUpdatedWayVersion(wayUpdate),
        TagMod.when(wayUpdate.directionReversed) {
          UiDetail(
            nls(
              "Direction reversed",
              "Wegrichting omgekeerd"
            )
          )
        },
        wayUpdateNodes(
          nls(
            "Removed node(s)",
            "Verwijderde punten"
          ),
          wayUpdate.removedNodes.map(_.id)
        ),
        wayUpdateNodes(
          nls(
            "Added node(s)",
            "Toegevoegde punten"
          ),
          wayUpdate.addedNodes.map(_.id)
        ),
        wayUpdateNodes(
          nls(
            "Updated node(s)",
            "Gewijzigde punten"
          ),
          wayUpdate.updatedNodes.map(_.id)
        ),
        wayUpdate.tagDiffs.whenDefined{ tagDiffs =>
          UiDetail(
            UiTagDiffs(tagDiffs)
          )
        }
      )
    }

    private def routeUpdatedWayVersion(wayUpdate: WayUpdate): TagMod = {
      UiDetail(
        UiThin(
          <.div(
            if (wayUpdate.isNewVersion) {
              nls(
                s"Changed to v${wayUpdate.after.version}.",
                s"Veranderd naar v${wayUpdate.after.version}."
              )
            }
            else {
              nls(
                s"Way version unchanged.",
                s"Versie niet veranderd."
              )
            },
            " ",
            UiMetaInfo(wayUpdate.before)
          )
        )
      )
    }

    private def wayUpdateNodes(title: String, nodeIds: Seq[Long]): TagMod = {
      TagMod.when(nodeIds.nonEmpty) {
        UiDetail(
          UiCommaList(
            nodeIds.map { nodeId =>
              UiOsmLink.osmNode(nodeId)
            },
            Some(title)
          )
        )
      }
    }
  }

}
