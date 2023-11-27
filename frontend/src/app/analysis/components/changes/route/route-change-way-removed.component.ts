import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { WayInfo } from '@api/common/diff';
import { MetaDataComponent } from '@app/components/shared';
import { OsmLinkWayComponent } from '@app/components/shared/link';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';

@Component({
  selector: 'kpn-route-change-way-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span class="kpn-label" i18n="@@route-change.way-removed.title">Removed way</span>
        <kpn-osm-link-way [wayId]="wayInfo.id" [title]="wayInfo.id.toString()" />
      </div>
      <div class="kpn-level-4-body">
        <div class="kpn-detail">
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
  standalone: true,
  imports: [OsmLinkWayComponent, MetaDataComponent, TagsTableComponent],
})
export class RouteChangeWayRemovedComponent {
  @Input() wayInfo: WayInfo;

  wayTags(wayInfo: WayInfo): InterpretedTags {
    return InterpretedTags.all(wayInfo.tags);
  }
}
