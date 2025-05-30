import { Component } from '@angular/core';
import { PlanResultComponent } from './plan-result.component';
import { PlanResultMenuComponent } from './plan-result-menu.component';
import { PlanTipComponent } from './plan-tip.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-plan',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-plan-tip />
    <ui-plan-result-menu />
    <ui-plan-result />
  `,
  imports: [PlanTipComponent, PlanResultMenuComponent, PlanResultComponent],
})
export class PlanComponent {}
