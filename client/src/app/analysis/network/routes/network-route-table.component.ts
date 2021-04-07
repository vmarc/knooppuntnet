import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkType } from '@api/custom/network-type';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { delay } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';
import { FilterOptions } from '../../../kpn/filter/filter-options';
import { NetworkRouteFilter } from './network-route-filter';
import { NetworkRouteFilterCriteria } from './network-route-filter-criteria';
import { NetworkRoutesService } from './network-routes.service';

@Component({
  selector: 'kpn-network-route-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      [length]="routes?.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    >
    </kpn-paginator>

    <table mat-table matSort [dataSource]="dataSource" class="kpn-spacer-above">
      <ng-container matColumnDef="nr">
        <th
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-routes.table.nr"
        >
          Nr
        </th>
        <td mat-cell *matCellDef="let route; let i = index">
          {{ rowNumber(i) }}
        </td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-routes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-network-route-analysis
            [route]="route"
            [networkType]="networkType"
          ></kpn-network-route-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <th
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-routes.table.node"
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
          mat-sort-header
          i18n="@@network-routes.table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.length + 'm' }}
        </td>
      </ng-container>

      <ng-container matColumnDef="role">
        <th
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-routes.table.role"
        >
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
          Last survey
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
    <!--      [length]="routes?.size">-->
    <!--    </kpn-paginator>-->
  `,
  styles: [
    `
      .mat-column-nr {
        width: 3rem;
      }
    `,
  ],
})
export class NetworkRouteTableComponent implements OnInit, OnDestroy {
  @Input() timeInfo: TimeInfo;
  @Input() surveyDateInfo: SurveyDateInfo;
  @Input() networkType: NetworkType;
  @Input() routes: NetworkRouteRow[];

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  dataSource: MatTableDataSource<NetworkRouteRow>;
  displayedColumns$: Observable<Array<string>>;

  private readonly filterCriteria$: BehaviorSubject<NetworkRouteFilterCriteria> = new BehaviorSubject(
    new NetworkRouteFilterCriteria()
  );

  constructor(
    private pageWidthService: PageWidthService,
    private networkRoutesService: NetworkRoutesService
  ) {
    this.displayedColumns$ = pageWidthService.current$.pipe(
      map(() => this.displayedColumns())
    );
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator.matPaginator;
    this.filterCriteria$
      .pipe(
        map(
          (criteria) =>
            new NetworkRouteFilter(
              this.timeInfo,
              this.surveyDateInfo,
              criteria,
              this.filterCriteria$
            )
        ),
        tap((filter) => (this.dataSource.data = filter.filter(this.routes))),
        delay(0)
      )
      .subscribe((filter) => {
        this.networkRoutesService.setFilterOptions(
          filter.filterOptions(this.routes)
        );
      });
  }

  ngOnDestroy() {
    this.networkRoutesService.setFilterOptions(FilterOptions.empty());
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return [
        'nr',
        'analysis',
        'route',
        'distance',
        'role',
        'last-survey',
        'last-edit',
      ];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'route', 'distance', 'role'];
    }

    return ['nr', 'analysis', 'route'];
  }
}
