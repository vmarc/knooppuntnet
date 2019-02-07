import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-link-network-changes',
  template: `
    <a routerLink="{{'/analysis/network-changes/' + networkId}}">Changes</a>
  `
})
export class LinkNetworkChangesComponent {
  @Input() networkId: string;
}
