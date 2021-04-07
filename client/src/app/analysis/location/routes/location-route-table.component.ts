import { ChangeDetectionStrategy } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { ViewChild } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { Input } from '@angular/core';
import { OnChanges } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkType } from '@api/custom/network-type';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';

@Component({
  selector: 'kpn-location-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      (page)="page.emit($event)"
      [pageIndex]="0"
      [length]="routeCount"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    >
    </kpn-paginator>

    <table mat-table matSort [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
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
            [networkType]="networkType"
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
        <td
          mat-cell
          *matCellDef="let route"
          i18n="@@location-routes.table.distance.value"
        >
          {{ route.meters }}m
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-routes.table.last-survey"
        >
          Last survey
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

    <!--    <kpn-paginator-->
    <!--      (page)="page.emit($event)"-->
    <!--      [pageIndex]="0"-->
    <!--      [length]="routeCount">-->
    <!--    </kpn-paginator>-->
  `,
  styles: [
    `
      .mat-column-nr {
        width: 4em;
      }
    `,
  ],
})
export class LocationRouteTableComponent implements OnInit, OnChanges {
  @Input() networkType: NetworkType;
  @Input() timeInfo: TimeInfo;
  @Input() routes: LocationRouteInfo[];
  @Input() routeCount: number;
  @Output() page = new EventEmitter<PageEvent>();

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  dataSource: MatTableDataSource<LocationRouteInfo>;
  displayedColumns$: Observable<Array<string>>;

  constructor(private pageWidthService: PageWidthService) {
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

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
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
