import {ChangeDetectionStrategy, Component} from "@angular/core";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-step-route",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>Click the end node of your route.</p>
    <p>OR: click several intermediate nodes and then the end node of your route.</p>
  `
})
export class PlanStepRouteComponent {
}
