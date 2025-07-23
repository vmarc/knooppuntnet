import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterService } from '@app/shared/services/router.service';
import { RouteMapComponent } from './components/route-map.component';
import { RouteMapService } from './components/route-map.service';
import { RouteMapPageService } from './route-map-page.service';

@Component({
  selector: 'ui-route-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      @if (!response.result) {
        <div class="kpn-spacer-above" i18n="@@route.route-not-found">Route not found</div>
      } @else {
        <ui-route-map />
      }
    }
  `,
  providers: [RouteMapPageService, RouterService, RouteMapService],
  imports: [RouteMapComponent],
})
export class RouteMapPageComponent implements OnInit {
  protected readonly service = inject(RouteMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
