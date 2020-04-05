import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../../kpn/api/common/common/ref";
import {RouteDiffsData} from "./route-diffs-data";

@Component({
  selector: "kpn-route-diffs-updated",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-updated.title">Updated routes</span>
        <span>({{refs().size}})</span>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs()" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref [ref]="ref" [knownElements]="data.knownElements"></kpn-link-route-ref>
          </div>
          <div class="kpn-level-3-body">
            <div *ngFor="let routeChangeInfo of data.findRouteChangeInfo(ref)">
              <kpn-version-change [before]="routeChangeInfo.before" [after]="routeChangeInfo.after"></kpn-version-change>
              <kpn-route-change-detail [routeChangeInfo]="routeChangeInfo"></kpn-route-change-detail>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RouteDiffsUpdatedComponent {

  @Input() data: RouteDiffsData;

  refs(): List<Ref> {
    return this.data.refDiffs.updated;
  }

}
