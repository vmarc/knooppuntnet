import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { TimeInfo } from '@api/common/time-info';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { EditParameters } from '@app/analysis/components/edit/edit-parameters';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit/edit-and-paginator.component';
import { DayComponent } from '@app/shared/components/day/day.component';
import { EditService } from '@app/shared/components/edit.service';
import { DayPipe } from '@app/shared/components/format/day.pipe';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { SymbolComponent } from '@app/symbol/symbol.component';
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
      [pageIndex]="service.pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="service.pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount()"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <table mat-table matSort [dataSource]="routes()">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.nr">Nr</th>
        <td mat-cell *matCellDef="let route">{{ route.rowIndex + 1 }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.analysis">Analysis</th>
        <td mat-cell *matCellDef="let route">
          <kpn-location-route-analysis [route]="route" [routeType]="service.routeType()" />
        </td>
      </ng-container>

      <ng-container matColumnDef="symbol">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.symbol">Symbol</th>
        <td mat-cell *matCellDef="let route" class="symbol">
          @if (route.symbol) {
            <kpn-symbol [description]="route.symbol" [width]="25" [height]="25" />
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.route">Route</th>
        <td mat-cell *matCellDef="let route" class="kpn-align-center action-button-table-cell">
          <kpn-action-button-route [routeType]="service.routeType()" [relationId]="route.id" />
          <kpn-link-route
            [routeId]="route.id"
            [routeName]="route.name"
            [routeType]="service.routeType()"
          />
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.distance">Distance</th>
        <td mat-cell *matCellDef="let route">
          <div class="distance">{{ (route.meters | integer) + ' m' }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.last-survey">Survey</th>
        <td mat-cell *matCellDef="let route">
          {{ route.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.last-edit">
          Last edit
        </th>
        <td mat-cell *matCellDef="let route" class="kpn-separated">
          <kpn-day [timestamp]="route.lastUpdated" />
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns()"></tr>
      <tr mat-row *matRowDef="let route; columns: displayedColumns()"></tr>
    </table>

    <kpn-paginator
      [pageIndex]="service.pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="service.pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount()"
    />
  `,
  styles: `
    .mat-column-nr {
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
    MatSortModule,
    MatTableModule,
    PaginatorComponent,
    SymbolComponent,
  ],
})
export class LocationRouteTableComponent {
  timeInfo = input.required<TimeInfo>();
  routes = input.required<LocationRouteInfo[]>();
  routeCount = input.required<number>();

  private readonly pageWidthService = inject(PageWidthService);
  private readonly editService = inject(EditService);

  protected readonly service = inject(LocationRoutesPageService);

  protected readonly displayedColumns = computed(() => {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'symbol', 'route', 'distance', 'last-survey', 'lastEdit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'route', 'distance', 'last-survey', 'lastEdit'];
    }

    return ['nr', 'analysis', 'route', 'distance'];
  });

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
