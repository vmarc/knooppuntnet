import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { MetaData } from '@api/common/data';
import { MetaDataComponent } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { LinkNodeRefHeaderComponent } from '@app/components/shared/link';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@node-diffs-added.title">Added network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
          <kpn-icon-happy />
        </div>
        <div class="kpn-level-2-body">
          @for (nodeRef of refs; track nodeRef.id) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <kpn-link-node-ref-header [ref]="nodeRef" [knownElements]="data().knownElements" />
              </div>
              <div class="kpn-level-3-body">
                @for (nodeChangeInfo of data().findNodeChangeInfo(nodeRef); track $index) {
                  @if (nodeChangeInfo.after) {
                    @if (isCreated(nodeChangeInfo.after)) {
                      <ng-container i18n="@@node-diffs-added.change-set-created">
                        Created in this changeset.
                      </ng-container>
                    }
                    @if (isUpdated(nodeChangeInfo.after)) {
                      <!-- eslint-disable @angular-eslint/template/i18n -->
                      <div>
                        <ng-container i18n="@@node-diffs-added.change-set-updated">
                          Updated in this changeset.
                        </ng-container>
                        v{{ nodeChangeInfo.after.version }}.
                      </div>
                    }
                    @if (isExisting(nodeChangeInfo.after)) {
                      <!-- eslint-enable @angular-eslint/template/i18n -->
                      <div>
                        <span i18n="@@node-diffs-added.change-set-existing" class="kpn-label">
                          Existing node
                        </span>
                        <kpn-meta-data [metaData]="nodeChangeInfo.after" />
                      </div>
                    }
                  }
                }
              </div>
            </div>
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [IconHappyComponent, LinkNodeRefHeaderComponent, MetaDataComponent],
})
export class NodeDiffsAddedComponent implements OnInit {
  data = input.required<NodeDiffsData>();

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.data().refDiffs.added;
  }

  isCreated(metaData: MetaData): boolean {
    return this.data().changeSetId === metaData.changeSetId && metaData.version === 1;
  }

  isUpdated(metaData: MetaData): boolean {
    return this.data().changeSetId === metaData.changeSetId && metaData.version !== 1;
  }

  isExisting(metaData: MetaData): boolean {
    return !this.isCreated(metaData) && !this.isUpdated(metaData);
  }
}
