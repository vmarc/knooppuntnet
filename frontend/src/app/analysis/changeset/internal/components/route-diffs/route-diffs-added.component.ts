import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FactDiffsComponent } from '@app/analysis/components/changes/fact-diffs.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { LinkRouteRefHeaderComponent } from '@app/shared/components/link/link-route-ref-header';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'ui-route-diffs-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span class="kpn-thick" i18n="@@route-diffs-added.title">Added routes</span>
          <span>({{ refs.length }})</span>
          <ui-icon-happy />
        </div>
        <div class="kpn-level-2-body">
          @for (ref of refs; track ref) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <ui-link-route-ref-header [ref]="ref.ref" [knownElements]="data().knownElements" />
              </div>
              @if (ref.routeChangeInfo) {
                <div class="kpn-level-3-body">
                  @if (ref.routeChangeInfo.after) {
                    <div>
                      <div class="kpn-thin">
                        @if (ref.routeChangeInfo.after.changeSetId === data().changeSetId) {
                          @if (ref.routeChangeInfo.after.version === 1) {
                            <ng-container i18n="@@route-diffs-added.new-relation">
                              New relation
                            </ng-container>
                          }
                          @if (ref.routeChangeInfo.after.version > 1) {
                            <ng-container i18n="@@route-diffs-added.updated-relation">
                              Relation updated in this changeset v{{
                                ref.routeChangeInfo.after.version
                              }}
                            </ng-container>
                          }
                        }
                        @if (ref.routeChangeInfo.after.changeSetId !== data().changeSetId) {
                          <ng-container
                            i18n="@@route-diffs-added.existing-relation"
                            class="kpn-label"
                            >Existing relation
                          </ng-container>
                          <ui-meta-data [metaData]="ref.routeChangeInfo.after" />
                        }
                      </div>
                    </div>
                  }
                  <ui-fact-diffs [factDiffs]="ref.routeChangeInfo.diffs.factDiffs" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  imports: [FactDiffsComponent, IconHappyComponent, LinkRouteRefHeaderComponent, MetaDataComponent],
})
export class RouteDiffsAddedComponent implements OnInit {
  readonly data = input.required<RouteDiffsData>();

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data().refDiffs.added.map(
      (ref) => new RefRouteChangeInfo(ref, this.data().findRouteChangeInfo(ref))
    );
  }
}
