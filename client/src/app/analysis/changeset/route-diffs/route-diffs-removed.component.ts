import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../kpn/shared/common/ref";
import {RouteDiffsData} from "./route-diffs-data";

@Component({
  selector: "kpn-route-diffs-removed",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-removed.title">Removed routes</span>
        <span>({{refs().size}})</span>
        <kpn-icon-investigate></kpn-icon-investigate>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs()" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref [ref]="ref" [knownElements]="data.knownElements"></kpn-link-route-ref>
          </div>
          <div *ngFor="let nodeChangeInfo of data.findRouteChangeInfo(ref)" class="kpn-level-3-body">
            <kpn-meta-data [metaData]="nodeChangeInfo.before"></kpn-meta-data>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RouteDiffsRemovedComponent {

  @Input() data: RouteDiffsData;

  refs(): List<Ref> {
    return this.data.refDiffs.removed;
  }

}
