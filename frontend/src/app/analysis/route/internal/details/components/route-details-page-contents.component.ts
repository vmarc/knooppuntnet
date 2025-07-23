import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteDetailsComponent } from '@app/route/route-details.component';
import { RouteDetailsPageService } from '../route-details-page.service';

@Component({
  selector: 'ui-route-details-page-contents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let page = response().result;
    <ui-route-details
      [situationOn]="response().situationOn"
      [routeDetailsData]="page.data"
      (segmentSelection)="selectSegment($event)"
    />
  `,
  imports: [RouteDetailsComponent],
})
export class RouteDetailsPageContentsComponent {
  private readonly service = inject(RouteDetailsPageService);
  protected readonly response = computed(() => this.service.response.value());

  selectSegment(routeSegment: RouteSegment): void {
    this.service.selectSegment(routeSegment);
  }
}
