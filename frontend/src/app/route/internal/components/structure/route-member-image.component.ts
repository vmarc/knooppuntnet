import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { StructureRow } from '@api/common/route/structure-row';
import { RouteGapComponent } from '@app/route/internal/components/structure/route-gap.component';
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
      <!--      {{ r.relation.gaps }}-->
      <!--      <ui-structure-canvas-wrapper [memberType]="r.memberType" [link]="null" />-->
      @if (r.relation.gaps !== undefined) {
        <ui-route-gap [description]="r.relation.gaps" [osmSegmentCount]="r.osmSegmentCount" />
      }
    }
    <!--    }-->
  `,
  styles: `
    :host {
      display: flex;
      flex-direction: row;
    }
  `,
  imports: [StructureCanvasWrapperComponent, RouteGapComponent],
})
export class RouteMemberImageComponent {
  readonly row = input.required<StructureRow>();
  protected readonly segmentIds = computed(() => this.row().segmentIds);
}
