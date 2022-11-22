import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkType } from '@api/custom/network-type';
import { Store } from '@ngrx/store';
import { BehaviorSubject } from 'rxjs';
import { Util } from '../../../components/shared/util';
import { AppState } from '../../../core/core.state';
import { actionPreferencesPageSize } from '../../../core/preferences/preferences.actions';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { actionSharedEdit } from '../../../core/shared/shared.actions';
import { EditAndPaginatorComponent } from '../../components/edit/edit-and-paginator.component';
import { EditParameters } from '../../components/edit/edit-parameters';
import { SubsetOrphanRouteFilter } from './subset-orphan-route-filter';
import { SubsetOrphanRouteFilterCriteria } from './subset-orphan-route-filter-criteria';
import { SubsetOrphanRoutesService } from './subset-orphan-routes.service';

@Component({
  selector: 'kpn-subset-orphan-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      title="Load the routes in this page in the editor (like JOSM)"
      i18n-title="@@subset-orphan-routes.edit.title"
      [pageSize]="pageSize$ | async"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="dataSource.data.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    ></kpn-edit-and-paginator>

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-routes.table.nr"
        >
          Nr
        </th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@subset-orphan-routes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-subset-orphan-route-analysis
            [route]="route"
            [networkType]="networkType"
          ></kpn-subset-orphan-route-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-routes.table.name"
        >
          Route
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-link-route
            [routeId]="route.id"
            [title]="route.name"
            [networkType]="networkType"
          ></kpn-link-route>
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-routes.table.distance"
        >
          Distance
        </th>
        <td mat-cell *matCellDef="let route">
          <div class="distance">{{ (route.meters | integer) + ' m' }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-routes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.lastSurvey }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th
          *matHeaderCellDef
          mat-header-cell
          i18n="@@subset-orphan-routes.table.last-edit"
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

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let node; columns: displayedColumns"></tr>
    </table>
  `,
  styles: [
    `
      .mat-column-nr {
        width: 3rem;
      }

      td.mat-mdc-cell:first-of-type {
        padding-left: 10px;
      }

      .distance {
        text-align: right;
        width: 100%;
      }
    `,
  ],
})
export class SubsetOrphanRoutesTableComponent implements OnInit {
  @Input() timeInfo: TimeInfo;
  @Input() networkType: NetworkType;
  @Input() orphanRoutes: OrphanRouteInfo[];

  @ViewChild(EditAndPaginatorComponent, { static: true })
  paginator: EditAndPaginatorComponent;

  dataSource: MatTableDataSource<OrphanRouteInfo>;

  displayedColumns = [
    'nr',
    'analysis',
    'name',
    'distance',
    'last-survey',
    'last-edit',
  ];

  readonly pageSize$ = this.store.select(selectPreferencesPageSize);

  private readonly filterCriteria = new BehaviorSubject(
    new SubsetOrphanRouteFilterCriteria()
  );

  constructor(
    private subsetOrphanRoutesService: SubsetOrphanRoutesService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator.paginator.matPaginator;
    this.filterCriteria.subscribe((criteria) => {
      const filter = new SubsetOrphanRouteFilter(
        this.timeInfo,
        criteria,
        this.filterCriteria
      );
      this.dataSource.data = filter.filter(this.orphanRoutes);
      this.subsetOrphanRoutesService.filterOptions$.next(
        filter.filterOptions(this.orphanRoutes)
      );
    });
  }

  rowNumber(index: number): number {
    return this.paginator.paginator.rowNumber(index);
  }

  onPageSizeChange(pageSize: number) {
    this.store.dispatch(actionPreferencesPageSize({ pageSize }));
  }

  edit(): void {
    const routeIds = Util.currentPageItems(this.dataSource).map(
      (orphanRoute) => orphanRoute.id
    );
    const editParameters: EditParameters = {
      relationIds: routeIds,
      fullRelation: true,
    };
    this.store.dispatch(actionSharedEdit(editParameters));
  }
}
