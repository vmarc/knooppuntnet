import {Component, Input} from "@angular/core";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: "kpn-network-type-icon",
  template: `
    <mat-icon [svgIcon]="networkType.name"></mat-icon>
  `
})
export class NetworkTypeIconComponent {
  @Input() networkType: NetworkType;
}
