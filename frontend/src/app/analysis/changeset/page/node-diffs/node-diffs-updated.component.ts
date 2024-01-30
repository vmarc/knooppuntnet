import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { NodeChangeDetailComponent } from '@app/analysis/components/changes/node';
import { MetaDataComponent } from '@app/components/shared';
import { LinkNodeRefHeaderComponent } from '@app/components/shared/link';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@node-diffs-updated.title">Updated network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
        </div>
        <div class="kpn-level-2-body">
          @for (nodeRef of refs; track nodeRef.id) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <kpn-link-node-ref-header [ref]="nodeRef" [knownElements]="data().knownElements" />
              </div>
              <div class="kpn-level-3-body">
                @for (nodeChangeInfo of data().findNodeChangeInfo(nodeRef); track $index) {
                  <div>
                    @if (nodeChangeInfo.before.version === nodeChangeInfo.after.version) {
                      <ng-container i18n="@@node-diffs-updated.existing-node">
                        Existing node v{{ nodeChangeInfo.after.version }}.
                      </ng-container>
                    }
                    @if (nodeChangeInfo.before.version !== nodeChangeInfo.after.version) {
                      <ng-container i18n="@@node-diffs-updated.node-changed">
                        Node changed to v{{ nodeChangeInfo.after.version }}
                      </ng-container>
                    }
                    <kpn-meta-data [metaData]="nodeChangeInfo.before" />
                    <kpn-node-change-detail [nodeChangeInfo]="nodeChangeInfo" />
                  </div>
                }
              </div>
            </div>
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [LinkNodeRefHeaderComponent, MetaDataComponent, NodeChangeDetailComponent],
})
export class NodeDiffsUpdatedComponent implements OnInit {
  data = input.required<NodeDiffsData>();

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.data().refDiffs.updated;
  }
}
