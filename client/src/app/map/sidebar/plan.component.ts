import {Component} from "@angular/core";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan",
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-names></kpn-poi-names>
    <kpn-plan-translations></kpn-plan-translations>
    <kpn-plan-actions></kpn-plan-actions>
    <kpn-plan-steps></kpn-plan-steps>
  `
})
export class PlanComponent {
}
