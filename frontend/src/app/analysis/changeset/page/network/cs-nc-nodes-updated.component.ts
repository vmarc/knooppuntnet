import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-cs-nc-nodes-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (nodeIds.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.updated-nodes">Updated non-network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ nodeIds.length }}</span>
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (nodeId of nodeIds; track nodeId) {
            <kpn-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [OsmLinkNodeComponent],
})
export class CsNcNodesUpdatedComponent implements OnInit {
  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeIds: number[];

  ngOnInit(): void {
    this.nodeIds = this.networkChangeInfo.nodes.updated;
  }
}
