import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-details",
  template: `
    <a routerLink="{{'/analysis/network/' + networkId}}">{{title}}</a>
  `
})
export class LinkNetworkDetailsComponent {
  @Input() networkId: number;
  @Input() title: string;
}
