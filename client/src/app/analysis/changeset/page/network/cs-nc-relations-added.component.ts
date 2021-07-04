import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { List } from 'immutable';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';

@Component({
  selector: 'kpn-cs-nc-relations-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!relationIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <span i18n="@@change-set.network-changes.added-relations"
          >Added non-route relations</span
        >
        <span class="kpn-brackets kpn-thin">{{ relationIds().size }}</span>
        <kpn-icon-investigate></kpn-icon-investigate>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-relation
          *ngFor="let relationId of relationIds()"
          [relationId]="relationId"
          [title]="relationId.toString()"
        >
        </kpn-osm-link-relation>
      </div>
    </div>
  `,
})
export class CsNcRelationsAddedComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;

  relationIds(): List<number> {
    return this.networkChangeInfo.relations.added;
  }
}
