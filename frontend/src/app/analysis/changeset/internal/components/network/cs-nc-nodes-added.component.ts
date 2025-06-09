import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { OsmLinkNodeComponent } from '@app/shared/components/link/osm-link-node.component';

@Component({
  selector: 'ui-cs-nc-nodes-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (nodeIds().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.added-nodes">Added non-network nodes</span>
          <span class="kpn-brackets kpn-thin">{{ nodeIds().length }}</span>
          <ui-icon-investigate />
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (nodeId of nodeIds(); track $index) {
            <ui-osm-link-node [nodeId]="nodeId" [title]="nodeId.toString()" />
          }
        </div>
      </div>
    }
  `,
  imports: [IconInvestigateComponent, OsmLinkNodeComponent],
})
export class CsNcNodesAddedComponent {
  readonly networkChangeInfo = input.required<NetworkChangeInfo>();
  protected readonly nodeIds = computed(() => this.networkChangeInfo().nodes.added);
}
