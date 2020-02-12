import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Ref} from "../../../kpn/api/common/common/ref";
import {RouteDiffsData} from "./route-diffs-data";

@Component({
  selector: "kpn-route-diffs-added",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-added.title">Added routes</span>
        <span>({{refs().size}})</span>
        <kpn-icon-happy></kpn-icon-happy>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs()" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref [ref]="ref" [knownElements]="data.knownElements"></kpn-link-route-ref>
          </div>
          <div class="kpn-level-3-body">
            <div *ngFor="let routeChangeInfo of data.findRouteChangeInfo(ref)">
              <div *ngIf="routeChangeInfo.after">
                <div class="kpn-thin">
                  <ng-container *ngIf="routeChangeInfo.after.changeSetId == data.changeSetId">
                    <ng-container *ngIf="routeChangeInfo.after.version == 1" i18n="@@route-diffs-added.new-relation">
                      New relation
                    </ng-container>
                    <ng-container *ngIf="routeChangeInfo.after.version > 1" i18n="@@route-diffs-added.updated-relation">
                      Relation updated in this changeset v{{routeChangeInfo.after.version}}
                    </ng-container>
                  </ng-container>
                  <ng-container *ngIf="routeChangeInfo.after.changeSetId != data.changeSetId">
                    <ng-container i18n="@@route-diffs-added.existing-relation">Existing relation</ng-container>
                    <kpn-meta-data [metaData]="routeChangeInfo.after"></kpn-meta-data>
                  </ng-container>
                </div>
              </div>
              <kpn-fact-diffs [factDiffs]="routeChangeInfo.diffs.factDiffs"></kpn-fact-diffs>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class RouteDiffsAddedComponent {

  @Input() data: RouteDiffsData;

  refs(): List<Ref> {
    return this.data.refDiffs.added;
  }
}
