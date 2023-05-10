import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { Input, OnInit } from '@angular/core';
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
import { Store } from '@ngrx/store';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteAdminRelationIdChanged } from '../../store/monitor.actions';
import { actionMonitorRouteInfo } from '../../store/monitor.actions';
import { selectMonitorRouteInfoPage } from '../../store/monitor.selectors';
import { MonitorRouteInfoComponent } from '../add/monitor-route-info.component';

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
          <span i18n="@@monitor.route.properties.relation.question.yes">
            Yes
          </span>
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

    <div
      *ngIf="relationIdKnown.value === false"
      id="relation-id-unknown-comment"
      class="comment"
    >
      <p i18n="@@monitor.route.properties.relation.ok">
        OK, no problem: if you do not know the relation id right now, you can
        still add it at any time later.
      </p>
      <p i18n="@@monitor.route.properties.relation.continue">
        Continue with next step.
      </p>
    </div>

    <div *ngIf="relationIdKnown.value === true">
      <mat-form-field>
        <mat-label i18n="@@monitor.route.properties.relation.label"
          >Route relation id
        </mat-label>
        <input
          matInput
          type="number"
          id="relation-id"
          [formControl]="relationId"
        />
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
      <div *ngIf="response()?.result as page">
        <kpn-monitor-route-info [routeInfo]="page" />
      </div>
    </div>

    <div
      *ngIf="form.invalid && (form.dirty || form.touched || ngForm.submitted)"
      id="relation.question-unanswered"
      class="kpn-warning"
    >
      <p
        *ngIf="form.errors.questionUnanswered"
        i18n="@@monitor.route.properties.relation.question-unanswered"
      >
        Please answer the question
      </p>
    </div>

    <div
      *ngIf="
        form.invalid &&
        (relationId.dirty || relationId.touched || ngForm.submitted)
      "
      class="kpn-warning"
    >
      <p
        *ngIf="form.errors?.relationIdMissing"
        id="relation-id-missing-warning"
        i18n="@@monitor.route.properties.relation.missing"
      >
        Provide a valid OSM route relation id
      </p>
    </div>

    <kpn-form-status
      formName="step3-form"
      [statusChanges]="ngForm.statusChanges"
    ></kpn-form-status>
    <div class="kpn-button-group">
      <button
        id="step3-back"
        mat-stroked-button
        matStepperPrevious
        i18n="@@action.back"
      >
        Back
      </button>
      <button
        id="step3-next"
        mat-stroked-button
        matStepperNext
        i18n="@@action.next"
      >
        Next
      </button>
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

      .comment {
        padding-top: 2em;
        padding-bottom: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatRadioModule,
    ReactiveFormsModule,
    NgIf,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MonitorRouteInfoComponent,
    MatStepperModule,
    AsyncPipe,
    FormStatusComponent,
  ],
})
export class MonitorRoutePropertiesStep3RelationComponent implements OnInit {
  @Input() ngForm: FormGroupDirective;
  @Input() form: FormGroup;
  @Input() relationIdKnown: FormControl<boolean>;
  @Input() relationId: FormControl<number | null>;

  readonly response = this.store.selectSignal(selectMonitorRouteInfoPage);
  private readonly subscriptions = new Subscriptions();

  constructor(private monitorService: MonitorService, private store: Store) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.relationId.valueChanges.subscribe(() => {
        this.resetRouteInformation();
      })
    );
  }

  getRouteInformation(): void {
    this.store.dispatch(
      actionMonitorRouteInfo({ relationId: this.relationId.value })
    );
  }

  resetRouteInformation(): void {
    this.store.dispatch(actionMonitorRouteAdminRelationIdChanged());
  }
}
