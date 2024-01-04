import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { PlanRoute } from '@api/common/planner';
import { Plan } from '../../../domain/plan/plan';
import { PlannerService } from '../../../planner.service';

@Component({
  selector: 'kpn-plan-detailed',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (plan.sourceNode !== null) {
      <div class="node user-selected">
        @if (plan.sourceNode.nodeName.length <= 3) {
          <div class="text">
            {{ plan.sourceNode.nodeName }}
          </div>
        }
        @if (plan.sourceNode.nodeName.length > 3) {
          <div class="text-long">
            {{ plan.sourceNode.nodeName }}
          </div>
        }
      </div>
    }

    @for (leg of plan.legs; track leg) {
      @if (leg.routes.isEmpty()) {
        <div class="leg" i18n="@@plan-detailed.calculating">Calculating...</div>
        <div class="node">
          @if (leg.sinkNode.nodeName.length <= 3) {
            <div class="text">
              {{ leg.sinkNode.nodeName }}
            </div>
          }
          @if (leg.sinkNode.nodeName.length > 3) {
            <div class="text-long">
              {{ leg.sinkNode.nodeName }}
            </div>
          }
        </div>
      }
      @for (legRoute of leg.routes; track legRoute; let i = $index) {
        <!-- eslint-disable @angular-eslint/template/i18n -->
        <div class="leg">
          {{ legRoute.meters }} m
          @if (hasColour(legRoute)) {
            <span class="colour">
              {{ colours(legRoute) }}
            </span>
          }
        </div>
        <!-- eslint-enable @angular-eslint/template/i18n -->
        <div class="node" [class.server-selected]="i < leg.routes.size - 1">
          @if (legRoute.sinkNode.nodeName.length <= 3) {
            <div class="text">
              {{ legRoute.sinkNode.nodeName }}
            </div>
          }
          @if (legRoute.sinkNode.nodeName.length > 3) {
            <div class="text-long">
              {{ legRoute.sinkNode.nodeName }}
            </div>
          }
        </div>
      }
    }
  `,
  styles: `
    .leg {
      padding-top: 5px;
      padding-bottom: 5px;
      padding-left: 35px;
    }

    .node {
      display: inline-block;
      border-color: grey;
      border-radius: 50%;
      border-style: solid;
      border-width: 3px;
      width: 30px;
      height: 30px;
    }

    .server-selected {
      border-width: 1px;
      padding-left: 2px;
    }

    .text {
      width: 30px;
      margin-top: 5px;
      text-align: center;
    }

    .text-long {
      width: 260px;
      margin-left: 40px;
      margin-top: 5px;
    }

    .colour {
      padding-left: 6px;
    }
  `,
  standalone: true,
  imports: [],
})
export class PlanDetailedComponent {
  @Input() plan: Plan;

  constructor(private plannerService: PlannerService) {}

  hasColour(planRoute: PlanRoute): boolean {
    return this.plannerService.hasColour(planRoute);
  }

  colours(planRoute: PlanRoute): string {
    return this.plannerService.colours(planRoute);
  }
}
