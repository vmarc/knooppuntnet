import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-fact",
  template: `
    <a routerLink="{{'/analysis/' + fact + '/' + country + '/' + networkType}}">{{fact}}</a>
  `
})
export class LinkFactComponent {
  @Input() fact: string;
  @Input() country: string;
  @Input() networkType: string;
}
