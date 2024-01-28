import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span class="kpn-thick" i18n="@@route-diffs-added.title">Added routes</span>
          <span>({{ refs.length }})</span>
          <kpn-icon-happy />
        </div>
        <div class="kpn-level-2-body">
          @for (ref of refs; track ref) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <kpn-link-route-ref-header [ref]="ref.ref" [knownElements]="data().knownElements" />
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
                          <kpn-meta-data [metaData]="ref.routeChangeInfo.after" />
                        }
                      </div>
                    </div>
                  }
                  <kpn-fact-diffs [factDiffs]="ref.routeChangeInfo.diffs.factDiffs" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [FactDiffsComponent, IconHappyComponent, LinkRouteRefHeaderComponent, MetaDataComponent],
})
export class RouteDiffsAddedComponent implements OnInit {
  data = input<RouteDiffsData | undefined>();

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data().refDiffs.added.map(
      (ref) => new RefRouteChangeInfo(ref, this.data().findRouteChangeInfo(ref))
    );
  }
}
