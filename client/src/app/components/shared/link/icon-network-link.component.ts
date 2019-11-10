import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/api/common/common/reference";

@Component({
  selector: "kpn-icon-network-link",
  template: `
    <kpn-icon-link [reference]="reference" [url]="'/analysis/network/' + reference.id"></kpn-icon-link>
  `
})
export class IconNetworkLinkComponent {
  @Input() reference: Reference;
}
