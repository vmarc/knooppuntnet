import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-route-page-header',
  template: `

    <h1>Route {{routeName}}</h1>

    <kpn-page-menu>

      <kpn-page-menu-option
        pageName="route"
        [selectedPageName]="pageName"
        [link]="link('route')"
        pageTitle="Details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="route-changes"
        [selectedPageName]="pageName"
        [link]="link('route-changes')"
        pageTitle="History">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class RoutePageHeaderComponent {

  @Input() routeId;
  @Input() routeName;
  @Input() pageName;

  link(pageName: string): string {
    return "/analysis/" + pageName + "/" + this.routeId;
  }

}
