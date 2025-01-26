import { Component } from '@angular/core';
import { PlanResultComponent } from './plan-result.component';
import { PlanResultMenuComponent } from './plan-result-menu.component';
import { PlanTipComponent } from './plan-tip.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-plan',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-plan-tip />
    <kpn-plan-result-menu />
    <kpn-plan-result />
  `,
  imports: [PlanTipComponent, PlanResultMenuComponent, PlanResultComponent],
})
export class PlanComponent {}
