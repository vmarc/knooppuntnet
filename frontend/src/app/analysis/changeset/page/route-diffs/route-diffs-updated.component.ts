import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteChangeDetailComponent } from '@app/analysis/components/changes/route';
import { LinkRouteRefHeaderComponent } from '@app/components/shared/link';
import { VersionChangeComponent } from '../version-change.component';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'kpn-route-diffs-updated',
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
                <kpn-link-route-ref-header [ref]="ref.ref" [knownElements]="data().knownElements" />
              </div>
              @if (ref.routeChangeInfo) {
                <div class="kpn-level-3-body">
                  <kpn-version-change
                    [before]="ref.routeChangeInfo.before"
                    [after]="ref.routeChangeInfo.after"
                  />
                  <kpn-route-change-detail [routeChangeInfo]="ref.routeChangeInfo" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [LinkRouteRefHeaderComponent, RouteChangeDetailComponent, VersionChangeComponent],
})
export class RouteDiffsUpdatedComponent implements OnInit {
  data = input<RouteDiffsData | undefined>();

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data().refDiffs.updated.map(
      (ref) => new RefRouteChangeInfo(ref, this.data().findRouteChangeInfo(ref))
    );
  }
}
