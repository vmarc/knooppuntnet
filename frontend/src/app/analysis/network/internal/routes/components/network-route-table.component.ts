import { viewChild } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { RouteType } from '@api/common/route-type';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit/edit-and-paginator.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { EditService } from '@app/shared/components/edit.service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { NzTrDirective } from 'ng-zorro-antd/table';
import { NzTheadComponent } from 'ng-zorro-antd/table';
import { NzThMeasureDirective } from 'ng-zorro-antd/table';
import { NzTbodyComponent } from 'ng-zorro-antd/table';
import { NzTableCellDirective } from 'ng-zorro-antd/table';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';
import { NetworkRoutesPageService } from '../network-routes-page.service';
import { NetworkRouteAnalysisComponent } from './network-route-analysis.component';

@Component({
  selector: 'ui-network-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-edit-and-paginator
      (edit)="edit()"
      i18n-editLinkTitle="@@network-routes.edit.title"
      editLinkTitle="Load the routes in this page in JOSM"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="totalRouteCount()"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <nz-table
      nzBordered
      #routeTable
      [nzData]="routes()"
      [nzPageSize]="pageSize()"
      nzPaginationPosition="both"
      nzShowSizeChanger="true"
      [nzPageSizeOptions]="[10, 25, 50, 100, 250, 500, 1000]"
      nzSize="small"
    >
      <thead>
        <tr>
          <th i18n="@@network-routes.table.nr">Nr</th>
          <th i18n="@@network-routes.table.analysis">Analysis</th>
          <th i18n="@@network-routes.table.symbol">Symbol</th>
          <th i18n="@@network-routes.table.node">Route</th>
          <th i18n="@@network-routes.table.distance">Distance</th>
          <th i18n="@@network-routes.table.role">Role</th>
          <th i18n="@@network-routes.table.last-survey">Survey</th>
          <th i18n="@@network-routes.table.last-edit">Last edit</th>
        </tr>
      </thead>

      <tbody>
        @for (route of routeTable.data; track route.id; let i = $index) {
          <tr>
            <td class="nr-column">
              {{ rowNumber(i) }}
            </td>
            <td>
              <ui-network-route-analysis [route]="route" [routeType]="routeType()" />
            </td>
            <td class="symbol">
              @if (route.symbol) {
                <ui-symbol [description]="route.symbol" [width]="25" [height]="25" />
              }
            </td>
            <td class="kpn-align-center route-column">
              <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
              <ui-link-route
                [routeId]="route.id"
                [routeName]="route.name"
                [routeType]="routeType()"
              />
            </td>
            <td>
              <div class="distance">{{ (route.length | integer) + ' m' }}</div>
            </td>
            <td>
              {{ route.role ? route.role : '-' }}
            </td>
            <td>
              {{ route.lastSurvey | day }}
            </td>
            <td class="kpn-separated">
              <ui-day [timestamp]="route.lastUpdated" />
            </td>
          </tr>
        }
      </tbody>
    </nz-table>
  `,
  styles: `
    .nr-column {
      width: 3rem;
    }

    .symbol {
      vertical-align: middle;
    }

    .distance {
      white-space: nowrap;
      text-align: right;
      width: 100%;
    }

    .route-column {
      padding-left: 0 !important;
      padding-right: 1rem !important;
    }
  `,
  imports: [
    ActionButtonRouteComponent,
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    IntegerFormatPipe,
    LinkRouteComponent,
    NetworkRouteAnalysisComponent,
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
    SymbolComponent,
  ],
})
export class NetworkRouteTableComponent {
  timeInfo = input.required<TimeInfo>();
  surveyDateInfo = input.required<SurveyDateInfo>();
  routeType = input.required<RouteType>();
  routes = input.required<NetworkRouteRow[]>();

  private readonly editAndPaginator = viewChild(EditAndPaginatorComponent);

  private readonly editService = inject(EditService);
  private readonly service = inject(NetworkRoutesPageService);

  readonly pageSize = this.service.pageSize;
  readonly totalRouteCount = this.service.totalRouteCount;

  rowNumber(index: number): number {
    return this.editAndPaginator().paginator().rowNumber(index);
  }

  onPageSizeChange(pageSize: number): void {
    this.service.updatePageSize(pageSize);
  }

  edit(): void {
    const relationIds = this.routes().map((route) => route.id);
    this.editService.edit({
      relationIds,
      fullRelation: true,
    });
  }
}
