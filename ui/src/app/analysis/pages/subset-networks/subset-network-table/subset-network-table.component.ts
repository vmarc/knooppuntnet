import {Component, Input, OnInit} from '@angular/core';
import {NetworkAttributes} from "../../../../kpn/shared/network/network-attributes";
import {MatTableDataSource} from "@angular/material";
import {TranslationUnit} from "../../../../translations/domain/translation-unit";

@Component({
  selector: 'kpn-subset-network-table',
  templateUrl: './subset-network-table.component.html',
  styleUrls: ['./subset-network-table.component.scss']
})
export class SubsetNetworkTableComponent implements OnInit {

  @Input() networks: Array<NetworkAttributes> = [];

  displayedColumnTitles = [
    'networkName',
    'length',
    'nodeCount',
    'routeCount',
    'integrityCount',
    'connectionCount'
  ];

  displayedColumnTitles2 = [
    'routeCount',
    'brokenRouteCount',
    'integrityCount',
    'integrityOkRate'
  ];

  displayedColumns = [
    'networkName',
    'happy',
    'length',
    'nodeCount',
    'routeCount',
    'brokenRouteCount',
    'brokenRoutePercentage',
    'integrityCount',
    'integrityCoverage',
    'integrityOkRate',
    'connectionCount'
  ];

  dataSource: MatTableDataSource<NetworkAttributes>;

  constructor() { }

  ngOnInit() {
    this.dataSource = new MatTableDataSource(this.networks);
  }

}
