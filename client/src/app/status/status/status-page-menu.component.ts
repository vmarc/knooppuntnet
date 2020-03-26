import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {StatusLinks} from "./status-links";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-status-page-menu",
  template: `
    <kpn-page-menu>
      <kpn-page-menu-option [link]="links.hour">Hour</kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.day">Day</kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.week">Week</kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.month">Month</kpn-page-menu-option>
      <kpn-page-menu-option [link]="links.year">Year</kpn-page-menu-option>
    </kpn-page-menu>
  `
})
export class StatusPageMenuComponent {
  @Input() links: StatusLinks;
}
