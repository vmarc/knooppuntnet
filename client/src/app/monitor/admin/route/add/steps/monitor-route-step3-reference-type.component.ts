import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-step-3-reference-type',
  template: `
    <div class="question">
      <p>
        What do you want to use as reference to compare the OSM relation to?
      </p>
      <mat-radio-group [formControl]="referenceType">
        <mat-radio-button class="answer" value="gpx">
          <span>A GPX trace that you will upload</span>
        </mat-radio-button>
        <mat-radio-button class="answer" value="osm">
          <span>The current or a previous state of the OSM relation</span>
        </mat-radio-button>
      </mat-radio-group>
    </div>
    <div
      *ngIf="
        referenceType.invalid &&
        (referenceType.dirty || referenceType.touched || ngForm.submitted)
      "
      class="warning test"
    >
      <p *ngIf="referenceType.errors.required">Please answer the question</p>
    </div>

    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious>Back</button>
      <button mat-stroked-button matStepperNext>Next</button>
    </div>
  `,
  styles: [
    `
      .question {
        padding-bottom: 1em;
      }

      .answer {
        display: block;
        padding-top: 0.5em;
      }
    `,
  ],
})
export class MonitorRouteStep3ReferenceTypeComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() referenceType: FormControl<string>;
}
