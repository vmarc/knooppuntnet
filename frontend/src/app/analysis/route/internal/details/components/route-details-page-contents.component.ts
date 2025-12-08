import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteDetailsComponent } from '@app/route/route-details.component';
import { RouteDetailsPageService } from '../route-details-page.service';

@Component({
  selector: 'ui-route-details-page-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let page = response()?.result;
    @if (page) {
      <ui-route-details [situationOn]="response().situationOn" [routeDetails]="page.details" />
    }
  `,
  imports: [RouteDetailsComponent],
})
export class RouteDetailsPageContentsComponent {
  private readonly service = inject(RouteDetailsPageService);
  protected readonly response = this.service.response;
}
