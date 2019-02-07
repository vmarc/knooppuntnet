import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/shared/common/reference";

@Component({
  selector: 'icon-network-link',
  template: `
    <icon-link [reference]="network" [url]="'/analysis/network-details/' + network.id"></icon-link>
  `
})
export class IconNetworkLinkComponent {
  @Input() network: Reference;
}
