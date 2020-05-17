import {ChangeDetectionStrategy, Component} from "@angular/core";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-steps",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-vertical-stepper class="kpn-stepper">
      <mat-step [completed]="false" label="Select route start">
        <kpn-plan-step-start></kpn-plan-step-start>
      </mat-step>
      <mat-step [completed]="false" label="Complete route">
        <kpn-plan-step-route></kpn-plan-step-route>
      </mat-step>
      <mat-step [completed]="false" label="Output">
        <kpn-plan-step-output></kpn-plan-step-output>
      </mat-step>
    </mat-vertical-stepper>
  `
})
export class PlanStepsComponent {
}
