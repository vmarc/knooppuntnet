import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Ref } from '@api/common/common/ref';
import { MetaData } from '@api/common/data/meta-data';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span i18n="@@node-diffs-added.title">Added network nodes</span>
        <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
        <kpn-icon-happy />
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let nodeRef of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-node-ref-header
              [ref]="nodeRef"
              [knownElements]="data.knownElements"
            />
          </div>
          <div class="kpn-level-3-body">
            <div
              *ngFor="let nodeChangeInfo of data.findNodeChangeInfo(nodeRef)"
            >
              <div *ngIf="nodeChangeInfo.after">
                <div *ngIf="isCreated(nodeChangeInfo.after)">
                  <ng-container i18n="@@node-diffs-added.change-set-created"
                  >Created in this changeset.
                  </ng-container
                  >
                </div>
                <!-- eslint-disable @angular-eslint/template/i18n -->
                <div *ngIf="isUpdated(nodeChangeInfo.after)">
                  <ng-container i18n="@@node-diffs-added.change-set-updated"
                  >Updated in this changeset.
                  </ng-container
                  >
                  v{{ nodeChangeInfo.after.version }}.
                </div>
                <!-- eslint-enable @angular-eslint/template/i18n -->
                <div *ngIf="isExisting(nodeChangeInfo.after)">
                  <span
                    i18n="@@node-diffs-added.change-set-existing"
                    class="kpn-label"
                  >Existing node</span
                  >
                  <kpn-meta-data [metaData]="nodeChangeInfo.after" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class NodeDiffsAddedComponent implements OnInit {
  @Input() data: NodeDiffsData;

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.data.refDiffs.added;
  }

  isCreated(metaData: MetaData): boolean {
    return (
      this.data.changeSetId === metaData.changeSetId && metaData.version === 1
    );
  }

  isUpdated(metaData: MetaData): boolean {
    return (
      this.data.changeSetId === metaData.changeSetId && metaData.version !== 1
    );
  }

  isExisting(metaData: MetaData): boolean {
    return !this.isCreated(metaData) && !this.isUpdated(metaData);
  }
}
