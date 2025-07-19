import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MonitorRouteDetail } from '@api/common/monitor/monitor-route-detail';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { TimestampDayPipe } from '@app/shared/components/format/timestamp-day.pipe';
import { TimestampPipe } from '@app/shared/components/format/timestamp-pipe';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzTrDirective } from 'ng-zorro-antd/table';
import { NzTheadComponent } from 'ng-zorro-antd/table';
import { NzThMeasureDirective } from 'ng-zorro-antd/table';
import { NzTbodyComponent } from 'ng-zorro-antd/table';
import { NzTableCellDirective } from 'ng-zorro-antd/table';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'ui-monitor-group-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    <nz-table nzBordered [nzFrontPagination]="false" #routeTable [nzData]="routes()" nzSize="small">
      <thead>
        <tr>
          <th i18n="@@monitor.group.route-table.nr">Nr</th>
          <th colspan="2" i18n="@@monitor.group.route-table.name">Name</th>
          <th i18n="@@monitor.group.route-table.relation">Relation</th>
          <th i18n="@@monitor.group.route-table.symbol">Symbol</th>
          <th i18n="@@monitor.group.route-table.description">Description</th>
          <th colspan="3" i18n="@@monitor.group.route-table.reference">Reference</th>
          <th colspan="2" i18n="@@monitor.group.route-table.deviations">Deviations</th>
          <th i18n="@@monitor.group.route-table.osm-segment-count">Segments</th>
          @if (admin()) {
            <th i18n="@@monitor.group.route-table.actions">Actions</th>
          }
        </tr>
      </thead>
      <tbody>
        @for (route of routeTable.data; track route.rowIndex) {
          <tr (click)="onRouteClicked(route)">
            <td>
              {{ route.rowIndex + 1 }}
            </td>

            <td class="column-name">
              <div class="kpn-line">
                <ui-action-button-relation [relationId]="route.relationId" />
                <a [routerLink]="routeLink(route)" [state]="route">{{ route.name }}</a>
              </div>
            </td>

            <td>
              @if (route.happy) {
                <ui-icon-happy />
              }
            </td>

            <td>
              @if (route.relationId) {
                {{ route.relationId }}
              }
            </td>

            <td class="column-symbol">
              @if (route.symbol) {
                <ui-symbol [description]="route.symbol" [width]="25" [height]="25" />
              }
            </td>

            <td class="column-description">
              {{ route.description }}
            </td>

            <td>
              {{ route.referenceType }}
            </td>

            <td
              nz-tooltip
              nzTooltipPlacement="right"
              [nzTooltipTitle]="route.referenceTimestamp | yyyymmddhhmm"
              class="column-reference-day"
            >
              {{ route.referenceTimestamp | yyyymmdd }}
            </td>

            <td class="column-reference-distance">
              @if (route.referenceType) {
                <span>
                  {{ route.referenceDistance | distance }}
                </span>
              }
            </td>

            <td class="column-deviation-count">
              @if (route.referenceType && route.relationId) {
                <span>
                  {{ route.deviationCount }}
                </span>
              }
            </td>

            <td class="column-deviation-distance">
              @if (route.referenceType && route.deviationCount > 0) {
                <span>
                  {{ route.deviationDistance | distance }}
                </span>
              } @else if (route.referenceType && route.relationId && route.deviationCount === 0) {
                <span>-</span>
              }
            </td>

            <td class="column-osm-segment-count">
              @if (route.relationId) {
                <span>
                  {{ route.osmSegmentCount }}
                </span>
              }
            </td>

            @if (admin()) {
              <td class="kpn-action-cell">
                <a
                  [routerLink]="routeUpdateLink(route)"
                  [state]="route"
                  title="Update"
                  i18n-title="@@action.update"
                  class="kpn-action-button kpn-link"
                >
                  <nz-icon nzType="edit" />
                </a>
                <a
                  [routerLink]="routeDeleteLink(route)"
                  [state]="route"
                  title="delete"
                  i18n-title="@@action.delete"
                  class="kpn-action-button kpn-warning"
                >
                  <nz-icon nzType="delete" />
                </a>
              </td>
            }
          </tr>
        }
      </tbody>
    </nz-table>
  `,
  styles: `
    .column-name {
      white-space: nowrap;
    }

    .column-description {
      min-width: 12em;
    }

    .column-reference-day {
      white-space: nowrap;
    }

    .column-reference-distance {
      text-align: right;
      white-space: nowrap;
    }

    .column-deviation-count {
      text-align: right;
      white-space: nowrap;
    }

    .column-deviation-distance {
      text-align: right;
      white-space: nowrap;
    }

    .column-osm-segment-count {
      text-align: right;
      white-space: nowrap;
    }

    .column-symbol {
      vertical-align: middle;
    }
  `,
  imports: [
    ActionButtonRelationComponent,
    DistancePipe,
    IconHappyComponent,
    NzIconDirective,
    NzTableCellDirective,
    NzTableModule,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzToolTipModule,
    NzTrDirective,
    RouterLink,
    SymbolComponent,
    TimestampDayPipe,
    TimestampPipe,
  ],
})
export class MonitorGroupRouteTableComponent {
  readonly admin = input.required<boolean>();
  readonly groupName = input.required<string>();
  readonly routes = input.required<MonitorRouteDetail[]>();

  readonly selectRoute = output<MonitorRouteDetail>();

  routeLink(route: MonitorRouteDetail): string {
    return this.buildUrl(route, '');
  }

  routeUpdateLink(route: MonitorRouteDetail): string {
    return this.buildUrl(route, '', true);
  }

  routeDeleteLink(route: MonitorRouteDetail): string {
    return this.buildUrl(route, 'delete', true);
  }

  private buildUrl(route: MonitorRouteDetail, action: string, isAdmin = false): string {
    const prefix = isAdmin ? 'admin/' : '';
    const suffix = action.length > 0 ? `/${action}` : '';
    return `/monitor/${prefix}groups/${this.groupName()}/routes/${route.name}${suffix}`;
  }

  onRouteClicked(route: MonitorRouteDetail) {
    this.selectRoute.emit(route);
  }
}
