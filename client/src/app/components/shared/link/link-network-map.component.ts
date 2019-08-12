import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-map",
  template: `
    <a routerLink="{{'/analysis/network/' + networkId + '/map'}}">Map</a>
  `
})
export class LinkNetworkMapComponent {
  @Input() networkId: string;
}
