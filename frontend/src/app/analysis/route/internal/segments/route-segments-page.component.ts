import { computed } from '@angular/core';
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
    @if (response.hasValue()) {
      <ui-route-segment-list
        [segments]="segments()"
        [relations]="[]"
        [selectedSegment]="undefined"
      />
    }
  `,
  providers: [RouteSegmentsPageService, RouterService],
  imports: [RouteSegmentListComponent],
})
export class RouteSegmentsPageComponent {
  readonly service = inject(RouteSegmentsPageService);
  protected readonly response = this.service.response;
  protected readonly segments = computed(() => this.response.value()?.result?.segments);
}
