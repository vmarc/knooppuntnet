import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteChangeDetailComponent } from '@app/analysis/components/changes/route';
import { LinkRouteRefHeaderComponent } from '@app/components/shared/link';
import { VersionChangeComponent } from '../version-change.component';
import { RefRouteChangeInfo } from './ref-route-change-info';
import { RouteDiffsData } from './route-diffs-data';

@Component({
  selector: 'kpn-route-diffs-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="refs.length > 0" class="kpn-level-2">
      <div class="kpn-line kpn-level-2-header">
        <span class="kpn-thick" i18n="@@route-diffs-updated.title"
          >Updated routes</span
        >
        <span>({{ refs.length }})</span>
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
            <kpn-version-change
              [before]="ref.routeChangeInfo.before"
              [after]="ref.routeChangeInfo.after"
            />
            <kpn-route-change-detail [routeChangeInfo]="ref.routeChangeInfo" />
          </div>
        </div>
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    LinkRouteRefHeaderComponent,
    VersionChangeComponent,
    RouteChangeDetailComponent,
  ],
})
export class RouteDiffsUpdatedComponent implements OnInit {
  @Input() data: RouteDiffsData;

  refs: Array<RefRouteChangeInfo>;

  ngOnInit(): void {
    this.refs = this.data.refDiffs.updated.map(
      (ref) => new RefRouteChangeInfo(ref, this.data.findRouteChangeInfo(ref))
    );
  }
}
