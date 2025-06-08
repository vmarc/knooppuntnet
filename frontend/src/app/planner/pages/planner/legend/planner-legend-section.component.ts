import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LegendItem } from '@app/planner/pages/planner/legend/legend-item';
import { PlannerLegendItemComponent } from './planner-legend-item.component';

@Component({
  selector: 'ui-planner-legend-section',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      @for (item of legendItems(); track item.label) {
        <ui-planner-legend-item [item]="item" />
      }
    </div>
  `,
  imports: [PlannerLegendItemComponent],
})
export class PlannerLegendSectionComponent {
  readonly legendItems = input.required<LegendItem[]>();
}
