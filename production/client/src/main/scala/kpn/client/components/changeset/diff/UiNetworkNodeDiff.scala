package kpn.client.components.changeset.diff

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiRouteRef
import kpn.client.components.common.UiTagDiffs
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref
import kpn.shared.diff.network.NetworkNodeDiff

object UiNetworkNodeDiff {

  private case class Props(context: Context, diff: NetworkNodeDiff, knownElements: KnownElements)

  private val component = ScalaComponent.builder[Props]("network-node-diff")
    .render_P { props =>
      new Renderer(props.diff, props.knownElements)(props.context).render()
    }
    .build

  def apply(diff: NetworkNodeDiff, knownElements: KnownElements)(implicit context: Context): VdomElement = {
    component(Props(context, diff, knownElements))
  }

  private class Renderer(diff: NetworkNodeDiff, knownElements: KnownElements)(implicit context: Context) {

    def render(): VdomElement = {
      val elements = Seq(
        connection(),
        roleConnection(),
        definedInNetworkRelation(),
        routeRefefenceDiffs(),
        nodeIntegrityCheckDiff(),
        tagDiffs()
      ).flatten

      <.div(elements.toTagMod)
    }

    private def connection(): Option[VdomElement] = {
      diff.connection.map { connection =>
        if (connection) {
          <.div(
            nls(
              """Node belongs to another network.""",
              """Knooppunt behoort to ander netwerk."""
            )
          )
        }
        else {
          <.div(
            nls(
              """Node no longer belongs to other network.""",
              """Knooppunt behoort niet langer tot ander netwerk."""
            )
          )
        }
      }
    }

    private def roleConnection(): Option[VdomElement] = {
      diff.roleConnection.map { roleConnection =>
        if (roleConnection) {
          <.div(
            nls(
              """Node has role "connection" in the network relation.""",
              """Knooppunt heeft rol "connection" in netwerk relatie."""
            )
          )
        }
        else {
          <.div(
            nls(
              """Node no longer has role "connection" in the network relation.""",
              """Knooppunt heeft niet langer rol "connnection" in de netwerk relatie."""
            )
          )
        }
      }
    }

    private def definedInNetworkRelation(): Option[VdomElement] = {
      diff.definedInNetworkRelation.map { defined =>
        if (defined) {
          <.div(
            nls(
              "Node is added to network relation",
              "Knooppunt is toegevoegd aan netwerk relatie"
            )
          )
        }
        else {
          <.div(
            nls(
              "Node is removed from network relation",
              "Knooppunt is verwijderd uit netwerk relatie"
            )
          )
        }
      }
    }

    private def routeRefefenceDiffs(): Option[VdomElement] = {
      diff.routeReferenceDiffs.map { diffs =>
        <.div(
          routeRefs(
            diffs.removed,
            nls(
              "Removed route reference(s)",
              "Verwijderde route referentie(s)"
            )
          ),
          routeRefs(
            diffs.added,
            nls(
              "Added route reference(s)",
              "Toegevoegde route referentie(s)"
            )
          ),
          routeRefs(
            diffs.remaining,
            nls(
              "Remaining route reference(s)",
              "Blijvende route referentie(s)"
            )
          )
        )
      }
    }

    private def routeRefs(refs: Seq[Ref], title: String): TagMod = {
      UiCommaList(
        refs.map { ref =>
          UiRouteRef(ref, knownElements)
        },
        Some(title)
      )
    }

    private def nodeIntegrityCheckDiff(): Option[VdomElement] = {
      diff.nodeIntegrityCheckDiff.map { nodeIntegrityCheckDiff =>
        <.div(
          TagMod.when(nodeIntegrityCheckDiff.before.isEmpty && nodeIntegrityCheckDiff.after.isDefined) {
            val comment: VdomElement = <.span(
              nls(
                "Node integrity check has been added",
                "Integriteitscontrole toegevoegd"
              )
            )
            UiLine(
              comment,
              UiHappy()
            )
          },
          TagMod.when(nodeIntegrityCheckDiff.before.isDefined && nodeIntegrityCheckDiff.after.isEmpty) {
            val comment: VdomElement = <.span(
              nls(
                "Node integrity check has been removed",
                "De integriteitscontrole is verwijderd"
              )
            )
            UiLine(
              comment,
              UiInvestigate()
            )
          },
          <.table(
            ^.title := "node integrity check differences",
            <.thead(
              <.tr(
                <.th(""),
                <.th(nls("Before", "Voor")),
                <.th(nls("After", "Na"))
              )
            ),
            <.tbody(
              <.tr(
                <.td(nls("Expected", "Verwacht")),
                <.td(nodeIntegrityCheckDiff.before.whenDefined(_.expected)),
                <.td(nodeIntegrityCheckDiff.after.whenDefined(_.expected))
              ),
              <.tr(
                <.td(nls("Actual", "Gevonden")),
                <.td(nodeIntegrityCheckDiff.before.whenDefined(_.actual)),
                <.td(nodeIntegrityCheckDiff.after.whenDefined(_.actual))
              ),
              <.tr(
                <.td(nls("Result", "Resultaat")),
                <.td(nodeIntegrityCheckDiff.before.whenDefined(c => if (c.failed) "NOK" else "OK")),
                <.td(nodeIntegrityCheckDiff.after.whenDefined(c => if (c.failed) "NOK" else "OK"))
              )
            )
          )
        )
      }
    }

    private def tagDiffs(): Option[VdomElement] = {
      diff.tagDiffs.map(UiTagDiffs(_))
    }
  }

}
