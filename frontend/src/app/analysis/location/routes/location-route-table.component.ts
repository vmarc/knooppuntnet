import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Input } from '@angular/core';
import { OnChanges } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
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
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { JosmRelationComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';
import { PaginatorComponent } from '@app/components/shared/paginator';
import { selectPreferencesPageSize } from '@app/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SymbolComponent } from '@app/symbol';
import { actionLocationRoutesPageSize } from '../store/location.actions';
import { actionLocationRoutesPageIndex } from '../store/location.actions';
import { selectLocationNetworkType } from '../store/location.selectors';
import { selectLocationRoutesPageIndex } from '../store/location.selectors';
import { LocationRouteAnalysisComponent } from './location-route-analysis';

@Component({
  selector: 'kpn-location-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      editLinkTitle="Load the routes in this page in the editor (like JOSM)"
      i18n-editLinkTitle="@@location-routes.edit.title"
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount"
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
          <kpn-location-route-analysis [route]="route" [networkType]="networkType()" />
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
        <td mat-cell *matCellDef="let route">
          <kpn-link-route
            [routeId]="route.id"
            [routeName]="route.name"
            [networkType]="networkType()"
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
          <kpn-josm-relation [relationId]="route.id" />
          <kpn-osm-link-relation [relationId]="route.id" />
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr mat-row *matRowDef="let route; columns: displayedColumns$ | async"></tr>
    </table>

    <kpn-paginator
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="routeCount"
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
    AsyncPipe,
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    IntegerFormatPipe,
    JosmRelationComponent,
    LinkRouteComponent,
    LocationRouteAnalysisComponent,
    MatSortModule,
    MatTableModule,
    OsmLinkRelationComponent,
    PaginatorComponent,
    SymbolComponent,
  ],
})
export class LocationRouteTableComponent implements OnInit, OnChanges {
  @Input() timeInfo: TimeInfo;
  @Input() routes: LocationRouteInfo[];
  @Input() routeCount: number;

  @ViewChild(PaginatorComponent, { static: true }) paginator: PaginatorComponent;

  private readonly pageWidthService = inject(PageWidthService);
  private readonly editService = inject(EditService);
  private readonly store = inject(Store);

  protected readonly pageSize = this.store.selectSignal(selectPreferencesPageSize);
  protected readonly pageIndex = this.store.selectSignal(selectLocationRoutesPageIndex);
  protected readonly networkType = this.store.selectSignal(selectLocationNetworkType);

  protected readonly dataSource = new MatTableDataSource<LocationRouteInfo>();
  protected readonly displayedColumns$ = this.pageWidthService.current$.pipe(
    map(() => this.displayedColumns())
  );

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
