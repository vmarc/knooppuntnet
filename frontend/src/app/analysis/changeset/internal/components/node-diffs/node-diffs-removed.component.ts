import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common/ref';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { LinkNodeRefHeaderComponent } from '@app/shared/components/link/link-node-ref-header';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'ui-node-diffs-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@node-diffs-removed.title">Removed network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
          <ui-icon-investigate />
        </div>
        <div class="kpn-level-2-body">
          @for (nodeRef of refs; track nodeRef.id) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <ui-link-node-ref-header [ref]="nodeRef" [knownElements]="data().knownElements" />
              </div>
              @for (nodeChangeInfo of data().findNodeChangeInfo(nodeRef); track $index) {
                <div class="kpn-level-3-body">
                  <ui-meta-data [metaData]="nodeChangeInfo.before" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  imports: [IconInvestigateComponent, LinkNodeRefHeaderComponent, MetaDataComponent],
})
export class NodeDiffsRemovedComponent implements OnInit {
  data = input.required<NodeDiffsData>();

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.data().refDiffs.removed;
  }
}
