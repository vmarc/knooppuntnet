// Migrated to Angular: change-set.component.ts
package kpn.client.components.changes

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.UiNetworkTypeIcon
import kpn.client.components.common.UiThin
import kpn.client.components.shared.UiChangeSet
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetNetwork
import kpn.shared.ChangeSetSummaryInfo

import scalacss.ScalaCssReact._

object UiChangeSetSummary {

  private case class Props(context: Context, changeSetSummaryInfo: ChangeSetSummaryInfo)

  private val component = ScalaComponent.builder[Props]("change-set")
    .render_P { props =>
      new Renderer(props.changeSetSummaryInfo)(props.context).render()
    }
    .build

  def apply(context: Context, changeSetSummaryInfo: ChangeSetSummaryInfo): VdomElement = {
    component(Props(context, changeSetSummaryInfo))
  }

  private class Renderer(changeSetSummaryInfo: ChangeSetSummaryInfo)(implicit context: Context) {

    private val s = changeSetSummaryInfo.summary

    def render(): VdomElement = {
      UiChangeSet(
        s.key,
        s.happy,
        s.investigate,
        changeSetSummaryInfo.comment,
        contents()
      )
    }

    private def contents(): VdomElement = {
      <.div(
        <.div(
          s.networkChanges.creates.toTagMod { network =>
            changeSetNetwork("C", network)
          },
          s.networkChanges.updates.toTagMod { network =>
            changeSetNetwork("U", network)
          },
          s.networkChanges.deletes.toTagMod { network =>
            changeSetNetwork("D", network)
          }
        ),
        TagMod.when(s.orphanNodeChanges.nonEmpty) {
          <.div(
            s.orphanNodeChanges.toTagMod { subsetNodeRefs =>
              <.div(
                <.div(
                  UiChangeSet.Styles.block,
                  UiThin(subsetNodeRefs.subset.country.domain.toUpperCase()),
                  " ",
                  UiNetworkTypeIcon(subsetNodeRefs.subset.networkType),
                  " ",
                  nls("Orphan node(s)", "Knooppunt wezen"),
                  <.div(
                    UiChangeSet.Styles.blockContents,
                    changeSetElementRefs(UiChangeSet.Styles.node, subsetNodeRefs.elementRefs)
                  )
                )
              )
            }
          )
        },
        TagMod.when(s.orphanRouteChanges.nonEmpty) {
          <.div(
            s.orphanRouteChanges.toTagMod { subsetRouteRefs =>
              <.div(
                UiChangeSet.Styles.block,
                <.div(
                  UiThin(subsetRouteRefs.subset.country.domain.toUpperCase()),
                  " ",
                  UiNetworkTypeIcon(subsetRouteRefs.subset.networkType),
                  " ",
                  nls("Orphan route(s)", "Route wezen"),
                  <.div(
                    UiChangeSet.Styles.blockContents,
                    changeSetElementRefs(UiChangeSet.Styles.route, subsetRouteRefs.elementRefs)
                  )
                )
              )
            }
          )
        }
      )
    }

    private def changeSetNetwork(action: String, network: ChangeSetNetwork): VdomElement = {
      <.div(
        UiChangeSet.Styles.block,
        UiThin(network.country.map(_.domain.toUpperCase()).getOrElse("")),
        " ",
        UiNetworkTypeIcon(network.networkType),
        " ",
        action,
        " ",
        context.gotoChangeSetNetwork(s.key.changeSetId, (s.key.replicationNumber, network.networkId), network.networkName),
        <.div(
          UiChangeSet.Styles.blockContents,
          TagMod.when(network.nodeChanges.nonEmpty) {
            <.div(
              changeSetElementRefs(UiChangeSet.Styles.node, network.nodeChanges)
            )
          },
          TagMod.when(network.routeChanges.nonEmpty) {
            <.div(
              changeSetElementRefs(UiChangeSet.Styles.route, network.routeChanges)
            )
          }
        )
      )
    }

    private def changeSetElementRefs(elementStyle: StyleA, refs: ChangeSetElementRefs): TagMod = {
      (refs.removed.map(ref => changeSetElementRef(UiChangeSet.Styles.delete, elementStyle, ref)) ++
        refs.added.map(ref => changeSetElementRef(UiChangeSet.Styles.add, elementStyle, ref)) ++
        refs.updated.map(ref => changeSetElementRef(UiChangeSet.Styles.update, elementStyle, ref))).toTagMod
    }

    private def changeSetElementRef(actionStyle: StyleA, elementStyle: StyleA, ref: ChangeSetElementRef): VdomElement = {

      val colorStyle = if (ref.investigate) {
        UiChangeSet.Styles.red
      }
      else if (ref.happy) {
        UiChangeSet.Styles.green
      }
      else {
        UiChangeSet.Styles.gray
      }

      <.div(
        UiChangeSet.Styles.refBlock + colorStyle,
        <.div(actionStyle),
        <.div(elementStyle),
        <.div(
          UiChangeSet.Styles.ref,
          ref.name
        )
      )
    }
  }

}
