import {Component, Input} from "@angular/core";
import {NodeDiffsData} from "./node-diffs-data";

@Component({
  selector: "kpn-node-diffs-added",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@node-diffs-added.title">Added network nodes</span>
        <span>({{refs().size}})</span>
        <kpn-icon-happy></kpn-icon-happy>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let nodeRef of refs()" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-node-ref [ref]="nodeRef" [knownElements]="data.knownElements"></kpn-link-node-ref>
          </div>
          <div class="kpn-level-3-body">
            <div *ngFor="let nodeChangeInfo of data.findNodeChangeInfo(nodeRef)">
              <div *ngIf="nodeChangeInfo.after">
                <ng-container [ngSwitch]="nodeChangeInfo.after.version">
                  <div *ngSwitchCase="1">
                    <ng-container i18n="@@node-diffs-added.change-set-created">Created in this changeset.</ng-container>
                  </div>
                  <div *ngSwitchCase="data.changeSetId">
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

  @Input() data: NodeDiffsData;

  refs() {
    return this.data.refDiffs.added;
  }

}
