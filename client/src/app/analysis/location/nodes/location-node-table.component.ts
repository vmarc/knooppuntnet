import {OnChanges} from "@angular/core";
import {SimpleChanges} from "@angular/core";
import {EventEmitter} from "@angular/core";
import {Output} from "@angular/core";
import {ViewChild} from "@angular/core";
import {Input} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {List} from "immutable";
import {PageWidthService} from "../../../components/shared/page-width.service";
import {PaginatorComponent} from "../../../components/shared/paginator/paginator.component";
import {LocationNodeInfo} from "../../../kpn/api/common/location/location-node-info";
import {TimeInfo} from "../../../kpn/api/common/time-info";

@Component({
  selector: "kpn-location-node-table",
  template: `
    <kpn-paginator
      (page)="page.emit($event)"
      [pageIndex]="0"
      [pageSize]="5"
      [pageSizeOptions]="[5, 10, 20, 50, 1000]"
      [length]="nodeCount"
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

      <ng-container matColumnDef="routes">
        <mat-header-cell *matHeaderCellDef i18n="@@location-nodes.table.routes">Routes</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <kpn-location-node-routes [node]="node"></kpn-location-node-routes>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <mat-header-cell *matHeaderCellDef mat-sort-header i18n="@@location-nodes.table.last-edit">Last edit</mat-header-cell>
        <mat-cell *matCellDef="let node" class="kpn-line">
          <kpn-day [timestamp]="node.timestamp"></kpn-day>
          <kpn-josm-node [nodeId]="node.id"></kpn-josm-node>
          <kpn-osm-link-node [nodeId]="node.id"></kpn-osm-link-node>
        </mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns()"></mat-header-row>
      <mat-row *matRowDef="let node; columns: displayedColumns();"></mat-row>
    </mat-table>
  `,
  styles: [`

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
      flex: 0 0 200px;
    }

    .mat-column-node {
      flex: 1 0 60px;
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

  dataSource: MatTableDataSource<LocationNodeInfo>;

  @ViewChild(PaginatorComponent, {static: true}) paginator: PaginatorComponent;

  // private readonly filterCriteria: BehaviorSubject<NetworkNodeFilterCriteria> = new BehaviorSubject(new NetworkNodeFilterCriteria());

  constructor(private pageWidthService: PageWidthService
              /*private networkNodesService: NetworkNodesService*/) {
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator.matPaginator;
    this.dataSource.data = this.nodes.toArray();
    // this.filterCriteria.subscribe(criteria => {
    //   const filter = new NetworkNodeFilter(this.timeInfo, criteria, this.filterCriteria);
    //   this.dataSource.data = filter.filter(this.nodes).toArray();
    //   this.networkNodesService.filterOptions.next(filter.filterOptions(this.nodes));
    // });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["nodes"]) {
      this.dataSource.data = this.nodes.toArray();
    }
  }

  displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ["nr", "analysis", "node", "routes", "lastEdit"];
    }

    if (this.pageWidthService.isLarge()) {
      return ["nr", "analysis", "node", "routes"];
    }

    return ["nr", "analysis", "node"];
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }
}
