import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SuperSegment } from '@api/common/route/super-segment';
import { ListItemComponent } from '@app/shared/components/list/list-item.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { RouteSegmentListItemComponent } from '@app/shared/components/route/segment/route-segment-list-item.component';

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
          <ui-route-segment-list-item [segment]="segment" />
        </ui-list-item>
      }
    </ui-list>
  `,
  imports: [ListComponent, ListItemComponent, RouteSegmentListItemComponent],
})
export class RouteSegmentListComponent {
  readonly segments = input.required<SuperSegment[]>();
  readonly selectedSegment = input.required<SuperSegment>();
  readonly selectChange = output<SuperSegment>();

  selectSegment(segment: SuperSegment): void {
    this.selectChange.emit(segment);
  }
}
