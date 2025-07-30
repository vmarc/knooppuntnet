import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteRelationInfo } from '@api/common/monitor/monitor-route-relation-info';
import { SegmentInfo } from '@api/common/route/segment-info';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { RouteSegmentListItemComponent } from './route-segment-list-item.component';

@Component({
  selector: 'ui-route-segment-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-list>
      @for (segment of segments(); track segment.id) {
        <ui-list-item
          [clickable]="true"
          [selected]="selectedSegment()?.id == segment.id"
          (click)="selectSegment(segment)"
        >
          <ui-route-segment-list-item [segment]="segment" [relations]="relations()" />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [ListComponent, ListItemComponent, RouteSegmentListItemComponent],
})
export class RouteSegmentListComponent {
  readonly segments = input.required<SegmentInfo[]>();
  readonly relations = input.required<MonitorRouteRelationInfo[]>();
  readonly selectedSegment = input.required<SegmentInfo>();
  readonly selectChange = output<SegmentInfo>();

  selectSegment(segment: SegmentInfo): void {
    this.selectChange.emit(segment);
  }
}
