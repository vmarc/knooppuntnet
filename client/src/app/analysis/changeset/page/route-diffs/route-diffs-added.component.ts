import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'kpn-route-diffs-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-added.title"
          >Added routes</span
        >
        <span>({{ refs.length }})</span>
        <kpn-icon-happy></kpn-icon-happy>
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref-header
              [ref]="ref.ref"
              [knownElements]="data.knownElements"
            ></kpn-link-route-ref-header>
          </div>
          <div *ngIf="ref.routeChangeInfo" class="kpn-level-3-body">
            <div *ngIf="ref.routeChangeInfo.after">
              <div class="kpn-thin">
                <ng-container
                  *ngIf="
                    ref.routeChangeInfo.after.changeSetId == data.changeSetId
                  "
                >
                  <ng-container
                    *ngIf="ref.routeChangeInfo.after.version == 1"
                    i18n="@@route-diffs-added.new-relation"
                  >
                    New relation
                  </ng-container>
                  <ng-container
                    *ngIf="ref.routeChangeInfo.after.version > 1"
                    i18n="@@route-diffs-added.updated-relation"
                  >
                    Relation updated in this changeset v{{
                      ref.routeChangeInfo.after.version
                    }}
                  </ng-container>
                </ng-container>
                <ng-container
                  *ngIf="
                    ref.routeChangeInfo.after.changeSetId != data.changeSetId
                  "
                >
                  <ng-container
                    i18n="@@route-diffs-added.existing-relation"
                    class="kpn-label"
                    >Existing relation</ng-container
                  >
                  <kpn-meta-data
                    [metaData]="ref.routeChangeInfo.after"
                  ></kpn-meta-data>
                </ng-container>
              </div>
            </div>
            <kpn-fact-diffs
              [factDiffs]="ref.routeChangeInfo.diffs.factDiffs"
            ></kpn-fact-diffs>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class RouteDiffsAddedComponent implements OnInit {
  @Input() data: RouteDiffsData;

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data.refDiffs.added.map(
      (ref) => new RefRouteChangeInfo(ref, this.data.findRouteChangeInfo(ref))
    );
  }
}
