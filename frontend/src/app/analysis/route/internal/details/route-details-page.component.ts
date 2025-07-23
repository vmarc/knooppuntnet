import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteDetailsPageContentsComponent } from '@app/analysis/route/internal/details/components/route-details-page-contents.component';
import { RouterService } from '@app/shared/services/router.service';
import { RouteDetailsPageService } from './route-details-page.service';

@Component({
  selector: 'ui-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response(); as response) {
      <div class="kpn-spacer-above">
        @if (!response.result) {
          <div i18n="@@route.route-not-found">Route not found</div>
        } @else {
          <ui-route-details-page-contents />
        }
      </div>
    }
  `,
  providers: [RouteDetailsPageService, RouterService],
  imports: [RouteDetailsPageContentsComponent],
})
export class RouteDetailsPageComponent {
  readonly service = inject(RouteDetailsPageService);
}
