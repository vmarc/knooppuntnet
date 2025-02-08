import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { WayInfo } from '@api/common/diff/way-info';
import { OsmLinkWayComponent } from '@app/shared/components/link/osm-link-way.component';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';

@Component({
  selector: 'kpn-route-change-way-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span class="kpn-label" i18n="@@route-change.way-removed.title">Removed way</span>
        <kpn-osm-link-way [wayId]="wayInfo().id" [title]="wayInfo().id.toString()" />
      </div>
      <div class="kpn-level-4-body">
        <div class="kpn-detail">
          <div class="kpn-thin">
            <kpn-meta-data [metaData]="wayInfo()" />
          </div>
        </div>
        <div class="kpn-detail">
          <kpn-tag-table [tags]="wayTags(wayInfo())" />
        </div>
      </div>
    </div>
  `,
  imports: [OsmLinkWayComponent, MetaDataComponent, TagTableComponent],
})
export class RouteChangeWayRemovedComponent {
  wayInfo = input.required<WayInfo>();

  wayTags(wayInfo: WayInfo): InterpretedTags {
    return InterpretedTags.all(wayInfo.tags);
  }
}
