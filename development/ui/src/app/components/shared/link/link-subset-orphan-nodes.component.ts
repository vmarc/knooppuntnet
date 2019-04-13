import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-subset-orphan-nodes",
  template: `
    <a routerLink="{{'/analysis/orphan-nodes/' + country + '/' + networkType}}">Orphan nodes</a>
  `
})
export class LinkSubsetOrphanNodesComponent {
  @Input() country: string;
  @Input() networkType: string;
}
