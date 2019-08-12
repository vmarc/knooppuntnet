import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-nodes",
  template: `
    <a routerLink="{{'/analysis/network/' + networkId + '/nodes'}}">Nodes</a>
  `
})
export class LinkNetworkNodesComponent {
  @Input() networkId: string;
}
