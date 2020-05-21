import {Component} from "@angular/core";

@Component({
  selector: "kpn-plan",
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-names></kpn-poi-names>
    <kpn-network-type-selector></kpn-network-type-selector>
    <kpn-plan-translations></kpn-plan-translations>
    <kpn-plan-actions></kpn-plan-actions>
    <kpn-plan-steps></kpn-plan-steps>
  `
})
export class PlanComponent {
}
