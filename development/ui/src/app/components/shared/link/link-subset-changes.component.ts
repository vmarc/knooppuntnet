import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-subset-changes",
  template: `
    <a routerLink="{{'/analysis/changes/' + country + '/' + networkType}}">Changes</a>
  `
})
export class LinkSubsetChangesComponent {
  @Input() country: string;
  @Input() networkType: string;
}
