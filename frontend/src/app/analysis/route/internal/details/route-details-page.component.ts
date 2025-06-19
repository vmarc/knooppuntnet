import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { RouteDetailsComponent } from '@app/analysis/route/internal/details/components/route-details.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { RouterService } from '@app/shared/services/router.service';
import { RoutePageHeaderComponent } from '../components/route-page-header.component';
import { RouteDetailsPageService } from './route-details-page.service';

@Component({
  selector: 'ui-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-route-page-header pageName="details" />

      @if (service.response(); as response) {
        <div class="kpn-spacer-above">
          @if (!response.result) {
            <div i18n="@@route.route-not-found">Route not found</div>
          } @else {
            <ui-route-details />
          }
        </div>
      }
    </ui-page>
  `,
  providers: [RouteDetailsPageService, RouterService],
  imports: [PageComponent, RoutePageHeaderComponent, RouteDetailsComponent],
})
export class RouteDetailsPageComponent implements OnInit {
  readonly service = inject(RouteDetailsPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
