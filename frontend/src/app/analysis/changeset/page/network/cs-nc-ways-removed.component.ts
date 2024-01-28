import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { IconHappyComponent } from '@app/components/shared/icon';
import { OsmLinkWayComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-cs-nc-ways-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (wayIds.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.removed-ways">Removed ways</span>
          <span class="kpn-brackets kpn-thin">{{ wayIds.length }}</span>
          <kpn-icon-happy />
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (wayId of wayIds; track wayId) {
            <kpn-osm-link-way [wayId]="wayId" [title]="wayId.toString()" />
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [IconHappyComponent, OsmLinkWayComponent],
})
export class CsNcWaysRemovedComponent implements OnInit {
  networkChangeInfo = input<NetworkChangeInfo | undefined>();

  wayIds: number[];

  ngOnInit(): void {
    this.wayIds = this.networkChangeInfo().ways.removed;
  }
}
