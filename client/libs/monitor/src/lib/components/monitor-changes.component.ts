import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteChangeSummary } from '@api/common/monitor';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { MonitorChangeHeaderComponent } from './monitor-change-header.component';

@Component({
  selector: 'kpn-monitor-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <kpn-items>
      <kpn-item
        *ngFor="let change of changes; let i = index"
        [index]="rowIndex(i)"
      >
        <div class="change-set">
          <kpn-monitor-change-header [changeSet]="change" />

          <div>
            <p *ngIf="change.groupDescription">
              <span class="kpn-label">Group</span>
              <a [routerLink]="'/monitor/groups/' + change.groupName">{{
                change.groupDescription
              }}</a>
            </p>
            <p *ngIf="change.routeName">
              <span class="kpn-label">Route</span>
              <a
                [routerLink]="
                  '/monitor/groups/' +
                  change.groupName +
                  '/routes/' +
                  change.key.elementId
                "
                >{{ change.routeName }}</a
              >
            </p>
            <!--            <p>-->
            <!--              Reference: {{change.gpxFilename}}-->
            <!--            </p>-->
            <table>
              <!--              <tr>-->
              <!--                <td>GPX</td>-->
              <!--                <td>{{change.gpxDistance}}km</td>-->
              <!--              </tr>-->
              <tr>
                <td>OSM</td>
                <td>{{ change.osmDistance }}km</td>
              </tr>
            </table>
            <p>
              ways={{ change.wayCount }}, added={{ change.waysAdded }},
              removed={{ change.waysRemoved }}, updated={{ change.waysUpdated }}
            </p>
            <p *ngIf="change.routeSegmentCount !== 1">
              Not OK: {{ change.routeSegmentCount }} route segments
            </p>
            <p *ngIf="change.routeSegmentCount === 1">OK: 1 route segment</p>
            <p *ngIf="change.resolvedNokSegmentCount > 0" class="kpn-line">
              <span
                >Resolved deviations: {{ change.resolvedNokSegmentCount }}</span
              >
              <kpn-icon-happy />
            </p>
            <p *ngIf="change.newNokSegmentCount > 0" class="kpn-line">
              <span>New deviations: {{ change.newNokSegmentCount }}</span>
              <kpn-icon-investigate />
            </p>
          </div>
        </div>
      </kpn-item>
    </kpn-items>
  `,
  styles: [
    `
      .change-set {
        margin-top: 5px;
        margin-bottom: 5px;
      }
    `,
  ],
  standalone: true,
  imports: [
    IconHappyComponent,
    IconInvestigateComponent,
    ItemComponent,
    ItemsComponent,
    MonitorChangeHeaderComponent,
    NgFor,
    NgIf,
    RouterLink,
  ],
})
export class MonitorChangesComponent {
  @Input() pageSize: number;
  @Input() pageIndex: number;
  @Input() changes: MonitorRouteChangeSummary[];

  rowIndex(index: number): number {
    return this.pageSize * this.pageIndex + index;
  }
}
