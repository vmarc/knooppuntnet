import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';

@Component({
  selector: 'kpn-cs-nc-relations-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="relationIds.length > 0" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <span i18n="@@change-set.network-changes.updated-relations"
          >Updated non-route relations</span
        >
        <span class="kpn-brackets kpn-thin">{{ relationIds.length }}</span>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-relation
          *ngFor="let relationId of relationIds"
          [relationId]="relationId"
          [title]="relationId.toString()"
        >
        </kpn-osm-link-relation>
      </div>
    </div>
  `,
})
export class CsNcRelationsUpdatedComponent implements OnInit {
  @Input() networkChangeInfo: NetworkChangeInfo;

  relationIds: number[];

  ngOnInit(): void {
    this.relationIds = this.networkChangeInfo.relations.updated;
  }
}
