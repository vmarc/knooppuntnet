import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { Subscriptions } from '../../../util/Subscriptions';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteAdminRelationIdChanged } from '../../store/monitor.actions';
import { actionMonitorRouteInfo } from '../../store/monitor.actions';
import { selectMonitorRouteInfoPage } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-properties-step-3-relation',
  template: `
    <div class="question">
      <p>Do you know the OSM relation id for this route?</p>
      <mat-radio-group [formControl]="relationIdKnown">
        <mat-radio-button
          class="answer"
          [value]="true"
          [checked]="relationIdKnown.value === true"
        >
          <span>Yes</span>
        </mat-radio-button>
        <mat-radio-button
          class="answer"
          [value]="false"
          [checked]="relationIdKnown.value === false"
        >
          <span>No</span>
        </mat-radio-button>
      </mat-radio-group>
    </div>

    <div *ngIf="relationIdKnown.value === false" class="comment">
      <p>
        OK, no problem: if you do not know the relation id right now, you can
        still add it at any time later.
      </p>
      <p>Continue with next step.</p>
    </div>

    <div *ngIf="relationIdKnown.value === true">
      <mat-form-field>
        <mat-label>Route relation id</mat-label>
        <input matInput type="number" [formControl]="relationId" />
      </mat-form-field>
      <div>
        <button
          mat-stroked-button
          type="button"
          (click)="getRouteInformation()"
        >
          Verify route relation id
        </button>
      </div>
      <div *ngIf="routeInfo$ | async as routeInfo">
        <kpn-monitor-route-info
          [routeInfo]="routeInfo.result"
        ></kpn-monitor-route-info>
      </div>
    </div>

    <div
      *ngIf="form.invalid && (form.dirty || form.touched || ngForm.submitted)"
      class="warning"
    >
      <p *ngIf="form.errors.questionUnanswered">Please answer the question</p>
    </div>

    <div
      *ngIf="
        form.invalid &&
        (relationId.dirty || relationId.touched || ngForm.submitted)
      "
      class="warning"
    >
      <p *ngIf="form.errors?.relationIdMissing">
        Provide a valid OSM route relation id
      </p>
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

      .comment {
        padding-top: 2em;
        padding-bottom: 1em;
      }
    `,
  ],
})
export class MonitorRoutePropertiesStep3RelationComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() form: FormGroup;
  @Input() relationIdKnown: FormControl<boolean>;
  @Input() relationId: FormControl<number | null>;

  readonly routeInfo$ = this.store.select(selectMonitorRouteInfoPage);
  private readonly subscriptions = new Subscriptions();

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>
  ) {}

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
