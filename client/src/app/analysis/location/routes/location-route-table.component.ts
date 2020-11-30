import {ChangeDetectionStrategy} from '@angular/core';
import {SimpleChanges} from '@angular/core';
import {ViewChild} from '@angular/core';
import {EventEmitter} from '@angular/core';
import {Output} from '@angular/core';
import {Input} from '@angular/core';
import {OnChanges} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {List} from 'immutable';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../../components/shared/page-width.service';
import {PaginatorComponent} from '../../../components/shared/paginator/paginator.component';
import {LocationRouteInfo} from '../../../kpn/api/common/location/location-route-info';
import {TimeInfo} from '../../../kpn/api/common/time-info';
import {BrowserStorageService} from '../../../services/browser-storage.service';

@Component({
  selector: 'kpn-location-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      (page)="page.emit($event)"
      [pageIndex]="0"
      [pageSize]="itemsPerPage"
      [pageSizeOptions]="[5, 10, 20, 50, 1000]"
      [length]="routeCount"
      [showFirstLastButtons]="true">
    </kpn-paginator>

    <mat-divider></mat-divider>

    <mat-table matSort [dataSource]="dataSource">

      <ng-container matColumnDef="nr">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-routes.table.nr">Nr</mat-header-cell>
        <mat-cell *matCellDef="let i=index">{{rowNumber(i)}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="route">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-routes.table.route">Route</mat-header-cell>
        <mat-cell *matCellDef="let route">
          <kpn-link-route [routeId]="route.id" [title]="route.name"></kpn-link-route>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="distance">
        <mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.distance">Distance</mat-header-cell>
        <mat-cell *matCellDef="let route" i18n="@@location-routes.table.distance.value">
          {{route.meters}}m
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="broken">
        <mat-header-cell *matHeaderCellDef i18n="@@location-routes.table.broken">Broken</mat-header-cell>
        <mat-cell *matCellDef="let route">
          {{route.broken}}
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-routes.table.last-edit">Last edit</mat-header-cell>
        <mat-cell *matCellDef="let route" class="kpn-line">
          <kpn-day [timestamp]="route.lastUpdated"></kpn-day>
          <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
        </mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns$ | async"></mat-header-row>
      <mat-row *matRowDef="let route; columns: displayedColumns$ | async;"></mat-row>
    </mat-table>
  `
})
export class LocationRouteTableComponent implements OnInit, OnChanges {

  @Input() timeInfo: TimeInfo;
  @Input() routes: List<LocationRouteInfo> = List();
  @Input() routeCount: number;
  @Output() page = new EventEmitter<PageEvent>();

  @ViewChild(PaginatorComponent, {static: true}) paginator: PaginatorComponent;

  itemsPerPage: number;
  dataSource: MatTableDataSource<LocationRouteInfo>;
  displayedColumns$: Observable<Array<string>>;

  constructor(private pageWidthService: PageWidthService,
              private browserStorageService: BrowserStorageService) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
  }

  ngOnInit(): void {
    this.itemsPerPage = this.browserStorageService.itemsPerPage;
    this.dataSource.data = this.routes.toArray();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['routes']) {
      this.dataSource.data = this.routes.toArray();
    }
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }

  private displayedColumns() {

    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'route', 'distance', 'broken', 'lastEdit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'route', 'distance', 'broken'];
    }

    return ['nr', 'route', 'distance'];
  }
}
