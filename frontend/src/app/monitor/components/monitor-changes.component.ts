import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
      @for (change of changes(); track $index) {
        <kpn-item [index]="rowIndex($index)">
          <div class="change-set">
            <kpn-monitor-change-header [changeSet]="change" />

            <div>
              @if (change.groupDescription) {
                <p>
                  <span class="kpn-label">Group</span>
                  <a [routerLink]="'/monitor/groups/' + change.groupName">{{
                    change.groupDescription
                  }}</a>
                </p>
              }

              @if (change.routeName) {
                <p>
                  <span class="kpn-label">Route</span>
                  <a
                    [routerLink]="
                      '/monitor/groups/' + change.groupName + '/routes/' + change.key.elementId
                    "
                    >{{ change.routeName }}</a
                  >
                </p>
              }
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
                ways={{ change.wayCount }}, added={{ change.waysAdded }}, removed={{
                  change.waysRemoved
                }}, updated={{ change.waysUpdated }}
              </p>

              @if (change.routeSegmentCount !== 1) {
                <p>Not OK: {{ change.routeSegmentCount }} route segments</p>
              }

              @if (change.routeSegmentCount === 1) {
                <p>OK: 1 route segment</p>
              }

              @if (change.resolvedNokSegmentCount > 0) {
                <p class="kpn-line">
                  <span>Resolved deviations: {{ change.resolvedNokSegmentCount }}</span>
                  <kpn-icon-happy />
                </p>
              }

              @if (change.newNokSegmentCount > 0) {
                <p class="kpn-line">
                  <span>New deviations: {{ change.newNokSegmentCount }}</span>
                  <kpn-icon-investigate />
                </p>
              }
            </div>
          </div>
        </kpn-item>
      }
    </kpn-items>
  `,
  styles: `
    .change-set {
      margin-top: 5px;
      margin-bottom: 5px;
    }
  `,
  standalone: true,
  imports: [
    IconHappyComponent,
    IconInvestigateComponent,
    ItemComponent,
    ItemsComponent,
    MonitorChangeHeaderComponent,
    RouterLink,
  ],
})
export class MonitorChangesComponent {
  pageSize = input.required<number>();
  pageIndex = input.required<number>();
  changes = input.required<MonitorRouteChangeSummary[]>();

  rowIndex(index: number): number {
    return this.pageSize() * this.pageIndex() + index;
  }
}
