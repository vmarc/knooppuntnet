import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-map",
  template: `
    <a routerLink="{{'/analysis/map/' + networkType}}">{{title}}</a>
  `
})
export class LinkMapComponent {
  @Input() networkType: string;
  @Input() title: string;
}
