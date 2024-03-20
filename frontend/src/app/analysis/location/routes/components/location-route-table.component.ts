import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { ViewChild } from '@angular/core';
import { OnChanges } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { TimeInfo } from '@api/common';
import { LocationRouteInfo } from '@api/common/location';
import { EditParameters } from '@app/analysis/components/edit';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { DayComponent } from '@app/components/shared/day';
import { DayPipe } from '@app/components/shared/format';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { LinkRouteComponent } from '@app/components/shared/link';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { SymbolComponent } from '@app/symbol';
import { map } from 'rxjs/operators';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';
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

    <table mat-table matSort [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.nr">Nr</th>
        <td mat-cell *matCellDef="let route">{{ route.rowIndex + 1 }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.analysis">Analysis</th>
        <td mat-cell *matCellDef="let route">
          <kpn-location-route-analysis [route]="route" [networkType]="service.networkType()" />
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
          <kpn-action-button-route [relationId]="route.id" />
          <kpn-link-route
            [routeId]="route.id"
            [routeName]="route.name"
            [networkType]="service.networkType()"
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

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr mat-row *matRowDef="let route; columns: displayedColumns$ | async"></tr>
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
  standalone: true,
  imports: [
    ActionButtonRouteComponent,
    AsyncPipe,
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
export class LocationRouteTableComponent implements OnInit, OnChanges {
  timeInfo = input.required<TimeInfo>();
  routes = input.required<LocationRouteInfo[]>();
  routeCount = input.required<number>();

  @ViewChild(PaginatorComponent, { static: true }) paginator: PaginatorComponent;

  private readonly pageWidthService = inject(PageWidthService);
  private readonly editService = inject(EditService);

  protected readonly service = inject(LocationRoutesPageService);

  protected readonly dataSource = new MatTableDataSource<LocationRouteInfo>();
  protected readonly displayedColumns$ = this.pageWidthService.current$.pipe(
    map(() => this.displayedColumns())
  );

  ngOnInit(): void {
    this.dataSource.data = this.routes();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['routes']) {
      this.dataSource.data = this.routes();
    }
  }

  onPageSizeChange(pageSize: number) {
    this.service.setPageSize(pageSize);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.service.setPageIndex(pageIndex);
  }

  edit(): void {
    const editParameters: EditParameters = {
      relationIds: this.routes().map((route) => route.id),
      fullRelation: true,
    };
    this.editService.edit(editParameters);
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'symbol', 'route', 'distance', 'last-survey', 'lastEdit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'route', 'distance', 'last-survey', 'lastEdit'];
    }

    return ['nr', 'analysis', 'route', 'distance'];
  }
}
