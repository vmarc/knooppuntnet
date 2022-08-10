import { ChangeDetectionStrategy } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Input } from '@angular/core';
import { OnChanges } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { TimeInfo } from '@api/common/time-info';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { actionSharedEdit } from '../../../core/shared/shared.actions';
import { EditParameters } from '../../components/edit/edit-parameters';
import { actionLocationRoutesPageSize } from '../store/location.actions';
import { actionLocationRoutesPageIndex } from '../store/location.actions';
import { selectLocationNetworkType } from '../store/location.selectors';
import { selectLocationRoutesPageIndex } from '../store/location.selectors';

@Component({
  selector: 'kpn-location-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      title="Load the routes in this page in the editor (like JOSM)"
      i18n-title="@@location-routes.edit.title"
      [pageIndex]="pageIndex$ | async"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize$ | async"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    ></kpn-edit-and-paginator>

    <table mat-table matSort [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let route">{{ route.rowIndex + 1 }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-routes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-location-route-analysis
            [route]="route"
            [networkType]="networkType$ | async"
          ></kpn-location-route-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-routes.table.route"
        >
          Route
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-link-route
            [routeId]="route.id"
            [title]="route.name"
            [networkType]="networkType$ | async"
          ></kpn-link-route>
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-routes.table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let route">
          <div class="distance">{{ (route.meters | integer) + ' m' }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-routes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-routes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let route" class="kpn-separated">
          <kpn-day [timestamp]="route.lastUpdated"></kpn-day>
          <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          <kpn-osm-link-relation
            [relationId]="route.id"
          ></kpn-osm-link-relation>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr
        mat-row
        *matRowDef="let route; columns: displayedColumns$ | async"
      ></tr>
    </table>

    <kpn-paginator
      [pageIndex]="pageIndex$ | async"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize$ | async"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount"
    >
    </kpn-paginator>
  `,
  styles: [
    `
      .mat-column-nr {
        width: 4em;
      }

      .distance {
        white-space: nowrap;
        text-align: right;
        width: 100%;
      }
    `,
  ],
})
export class LocationRouteTableComponent implements OnInit, OnChanges {
  @Input() timeInfo: TimeInfo;
  @Input() routes: LocationRouteInfo[];
  @Input() routeCount: number;

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  readonly pageSize$ = this.store.select(selectPreferencesPageSize);
  readonly pageIndex$ = this.store.select(selectLocationRoutesPageIndex);
  readonly networkType$ = this.store.select(selectLocationNetworkType);

  dataSource: MatTableDataSource<LocationRouteInfo>;
  displayedColumns$: Observable<Array<string>>;

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store<AppState>
  ) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(
      map(() => this.displayedColumns())
    );
  }

  ngOnInit(): void {
    this.dataSource.data = this.routes;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['routes']) {
      this.dataSource.data = this.routes;
    }
  }

  onPageSizeChange(pageSize: number) {
    this.store.dispatch(actionLocationRoutesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.store.dispatch(actionLocationRoutesPageIndex({ pageIndex }));
  }

  edit(): void {
    const editParameters: EditParameters = {
      relationIds: this.routes.map((route) => route.id),
      fullRelation: true,
    };
    this.store.dispatch(actionSharedEdit({ editParameters }));
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'route', 'distance', 'last-survey', 'lastEdit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'route', 'distance', 'last-survey', 'lastEdit'];
    }

    return ['nr', 'analysis', 'route', 'distance'];
  }
}
