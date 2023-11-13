import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { FactDiffsComponent } from '@app/analysis/components/changes';
import { MetaDataComponent } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { LinkRouteRefHeaderComponent } from '@app/components/shared/link';
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
        <kpn-icon-happy />
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref-header
              [ref]="ref.ref"
              [knownElements]="data.knownElements"
            />
          </div>
          <div *ngIf="ref.routeChangeInfo" class="kpn-level-3-body">
            <div *ngIf="ref.routeChangeInfo.after">
              <div class="kpn-thin">
                <ng-container
                  *ngIf="
                    ref.routeChangeInfo.after.changeSetId === data.changeSetId
                  "
                >
                  <ng-container
                    *ngIf="ref.routeChangeInfo.after.version === 1"
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
                    ref.routeChangeInfo.after.changeSetId !== data.changeSetId
                  "
                >
                  <ng-container
                    i18n="@@route-diffs-added.existing-relation"
                    class="kpn-label"
                    >Existing relation
                  </ng-container>
                  <kpn-meta-data [metaData]="ref.routeChangeInfo.after" />
                </ng-container>
              </div>
            </div>
            <kpn-fact-diffs [factDiffs]="ref.routeChangeInfo.diffs.factDiffs" />
          </div>
        </div>
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    FactDiffsComponent,
    IconHappyComponent,
    LinkRouteRefHeaderComponent,
    MetaDataComponent,
    NgFor,
    NgIf,
  ],
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
