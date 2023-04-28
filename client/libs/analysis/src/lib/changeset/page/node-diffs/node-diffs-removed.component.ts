import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Ref } from '@api/common/common';
import { NodeDiffsData } from './node-diffs-data';

@Component({
  selector: 'kpn-node-diffs-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span i18n="@@node-diffs-removed.title">Removed network nodes</span>
        <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
        <kpn-icon-investigate />
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let nodeRef of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-node-ref-header
              [ref]="nodeRef"
              [knownElements]="data.knownElements"
            />
          </div>
          <div
            *ngFor="let nodeChangeInfo of data.findNodeChangeInfo(nodeRef)"
            class="kpn-level-3-body"
          >
            <kpn-meta-data [metaData]="nodeChangeInfo.before" />
          </div>
        </div>
      </div>
    </div>
  `,
})
export class NodeDiffsRemovedComponent implements OnInit {
  @Input() data: NodeDiffsData;

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.data.refDiffs.removed;
  }
}
