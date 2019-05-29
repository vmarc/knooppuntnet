import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-details",
  template: `
    <a routerLink="{{'/analysis/network-details/' + networkId}}">{{title}}</a>
  `
})
export class LinkNetworkDetailsComponent {
  @Input() networkId: string;
  @Input() title: string;
}
