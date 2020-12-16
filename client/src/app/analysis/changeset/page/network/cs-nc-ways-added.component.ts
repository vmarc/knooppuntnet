import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkChangeInfo} from '@api/common/changes/details/network-change-info';
import {List} from 'immutable';

@Component({
  selector: 'kpn-cs-nc-ways-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!wayIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <span i18n="@@change-set.network-changes.added-ways">Added ways</span>
        <span class="kpn-brackets kpn-thin">{{wayIds().size}}</span>
        <kpn-icon-investigate></kpn-icon-investigate>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-way *ngFor="let wayId of wayIds()" [wayId]="wayId" [title]="wayId.toString()"></kpn-osm-link-way>
      </div>
    </div>
  `
})
export class CsNcWaysAddedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  wayIds(): List<number> {
    return this.networkChangeInfo.ways.added;
  }

}
