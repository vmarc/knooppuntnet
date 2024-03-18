import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ViewChild } from '@angular/core';
import { input } from '@angular/core';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkRouteRow } from '@api/common/network';
import { NetworkType } from '@api/custom';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { DayComponent } from '@app/components/shared/day';
import { DayPipe } from '@app/components/shared/format';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { LinkRouteComponent } from '@app/components/shared/link';
import { FilterOptions } from '@app/kpn/filter';
import { SymbolComponent } from '@app/symbol';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { delay } from 'rxjs/operators';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';
import { NetworkRoutesStore } from '../network-routes.store';
import { NetworkRouteAnalysisComponent } from './network-route-analysis.component';
import { NetworkRouteFilter } from './network-route-filter';
import { NetworkRouteFilterCriteria } from './network-route-filter-criteria';
import { NetworkRoutesService } from './network-routes.service';

@Component({
  selector: 'kpn-network-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      i18n-editLinkTitle="@@network-routes.edit.title"
      editLinkTitle="Load the routes in this page in JOSM"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routes()?.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <table mat-table matSort [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let route; let i = index">
          {{ rowNumber(i) }}
        </td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-routes.table.analysis">Analysis</th>
        <td mat-cell *matCellDef="let route">
          <kpn-network-route-analysis [route]="route" [networkType]="networkType()" />
        </td>
      </ng-container>

      <ng-container matColumnDef="symbol">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.symbol">
          Symbol
        </th>
        <td mat-cell *matCellDef="let route" class="symbol">
          @if (route.symbol) {
            <kpn-symbol [description]="route.symbol" [width]="25" [height]="25" />
          }
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.node">
          Route
        </th>
        <td mat-cell *matCellDef="let route" class="kpn-align-center route-column">
          <kpn-action-button-route [relationId]="route.id" />
          <kpn-link-route
            [routeId]="route.id"
            [routeName]="route.name"
            [networkType]="route.networkType"
          />
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-routes.table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let route">
          <div class="distance">{{ (route.length | integer) + ' m' }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="role">
        <th mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@network-routes.table.role">
          Role
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.role ? route.role : '-' }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-routes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-routes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let route" class="kpn-separated">
          <kpn-day [timestamp]="route.lastUpdated" />
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr mat-row *matRowDef="let route; columns: displayedColumns$ | async"></tr>
    </table>
  `,
  styles: `
    .mat-column-nr {
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
  standalone: true,
  imports: [
    AsyncPipe,
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    IntegerFormatPipe,
    LinkRouteComponent,
    MatSortModule,
    MatTableModule,
    NetworkRouteAnalysisComponent,
    SymbolComponent,
    ActionButtonRouteComponent,
  ],
})
export class NetworkRouteTableComponent implements OnInit, OnDestroy {
  timeInfo = input.required<TimeInfo>();
  surveyDateInfo = input.required<SurveyDateInfo>();
  networkType = input.required<NetworkType>();
  routes = input.required<NetworkRouteRow[]>();

  @ViewChild(EditAndPaginatorComponent, { static: true }) paginator: EditAndPaginatorComponent;

  private readonly pageWidthService = inject(PageWidthService);
  private readonly networkRoutesService = inject(NetworkRoutesService);
  private readonly editService = inject(EditService);
  private readonly store = inject(NetworkRoutesStore);

  protected readonly pageSize = this.store.pageSize();
  protected readonly dataSource = new MatTableDataSource<NetworkRouteRow>();
  protected readonly displayedColumns$ = this.pageWidthService.current$.pipe(
    map(() => this.displayedColumns())
  );

  private readonly filterCriteria$ = new BehaviorSubject<NetworkRouteFilterCriteria>(
    new NetworkRouteFilterCriteria()
  );

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator.paginator.matPaginator;
    this.filterCriteria$
      .pipe(
        map(
          (criteria) =>
            new NetworkRouteFilter(
              this.timeInfo(),
              this.surveyDateInfo(),
              criteria,
              this.filterCriteria$
            )
        ),
        tap((filter) => (this.dataSource.data = filter.filter(this.routes()))),
        delay(0)
      )
      .subscribe((filter) => {
        this.networkRoutesService.setFilterOptions(filter.filterOptions(this.routes()));
      });
  }

  ngOnDestroy() {
    this.networkRoutesService.setFilterOptions(FilterOptions.empty());
  }

  rowNumber(index: number): number {
    return this.paginator.paginator.rowNumber(index);
  }

  onPageSizeChange(pageSize: number) {
    this.store.updatePageSize(pageSize);
  }

  edit(): void {
    const relationIds = Util.currentPageItems(this.dataSource).map((route) => route.id);
    this.editService.edit({
      relationIds,
      fullRelation: true,
    });
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'symbol', 'route', 'distance', 'role', 'last-survey', 'last-edit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'route', 'distance', 'role'];
    }

    return ['nr', 'analysis', 'route'];
  }
}
