import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {MetaData} from '@api/common/data/meta-data';
import {NodeDiffsData} from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span i18n="@@node-diffs-added.title">Added network nodes</span>
        <span class="kpn-brackets kpn-thin">{{refs().size}}</span>
        <kpn-icon-happy></kpn-icon-happy>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let nodeRef of refs()" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-node-ref-header [ref]="nodeRef" [knownElements]="data.knownElements"></kpn-link-node-ref-header>
          </div>
          <div class="kpn-level-3-body">
            <div *ngFor="let nodeChangeInfo of data.findNodeChangeInfo(nodeRef)">
              <div *ngIf="nodeChangeInfo.after">
                <div *ngIf="isCreated(nodeChangeInfo.after)">
                  <ng-container i18n="@@node-diffs-added.change-set-created">Created in this changeset.</ng-container>
                </div>
                <div *ngIf="isUpdated(nodeChangeInfo.after)">
                  <ng-container i18n="@@node-diffs-added.change-set-updated">Updated in this changeset.</ng-container>
                  v{{nodeChangeInfo.after.version}}.
                </div>
                <div *ngIf="isExisting(nodeChangeInfo.after)">
                  <span i18n="@@node-diffs-added.change-set-existing" class="kpn-label">Existing node</span>
                  <kpn-meta-data [metaData]="nodeChangeInfo.after"></kpn-meta-data>
                </div>
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

  isCreated(metaData: MetaData): boolean {
    return this.data.changeSetId === metaData.changeSetId && metaData.version === 1;
  }

  isUpdated(metaData: MetaData): boolean {
    return this.data.changeSetId === metaData.changeSetId && metaData.version !== 1;
  }

  isExisting(metaData: MetaData): boolean {
    return !this.isCreated(metaData) && !this.isUpdated(metaData);
  }
}
