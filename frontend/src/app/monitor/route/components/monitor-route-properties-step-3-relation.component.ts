import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Input } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatStepperModule } from '@angular/material/stepper';
import { FormStatusComponent } from '@app/components/shared';
import { Subscriptions } from '@app/util';
import { MonitorRouteInfoComponent } from '../add/monitor-route-info.component';
import { MonitorRoutePropertiesStep3RelationService } from './monitor-route-properties-step-3-relation.service';

@Component({
  selector: 'kpn-monitor-route-properties-step-3-relation',
  template: `
    <div class="question">
      <p i18n="@@monitor.route.properties.relation.question">
        Do you know the OSM relation id for this route?
      </p>
      <mat-radio-group [formControl]="relationIdKnown">
        <mat-radio-button
          id="relation-id-known-yes"
          class="answer"
          [value]="true"
          [checked]="relationIdKnown.value === true"
        >
          <span i18n="@@monitor.route.properties.relation.question.yes"> Yes </span>
        </mat-radio-button>
        <mat-radio-button
          id="relation-id-known-no"
          class="answer"
          [value]="false"
          [checked]="relationIdKnown.value === false"
        >
          <span i18n="@@monitor.route.properties.relation.question.no">No</span>
        </mat-radio-button>
      </mat-radio-group>
    </div>

    @if (relationIdKnown.value === false) {
      <div id="relation-id-unknown-comment" class="comment">
        <p i18n="@@monitor.route.properties.relation.ok">
          OK, no problem: if you do not know the relation id right now, you can still add it at any
          time later.
        </p>
        <p i18n="@@monitor.route.properties.relation.continue">Continue with next step.</p>
      </div>
    }

    @if (relationIdKnown.value === true) {
      <div>
        <mat-form-field>
          <mat-label i18n="@@monitor.route.properties.relation.label">Route relation id </mat-label>
          <input matInput type="number" id="relation-id" [formControl]="relationId" />
        </mat-form-field>
        <div>
          <button
            mat-stroked-button
            type="button"
            id="verify"
            (click)="getRouteInformation()"
            i18n="@@monitor.route.properties.relation.action.verify"
          >
            Verify route relation id
          </button>
        </div>

        @if (apiResponse(); as response) {
          <div>
            <kpn-monitor-route-info [routeInfo]="response.result" />
          </div>
        }
      </div>
    }

    @if (form.invalid && (form.dirty || form.touched || ngForm.submitted)) {
      @if (form.errors.questionUnanswered) {
        <p
          id="relation.question-unanswered"
          class="kpn-warning"
          i18n="@@monitor.route.properties.relation.question-unanswered"
        >
          Please answer the question
        </p>
      }
    }

    @if (form.invalid && (relationId.dirty || relationId.touched || ngForm.submitted)) {
      @if (form.errors?.relationIdMissing) {
        <p
          id="relation-id-missing-warning"
          class="kpn-error"
          i18n="@@monitor.route.properties.relation.missing"
        >
          Provide a valid OSM route relation id
        </p>
      }
    }

    <kpn-form-status formName="step3-form" [statusChanges]="ngForm.statusChanges"></kpn-form-status>
    <div class="kpn-button-group">
      <button id="step3-back" mat-stroked-button matStepperPrevious i18n="@@action.back">
        Back
      </button>
      <button id="step3-next" mat-stroked-button matStepperNext i18n="@@action.next">Next</button>
    </div>
  `,
  styles: `
    .question {
      padding-bottom: 1em;
    }

    .answer {
      display: block;
      padding-top: 0.5em;
    }

    .comment {
      padding-top: 2em;
      padding-bottom: 1em;
    }
  `,
  providers: [MonitorRoutePropertiesStep3RelationService],
  standalone: true,
  imports: [
    AsyncPipe,
    FormStatusComponent,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatStepperModule,
    MonitorRouteInfoComponent,
    ReactiveFormsModule,
  ],
})
export class MonitorRoutePropertiesStep3RelationComponent implements OnInit, OnDestroy {
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) form: FormGroup;
  @Input({ required: true }) relationIdKnown: FormControl<boolean>;
  @Input({ required: true }) relationId: FormControl<number | null>;

  private readonly service = inject(MonitorRoutePropertiesStep3RelationService);

  readonly apiResponse = this.service.apiResponse;
  private readonly subscriptions = new Subscriptions();

  ngOnInit(): void {
    this.subscriptions.add(
      this.relationId.valueChanges.subscribe(() => {
        this.resetRouteInformation();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  getRouteInformation(): void {
    this.service.getRouteInformation(this.relationId.value);
  }

  resetRouteInformation(): void {
    this.service.resetRouteInformation();
  }
}
