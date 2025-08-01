import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SegmentInfo } from '@api/common/route/segment-info';
import { RouteSegmentsComponent } from '@app/shared/components/route/segments/route-segments.component';
import { NzContextMenuService } from 'ng-zorro-antd/dropdown';
import { MonitorRouteSegmentsPageService } from './monitor-route-segments-page.service';

@Component({
  selector: 'ui-monitor-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response.hasValue()) {
      @if (response.value().result; as page) {
        <ui-route-segments
          [segments]="page.segments"
          [relations]="page.relations"
          [showSegments]="showSegments()"
          (selectChange)="selectSegment($event)"
          (showSegmentsChange)="showSegmentsChanged($event)"
          (zoomToFitRoute)="zoomToFitRoute()"
        />
      }
    }
  `,
  providers: [MonitorRouteSegmentsPageService, NzContextMenuService],
  imports: [FormsModule, RouteSegmentsComponent],
})
export class MonitorRouteSegmentsPageComponent {
  private readonly service = inject(MonitorRouteSegmentsPageService);
  protected readonly response = this.service.response;
  protected readonly showSegments = this.service.monitorShowSegments;

  selectSegment(segment: SegmentInfo): void {
    this.service.selectSegment(segment);
  }

  showSegmentsChanged(value: boolean): void {
    this.service.updateMonitorShowSegments(value);
  }

  zoomToFitRoute(): void {
    this.service.zoomToFitRoute();
  }
}
