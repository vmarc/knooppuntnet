import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { OsmLinkWayComponent } from '@app/shared/components/link/osm-link-way.component';

@Component({
  selector: 'ui-cs-nc-ways-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (wayIds().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.removed-ways">Removed ways</span>
          <span class="kpn-brackets kpn-thin">{{ wayIds().length }}</span>
          <ui-icon-happy />
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (wayId of wayIds(); track wayId) {
            <ui-osm-link-way [wayId]="wayId" [title]="wayId.toString()" />
          }
        </div>
      </div>
    }
  `,
  imports: [IconHappyComponent, OsmLinkWayComponent],
})
export class CsNcWaysRemovedComponent {
  networkChangeInfo = input.required<NetworkChangeInfo>();
  readonly wayIds = computed(() => this.networkChangeInfo().ways.removed);
}
