import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouteSegment } from '@api/common/route/route-segment';
import { RouteSegmentComponent } from '@app/route/internal/components/route-segment.component';
import { RouteDetailsPageService } from '@app/analysis/route/internal/details/route-details-page.service';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { RouterService } from '@app/shared/services/router.service';

@Component({
  selector: 'ui-route-segments',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <span i18n="@@route.segments.title">Segments</span>
    </p>
    <ui-list>
      @for (segment of segments(); track segment.id) {
        <ui-list-item
          [clickable]="true"
          [selected]="selectedSegment()?.id == segment.id"
          (click)="onSelectionChange(segment)"
        >
          <ui-route-segment [segment]="segment" />
        </ui-list-item>
      }
    </ui-list>
  `,
  providers: [RouterService],
  imports: [ListComponent, ListItemComponent, RouteSegmentComponent],
})
export class RouteSegmentsComponent {
  private readonly service = inject(RouteDetailsPageService);
  protected readonly segments = computed(() => this.service.response()?.result?.data.segments);
  protected readonly selectedSegment = this.service.selectedSegment;

  onSelectionChange(routeSegment: RouteSegment): void {
    this.service.selectSegment(routeSegment);
  }
}
