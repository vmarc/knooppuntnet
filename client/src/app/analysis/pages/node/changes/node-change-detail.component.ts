import {Component, Input} from "@angular/core";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-change-detail",
  template: `

<!-- TODO included in parent component already? 
    private def changeSetTags: TagMod = {
      TagMod.when(nodeChange.changeTags.nonEmpty) {
        UiChangeSetTags(nodeChange.changeTags)
      }
    }

-->
<!-- TODO included in parent component already? 
    private def version: TagMod = {
      nodeChange.version.whenDefined { version =>
        UiDetail(
          nls("Version", "Versie") + " " + version
        )
      }
    }
-->



<!-- ** facts -->
<!-- 
    private def facts = {
      nodeChange.facts.toTagMod { fact =>
        UiDetail(
          nls(fact.name, fact.nlName)
        )
      }
    }
-->


<!-- ** connectionChanges -->
<!-- 
    private def connectionChanges = {
      TagMod.when(nodeChange.connectionChanges.nonEmpty) {
        nodeChange.connectionChanges.toTagMod { refBooleanChange =>
          val text = if (refBooleanChange.after) {
            nls(
              """This node belongs to another network.""",
              """Dit knooppunt behoort tot een ander netwerk."""
            )
          }
          else {
            nls(
              """This node is no longer belongs to another network.""",
              """Dit knooppunt behoort niet langer tot een ander netwerk."""
            )
          }
          networkMessage(text, refBooleanChange.ref)
        }
      }
    }
-->


<!-- ** roleConnectionChanges -->
<!-- 
    private def roleConnectionChanges = {
      TagMod.when(nodeChange.roleConnectionChanges.nonEmpty) {
        nodeChange.roleConnectionChanges.toTagMod { refBooleanChange =>
          val text = if (refBooleanChange.after) {
            nls(
              """This node has role "connection" in the network relation.""",
              """Dit knooppunt heeft rol "connection" in de netwerk relatie."""
            )
          }
          else {
            nls(
              """This node is no longer has role "connection" in the network relation.""",
              """Dit knooppunt heeft niet langer de rol "connectinon" in de netwerk relatie."""
            )
          }
          networkMessage(text, refBooleanChange.ref)
        }
      }
    }
-->


<!-- ** definedInNetworkChanges -->
<!-- 
    private def definedInNetworkChanges: TagMod = {
      TagMod.when(nodeChange.definedInNetworkChanges.nonEmpty) {
        <.div(
          nodeChange.definedInNetworkChanges.toTagMod { refBooleanChange =>
            val text = if (refBooleanChange.after) {
              nls("Added to network relation of ", "Toegevoegd aan netwerkrelatie van")
            }
            else {
              nls("Removed from network relation of ", "Verwijderd uit netwerkrelatie van")
            }
            networkMessage(text, refBooleanChange.ref)
          }
        )
      }
    }
-->


    <!-- ** nodeChange.addedToRoute -->
    <div *ngFor="let ref of nodeChangeInfo.addedToRoute" class="kpn-detail">
      <ng-container>Added to route</ng-container> <!-- Toegevoegd aan route -->
      <a routerLink="{{'/analysis/route/' + ref.id}}">{{ref.name}}</a>.
    </div>

    <!-- ** nodeChange.addedToNetwork -->
    <div *ngFor="let ref of nodeChangeInfo.addedToNetwork" class="kpn-detail">
      <ng-container>Added to network</ng-container> <!-- Toegevoegd aan netwerk -->
      <a routerLink="{{'/analysis/network/' + ref.id}}">{{ref.name}}</a>.
    </div>


    <!-- ** nodeChange.removedFromRoute -->
    <div *ngFor="let ref of nodeChangeInfo.removedFromRoute" class="kpn-detail">
      <ng-container>Removed from route</ng-container> <!-- Verwijderd uit route -->
      <a routerLink="{{'/analysis/route/' + ref.id}}">{{ref.name}}</a>.
    </div>


    <!-- ** nodeChange.removedFromNetwork -->
    <div *ngFor="let ref of nodeChangeInfo.removedFromNetwork" class="kpn-detail">
      <ng-container>Removed from network</ng-container> <!-- Verwijderd uit netwerk -->
      <a routerLink="{{'/analysis/network/' + ref.id}}">{{ref.name}}</a>.
    </div>


    <!-- ** factDiffs -->
    <!-- 
        private def factDiffs = {
          TagMod.when(nodeChange.factDiffs.nonEmpty) {
            UiFactDiffs(nodeChange.factDiffs)
          }
        }
    -->


    <!-- ** tagDiffs -->
    <div *ngIf="nodeChangeInfo.tagDiffs" class="kpn-detail">
      <kpn-tag-diffs [tagDiffs]="nodeChangeInfo.tagDiffs"></kpn-tag-diffs>
    </div>



<!-- ** nodeMoved -->
<!-- 
    private def nodeMoved = {
      nodeChange.nodeMoved.whenDefined { nodeMoved =>
        <.div(
          TagMod.when(nodeMoved.distance > 0) {
            UiDetail(
              nls(
                s"The node has moved {nodeMoved.distance} meters.",
                s"Het knooppunt is {nodeMoved.distance} meter verplaatst."
              )
            )
          },
          TagMod.when(nodeMoved.distance == 0) {
            UiDetail(
              nls(
                s"The node has moved less than 1 meter.",
                s"Het knooppunt is minder dan 1 meter verplaatst."
              )
            )
          },
          UiDetail(
            <.div(
              UiSmallMap(new NodeHistoryMap("map-" + key, nodeMoved.after, nodeMoved.before)),
              <.div(
                GlobalStyles.note,
                nls(
                  s"Note: Node position is shown as it was at {nodeChange.changeKey.timestamp.yyyymmddhhmm}, while the map background is shown as it is today.",
                  s"Noot: De knooppunt positie wordt getoond zoals die was op {nodeChange.changeKey.timestamp.yyyymmddhhmm}, de kaart achtergrond toont " +
                    s"de toestand van vandaag."
                )
              )
            )
          )
        )
      }
    }
-->



<!--

    private def referenceChanges(refs: Seq[Ref], title: String, link: (Ref) => VdomElement): TagMod = {
      TagMod.when(refs.nonEmpty) {
        refs.toTagMod { ref =>
          UiDetail(
            <.div(
              title + ": ",
              link(ref)
            )
          )
        }
      }
    }

    private def networkMessage(string: String, networkRef: Ref): TagMod = {
      UiDetail(
        <.div(
          string + " ",
          networkLink(networkRef),
          "."
        )
      )
    }

    private def routeLink(ref: Ref): VdomElement = {
      context.gotoRoute(ref.id, ref.name)
    }

    private def networkLink(ref: Ref): VdomElement = {
      context.gotoNetworkDetails(ref.id, ref.name)
    }
  }
-->    
  `
})
export class NodeChangeDetailComponent {
  @Input() nodeChangeInfo: NodeChangeInfo;
}
