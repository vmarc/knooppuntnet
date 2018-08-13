import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {RouteSummary} from "../../../../kpn/shared/route-summary";

@Component({
  selector: 'kpn-subset-orphan-routes-table',
  templateUrl: './subset-orphan-routes-table.component.html',
  styleUrls: ['./subset-orphan-routes-table.component.scss']
})
export class SubsetOrphanRoutesTableComponent implements OnInit {

  @Input() orphanRoutes: Array<RouteSummary> = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  dataSource: MatTableDataSource<RouteSummary>;

  displayedColumns = ['rowNumber', 'route'];

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.orphanRoutes);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
}
