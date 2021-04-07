import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';

@Component({
  selector: 'kpn-cs-nc-orphan-nodes-old',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-detail">
      <span
        i18n="@@change-set.network-diffs.orphan-nodes-resolved"
        class="kpn-label"
      >
        Orphan nodes added to this network</span
      >
      <div class="kpn-comma-list">
        <kpn-link-node-ref
          *ngFor="let ref of refs"
          [ref]="ref"
          [knownElements]="knownElements"
        >
        </kpn-link-node-ref>
      </div>
      <kpn-icon-happy class="kpn-separate"></kpn-icon-happy>
    </div>
  `,
})
export class CsNcOrphanNodesOldComponent implements OnInit {
  @Input() networkChangeInfo: NetworkChangeInfo;
  @Input() knownElements: KnownElements;

  refs: Ref[];

  ngOnInit(): void {
    this.refs = this.networkChangeInfo.orphanNodes.oldRefs;
  }
}
