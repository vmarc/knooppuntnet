import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { LinkRouteRefHeaderComponent } from '@app/shared/components/link/link-route-ref-header';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'ui-route-diffs-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (refs.length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-line kpn-level-2-header">
          <span i18n="@@route-diffs-removed.title">Removed routes</span>
          <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
          <ui-icon-investigate />
        </div>
        <div class="kpn-level-2-body">
          @for (ref of refs; track ref) {
            <div class="kpn-level-3">
              <div class="kpn-line kpn-level-3-header">
                <ui-link-route-ref-header [ref]="ref.ref" [knownElements]="data().knownElements" />
              </div>
              @if (ref.routeChangeInfo) {
                <div class="kpn-level-3-body">
                  <ui-meta-data [metaData]="ref.routeChangeInfo.before" />
                </div>
              }
            </div>
          }
        </div>
      </div>
    }
  `,
  imports: [IconInvestigateComponent, LinkRouteRefHeaderComponent, MetaDataComponent],
})
export class RouteDiffsRemovedComponent implements OnInit {
  readonly data = input.required<RouteDiffsData>();

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data().refDiffs.removed.map(
      (ref) => new RefRouteChangeInfo(ref, this.data().findRouteChangeInfo(ref))
    );
  }
}
