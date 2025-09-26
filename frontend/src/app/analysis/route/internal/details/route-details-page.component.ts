import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteDetailsPageContentsComponent } from '@app/analysis/route/internal/details/components/route-details-page-contents.component';
import { RouteDetailsPageService } from './route-details-page.service';

@Component({
  selector: 'ui-route-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-spacer-above">
      <ui-route-details-page-contents />
    </div>
  `,
  providers: [RouteDetailsPageService],
  imports: [RouteDetailsPageContentsComponent],
})
export class RouteDetailsPageComponent {
  private readonly service = inject(RouteDetailsPageService);
  protected readonly response = this.service.response;
}
