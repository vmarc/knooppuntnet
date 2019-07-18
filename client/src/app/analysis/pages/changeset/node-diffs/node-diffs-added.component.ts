import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../../kpn/shared/common/ref";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-diffs-added",
  template: `
    <div *ngIf="!nodeRefs.isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@node-diffs-added.title">Added network nodes</span>
        <span>({{nodeRefs.size}})</span>
        <mat-icon svgIcon="happy"></mat-icon>
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
              <div *ngIf="nodeChangeInfo.after">
                <ng-container [ngSwitch]="nodeChangeInfo.after.version">
                  <div *ngSwitchCase="1">
                    <ng-container i18n="@@node-diffs-added.change-set-created">Created in this changeset.</ng-container>
                  </div>
                  <div *ngSwitchCase="changeSetId">
                    <ng-container i18n="@@node-diffs-added.change-set-updated">Updated in this changeset.</ng-container>
                    v{{nodeChangeInfo.after.version}}.
                  </div>
                  <div *ngSwitchDefault>
                    <ng-container i18n="@@node-diffs-added.change-set-existing">Existing node</ng-container>
                    <kpn-meta-data [metaData]="nodeChangeInfo.after"></kpn-meta-data>
                  </div>
                </ng-container>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class NodeDiffsAddedComponent {

  @Input() changeSetId: number;
  @Input() nodeRefs: List<Ref>;
  @Input() nodeChangeInfos: List<NodeChangeInfo>;

  findNodeChangeInfo(nodeId: number): List<NodeChangeInfo> {
    return this.nodeChangeInfos.filter(n => n.id === nodeId);
  }

}
