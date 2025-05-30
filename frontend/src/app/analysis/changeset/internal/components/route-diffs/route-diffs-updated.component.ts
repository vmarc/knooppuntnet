import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteChangeDetailComponent } from '@app/analysis/components/changes/route/route-change-detail.component';
import { LinkRouteRefHeaderComponent } from '@app/shared/components/link/link-route-ref-header';
import { VersionChangeComponent } from '../version-change.component';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'ui-route-diffs-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span class="kpn-thick" i18n="@@route-diffs-updated.title">Updated routes</span>
          <span>({{ refs.length }})</span>
        </div>
        <div class="kpn-level-2-body">
          @for (ref of refs; track ref) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <ui-link-route-ref-header [ref]="ref.ref" [knownElements]="data().knownElements" />
              </div>
              @if (ref.routeChangeInfo) {
                <div class="kpn-level-3-body">
                  <ui-version-change
                    [before]="ref.routeChangeInfo.before"
                    [after]="ref.routeChangeInfo.after"
                  />
                  <ui-route-change-detail [routeChangeInfo]="ref.routeChangeInfo" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  imports: [LinkRouteRefHeaderComponent, RouteChangeDetailComponent, VersionChangeComponent],
})
export class RouteDiffsUpdatedComponent implements OnInit {
  data = input.required<RouteDiffsData>();

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data().refDiffs.updated.map(
      (ref) => new RefRouteChangeInfo(ref, this.data().findRouteChangeInfo(ref))
    );
  }
}
