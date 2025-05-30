import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { OsmLinkNodeComponent } from '@app/shared/components/link/osm-link-node.component';

@Component({
  selector: 'ui-cs-nc-nodes-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (nodeIds().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.updated-nodes">Updated non-network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ nodeIds().length }}</span>
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (nodeId of nodeIds(); track nodeId) {
            <ui-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
          }
        </div>
      </div>
    }
  `,
  imports: [OsmLinkNodeComponent],
})
export class CsNcNodesUpdatedComponent {
  networkChangeInfo = input.required<NetworkChangeInfo>();
  readonly nodeIds = computed(() => this.networkChangeInfo().nodes.updated);
}
