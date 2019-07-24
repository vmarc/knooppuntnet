import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../../kpn/shared/common/ref";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-diffs-updated",
  template: `
    <div *ngIf="!nodeRefs.isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@node-diffs-updated.title">Updated network nodes</span>
        <span>({{nodeRefs.size}})</span>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let nodeRef of nodeRefs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <!-- TODO show name only instead of link if nodeId not in knownElements.nodeIds -->
            <kpn-link-node [nodeId]="nodeRef.id" [nodeName]="nodeRef.name" class="kpn-thick"></kpn-link-node>
            <osm-link-node [nodeId]="nodeRef.id"></osm-link-node>
          </div>
          <div class="kpn-level-3-body">
            <div *ngFor="let nodeChangeInfo of findNodeChangeInfo(nodeRef.id)">
              <ng-container *ngIf="nodeChangeInfo.before == nodeChangeInfo.after">
                <ng-container i18n="@@node-diffs-updated.existing-node">
                  Existing node
                </ng-container>
                v{{nodeChangeInfo.after.version}}
              </ng-container>
              <ng-container *ngIf="nodeChangeInfo.before != nodeChangeInfo.after">
                <ng-container i18n="@@node-diffs-updated.node-changed">
                  Node change to
                </ng-container>
                v{{nodeChangeInfo.after.version}}
              </ng-container>
              <kpn-meta-data [metaData]="nodeChangeInfo.after"></kpn-meta-data>

              <div>

                <!--SEE: UiNodeChangeDetail-->

                <!--TODO facts,-->

                <!--connectionChanges,-->
                <div *ngFor="let refBooleanChange of nodeChangeInfo.connectionChanges" class="kpn-detail">
                  <span *ngIf="refBooleanChange.after === true" i18n="@@node-diffs-updated.node-belongs-to-other-network">
                    This node belongs to another network.
                  </span>
                  <span *ngIf="refBooleanChange.after === false" i18n="@@node-diffs-updated.node-no-longer-belongs-to-other-network">
                    This node is no longer belongs to another network.
                  </span>
                  <kpn-link-network-details [networkId]="refBooleanChange.ref.id" [title]="refBooleanChange.ref.name"></kpn-link-network-details>
                </div>

                <!--roleConnectionChanges,-->
                <div *ngFor="let refBooleanChange of nodeChangeInfo.roleConnectionChanges" class="kpn-detail">
                  <span *ngIf="refBooleanChange.after === true" i18n="@@node-diffs-updated.received-role-connection">
                    This node received role "connection" in the network relation
                  </span>
                  <span *ngIf="refBooleanChange.after === false" i18n="@@node-diffs-updated.lost-role-connection">
                    This node is no longer has role "connection" in the network relation.
                  </span>
                  <kpn-link-network-details [networkId]="refBooleanChange.ref.id" [title]="refBooleanChange.ref.name"></kpn-link-network-details>
                </div>


                <!--definedInNetworkChanges,-->
                <div *ngFor="let refBooleanChange of nodeChangeInfo.definedInNetworkChanges" class="kpn-detail">
                  <span *ngIf="refBooleanChange.after === true" i18n="@@node-diffs-updated.added-to-network-relation">
                    Added to network relation of
                  </span>
                  <span *ngIf="refBooleanChange.after === false" i18n="@@node-diffs-updated.removed-from-network-relation">
                    Removed from network relation of
                  </span>
                  <kpn-link-network-details [networkId]="refBooleanChange.ref.id" [title]="refBooleanChange.ref.name"></kpn-link-network-details>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.addedToRoute" class="kpn-detail">
                  <span i18n="@@node-diffs-updated.added-to-route">Added to route</span>
                  <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.addedToNetwork" class="kpn-detail">
                  <span i18n="@@node-diffs-updated.added-to-network">Added to network</span>
                  <kpn-link-network-details [networkId]="ref.id" [title]="ref.name"></kpn-link-network-details>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.removedFromRoute" class="kpn-detail">
                  <span i18n="@@node-diffs-updated.removed-from-route">Removed from route</span>
                  <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
                </div>

                <div *ngFor="let ref of nodeChangeInfo.removedFromNetwork" class="kpn-detail">
                  <span i18n="@@node-diffs-updated.removed-from-network">Removed from network</span>
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
