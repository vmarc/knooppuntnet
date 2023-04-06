import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { delay } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { PageWidthService } from '@app/components/shared/page-width.service';
import { Util } from '@app/components/shared/util';
import { actionPreferencesPageSize } from '@app/core/preferences/preferences.actions';
import { selectPreferencesPageSize } from '@app/core/preferences/preferences.selectors';
import { actionSharedEdit } from '@app/core/shared/shared.actions';
import { FilterOptions } from '@app/kpn/filter/filter-options';
import { EditAndPaginatorComponent } from '../../components/edit/edit-and-paginator.component';
import { EditParameters } from '../../components/edit/edit-parameters';
import { NetworkNodeFilter } from './network-node-filter';
import { NetworkNodeFilterCriteria } from './network-node-filter-criteria';
import { NetworkNodesService } from './network-nodes.service';

@Component({
  selector: 'kpn-network-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-edit-and-paginator
      (edit)="edit()"
      editLinkTitle="Load the nodes in this page in the editor (like JOSM)"
      i18n-editLinkTitle="@@network-nodes.edit.title"
      [pageSize]="pageSize$ | async"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="nodes?.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    />

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.nr"
        >
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
            [networkType]="networkType"
            [networkScope]="networkScope"
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
          <kpn-link-node
            [nodeId]="node.detail.id"
            [nodeName]="node.detail.name"
          />
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
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.routes.expected"
        >
          Expected
        </th>
        <td mat-cell *matCellDef="let node">
          {{ expectedRouteCount(node) }}
        </td>
      </ng-container>

      <ng-container matColumnDef="routes-actual">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@network-nodes.table.routes.actual"
        >
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
      <tr
        mat-row
        *matRowDef="let node; columns: displayedColumns$ | async"
      ></tr>
    </table>

    <!--    <kpn-paginator-->
    <!--      [length]="nodes?.size">-->
    <!--    </kpn-paginator>-->
  `,
  styles: [
    `
      .mat-column-nr {
        width: 3rem;
      }

      .mat-column-routes-actual {
        width: 12rem;
      }
    `,
  ],
})
export class NetworkNodeTableComponent implements OnInit, OnDestroy {
  @Input() networkType: NetworkType;
  @Input() networkScope: NetworkScope;
  @Input() timeInfo: TimeInfo;
  @Input() surveyDateInfo: SurveyDateInfo;
  @Input() nodes: NetworkNodeRow[];

  readonly pageSize$ = this.store.select(selectPreferencesPageSize);

  @ViewChild(EditAndPaginatorComponent, { static: true })
  paginator: EditAndPaginatorComponent;

  dataSource: MatTableDataSource<NetworkNodeRow>;
  headerColumns1$: Observable<Array<string>>;
  headerColumns2$: Observable<Array<string>>;
  displayedColumns$: Observable<Array<string>>;

  private readonly filterCriteria: BehaviorSubject<NetworkNodeFilterCriteria> =
    new BehaviorSubject(new NetworkNodeFilterCriteria());

  constructor(
    private pageWidthService: PageWidthService,
    private networkNodesService: NetworkNodesService,
    private store: Store
  ) {
    this.headerColumns1$ = pageWidthService.current$.pipe(
      map(() => this.headerColumns1())
    );
    this.headerColumns2$ = pageWidthService.current$.pipe(
      map(() => this.headerColumns2())
    );
    this.displayedColumns$ = pageWidthService.current$.pipe(
      map(() => this.displayedColumns())
    );
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<NetworkNodeRow>();
    this.dataSource.paginator = this.paginator.paginator.matPaginator;
    this.filterCriteria
      .pipe(
        map(
          (criteria) =>
            new NetworkNodeFilter(
              this.timeInfo,
              this.surveyDateInfo,
              criteria,
              this.filterCriteria
            )
        ),
        tap((filter) => (this.dataSource.data = filter.filter(this.nodes))),
        delay(0)
      )
      .subscribe((filter) => {
        this.networkNodesService.setFilterOptions(
          filter.filterOptions(this.nodes)
        );
      });
  }

  ngOnDestroy() {
    this.networkNodesService.setFilterOptions(FilterOptions.empty());
  }

  rowNumber(index: number): number {
    return this.paginator.paginator.rowNumber(index);
  }

  expectedRouteCount(node: NetworkNodeRow): string {
    return node.detail.expectedRouteCount
      ? node.detail.expectedRouteCount.toString()
      : '-';
  }

  onPageSizeChange(pageSize: number) {
    this.store.dispatch(actionPreferencesPageSize({ pageSize }));
  }

  edit(): void {
    const nodeIds = Util.currentPageItems(this.dataSource).map(
      (node) => node.detail.id
    );
    const editParameters: EditParameters = {
      nodeIds,
    };
    this.store.dispatch(actionSharedEdit(editParameters));
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
      return [
        'nr',
        'analysis',
        'node',
        'name',
        'routes',
        'last-survey',
        'last-edit',
      ];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'routes'];
    }

    return ['nr', 'analysis', 'node'];
  }

  private headerColumns2() {
    if (
      this.pageWidthService.isVeryLarge() ||
      this.pageWidthService.isLarge()
    ) {
      return ['routes-expected', 'routes-actual'];
    }
    return [];
  }
}
