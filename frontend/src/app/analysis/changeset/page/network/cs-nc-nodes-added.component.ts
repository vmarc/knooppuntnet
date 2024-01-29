import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { OsmLinkNodeComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-cs-nc-nodes-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (nodeIds().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.added-nodes">Added non-network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ nodeIds().length }}</span>
          <kpn-icon-investigate />
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (nodeId of nodeIds(); track $index) {
            <kpn-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [IconInvestigateComponent, OsmLinkNodeComponent],
})
export class CsNcNodesAddedComponent {
  networkChangeInfo = input.required<NetworkChangeInfo>();
  readonly nodeIds = computed(() => this.networkChangeInfo().nodes.added);
}
