import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MetaDataComponent } from '@app/components/shared';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { LinkRouteRefHeaderComponent } from '@app/components/shared/link';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'kpn-route-diffs-removed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span i18n="@@route-diffs-removed.title">Removed routes</span>
        <span class="kpn-brackets kpn-thin">{{ refs.length }}</span>
        <kpn-icon-investigate />
      </div>
      <div class="kpn-level-2-body">
        <div *ngFor="let ref of refs" class="kpn-level-3">
          <div class="kpn-line kpn-level-3-header">
            <kpn-link-route-ref-header [ref]="ref.ref" [knownElements]="data.knownElements" />
          </div>
          <div *ngIf="ref.routeChangeInfo" class="kpn-level-3-body">
            <kpn-meta-data [metaData]="ref.routeChangeInfo.before" />
          </div>
        </div>
      </div>
    </div>
  `,
  standalone: true,
  imports: [IconInvestigateComponent, LinkRouteRefHeaderComponent, MetaDataComponent, NgFor, NgIf],
})
export class RouteDiffsRemovedComponent implements OnInit {
  @Input() data: RouteDiffsData;

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data.refDiffs.removed.map(
      (ref) => new RefRouteChangeInfo(ref, this.data.findRouteChangeInfo(ref))
    );
  }
}
