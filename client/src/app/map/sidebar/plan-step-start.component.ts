import {ChangeDetectionStrategy, Component} from "@angular/core";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-step-start",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      Zoom in and select a node as that start of your route.
    </p>
    <p>
      OR: click magnifying glass and type locationname to zoom in.
    </p>
  `
})
export class PlanStepStartComponent {
}
