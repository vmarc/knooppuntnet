import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { OsmLinkWayComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-cs-nc-ways-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="wayIds.length > 0" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <span i18n="@@change-set.network-changes.added-ways">Added ways</span>
        <span class="kpn-brackets kpn-thin">{{ wayIds.length }}</span>
        <kpn-icon-investigate />
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-way
          *ngFor="let wayId of wayIds"
          [wayId]="wayId"
          [title]="wayId.toString()"
        />
      </div>
    </div>
  `,
  standalone: true,
  imports: [NgIf, IconInvestigateComponent, NgFor, OsmLinkWayComponent],
})
export class CsNcWaysAddedComponent implements OnInit {
  @Input() networkChangeInfo: NetworkChangeInfo;

  wayIds: number[];

  ngOnInit(): void {
    this.wayIds = this.networkChangeInfo.ways.added;
  }
}
