import {Component, Input, OnInit} from "@angular/core";
import {MatTableDataSource} from "@angular/material";
import {List} from "immutable";
import {NetworkAttributes} from "../../../kpn/shared/network/network-attributes";

@Component({
  selector: "kpn-subset-network-table",
  template: `
    <mat-divider></mat-divider>
    <table mat-table [dataSource]="dataSource" class="kpn-bordered-table">

      <ng-container matColumnDef="networkName">
        <th mat-header-cell *matHeaderCellDef colSpan="2" rowSpan="2">Network</th>
        <td mat-cell *matCellDef="let network">
          <a href="">{{network.name}}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="happy">
        <td mat-cell *matCellDef="let network">
          <kpn-subset-network-happy [network]="network"></kpn-subset-network-happy>
        </td>
      </ng-container>

      <ng-container matColumnDef="length">
        <th mat-header-cell *matHeaderCellDef rowSpan="2">Length</th>
        <td mat-cell *matCellDef="let network">
          {{network.km}} km
        </td>
      </ng-container>

      <ng-container matColumnDef="nodeCount">
        <th mat-header-cell *matHeaderCellDef rowSpan="2">Nodes</th>
        <td mat-cell *matCellDef="let network">
          {{network.nodeCount}}
        </td>
      </ng-container>

      <ng-container matColumnDef="routeCount">
        <th mat-header-cell *matHeaderCellDef colSpan="3">Routes</th>
        <th mat-header-cell *matHeaderCellDef>R</th>
        <td mat-cell *matCellDef="let network">
          {{network.routeCount}}
        </td>
      </ng-container>

      <ng-container matColumnDef="brokenRouteCount">
        <th mat-header-cell *matHeaderCellDef colSpan="2">Broken</th>
        <td mat-cell *matCellDef="let network">
          {{network.brokenRouteCount}}
        </td>
      </ng-container>

      <ng-container matColumnDef="brokenRoutePercentage">
        <td mat-cell *matCellDef="let network">
          {{network.brokenRoutePercentage}}
        </td>
      </ng-container>

      <ng-container matColumnDef="integrityCount">
        <th mat-header-cell *matHeaderCellDef colSpan="3">Integrity</th>
        <th mat-header-cell *matHeaderCellDef colSpan="2">Nodes</th>
        <td mat-cell *matCellDef="let network">
          {{network.integrity.count}}
        </td>
      </ng-container>

      <ng-container matColumnDef="integrityCoverage">
        <td mat-cell *matCellDef="let network">
          {{network.integrity.coverage}}
        </td>
      </ng-container>

      <ng-container matColumnDef="integrityOkRate">
        <th mat-header-cell *matHeaderCellDef>OK</th>
        <td mat-cell *matCellDef="let network">
          {{network.integrity.okRate}}
        </td>
      </ng-container>

      <ng-container matColumnDef="connectionCount">
        <th mat-header-cell *matHeaderCellDef rowSpan="2">Connections</th>
        <td mat-cell *matCellDef="let network">
          {{network.connectionCount}}
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumnTitles"></tr>
      <tr mat-header-row *matHeaderRowDef="displayedColumnTitles2"></tr>
      <tr mat-row *matRowDef="let network; columns: displayedColumns;"></tr>

    </table>
  `,
  styles: [`
    table {
      width: 100%;
    }
  `]
})
export class SubsetNetworkTableComponent implements OnInit {

  @Input() networks: List<NetworkAttributes> = List();

  displayedColumnTitles = [
    "networkName",
    "length",
    "nodeCount",
    "routeCount",
    "integrityCount",
    "connectionCount"
  ];

  displayedColumnTitles2 = [
    "routeCount",
    "brokenRouteCount",
    "integrityCount",
    "integrityOkRate"
  ];

  displayedColumns = [
    "networkName",
    "happy",
    "length",
    "nodeCount",
    "routeCount",
    "brokenRouteCount",
    "brokenRoutePercentage",
    "integrityCount",
    "integrityCoverage",
    "integrityOkRate",
    "connectionCount"
  ];

  dataSource: MatTableDataSource<NetworkAttributes>;

  constructor() {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(this.networks.toArray());
  }

}
