import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-routes",
  template: `
    <a routerLink="{{'/analysis/network/' + networkId + '/routes'}}">Routes</a>
  `
})
export class LinkNetworkRoutesComponent {
  @Input() networkId: string;
}
