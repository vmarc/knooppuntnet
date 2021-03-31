import {Component} from '@angular/core';

@Component({
  selector: 'kpn-plan',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-plan-translations></kpn-plan-translations>
    <kpn-plan-tip></kpn-plan-tip>
    <kpn-plan-result-menu></kpn-plan-result-menu>
    <kpn-plan-result></kpn-plan-result>
  `
})
export class PlanComponent {
}
