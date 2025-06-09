import { computed } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common/ref';
import { NodeChangeDetailComponent } from '@app/analysis/components/changes/node/node-change-detail.component';
import { LinkNodeRefHeaderComponent } from '@app/shared/components/link/link-node-ref-header';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'ui-node-diffs-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (refs().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@node-diffs-updated.title">Updated network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ refs().length }}</span>
        </div>
        <div class="kpn-level-2-body">
          @for (nodeRef of refs(); track nodeRef.id) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <ui-link-node-ref-header [ref]="nodeRef" [knownElements]="data().knownElements" />
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
                    <ui-meta-data [metaData]="nodeChangeInfo.before" />
                    <ui-node-change-detail [nodeChangeInfo]="nodeChangeInfo" />
                  </div>
                }
              </div>
            </div>
          }
        </div>
      </div>
    }
  `,
  imports: [LinkNodeRefHeaderComponent, MetaDataComponent, NodeChangeDetailComponent],
})
export class NodeDiffsUpdatedComponent {
  readonly data = input.required<NodeDiffsData>();
  protected readonly refs = computed(() => this.data().refDiffs.updated);
}
