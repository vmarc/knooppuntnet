import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {RouteSummary} from "../../../kpn/shared/route-summary";
import {List} from "immutable";

@Component({
  selector: 'kpn-subset-orphan-routes-table',
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
        <td mat-cell *matCellDef="let route; let i = index">
          {{i + 1}}
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <td mat-cell *matCellDef="let route">
          <kpn-subset-orphan-route [route]="route"></kpn-subset-orphan-route>
        </td>
      </ng-container>

      <tr mat-row *matRowDef="let network; columns: displayedColumns;"></tr>

    </table>
  `,
  styles: [`
    table {
      width: 100%;
    }
  `]
})
export class SubsetOrphanRoutesTableComponent implements OnInit {

  @Input() orphanRoutes: List<RouteSummary> = List();

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  dataSource: MatTableDataSource<RouteSummary>;

  displayedColumns = ['rowNumber', 'route'];

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.orphanRoutes.toArray());
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
}
