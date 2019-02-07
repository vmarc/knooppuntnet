import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: 'kpn-network-type-name',
  template: `
    <ng-container *ngIf="networkType.name == 'rwn'">Hiking <!-- Wandelen --></ng-container>
    <ng-container *ngIf="networkType.name == 'rcn'">Cycling <!-- Fietsen --></ng-container>
    <ng-container *ngIf="networkType.name == 'rhn'">Horse <!-- Ruiter --></ng-container>
    <ng-container *ngIf="networkType.name == 'rmn'">Motorboat <!-- Motorboot --></ng-container>
    <ng-container *ngIf="networkType.name == 'rpn'">Canoe <!-- Kano --></ng-container>
    <ng-container *ngIf="networkType.name == 'rin'">Inline Skating</ng-container>
  `
})
export class NetworkTypeNameComponent {
  @Input() networkType: NetworkType;
}
