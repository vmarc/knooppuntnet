import { output } from '@angular/core';
import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteRelationInfo } from '@api/common/monitor/monitor-route-relation-info';
import { SegmentInfo } from '@api/common/route/segment-info';
import { SegmentColors } from '@app/mapold/domain/segment-colors';
import { LegendLineComponent } from '@app/shared/components/legend-line';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { SegmentPopupEvent } from '@app/shared/components/route/segments/segment-popup-event';
import { SegmentRelationPopupEvent } from '@app/shared/components/route/segments/segment-relation-popup-event';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-route-segment-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="segment">
      <span class="segment-id">{{ id() }}</span>
      <span class="segment-legend">
        <ui-legend-line [color]="segmentColor()" />
      </span>
      <span class="segment-meters">{{ meters() | distance }}</span>
      <button nz-button nzShape="circle" (click)="openSegmentPopup($event)">
        <nz-icon nzType="ellipsis" />
      </button>
    </div>
    @for (relationId of relationIds(); track relationId) {
      <div class="segment-relation">
        <button nz-button nzShape="circle" (click)="openRelationPopup($event, relationId)">
          <nz-icon nzType="ellipsis" />
        </button>
        <span class="kpn-separated">
          <span>{{ relationId }}</span>
          <span>{{ relationInfo(relationId) }}</span>
        </span>
      </div>
    }
  `,
  styles: `
    .segment {
      display: flex;
    }

    .segment-id {
      width: 3em;
    }

    .segment-legend {
      width: 3em;
    }

    .segment-meters {
      width: 5em;
    }

    .segment button {
      margin-left: auto;
    }

    .segment-relation {
      padding-top: 0.5em;
    }

    .segment-relation button {
      margin-right: 0.5em;
    }
  `,
  imports: [DistancePipe, LegendLineComponent, NzIconDirective, NzButtonComponent],
})
export class RouteSegmentListItemComponent {
  readonly segment = input.required<SegmentInfo>();
  readonly relations = input.required<MonitorRouteRelationInfo[]>();
  readonly relationPopup = output<SegmentRelationPopupEvent>();
  readonly segmentPopup = output<SegmentPopupEvent>();

  protected readonly id = computed(() => this.segment().id);
  protected readonly meters = computed(() => this.segment().meters);
  protected readonly relationIds = computed(() =>
    this.segment().routeInfos.map((ri) => ri.relationId)
  );

  protected readonly segmentColor = computed(() => SegmentColors.colorForSegmentId(this.id()));

  relationInfo(relationId: number): string {
    const info = this.relations().find((r) => r.relationId === relationId);
    if (info) {
      return info.name;
    }
    return '';
  }

  openSegmentPopup(mouseEvent: MouseEvent): void {
    const event: SegmentPopupEvent = {
      event: mouseEvent,
      segment: this.segment(),
    };
    this.segmentPopup.emit(event);
  }

  openRelationPopup(mouseEvent: MouseEvent, relationId: number): void {
    const event: SegmentRelationPopupEvent = {
      event: mouseEvent,
      segment: this.segment(),
      relationId: relationId,
    };
    this.relationPopup.emit(event);
  }
}
