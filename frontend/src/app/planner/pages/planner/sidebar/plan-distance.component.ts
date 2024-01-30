import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnChanges } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { input } from '@angular/core';
import { Plan } from '../../../domain/plan/plan';

@Component({
  selector: 'kpn-plan-distance',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (meters > 0) {
      <div class="distance">
        <span i18n="@@plan.distance" class="kpn-label">Distance</span>{{ distance() }}
        <span i18n="@@plan.unpaved" class="kpn-label">Unpaved</span>{{ unpaved() }}
      </div>
    }
  `,
  styles: `
    .distance {
      margin-bottom: 10px;
      color: grey;
    }

    .distance :last-child {
      padding-left: 20px;
    }
  `,
  standalone: true,
  imports: [],
})
export class PlanDistanceComponent implements OnChanges {
  plan = input.required<Plan>();

  meters = 0;

  ngOnChanges(changes: SimpleChanges): void {
    this.meters = this.plan().meters();
  }

  distance(): string {
    if (this.meters > 1000) {
      const km = (this.meters / 1000).toFixed(1).toString().replace('.', ',');
      return `${km} km`;
    }
    return `${this.meters} m`;
  }

  unpaved(): string {
    return this.plan().unpavedPercentage();
  }
}
