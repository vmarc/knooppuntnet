import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PageComponent } from '@app/components/shared/page';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
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
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li>
          <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
        </li>
        <li i18n="@@breadcrumb.route-map">Route map</li>
      </ul>

      <kpn-route-page-header pageName="map" />

      @if (service.response(); as response) {
        @if (!response.result) {
          <div class="kpn-spacer-above" i18n="@@route.route-not-found">Route not found</div>
        } @else {
          <kpn-route-map />
        }
      }
      <kpn-analysis-sidebar sidebar />
    </kpn-page>
  `,
  providers: [RouteMapPageService, RouterService, RouteMapService],
  standalone: true,
  imports: [
    AnalysisSidebarComponent,
    PageComponent,
    RouteMapComponent,
    RoutePageHeaderComponent,
    RouterLink,
  ],
})
export class RouteMapPageComponent implements OnInit {
  protected readonly service = inject(RouteMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
