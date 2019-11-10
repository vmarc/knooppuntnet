import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/api/custom/network-type";

@Component({
  selector: "kpn-network-type-icon",
  template: `
    <mat-icon [svgIcon]="networkType.name"></mat-icon>
  `
})
export class NetworkTypeIconComponent {
  @Input() networkType: NetworkType;
}
