import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ViewChild } from '@angular/core';
import { input } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { OrphanRouteInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkType } from '@api/custom';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { DayComponent } from '@app/components/shared/day';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { LinkRouteComponent } from '@app/components/shared/link';
import { BehaviorSubject } from 'rxjs';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';
import { SubsetOrphanRoutesStore } from '../subset-orphan-routes.store';
import { SubsetOrphanRouteAnalysisComponent } from './subset-orphan-route-analysis.component';
import { SubsetOrphanRouteFilter } from './subset-orphan-route-filter';
import { SubsetOrphanRouteFilterCriteria } from './subset-orphan-route-filter-criteria';
import { SubsetOrphanRoutesService } from './subset-orphan-routes.service';

@Component({
  selector: 'kpn-subset-orphan-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      i18n-editLinkTitle="@@subset-orphan-routes.edit.title"
      editLinkTitle="Load the routes in this page in JOSM"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="dataSource.data.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th *matHeaderCellDef mat-header-cell i18n="@@subset-orphan-routes.table.nr">Nr</th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th mat-header-cell *matHeaderCellDef i18n="@@subset-orphan-routes.table.analysis">
          Analysis
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-subset-orphan-route-analysis [route]="route" [networkType]="networkType()" />
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th *matHeaderCellDef mat-header-cell i18n="@@subset-orphan-routes.table.name">Route</th>
        <td mat-cell *matCellDef="let route" class="kpn-align-center action-button-table-cell">
          <kpn-action-button-route [relationId]="route.id" />
          <kpn-link-route
            [routeId]="route.id"
            [routeName]="route.name"
            [networkType]="networkType()"
          />
        </td>
      </ng-container>

      <ng-container matColumnDef="distance">
        <th *matHeaderCellDef mat-header-cell i18n="@@subset-orphan-routes.table.distance">
          Distance
        </th>
        <td mat-cell *matCellDef="let route">
          <div class="distance">{{ (route.meters | integer) + ' m' }}</div>
        </td>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th *matHeaderCellDef mat-header-cell i18n="@@subset-orphan-routes.table.last-survey">
          Survey
        </th>
        <td mat-cell *matCellDef="let route">
          {{ route.lastSurvey }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th *matHeaderCellDef mat-header-cell i18n="@@subset-orphan-routes.table.last-edit">
          Last edit
        </th>
        <td mat-cell *matCellDef="let route">
          <kpn-day [timestamp]="route.lastUpdated" />
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let node; columns: displayedColumns"></tr>
    </table>
  `,
  styles: `
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
  standalone: true,
  imports: [
    AsyncPipe,
    DayComponent,
    EditAndPaginatorComponent,
    IntegerFormatPipe,
    LinkRouteComponent,
    MatTableModule,
    SubsetOrphanRouteAnalysisComponent,
    ActionButtonRouteComponent,
  ],
})
export class SubsetOrphanRoutesTableComponent implements OnInit {
  timeInfo = input.required<TimeInfo>();
  networkType = input.required<NetworkType>();
  orphanRoutes = input.required<OrphanRouteInfo[]>();

  @ViewChild(EditAndPaginatorComponent, { static: true }) paginator: EditAndPaginatorComponent;

  private readonly subsetOrphanRoutesService = inject(SubsetOrphanRoutesService);
  private readonly editService = inject(EditService);
  private readonly store = inject(SubsetOrphanRoutesStore);

  protected dataSource = new MatTableDataSource<OrphanRouteInfo>();
  protected displayedColumns = ['nr', 'analysis', 'name', 'distance', 'last-survey', 'last-edit'];
  protected readonly pageSize = this.store.pageSize();
  private readonly filterCriteria$ = new BehaviorSubject(new SubsetOrphanRouteFilterCriteria());

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator.paginator.matPaginator;
    this.filterCriteria$.subscribe((criteria) => {
      const filter = new SubsetOrphanRouteFilter(this.timeInfo(), criteria, this.filterCriteria$);
      this.dataSource.data = filter.filter(this.orphanRoutes());
      this.subsetOrphanRoutesService.filterOptions$.next(filter.filterOptions(this.orphanRoutes()));
    });
  }

  rowNumber(index: number): number {
    return this.paginator.paginator.rowNumber(index);
  }

  onPageSizeChange(pageSize: number) {
    this.store.updatePageSize(pageSize);
  }

  edit(): void {
    const routeIds = Util.currentPageItems(this.dataSource).map((orphanRoute) => orphanRoute.id);
    this.editService.edit({
      relationIds: routeIds,
      fullRelation: true,
    });
  }
}
