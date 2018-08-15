import {Component, Input} from "@angular/core";

@Component({
  selector: 'kpn-link-overview',
  template: `
    <a routerLink="{{'/analysis/overview'}}">Overview</a>
  `
})
export class LinkOverviewComponent {
}
