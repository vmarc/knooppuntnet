import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
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
    <div *ngIf="plan.sourceNode !== null" class="node user-selected">
      <div *ngIf="plan.sourceNode.nodeName.length <= 3" class="text">
        {{ plan.sourceNode.nodeName }}
      </div>
      <div *ngIf="plan.sourceNode.nodeName.length > 3" class="text-long">
        {{ plan.sourceNode.nodeName }}
      </div>
    </div>

    <div *ngFor="let leg of plan.legs">
      <div *ngIf="leg.routes.isEmpty()">
        <div class="leg" i18n="@@plan-detailed.calculating">Calculating...</div>
        <div class="node">
          <div *ngIf="leg.sinkNode.nodeName.length <= 3" class="text">
            {{ leg.sinkNode.nodeName }}
          </div>
          <div *ngIf="leg.sinkNode.nodeName.length > 3" class="text-long">
            {{ leg.sinkNode.nodeName }}
          </div>
        </div>
      </div>
      <div *ngFor="let legRoute of leg.routes; let i = index">
        <!-- eslint-disable @angular-eslint/template/i18n -->
        <div class="leg">
          {{ legRoute.meters }} m
          <span *ngIf="hasColour(legRoute)" class="colour">
            {{ colours(legRoute) }}
          </span>
        </div>
        <!-- eslint-enable @angular-eslint/template/i18n -->
        <div class="node" [class.server-selected]="i < leg.routes.size - 1">
          <div *ngIf="legRoute.sinkNode.nodeName.length <= 3" class="text">
            {{ legRoute.sinkNode.nodeName }}
          </div>
          <div *ngIf="legRoute.sinkNode.nodeName.length > 3" class="text-long">
            {{ legRoute.sinkNode.nodeName }}
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
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
  ],
  standalone: true,
  imports: [NgIf, NgFor],
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
