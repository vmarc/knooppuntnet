import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: "kpn-network-type-icon",
  template: `
    <mat-icon svgIcon="rcn" *ngIf="networkType?.name == 'rcn'"></mat-icon>
    <mat-icon svgIcon="rwn" *ngIf="networkType?.name == 'rwn'"></mat-icon>
    <mat-icon svgIcon="rhn" *ngIf="networkType?.name == 'rhn'"></mat-icon>
    <mat-icon svgIcon="rmn" *ngIf="networkType?.name == 'rmn'"></mat-icon>
    <mat-icon svgIcon="rpn" *ngIf="networkType?.name == 'rpn'"></mat-icon>
    <mat-icon svgIcon="rin" *ngIf="networkType?.name == 'rin'"></mat-icon>
  `
})
export class NetworkTypeIconComponent {
  @Input() networkType: NetworkType;
}
