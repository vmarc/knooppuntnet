import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteStructureRow } from '@api/common/route/route-structure-row';
import { StructureCanvasWrapperComponent } from '@app/shared/components/structure/structure-canvas-wrapper.component';

@Component({
  selector: 'ui-route-member-image',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let r = row();
    @if (r.memberType === 'node') {
      <ui-structure-canvas-wrapper [memberType]="r.memberType" [link]="null" />
    } @else if (r.memberType === 'way') {
      <ui-structure-canvas-wrapper
        [memberType]="r.memberType"
        [segmentIds]="segmentIds()"
        [link]="r.link"
      />
    } @else if (r.memberType === 'relation') {
      <!--      @if (r.relation) {-->
      <ui-structure-canvas-wrapper [memberType]="r.memberType" [link]="null" />
      <!--        @if (r.relation.gaps !== undefined) {-->
      <!--          <ui-monitor-route-gap [description]="" [osmSegmentCount]="r.relation.osmSegmentCount" />-->
      <!--        }-->
    }
    <!--    }-->
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: row;
    }
  `,
  imports: [StructureCanvasWrapperComponent],
})
export class RouteMemberImageComponent {
  readonly row = input.required<RouteStructureRow>();
  protected readonly segmentIds = computed(() => this.row().segmentIds);
}
