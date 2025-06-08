import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LegendIconComponent } from './legend-icon.component';
import { LegendItem } from './legend-item';

@Component({
  selector: 'ui-planner-legend-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="legend-item">
      @if (color()) {
        <ui-legend-icon [color]="color()" [circleColor]="circleColor()" [proposed]="proposed()" />
      } @else {
        <div class="legend-icon">
          <img [src]="src()" class="image" [alt]="alt()" />
        </div>
      }
      <span>{{ label() }}</span>
    </div>
  `,
  styles: `
    .legend-item {
      display: flex;
      align-items: center;
    }

    .legend-icon {
      width: 70px;
      padding-right: 10px;
      text-align: center;
    }
  `,
  imports: [LegendIconComponent],
})
export class PlannerLegendItemComponent {
  readonly item = input.required<LegendItem>();
  protected label = computed(() => this.item().label);
  protected src = computed(() => this.item().src);
  protected alt = computed(() => this.item().alt);
  protected color = computed(() => this.item().color);
  protected circleColor = computed(() => this.item().circleColor);
  protected proposed = computed(() => this.item().proposed);
}
