import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: "kpn-network-type-name",
  template: `
    <ng-container *ngIf="networkType.name == 'rwn'" i18n="@@network-type.rwn">Hiking</ng-container>
    <ng-container *ngIf="networkType.name == 'rcn'" i18n="@@network-type.rcn">Cycling</ng-container>
    <ng-container *ngIf="networkType.name == 'rhn'" i18n="@@network-type.rhn">Horse</ng-container>
    <ng-container *ngIf="networkType.name == 'rmn'" i18n="@@network-type.rmn">Motorboat</ng-container>
    <ng-container *ngIf="networkType.name == 'rpn'" i18n="@@network-type.rpn">Canoe</ng-container>
    <ng-container *ngIf="networkType.name == 'rin'" i18n="@@network-type.rin">Inline Skating</ng-container>
  `
})
export class NetworkTypeNameComponent {
  @Input() networkType: NetworkType;
}
