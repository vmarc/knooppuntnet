import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: "kpn-network-type-icon",
  template: `
    <mat-icon svgIcon="cycling" *ngIf="networkType?.name == 'cycling'"></mat-icon>
    <mat-icon svgIcon="hiking" *ngIf="networkType?.name == 'hiking'"></mat-icon>
    <mat-icon svgIcon="horse" *ngIf="networkType?.name == 'horse'"></mat-icon>
    <mat-icon svgIcon="motorboat" *ngIf="networkType?.name == 'motorboat'"></mat-icon>
    <mat-icon svgIcon="canoe" *ngIf="networkType?.name == 'canoe'"></mat-icon>
    <mat-icon svgIcon="inline-skating" *ngIf="networkType?.name == 'inline-skating'"></mat-icon>
  `
})
export class NetworkTypeIconComponent {
  @Input() networkType: NetworkType;
}
