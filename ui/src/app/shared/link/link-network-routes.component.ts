import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-link-network-routes',
  template: `
    <a routerLink="{{'/analysis/network-routes/' + networkId}}">Routes</a>
  `
})
export class LinkNetworkRoutesComponent {
  @Input() networkId: string;
}
