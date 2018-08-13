import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: 'network-node-table',
  templateUrl: './network-node-table.component.html',
  styleUrls: ['./network-node-table.component.scss']
})
export class NetworkNodeTableComponent implements OnInit {

  @Input() nodes: Array<NetworkNodeInfo2> = [];

  displayedColumns = ['nr', 'analysis', 'node', 'routes', 'lastEdit'];

  dataSource: MatTableDataSource<NetworkNodeInfo2>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.nodes);
    this.dataSource.paginator = this.paginator;
  }

}
