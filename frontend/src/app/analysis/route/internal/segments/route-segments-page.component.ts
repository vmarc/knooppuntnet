import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSegmentListComponent } from '@app/shared/components/route/segments/route-segment-list.component';
import { RouteSegmentsPageService } from './route-segments-page.service';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (segments()) {
      <ui-route-segment-list [segments]="segments()" [selectedSegment]="undefined" />
    }
  `,
  providers: [RouteSegmentsPageService, RouterService],
  imports: [RouteSegmentListComponent],
})
export class RouteSegmentsPageComponent {
  private readonly service = inject(RouteSegmentsPageService);
  protected readonly segments = () => this.service.response()?.result?.segments;
}
