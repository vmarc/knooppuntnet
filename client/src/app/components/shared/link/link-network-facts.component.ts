import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-facts",
  template: `
    <a routerLink="{{'/analysis/network-facts/' + networkId}}">Facts</a>
  `
})
export class LinkNetworkFactsComponent {
  @Input() networkId: string;
}
