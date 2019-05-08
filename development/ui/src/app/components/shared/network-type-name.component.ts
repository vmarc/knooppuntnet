import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: "kpn-network-type-name",
  template: `
    <ng-container *ngIf="networkType.name == 'hiking'" i18n="@@network-type.hiking">Hiking</ng-container>
    <ng-container *ngIf="networkType.name == 'cycling'" i18n="@@network-type.cycling">Cycling</ng-container>
    <ng-container *ngIf="networkType.name == 'horse'" i18n="@@network-type.horse">Horse</ng-container>
    <ng-container *ngIf="networkType.name == 'motorboat'" i18n="@@network-type.motorboat">Motorboat</ng-container>
    <ng-container *ngIf="networkType.name == 'canoe'" i18n="@@network-type.canoe">Canoe</ng-container>
    <ng-container *ngIf="networkType.name == 'inline-skating'" i18n="@@network-type.inline-skating">Inline Skating</ng-container>
  `
})
export class NetworkTypeNameComponent {
  @Input() networkType: NetworkType;
}
