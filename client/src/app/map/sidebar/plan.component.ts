import {Component} from "@angular/core";

@Component({
  selector: "kpn-plan",
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-names></kpn-poi-names>
    <kpn-plan-translations></kpn-plan-translations>
    <kpn-plan-step-start></kpn-plan-step-start>
    <kpn-plan-step-route></kpn-plan-step-route>
    <kpn-plan-step-output></kpn-plan-step-output>
  `
})
export class PlanComponent {
}
