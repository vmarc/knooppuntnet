import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SegmentInfo } from '@api/common/route/segment-info';
import { RouteSegmentsComponent } from '@app/shared/components/route/segments/route-segments.component';
import { NzContextMenuService } from 'ng-zorro-antd/dropdown';
import { RouteSegmentsPageService } from './route-segments-page.service';

@Component({
  selector: 'ui-route-segments-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response()) {
      <ui-route-segments
        [segments]="segments()"
        [relations]="[]"
        [showSegments]="showSegments()"
        (selectChange)="selectSegment($event)"
        (showSegmentsChange)="showSegmentsChanged($event)"
        (zoomToFitRoute)="zoomToFitRoute()"
      />
    }
  `,
  providers: [RouteSegmentsPageService, NzContextMenuService],
  imports: [RouteSegmentsComponent],
})
export class RouteSegmentsPageComponent implements OnInit, OnDestroy {
  readonly service = inject(RouteSegmentsPageService);
  protected readonly response = this.service.response;
  protected readonly segments = computed(() => this.response()?.result?.segments);

  readonly showSegments = signal<boolean>(true); // TODO redesign - this.service.monitorShowSegments;

  ngOnInit(): void {
    this.service.onInit();
  }

  ngOnDestroy() {
    this.service.onDestroy();
  }
  selectSegment(segment: SegmentInfo): void {
    this.service.selectSegment(segment);
  }

  showSegmentsChanged(value: boolean): void {
    // TODO redesign - this.service.updateMonitorShowSegments(value);
  }

  zoomToFitRoute(): void {
    // TODO redesign - this.service.zoomToFitRoute();
  }
}
