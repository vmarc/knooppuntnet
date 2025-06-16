import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteMapComponent } from './components/route-map.component';
import { RouteMapService } from './components/route-map.service';
import { RouteMapPageService } from './route-map-page.service';

@Component({
  selector: 'ui-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="map" />

      @if (service.response(); as response) {
        @if (!response.result) {
          <div class="kpn-spacer-above" i18n="@@route.route-not-found">Route not found</div>
        } @else {
          <ui-route-map />
        }
      }
    </ui-page>
  `,
  providers: [RouteMapPageService, RouterService, RouteMapService],
  imports: [PageComponent, RouteMapComponent, RoutePageHeaderComponent],
})
export class RouteMapPageComponent implements OnInit {
  protected readonly service = inject(RouteMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
