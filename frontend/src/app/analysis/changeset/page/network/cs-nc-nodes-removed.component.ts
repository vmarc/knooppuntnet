import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { IconHappyComponent } from '@app/components/shared/icon';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-cs-nc-nodes-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (nodeIds.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@change-set.network-changes.removed-nodes">Removed non-network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ nodeIds.length }}</span>
          <kpn-icon-happy />
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
  imports: [IconHappyComponent, OsmLinkNodeComponent],
})
export class CsNcNodesRemovedComponent implements OnInit {
  networkChangeInfo = input<NetworkChangeInfo | undefined>();

  nodeIds: number[];

  ngOnInit(): void {
    this.nodeIds = this.networkChangeInfo().nodes.removed;
  }
}
