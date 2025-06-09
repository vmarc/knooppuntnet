import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { OsmLinkWayComponent } from '@app/shared/components/link/osm-link-way.component';

@Component({
  selector: 'ui-cs-nc-ways-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (wayIds().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.added-ways">Added ways</span>
          <span class="kpn-brackets kpn-thin">{{ wayIds().length }}</span>
          <ui-icon-investigate />
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (wayId of wayIds(); track wayId) {
            <ui-osm-link-way [wayId]="wayId" [title]="wayId.toString()" />
          }
        </div>
      </div>
    }
  `,
  imports: [IconInvestigateComponent, OsmLinkWayComponent],
})
export class CsNcWaysAddedComponent {
  readonly networkChangeInfo = input.required<NetworkChangeInfo>();
  protected readonly wayIds = computed(() => this.networkChangeInfo().ways.added);
}
