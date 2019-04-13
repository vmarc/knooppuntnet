import {Component, Input} from "@angular/core";
import {Subset} from "../../kpn/shared/subset";

@Component({
  selector: "kpn-subset-name",
  template: `
    <kpn-network-type-name [networkType]="subset.networkType"></kpn-network-type-name>
    in
    <kpn-country-name [country]="subset.country"></kpn-country-name>
  `,
  styles: []
})
export class SubsetNameComponent {
  @Input() subset: Subset;
}
