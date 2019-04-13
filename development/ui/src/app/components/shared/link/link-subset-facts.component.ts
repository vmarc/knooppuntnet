import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-subset-facts",
  template: `
    <a routerLink="{{'/analysis/facts/' + country + '/' + networkType}}">Facts</a>
  `
})
export class LinkSubsetFactsComponent {
  @Input() country: string;
  @Input() networkType: string;
}
