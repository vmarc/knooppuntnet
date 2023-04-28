import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { IconHappyComponent } from '@app/components/shared/icon';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-cs-nc-nodes-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="nodeIds.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span i18n="@@change-set.network-changes.removed-nodes"
          >Removed non-network nodes</span
        >
        <span class="kpn-brackets kpn-thin">{{ nodeIds.length }}</span>
        <kpn-icon-happy />
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-node
          *ngFor="let nodeId of nodeIds"
          [nodeId]="nodeId"
          [title]="nodeId.toString()"
        />
      </div>
    </div>
  `,
  standalone: true,
  imports: [NgIf, IconHappyComponent, NgFor, OsmLinkNodeComponent],
})
export class CsNcNodesRemovedComponent implements OnInit {
  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeIds: number[];

  ngOnInit(): void {
    this.nodeIds = this.networkChangeInfo.nodes.removed;
  }
}
