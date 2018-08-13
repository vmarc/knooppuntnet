import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-link-network-map',
  template: `
    <a routerLink="{{'/analysis/network-map/' + networkId}}">Map</a>
  `
})
export class LinkNetworkMapComponent {
  @Input() networkId: string;
}
