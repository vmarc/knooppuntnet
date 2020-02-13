import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-link-route",
  template: `
    <a [routerLink]="'/analysis/route/' + routeId">{{title}}</a>
  `
})
export class LinkRouteComponent {
  @Input() routeId: number;
  @Input() title: string;
}
