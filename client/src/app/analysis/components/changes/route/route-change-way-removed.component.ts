import {Component, Input} from "@angular/core";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {WayInfo} from "../../../../kpn/api/common/diff/way-info";

@Component({
  selector: "kpn-route-change-way-removed",
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span i18n="@@route-change.way-removed.title">Removed way</span>
        <kpn-osm-link-way [wayId]="wayInfo.id" [title]="wayInfo.id.toString()"></kpn-osm-link-way>
      </div>
      <div class="kpn-level-4-body">
        <div class="kpn-detail">
          <div class="kpn-thin">
            <kpn-meta-data [metaData]="wayInfo"></kpn-meta-data>
          </div>
        </div>
        <div class="kpn-detail">
          <kpn-tags-table [tags]="wayTags(wayInfo)"></kpn-tags-table>
        </div>
      </div>
    </div>
  `
})
export class RouteChangeWayRemovedComponent {

  @Input() wayInfo: WayInfo;

  wayTags(wayInfo: WayInfo): InterpretedTags {
    return InterpretedTags.all(wayInfo.tags);
  }

}
