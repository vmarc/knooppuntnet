import {ChangeDetectionStrategy} from "@angular/core";
import {OnInit} from "@angular/core";
import {Component, Input} from "@angular/core";
import {RefRouteChangeInfo} from "./ref-route-change-info";
import {RouteDiffsData} from "./route-diffs-data";

@Component({
  selector: "kpn-route-diffs-updated",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-updated.title">Updated routes</span>
        <span>({{refs.length}})</span>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref [ref]="ref.ref" [knownElements]="data.knownElements"></kpn-link-route-ref>
          </div>
          <div class="kpn-level-3-body">
            <kpn-version-change [before]="ref.routeChangeInfo.before" [after]="ref.routeChangeInfo.after"></kpn-version-change>
            <kpn-route-change-detail [routeChangeInfo]="ref.routeChangeInfo"></kpn-route-change-detail>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RouteDiffsUpdatedComponent implements OnInit {

  refs: Array<RefRouteChangeInfo>;

  @Input() data: RouteDiffsData;

  ngOnInit(): void {
    this.refs = this.data.refDiffs.updated.map(ref => new RefRouteChangeInfo(ref, this.data.findRouteChangeInfo(ref))).toArray();
  }

}
