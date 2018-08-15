package kpn.client.components.changeset.diff

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiDetail
import kpn.client.components.common.UiFactDiffs
import kpn.client.components.common.UiNodeRef
import kpn.client.components.common.UiTagDiffs
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref
import kpn.shared.diff.route.RouteDiff

object UiRouteDiff {

  private case class Props(context: Context, diff: RouteDiff, knownElements: KnownElements)

  private val component = ScalaComponent.builder[Props]("route-diff")
    .render_P { props =>
      new Renderer(props.diff, props.knownElements)(props.context).render()
    }
    .build

  def apply(diff: RouteDiff, knownElements: KnownElements)(implicit context: Context): VdomElement = {
    component(Props(context, diff, knownElements))
  }

  private class Renderer(diff: RouteDiff, knownElements: KnownElements)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        nameDiff,
        roleDiff,
        factDiffs,
        nodeDiffs,
        memberOrderChanged,
        tagDiffs
      )
    }

    private def nameDiff: TagMod = {
      diff.nameDiff.whenDefined { nameDiff =>
        UiDetail(
          nls(
            s"""Route name changed from "${nameDiff.before}" to "${nameDiff.after}".""",
            s"""Route naam gewijzigd van "${nameDiff.before}" naar "${nameDiff.after}"."""
          )
        )
      }
    }

    private def roleDiff: TagMod = {
      diff.roleDiff.whenDefined { roleDiff =>
        if (roleDiff.isAdd) {
          UiDetail(
            nls(
              s"""Route role "${roleDiff.after}" added to networkrelation.""",
              s"""Route rol "${roleDiff.after}" toegevoegd in netwerkrelatie."""
            )
          )
        }
        else if (roleDiff.isDelete) {
          UiDetail(
            nls(
              s"""Route role "${roleDiff.before}" removed from networkrelation.""",
              s"""Route rol "${roleDiff.before}" verwijderd uit netwerkrelatie."""
            )
          )
        }
        else {
          UiDetail(
            nls(
              s"""Route role in networkrelation changed from "${roleDiff.before.get}" to "${roleDiff.after.get}".""",
              s"""Route rol in netwerkrelatie gewijzigd van "${roleDiff.before.get}" naar "${roleDiff.after.get}"."""
            )
          )
        }
      }
    }

    private def factDiffs: TagMod = {
      diff.factDiffs.whenDefined(UiFactDiffs(_))
    }

    private def nodeDiffs: TagMod = {
      if (diff.nodeDiffs.nonEmpty) {
        <.div(
          diff.nodeDiffs.toTagMod { routeNodeDiff =>
            <.div(
              nodeRefs(routeNodeDiff.title + " " + nls("added", "toegevoegd"), routeNodeDiff.added),
              nodeRefs(routeNodeDiff.title + " " + nls("removed", "verwijderd"), routeNodeDiff.removed)
            )
          }
        )
      }
      else {
        TagMod()
      }
    }

    private def nodeRefs(title: String, refs: Seq[Ref]): TagMod = {
      TagMod.when(refs.nonEmpty) {
        UiDetail(
          UiCommaList(
            refs.map { ref =>
              UiNodeRef(ref, knownElements)
            },
            Some(title)
          )
        )
      }
    }

    private def memberOrderChanged: TagMod = {
      if (diff.memberOrderChanged) {
        UiDetail(
          nls(
            "Member order changed.",
            "Volgorde in route relatie gewijzigd."
          )
        )
      }
      else {
        TagMod()
      }
    }

    private def tagDiffs: TagMod = {
      diff.tagDiffs.whenDefined(UiTagDiffs(_))
    }
  }

}
