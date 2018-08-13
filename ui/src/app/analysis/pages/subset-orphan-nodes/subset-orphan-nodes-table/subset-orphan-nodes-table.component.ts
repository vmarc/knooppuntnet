import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material';
import {SubsetOrphanNodesTableDataSource} from './subset-orphan-nodes-table-datasource';
import {NodeInfo} from "../../../../kpn/shared/node-info";

@Component({
  selector: 'kpn-subset-orphan-nodes-table',
  templateUrl: './subset-orphan-nodes-table.component.html',
  styleUrls: ['./subset-orphan-nodes-table.component.scss']
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
