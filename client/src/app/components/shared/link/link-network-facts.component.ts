import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-facts",
  template: `
    <a routerLink="{{'/analysis/network/' + networkId + '/facts'}}">Facts</a>
  `
})
export class LinkNetworkFactsComponent {
  @Input() networkId: string;
}
