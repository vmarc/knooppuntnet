import {ChangeDetectionStrategy} from '@angular/core';
import {OnChanges} from '@angular/core';
import {SimpleChanges} from '@angular/core';
import {EventEmitter} from '@angular/core';
import {Output} from '@angular/core';
import {ViewChild} from '@angular/core';
import {Input} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {List} from 'immutable';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../../components/shared/page-width.service';
import {PaginatorComponent} from '../../../components/shared/paginator/paginator.component';
import {LocationNodeInfo} from '../../../kpn/api/common/location/location-node-info';
import {TimeInfo} from '../../../kpn/api/common/time-info';
import {BrowserStorageService} from '../../../services/browser-storage.service';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';

@Component({
  selector: 'kpn-location-node-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <!--
      <mat-slide-toggle
        [checked]="unexpectedRouteCountOnly"
        (change)="unexpectedRouteCountOnlyChanged($event)"
        i18n="@@location-nodes.unexpected-route-count-only">
        Unexpected route count only
      </mat-slide-toggle>
    -->

    <kpn-paginator
      (page)="page.emit($event)"
      [length]="nodeCount"
      [pageIndex]="0"
      [pageSize]="itemsPerPage"
      [pageSizeOptions]="[5, 10, 20, 50, 1000]"
      [showFirstLastButtons]="true">
    </kpn-paginator>

    <mat-divider></mat-divider>

    <mat-table matSort [dataSource]="dataSource">

      <ng-container matColumnDef="nr">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-nodes.table.nr">Nr</mat-header-cell>
        <mat-cell *matCellDef="let i=index">{{rowNumber(i)}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <mat-header-cell *matHeaderCellDef i18n="@@location-nodes.table.analysis">Analysis</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <!-- kpn-network-node-analysis [node]="node" [networkType]="networkType"></kpn-network-node-analysis -->
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="node">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-nodes.table.node">Node</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <kpn-link-node [nodeId]="node.id" [nodeName]="node.name"></kpn-link-node>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="expectedRouteCount">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-nodes.table.expected-route-count">Expected
        </mat-header-cell>
        <mat-cell *matCellDef="let node">
          {{node.expectedRouteCount}}
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="routes">
        <mat-header-cell *matHeaderCellDef i18n="@@location-nodes.table.routes">Routes</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <kpn-location-node-routes [node]="node"></kpn-location-node-routes>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-nodes.table.last-edit">Last edit
        </mat-header-cell>
        <mat-cell *matCellDef="let node" class="kpn-line">
          <kpn-day [timestamp]="node.timestamp"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
        </mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns$ | async"></mat-header-row>
      <mat-row *matRowDef="let node; columns: displayedColumns$ | async;"></mat-row>
    </mat-table>
  `,
  styles: [`

    .mat-slide-toggle {
      margin-top: 1em;
    }

    .mat-header-cell {
      margin-right: 10px;
    }

    .mat-cell {
      margin-right: 10px;
      display: inline-block;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      line-height: 45px;
      vertical-align: middle;
    }

    .mat-column-nr {
      flex: 0 0 30px;
    }

    .mat-column-analysis {
      flex: 0 0 60px;
    }

    .mat-column-node {
      flex: 1 0 60px;
    }

    .mat-column-expectedRouteCount {
      flex: 2 0 60px;
    }

    .mat-column-routes {
      flex: 2 0 200px;
    }

    .mat-column-lastEdit {
      flex: 0 0 200px;
    }
  `]
})
export class LocationNodeTableComponent implements OnInit, OnChanges {

  @Input() timeInfo: TimeInfo;
  @Input() nodes: List<LocationNodeInfo> = List();
  @Input() nodeCount: number;
  @Output() page = new EventEmitter<PageEvent>();

  @ViewChild(PaginatorComponent, {static: true}) paginator: PaginatorComponent;

  unexpectedRouteCountOnly = false;

  itemsPerPage: number;
  dataSource: MatTableDataSource<LocationNodeInfo>;
  displayedColumns$: Observable<Array<string>>;

  constructor(private pageWidthService: PageWidthService,
              private browserStorageService: BrowserStorageService) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
  }

  ngOnInit(): void {
    this.itemsPerPage = this.browserStorageService.itemsPerPage;
    this.dataSource.data = this.nodes.toArray();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['nodes']) {
      this.dataSource.data = this.nodes.toArray();
    }
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }

  unexpectedRouteCountOnlyChanged(event: MatSlideToggleChange): void {
    this.unexpectedRouteCountOnly = event.checked;
    this.dataSource.data = this.nodes.filter(node => {
      if (this.unexpectedRouteCountOnly) {
        return node.expectedRouteCount !== node.routeReferences.size;
      } else {
        return node.expectedRouteCount === node.routeReferences.size;
      }
    }).toArray();
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'analysis', 'node', 'expectedRouteCount', 'routes', 'lastEdit'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'analysis', 'node', 'expectedRouteCount', 'routes'];
    }

    return ['nr', 'analysis', 'node'];
  }
}
