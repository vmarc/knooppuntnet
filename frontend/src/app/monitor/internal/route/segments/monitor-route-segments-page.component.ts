import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SegmentInfo } from '@api/common/route/segment-info';
import { RouteSegmentListComponent } from '@app/shared/components/route/segments/route-segment-list.component';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { MonitorRouteSegmentsPageService } from './monitor-route-segments-page.service';

@Component({
  selector: 'ui-monitor-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response.hasValue()) {
      @if (response.value().result; as page) {
        <label
          nz-checkbox
          [nzChecked]="service.monitorShowSegments()"
          (nzCheckedChange)="updateMonitorShowSegments($event)"
          class="kpn-spacer-above"
        >
          Show in map
        </label>
        <ui-route-segment-list
          [segments]="page.segments"
          [selectedSegment]="selectedSegment()"
          (selectChange)="selectSegment($event)"
        />
      }
    }
  `,
  providers: [MonitorRouteSegmentsPageService],
  imports: [FormsModule, NzCheckboxComponent, RouteSegmentListComponent],
})
export class MonitorRouteSegmentsPageComponent {
  readonly service = inject(MonitorRouteSegmentsPageService);
  readonly response = this.service.response;

  readonly selectedSegment = signal<SegmentInfo>(undefined);

  selectSegment(segment: SegmentInfo): void {
    this.service.selectSegment(segment);
  }

  updateMonitorShowSegments(value: boolean): void {
    this.service.updateMonitorShowSegments(value);
  }
}
