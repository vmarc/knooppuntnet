package kpn.client.components.changeset.diff

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.changeset.UiChangeSetChange
import kpn.client.components.changeset.UiMetaInfo
import kpn.client.components.changeset.UiVersionChange
import kpn.client.components.common.UiFactDiffs
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLevel3
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.client.components.route.UiRouteChangeDetail
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref
import kpn.shared.diff.RefDiffs
import kpn.shared.route.RouteChangeInfo

object UiRouteDiffs {

  private case class Props(
    context: Context,
    changeSetId: Long,
    contextId: String,
    routeDiffs: RefDiffs,
    routeChangeInfos: Seq[RouteChangeInfo],
    knownElements: KnownElements
  )

  private val component = ScalaComponent.builder[Props]("route-diffs")
    .render_P { props =>
      new Renderer(
        props.changeSetId,
        props.contextId,
        props.routeDiffs,
        props.routeChangeInfos,
        props.knownElements
      )(props.context).render()
    }
    .build

  def apply(
    changeSetId: Long,
    contextId: String,
    routeDiffs: RefDiffs,
    routeChangeInfos: Seq[RouteChangeInfo],
    knownElements: KnownElements
  )(implicit context: Context): VdomElement = {
    component(
      Props(
        context,
        changeSetId,
        contextId,
        routeDiffs,
        routeChangeInfos,
        knownElements
      )
    )
  }

  private class Renderer(
    changeSetId: Long,
    contextId: String,
    routeDiffs: RefDiffs,
    routeChangeInfos: Seq[RouteChangeInfo],
    knownElements: KnownElements
  )(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        removedRoutes(),
        addedRoutes(),
        updatedRoutes()
      )
    }

    private def removedRoutes(): TagMod = {
      TagMod.when(routeDiffs.removed.nonEmpty) {
        val title = nls("Removed routes", "Verwijderde routes")
        UiChangeSetChange(
          title,
          Some(s"(${routeDiffs.removed.size})"),
          Some(UiInvestigate())
        ) {
          routeDiffs.removed.toTagMod { ref =>
            UiLevel3(
              routeTitle(ref.id, ref.name),
              removedRouteDetail(ref)
            )
          }
        }
      }
    }

    private def removedRouteDetail(ref: Ref): TagMod = {
      routeChangeInfos.find(_.id == ref.id).whenDefined { routeChange =>
        routeChange.before.whenDefined { metaData =>
          UiMetaInfo(metaData)
        }
      }
    }

    private def addedRoutes(): TagMod = {
      TagMod.when(routeDiffs.added.nonEmpty) {
        val title = nls("Added routes", "Toegevoegde routes")
        UiChangeSetChange(title, Some(s"(${routeDiffs.added.size})"), Some(UiHappy())) {
          routeDiffs.added.toTagMod { ref =>
            UiLevel3(
              routeTitle(ref.id, ref.name),
              addedRouteDetail(ref)
            )
          }
        }
      }
    }

    private def addedRouteDetail(ref: Ref): TagMod = {
      routeChangeInfos.find(_.id == ref.id).whenDefined { routeChangeInfo =>
        <.div(
          addedRouteVersion(routeChangeInfo),
          routeChangeInfo.diffs.factDiffs.whenDefined(UiFactDiffs(_))
        )
      }
    }

    private def addedRouteVersion(routeChangeInfo: RouteChangeInfo): TagMod = {
      routeChangeInfo.after.whenDefined { metaData =>
        UiThin(
          <.div(
            if (metaData.changeSetId == changeSetId) {
              if (metaData.version == 1) {
                nls(
                  "New relation.",
                  "Nieuwe relatie."
                )
              } else {
                nls(
                  "Relation updated in this changeset.",
                  "Relatie gewijzigd in deze changeset."
                )
              } +
                s"v${metaData.version}"
            } else {
              <.span(
                nls(
                  "Existing relation.",
                  "Bestaande relatie."
                ),
                " ",
                UiMetaInfo(metaData)
              )
            }
          )
        )
      }
    }

    private def updatedRoutes(): TagMod = {
      TagMod.when(routeDiffs.updated.nonEmpty) {
        val title = nls("Updated routes", "Gewijzigde routes")
        UiChangeSetChange(title, Some(s" (${routeDiffs.updated.size})")) {
          routeDiffs.updated.toTagMod { ref =>
            UiLevel3(
              routeTitle(ref.id, ref.name),
              updatedRouteDetail(ref)
            )
          }
        }
      }
    }

    private def updatedRouteDetail(ref: Ref): TagMod = {
      routeChangeInfos.find(_.id == ref.id) whenDefined { routeChangeInfo =>
        val key = "route-" + contextId + "-" + ref.id
        val before = routeChangeInfo.before.get
        val after = routeChangeInfo.after.get
        <.div(
          UiVersionChange(before, after),
          UiRouteChangeDetail(key, routeChangeInfo)
        )
      }
    }

    private def routeTitle(id: Long, name: String): VdomElement = {
      if (knownElements.routeIds.contains(id)) {
        UiLine(
          UiThick(context.gotoRoute(id, name)),
          UiThin(UiOsmLink.osmRelation(id))
        )
      }
      else {
        UiLine(
          UiThick(name),
          UiThin(UiOsmLink.osmRelation(id))
        )
      }
    }
  }

}
