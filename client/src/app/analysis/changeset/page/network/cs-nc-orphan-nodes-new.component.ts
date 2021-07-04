import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { KnownElements } from '@api/common/common/known-elements';
import { Ref } from '@api/common/common/ref';
import { List } from 'immutable';

@Component({
  selector: 'kpn-cs-nc-orphan-nodes-new',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-detail">
      <span
        i18n="@@change-set.network-changes.orphan-nodes-introduced"
        class="kpn-label"
      >
        Following nodes that used to be part of this network have become
        orphan</span
      >
      <div class="kpn-comma-list">
        <kpn-link-node-ref
          *ngFor="let ref of refs()"
          [ref]="ref"
          [knownElements]="knownElements"
        >
        </kpn-link-node-ref>
      </div>
      <kpn-icon-investigate class="kpn-separate"></kpn-icon-investigate>
    </div>
  `,
})
export class CsNcOrphanNodesNewComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;
  @Input() knownElements: KnownElements;

  refs(): List<Ref> {
    return this.networkChangeInfo.orphanNodes.newRefs;
  }
}
