import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';

@Component({
  selector: 'kpn-cs-nc-nodes-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="nodeIds.length > 0" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <span i18n="@@change-set.network-changes.updated-nodes"
          >Updated non-network nodes</span
        >
        <span class="kpn-brackets kpn-thin">{{ nodeIds.length }}</span>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-node
          *ngFor="let nodeId of nodeIds"
          [nodeId]="nodeId"
          [title]="nodeId.toString()"
        >
        </kpn-osm-link-node>
      </div>
    </div>
  `,
})
export class CsNcNodesUpdatedComponent implements OnInit {
  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeIds: number[];

  ngOnInit(): void {
    this.nodeIds = this.networkChangeInfo.nodes.updated;
  }
}
