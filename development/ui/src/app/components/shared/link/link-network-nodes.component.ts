import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-nodes",
  template: `
    <a routerLink="{{'/analysis/network-nodes/' + networkId}}">Nodes</a>
  `
})
export class LinkNetworkNodesComponent {
  @Input() networkId: string;
}
