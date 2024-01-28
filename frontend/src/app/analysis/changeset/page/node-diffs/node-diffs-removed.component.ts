import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Ref } from '@api/common/common';
import { MetaDataComponent } from '@app/components/shared';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { LinkNodeRefHeaderComponent } from '@app/components/shared/link';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@node-diffs-removed.title">Removed network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
          <kpn-icon-investigate />
        </div>
        <div class="kpn-level-2-body">
          @for (nodeRef of refs; track nodeRef.id) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <kpn-link-node-ref-header [ref]="nodeRef" [knownElements]="data().knownElements" />
              </div>
              @for (nodeChangeInfo of data().findNodeChangeInfo(nodeRef); track $index) {
                <div class="kpn-level-3-body">
                  <kpn-meta-data [metaData]="nodeChangeInfo.before" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [IconInvestigateComponent, LinkNodeRefHeaderComponent, MetaDataComponent],
})
export class NodeDiffsRemovedComponent implements OnInit {
  data = input<NodeDiffsData | undefined>();

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.data().refDiffs.removed;
  }
}
