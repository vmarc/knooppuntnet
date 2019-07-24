import {Component, Input, OnInit, ViewChild} from "@angular/core";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {List} from "immutable";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: "network-node-table",
  template: `
    <mat-paginator [pageSizeOptions]="[5, 10, 20, 50, 1000]" [length]="nodes?.size" showFirstLastButtons></mat-paginator>
    <mat-divider></mat-divider>

    <mat-table matSort [dataSource]="dataSource">

      <ng-container matColumnDef="nr">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Nr</mat-header-cell>
        <mat-cell *matCellDef="let node">{{node.number}}</mat-cell>
      </ng-container>

      <ng-container matColumnDef="analysis">
        <mat-header-cell *matHeaderCellDef>Analysis</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <network-node-analysis [node]="node"></network-node-analysis>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="node">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Node</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <kpn-link-node [nodeId]="node.id" [nodeName]="node.name"></kpn-link-node>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="routes">
        <mat-header-cell *matHeaderCellDef>Routes</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <network-node-routes [node]="node"></network-node-routes>
        </mat-cell>
      </ng-container>

      <ng-container matColumnDef="lastEdit">
        <mat-header-cell *matHeaderCellDef mat-sort-header>Last edit</mat-header-cell>
        <mat-cell *matCellDef="let node">
          <day [timestamp]="node.timestamp"></day>
          edit osm
        </mat-cell>
      </ng-container>

      <mat-header-row *matHeaderRowDef="displayedColumns"></mat-header-row>
      <mat-row *matRowDef="let node; columns: displayedColumns;"></mat-row>
    </mat-table>
  `
})
export class NetworkNodeTableComponent implements OnInit {

  @Input() nodes: List<NetworkNodeInfo2> = List();

  displayedColumns = ["nr", "analysis", "node", "routes", "lastEdit"];

  dataSource: MatTableDataSource<NetworkNodeInfo2>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.nodes.toArray());
    this.dataSource.paginator = this.paginator;
  }

}
