import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { NetworkNodeDetail } from '@api/common/network/network-node-detail';
import { SurveyDateInfo } from '@api/common/survey-date-info';
import { TimeInfo } from '@api/common/time-info';
import { NetworkType } from '@api/custom/network-type';
import { List } from 'immutable';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { delay } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { PageWidthService } from '../../../components/shared/page-width.service';
import { PaginatorComponent } from '../../../components/shared/paginator/paginator.component';
import { FilterOptions } from '../../../kpn/filter/filter-options';
import { NetworkNodeFilter } from './network-node-filter';
import { NetworkNodeFilterCriteria } from './network-node-filter-criteria';
import { NetworkNodesService } from './network-nodes.service';

@Component({
  selector: 'kpn-network-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      [length]="nodes?.size"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true"
    >
    </kpn-paginator>

    <table mat-table matSort [dataSource]="dataSource" class="kpn-spacer-above">
      <ng-container matColumnDef="nr">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
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
            [node]="node"
            [networkType]="networkType"
          ></kpn-network-node-analysis>
        </td>
      </ng-container>

      <ng-container matColumnDef="node">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-nodes.table.node"
        >
          Node
        </th>
        <td mat-cell *matCellDef="let node">
          <kpn-link-node
            [nodeId]="node.id"
            [nodeName]="node.name"
          ></kpn-link-node>
        </td>
      </ng-container>

      <ng-container matColumnDef="name">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-nodes.table.name"
        >
          Name
        </th>
        <td mat-cell *matCellDef="let node">
          {{ name(node) }}
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
          {{ node.expectedRouteCount }}
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
          <kpn-network-node-routes [node]="node"></kpn-network-node-routes>
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
          mat-sort-header
          i18n="@@network-nodes.table.last-survey"
        >
          Last survey
        </th>
        <td mat-cell *matCellDef="let node">
          {{ node.lastSurvey | day }}
        </td>
      </ng-container>

      <ng-container matColumnDef="last-edit">
        <th
          [attr.rowspan]="2"
          mat-header-cell
          *matHeaderCellDef
          mat-sort-header
          i18n="@@network-nodes.table.last-edit"
        >
          Last edit
        </th>
        <td mat-cell *matCellDef="let node" class="kpn-separated">
          <kpn-day [timestamp]="node.timestamp"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
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
  @Input() timeInfo: TimeInfo;
  @Input() surveyDateInfo: SurveyDateInfo;
  @Input() nodes: List<NetworkNodeDetail> = List();

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  dataSource: MatTableDataSource<NetworkNodeDetail>;
  headerColumns1$: Observable<Array<string>>;
  headerColumns2$: Observable<Array<string>>;
  displayedColumns$: Observable<Array<string>>;

  private readonly filterCriteria: BehaviorSubject<NetworkNodeFilterCriteria> =
    new BehaviorSubject(new NetworkNodeFilterCriteria());

  constructor(
    private pageWidthService: PageWidthService,
    private networkNodesService: NetworkNodesService
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
    this.dataSource = new MatTableDataSource<NetworkNodeDetail>();
    this.dataSource.paginator = this.paginator.matPaginator;
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
        tap(
          (filter) =>
            (this.dataSource.data = filter.filter(this.nodes).toArray())
        ),
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
    return this.paginator.rowNumber(index);
  }

  name(node: NetworkNodeDetail): string {
    const nameTagKeys = List([
      `${this.networkType.id}:name`,
      `name:${this.networkType.id}_ref`,
    ]);
    if (node.tags) {
      const nameTag = node.tags.tags.find((tag) =>
        nameTagKeys.contains(tag.key)
      );
      if (nameTag) {
        return nameTag.value;
      }
    }
    return '-';
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
