import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { TimeInfo } from '@api/common/time-info';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit/edit-and-paginator.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { EditService } from '@app/shared/components/edit.service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
import { NzTrDirective } from 'ng-zorro-antd/table';
import { NzTheadComponent } from 'ng-zorro-antd/table';
import { NzThMeasureDirective } from 'ng-zorro-antd/table';
import { NzTbodyComponent } from 'ng-zorro-antd/table';
import { NzTableComponent } from 'ng-zorro-antd/table';
import { NzTableCellDirective } from 'ng-zorro-antd/table';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';
import { LocationRoutesPageService } from '../location-routes-page.service';
import { LocationRouteAnalysisComponent } from './location-route-analysis';

@Component({
  selector: 'kpn-location-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      i18n-editLinkTitle="@@location-routes.edit.title"
      editLinkTitle="Load the routes in this page in JOSM"
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount()"
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
          <th i18n="@@location-routes.table.nr">Nr</th>
          <th i18n="@@location-routes.table.analysis">Analysis</th>
          <th i18n="@@location-routes.table.symbol">Symbol</th>
          <th i18n="@@location-routes.table.route">Route</th>
          <th i18n="@@location-routes.table.distance">Distance</th>
          <th i18n="@@location-routes.table.last-survey">Survey</th>
          <th i18n="@@location-routes.table.last-edit">Last edit</th>
        </tr>
      </thead>

      <tbody>
        @for (route of routeTable.data; track route.id; let i = $index) {
          <tr>
            <td class="column-nr">
              {{ route.rowIndex + 1 }}
            </td>
            <td>
              <kpn-location-route-analysis [route]="route" [routeType]="routeType()" />
            </td>
            <td class="symbol">
              @if (route.symbol) {
                <kpn-symbol [description]="route.symbol" [width]="25" [height]="25" />
              }
            </td>
            <td>
              <kpn-action-button-route [routeType]="routeType()" [relationId]="route.id" />
              <kpn-link-route
                [routeId]="route.id"
                [routeName]="route.name"
                [routeType]="routeType()"
              />
            </td>
            <td>
              <div class="distance">{{ (route.meters | integer) + ' m' }}</div>
            </td>
            <td>
              {{ route.lastSurvey | day }}
            </td>
            <td>
              <kpn-day [timestamp]="route.lastUpdated" />
            </td>
          </tr>
        }
      </tbody>
    </nz-table>

    <kpn-paginator
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount()"
    />
  `,
  styles: `
    .column-nr {
      width: 4em;
    }

    .distance {
      white-space: nowrap;
      text-align: right;
      width: 100%;
    }

    .symbol {
      vertical-align: middle;
    }
  `,
  imports: [
    ActionButtonRouteComponent,
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    IntegerFormatPipe,
    LinkRouteComponent,
    LocationRouteAnalysisComponent,
    PaginatorComponent,
    SymbolComponent,
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
  ],
})
export class LocationRouteTableComponent {
  private readonly service = inject(LocationRoutesPageService);
  private readonly editService = inject(EditService);

  timeInfo = input.required<TimeInfo>();
  routes = input.required<LocationRouteInfo[]>();
  routeCount = input.required<number>();

  readonly pageIndex = this.service.pageIndex;
  readonly pageSize = this.service.pageSize;
  readonly routeType = this.service.routeType;

  onPageSizeChange(pageSize: number) {
    this.service.updatePageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.service.updatePageIndex(pageIndex);
  }

  edit(): void {
    const editParameters: EditParameters = {
      relationIds: this.routes().map((route) => route.id),
      fullRelation: true,
    };
    this.editService.edit(editParameters);
  }
}
