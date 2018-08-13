import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-link-subset-networks',
  template: `
    <a routerLink="{{'/analysis/networks/' + country + '/' + networkType}}">Networks</a>
  `
})
export class LinkSubsetNetworksComponent {
  @Input() country: string;
  @Input() networkType: string;
}
