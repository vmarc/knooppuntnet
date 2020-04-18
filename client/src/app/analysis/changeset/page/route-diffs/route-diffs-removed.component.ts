import {Component, Input} from "@angular/core";
import {RefRouteChangeInfo} from "./ref-route-change-info";
import {RouteDiffsData} from "./route-diffs-data";

@Component({
  selector: "kpn-route-diffs-removed",
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-removed.title">Removed routes</span>
        <span>({{refs.length}})</span>
        <kpn-icon-investigate></kpn-icon-investigate>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref [ref]="ref.ref" [knownElements]="data.knownElements"></kpn-link-route-ref>
          </div>
          <kpn-meta-data [metaData]="ref.routeChangeInfo.before"></kpn-meta-data>
        </div>
      </div>
    </div>
  `
})
export class RouteDiffsRemovedComponent {

  refs: Array<RefRouteChangeInfo>;

  @Input() data: RouteDiffsData;

  ngOnInit(): void {
    this.refs = this.data.refDiffs.removed.map(ref => new RefRouteChangeInfo(ref, this.data.findRouteChangeInfo(ref))).toArray();
  }

}
