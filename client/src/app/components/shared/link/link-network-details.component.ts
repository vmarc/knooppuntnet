import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-network-details",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/network/' + networkId">{{title}}</a>
  `
})
export class LinkNetworkDetailsComponent {
  @Input() networkId: number;
  @Input() title: string;
}
