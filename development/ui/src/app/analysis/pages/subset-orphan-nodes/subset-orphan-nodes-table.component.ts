import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material';
import {SubsetOrphanNodesTableDataSource} from './subset-orphan-nodes-table-datasource';
import {NodeInfo} from "../../../kpn/shared/node-info";

@Component({
  selector: 'kpn-subset-orphan-nodes-table',
  template: `
    <mat-paginator
      #paginator
      [length]="dataSource.data.length"
      [pageIndex]="0"
      [pageSize]="50"
      [pageSizeOptions]="[25, 50, 100, 250, 1000]">
    </mat-paginator>

    <mat-divider></mat-divider>
    <table mat-table [dataSource]="dataSource" class="kpn-columns-table">

      <ng-container matColumnDef="rowNumber">
        <td mat-cell *matCellDef="let item">
          {{item.row}}
        </td>
      </ng-container>

      <ng-container matColumnDef="node">
        <td mat-cell *matCellDef="let item">
          <kpn-subset-orphan-node [node]="item.node"></kpn-subset-orphan-node>
        </td>
      </ng-container>

      <tr mat-row *matRowDef="let item; columns: displayedColumns;"></tr>

    </table>
  `,
  styles: [`
    table {
      width: 100%;
    }
  `]
})
export class SubsetOrphanNodesTableComponent implements OnInit {

  @Input() nodes: Array<NodeInfo>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  dataSource: SubsetOrphanNodesTableDataSource;

  displayedColumns = ['rowNumber', 'node'];

  ngOnInit() {
    this.dataSource = new SubsetOrphanNodesTableDataSource(this.paginator, this.nodes);
  }
}
