import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ViewChild } from '@angular/core';
import { input } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { SurveyDateInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { NetworkNodeRow } from '@api/common/network';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';
import { EditAndPaginatorComponent } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { DayComponent } from '@app/components/shared/day';
import { DayPipe } from '@app/components/shared/format';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { JosmNodeComponent } from '@app/components/shared/link';
import { LinkNodeComponent } from '@app/components/shared/link';
import { actionPreferencesPageSize } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { FilterOptions } from '@app/kpn/filter';
import { Store } from '@ngrx/store';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { delay } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { NetworkNodeAnalysisComponent } from './network-node-analysis.component';
import { NetworkNodeFilter } from './network-node-filter';
import { NetworkNodeFilterCriteria } from './network-node-filter-criteria';
import { NetworkNodeRoutesComponent } from './network-node-routes.component';
import { NetworkNodesService } from './network-nodes.service';

@Component({
  selector: 'kpn-network-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      editLinkTitle="Load the nodes() in this page in the editor (like JOSM)"
      i18n-editLinkTitle="@@network-nodes.edit.title"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodes()?.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th [attr.rowspan]="2" mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let i = index">{{ rowNumber(i) }}</td>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.analysis"
        >
          Analysis
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-network-node-analysis
            [networkType]="networkType()"
            [networkScope]="networkScope()"
            [node]="node"
          />
        </td>
      </ng-container>

      <ng-container matColumnDef="node">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.node"
        >
          Node
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-link-node [nodeId]="node.detail.id" [nodeName]="node.detail.name" />
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.detail.longName }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-expected">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes.expected">
          Expected
        </th>
        <td mat-cell *matCellDef="let node">
          {{ expectedRouteCount(node) }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-actual">
        <th mat-header-cell *matHeaderCellDef i18n="@@network-nodes.table.routes.actual">
          Actual
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-network-node-routes [node]="node" />
        </td>
      </ng-container>

      <ng-container matColumnDef="routes">
        <th
          [attr.colspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.routes"
        >
          Routes
        </th>
      </ng-container>

      <ng-container matColumnDef="last-survey">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.last-survey"
        >
          Survey
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.detail.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let node" class="kpn-separated">
          <kpn-day [timestamp]="node.detail.timestamp" />
          <kpn-josm-node [nodeId]="node.detail.id" />
          <kpn-osm-link-node [nodeId]="node.detail.id" />
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="headerColumns1$ | async"></tr>
      <tr mat-header-row *matHeaderRowDef="headerColumns2$ | async"></tr>
      <tr mat-row *matRowDef="let node; columns: displayedColumns$ | async"></tr>
    </table>

    <!--    <kpn-paginator-->
    <!--      [length]="nodes()?.size">-->
    <!--    </kpn-paginator>-->
  `,
  styles: `
    .mat-column-nr {
      width: 3rem;
    }

    .mat-column-routes-actual {
      width: 12rem;
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    DayComponent,
    DayPipe,
    EditAndPaginatorComponent,
    JosmNodeComponent,
    LinkNodeComponent,
    MatTableModule,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
    OsmLinkNodeComponent,
  ],
})
export class NetworkNodeTableComponent implements OnInit, OnDestroy {
  networkType = input.required<NetworkType>();
  networkScope = input.required<NetworkScope>();
  timeInfo = input.required<TimeInfo>();
  surveyDateInfo = input.required<SurveyDateInfo>();
  nodes = input.required<NetworkNodeRow[]>();

  @ViewChild(EditAndPaginatorComponent, { static: true }) paginator: EditAndPaginatorComponent;

  private readonly pageWidthService = inject(PageWidthService);
  private readonly networkNodesService = inject(NetworkNodesService);
  private readonly editService = inject(EditService);
  private readonly store = inject(Store);
  protected readonly pageSize = this.store.selectSignal(selectPreferencesPageSize);

  protected readonly dataSource = new MatTableDataSource<NetworkNodeRow>();
  protected readonly headerColumns1$ = this.pageWidthService.current$.pipe(
    map(() => this.headerColumns1())
  );
  protected readonly headerColumns2$ = this.pageWidthService.current$.pipe(
    map(() => this.headerColumns2())
  );
  protected readonly displayedColumns$ = this.pageWidthService.current$.pipe(
    map(() => this.displayedColumns())
  );

  private readonly filterCriteria$: BehaviorSubject<NetworkNodeFilterCriteria> =
    new BehaviorSubject(new NetworkNodeFilterCriteria());

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator.paginator.matPaginator;
    this.filterCriteria$
      .pipe(
        map(
          (criteria) =>
            new NetworkNodeFilter(
              this.timeInfo(),
              this.surveyDateInfo(),
              criteria,
              this.filterCriteria$
            )
        ),
        tap((filter) => (this.dataSource.data = filter.filter(this.nodes()))),
        delay(0)
      )
      .subscribe((filter) => {
        this.networkNodesService.setFilterOptions(filter.filterOptions(this.nodes()));
      });
  }

  ngOnDestroy() {
    this.networkNodesService.setFilterOptions(FilterOptions.empty());
  }

  rowNumber(index: number): number {
    return this.paginator.paginator.rowNumber(index);
  }

  expectedRouteCount(node: NetworkNodeRow): string {
    return node.detail.expectedRouteCount ? node.detail.expectedRouteCount.toString() : '-';
  }

  onPageSizeChange(pageSize: number) {
    this.store.dispatch(actionPreferencesPageSize({ pageSize }));
  }

  edit(): void {
    const nodeIds = Util.currentPageItems(this.dataSource).map((node) => node.detail.id);
    this.editService.edit({
      nodeIds,
    });
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return [
        'nr',
        'analysis',
        'node',
        'name',
        'routes-expected',
        'routes-actual',
        'last-survey',
        'last-edit',
      ];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'routes-expected', 'routes-actual'];
    }

    return ['nr', 'analysis', 'node'];
  }

  private headerColumns1() {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'node', 'name', 'routes', 'last-survey', 'last-edit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'routes'];
    }

    return ['nr', 'analysis', 'node'];
  }

  private headerColumns2() {
    if (this.pageWidthService.isVeryLarge() || this.pageWidthService.isLarge()) {
      return ['routes-expected', 'routes-actual'];
    }
    return [];
  }
}
