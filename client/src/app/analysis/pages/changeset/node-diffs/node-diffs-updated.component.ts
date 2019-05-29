import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../../kpn/shared/common/ref";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-diffs-updated",
  template: `
    <div *ngIf="!nodeRefs.isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick">Updated network nodes</span> <!-- Gewijzigde knooppunten -->
        <span>({{nodeRefs.size}})</span>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let nodeRef of nodeRefs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <!-- TODO show name only instead of link if nodeId not in knownElements.nodeIds -->
            <kpn-link-node [nodeId]="nodeRef.id" [title]="nodeRef.name" class="kpn-thick"></kpn-link-node>
            <osm-link-node [nodeId]="nodeRef.id"></osm-link-node>
          </div>
          <div class="kpn-level-3-body">
            <div *ngFor="let nodeChangeInfo of findNodeChangeInfo(nodeRef.id)">
              <ng-container *ngIf="nodeChangeInfo.before == nodeChangeInfo.after">
                Existing node <!-- Bestaand knooppunt -->
                v{{nodeChangeInfo.after.version}}
              </ng-container>
              <ng-container *ngIf="nodeChangeInfo.before != nodeChangeInfo.after">
                Node change to <!-- Knooppunt veranderd naar -->
                v{{nodeChangeInfo.after.version}}
              </ng-container>
              <kpn-meta-data [metaData]="nodeChangeInfo.after"></kpn-meta-data>

              <div>

                <!--SEE: UiNodeChangeDetail-->

                <!--TODO facts,-->

                <!--connectionChanges,-->
                <div *ngFor="let refBooleanChange of nodeChangeInfo.connectionChanges" class="kpn-detail">
                  <span *ngIf="refBooleanChange.after === true">
                    This node belongs to another network.
                  </span> <!-- Dit knooppunt behoort tot een ander netwerk.-->
                  <span *ngIf="refBooleanChange.after === false">
                    This node is no longer belongs to another network.
                    <!-- Dit knooppunt behoort niet langer tot een ander netwerk. -->
                  </span>
                  <kpn-link-network-details [networkId]="refBooleanChange.ref.id" [title]="refBooleanChange.ref.name"></kpn-link-network-details>
                </div>

                <!--roleConnectionChanges,-->
                <div *ngFor="let refBooleanChange of nodeChangeInfo.roleConnectionChanges" class="kpn-detail">
                  <span *ngIf="refBooleanChange.after === true">
                    This node received role "connection" in the network relation <!-- Dit knooppunt kreeg rol "connection" in de netwerk relatie -->
                  </span>
                  <span *ngIf="refBooleanChange.after === false">
                    This node is no longer has role "connection" in the network relation.
                    <!-- Dit knooppunt heeft niet langer de rol "connectinon" in de netwerk relatie.-->
                  </span>
                  <kpn-link-network-details [networkId]="refBooleanChange.ref.id" [title]="refBooleanChange.ref.name"></kpn-link-network-details>
                </div>


                <!--definedInNetworkChanges,-->
                <div *ngFor="let refBooleanChange of nodeChangeInfo.definedInNetworkChanges" class="kpn-detail">
                  <span *ngIf="refBooleanChange.after === true">
                    Added to network relation of
                  </span> <!--Toegevoegd aan netwerkrelatie van -->
                  <span *ngIf="refBooleanChange.after === false">
                    Removed from network relation of  <!--Verwijderd uit netwerkrelatie van -->
                  </span>
                  <kpn-link-network-details [networkId]="refBooleanChange.ref.id" [title]="refBooleanChange.ref.name"></kpn-link-network-details>
                </div>


                <div *ngFor="let ref of nodeChangeInfo.addedToRoute" class="kpn-detail">
                  <span>Added to route</span> <!--Toegevoegd aan route -->
                  <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.addedToNetwork" class="kpn-detail">
                  <span>Added to network</span> <!-- Toegevoegd aan netwerk -->
                  <kpn-link-network-details [networkId]="ref.id" [title]="ref.name"></kpn-link-network-details>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.removedFromRoute" class="kpn-detail">
                  <span>Removed from route</span> <!--Verwijderd uit route -->
                  <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.removedFromNetwork" class="kpn-detail">
                  <span>Removed from network</span> <!-- Verwijderd uit netwerk -->
                  <kpn-link-network-details [networkId]="ref.id" [title]="ref.name"></kpn-link-network-details>
                </div>

                <!-- TODO factDiffs,-->
                <!-- TODO tagDiffs,-->
                <!-- TODO nodeMoved-->
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class NodeDiffsUpdatedComponent {

  @Input() changeSetId: number;
  @Input() nodeRefs: List<Ref>;
  @Input() nodeChangeInfos: List<NodeChangeInfo>;

  findNodeChangeInfo(nodeId: number): List<NodeChangeInfo> {
    return this.nodeChangeInfos.filter(n => n.id === nodeId);
  }

}
