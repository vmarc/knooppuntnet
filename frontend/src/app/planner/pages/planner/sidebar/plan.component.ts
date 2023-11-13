import { Component } from '@angular/core';
import { PlanResultComponent } from './plan-result.component';
import { PlanResultMenuComponent } from './plan-result-menu.component';
import { PlanTipComponent } from './plan-tip.component';

@Component({
  selector: 'kpn-plan',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-plan-tip />
    <kpn-plan-result-menu />
    <kpn-plan-result />
  `,
  standalone: true,
  imports: [PlanTipComponent, PlanResultMenuComponent, PlanResultComponent],
})
export class PlanComponent {}
