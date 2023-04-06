import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { InterpretedTags } from '@app/components/shared/tags/interpreted-tags';
import { WayInfo } from '@api/common/diff/way-info';
import { RouteChangeInfo } from '@api/common/route/route-change-info';

@Component({
  selector: 'kpn-route-change-way-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span class="kpn-label" i18n="@@route-change.way-added.title"
          >Added way</span
        >
        <kpn-osm-link-way
          [wayId]="wayInfo.id"
          [title]="wayInfo.id.toString()"
        />
      </div>
      <div class="kpn-level-4-body">
        <div *ngIf="isWayChangedInThisChangeset(wayInfo)" class="kpn-detail">
          <!-- eslint-disable @angular-eslint/template/i18n -->
          <div class="kpn-thin">
            [ v{{ wayInfo.version }}
            <i i18n="@@route-change.way-added.this-changeset">this changeset</i>
            ]
          </div>
          <!-- eslint-enable @angular-eslint/template/i18n -->
        </div>
        <div *ngIf="!isWayChangedInThisChangeset(wayInfo)" class="kpn-detail">
          <div class="kpn-thin">
            <kpn-meta-data [metaData]="wayInfo" />
          </div>
        </div>
        <div class="kpn-detail">
          <kpn-tags-table [tags]="wayTags(wayInfo)" />
        </div>
      </div>
    </div>
  `,
})
export class RouteChangeWayAddedComponent {
  @Input() wayInfo: WayInfo;
  @Input() routeChangeInfo: RouteChangeInfo;

  wayTags(wayInfo: WayInfo): InterpretedTags {
    return InterpretedTags.all(wayInfo.tags);
  }

  isWayChangedInThisChangeset(wayInfo: WayInfo): boolean {
    return wayInfo.changeSetId === this.routeChangeInfo.changeKey.changeSetId;
  }
}
