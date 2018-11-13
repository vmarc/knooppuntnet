import {Component, Input} from "@angular/core";
import {NetworkType} from "../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-type-name',
  template: `
    <ng-container i18n *ngIf="networkType.name == 'rwn'">Hiking <!-- Wandelen --></ng-container>
    <ng-container i18n *ngIf="networkType.name == 'rcn'">Cycling <!-- Fietsen --></ng-container>
    <ng-container i18n *ngIf="networkType.name == 'rhn'">Horse <!-- Ruiter --></ng-container>
    <ng-container i18n *ngIf="networkType.name == 'rmn'">Motorboat <!-- Motorboot --></ng-container>
    <ng-container i18n *ngIf="networkType.name == 'rpn'">Canoe <!-- Kano --></ng-container>
    <ng-container i18n *ngIf="networkType.name == 'rin'">Inline Skates</ng-container>
  `
})
export class NetworkTypeNameComponent {
  @Input() networkType: NetworkType;
}
