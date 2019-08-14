import {Component, Input, OnInit} from "@angular/core";
import {MatTableDataSource} from "@angular/material";
import {List} from "immutable";
import {NetworkAttributes} from "../../../kpn/shared/network/network-attributes";

@Component({
  selector: "kpn-subset-network-list",
  template: `
    <mat-divider></mat-divider>
    <table mat-table [dataSource]="dataSource" class="kpn-columns-table">

      <ng-container matColumnDef="networkNumber">
        <td mat-cell *matCellDef="let network; let i = index">
          {{i + 1}}
        </td>
      </ng-container>

      <ng-container matColumnDef="network">
        <td mat-cell *matCellDef="let network">
          <kpn-subset-network [network]="network"></kpn-subset-network>
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
export class SubsetNetworkListComponent implements OnInit {

  @Input() networks: List<NetworkAttributes>;

  displayedColumns = ["networkNumber", "network"];

  dataSource: MatTableDataSource<NetworkAttributes>;

  constructor() {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource(this.networks.toArray());
  }

}
