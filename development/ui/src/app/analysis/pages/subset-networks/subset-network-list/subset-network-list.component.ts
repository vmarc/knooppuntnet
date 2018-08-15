import {Component, Input, OnInit} from '@angular/core';
import {NetworkAttributes} from "../../../../kpn/shared/network/network-attributes";
import {MatTableDataSource} from "@angular/material";

@Component({
  selector: 'kpn-subset-network-list',
  templateUrl: './subset-network-list.component.html',
  styleUrls: ['./subset-network-list.component.scss']
})
export class SubsetNetworkListComponent implements OnInit {

  @Input() networks: Array<NetworkAttributes>;

  displayedColumns = ['networkNumber','network'];

  dataSource: MatTableDataSource<NetworkAttributes>;

  constructor() { }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.networks);
  }

}
