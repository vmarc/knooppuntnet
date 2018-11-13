import {Component, Input} from "@angular/core";
import {NetworkType} from "../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-type-icon',
  template: `
    <mat-icon *ngIf="networkType.name == 'rcn'">directions_bike</mat-icon>
    <mat-icon *ngIf="networkType.name == 'rwn'">directions_walk</mat-icon>
  `
})
export class NetworkTypeIconComponent {
  @Input() networkType: NetworkType;
}
