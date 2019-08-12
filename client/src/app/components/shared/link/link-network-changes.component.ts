import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-changes",
  template: `
    <a routerLink="{{'/analysis/network/' + networkId + '/changes'}}">Changes</a>
  `
})
export class LinkNetworkChangesComponent {
  @Input() networkId: string;
}
