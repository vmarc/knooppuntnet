import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {StatusLinks} from "./status-links";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-status-links",
  template: `
    <span class="kpn-comma-list">
      <a [routerLink]="links.hour">Hour</a>
      <a [routerLink]="links.day">Day</a>
      <a [routerLink]="links.week">Week</a>
      <a [routerLink]="links.month">Month</a>
      <a [routerLink]="links.year">Year</a>
    </span>
  `
})
export class StatusLinksComponent {

  @Input() links: StatusLinks;

}
