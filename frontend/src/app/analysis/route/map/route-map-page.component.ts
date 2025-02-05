import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NzBreadCrumbItemComponent } from 'ng-zorro-antd/breadcrumb';
import { NzBreadCrumbComponent } from 'ng-zorro-antd/breadcrumb';
import { PageComponent } from '../../../shared/components/page/page.component';
import { RouterService } from '../../../shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteMapComponent } from './components/route-map.component';
import { RouteMapService } from './components/route-map.service';
import { RouteMapPageService } from './route-map-page.service';

@Component({
  selector: 'kpn-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <nz-breadcrumb>
        <nz-breadcrumb-item>
          <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </nz-breadcrumb-item>
        <nz-breadcrumb-item>
          <span i18n="@@breadcrumb.route-map">Route map</span>
        </nz-breadcrumb-item>
      </nz-breadcrumb>

      <kpn-route-page-header pageName="map" />

      @if (service.response(); as response) {
        @if (!response.result) {
          <div class="kpn-spacer-above" i18n="@@route.route-not-found">Route not found</div>
        } @else {
          <kpn-route-map />
        }
      }
    </kpn-page>
  `,
  providers: [RouteMapPageService, RouterService, RouteMapService],
  imports: [
    NzBreadCrumbComponent,
    NzBreadCrumbItemComponent,
    PageComponent,
    RouteMapComponent,
    RoutePageHeaderComponent,
    RouterLink,
  ],
})
export class RouteMapPageComponent implements OnInit {
  readonly service = inject(RouteMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
