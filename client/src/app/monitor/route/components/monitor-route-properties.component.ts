import { Input, OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteInfo } from '../../store/monitor.actions';
import { urlFragmentValidator } from '../../validator/url-fragment-validator';

@Component({
  selector: 'kpn-monitor-route-properties',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-stepper orientation="vertical" [linear]="initialProperties === null">
      <mat-step label="Route name and description" [stepControl]="nameForm">
        <form [formGroup]="nameForm">
          <kpn-monitor-route-properties-step-1-name
            [form]="nameForm"
            [name]="name"
            [description]="description"
          ></kpn-monitor-route-properties-step-1-name>
        </form>
      </mat-step>
      <mat-step label="OSM relation" [stepControl]="relationIdForm">
        <form [formGroup]="relationIdForm" #ngForm="ngForm">
          <kpn-monitor-route-properties-step-2-relation
            [ngForm]="ngForm"
            [form]="relationIdForm"
            [relationIdKnown]="relationIdKnown"
            [relationId]="relationId"
          ></kpn-monitor-route-properties-step-2-relation>
        </form>
      </mat-step>
      <mat-step label="Reference type" [stepControl]="referenceTypeForm">
        <form [formGroup]="referenceTypeForm" #ngReferenceTypeForm="ngForm">
          <kpn-monitor-route-properties-step-3-reference-type
            [ngForm]="ngReferenceTypeForm"
            [referenceType]="referenceType"
          ></kpn-monitor-route-properties-step-3-reference-type>
        </form>
      </mat-step>
      <mat-step label="Reference details">
        <kpn-monitor-route-properties-step-4-reference-details
          [referenceType]="referenceType"
          [referenceTimestamp]="referenceTimestamp"
          [gpxFilename]="gpxFilename"
          [gpxFile]="gpxFile"
        ></kpn-monitor-route-properties-step-4-reference-details>
      </mat-step>
      <mat-step label="Save">
        <kpn-monitor-route-properties-step-5-save
          [mode]="mode"
          [name]="name"
          [description]="description"
          [relationId]="relationId"
          [referenceType]="referenceType"
          [referenceTimestamp]="referenceTimestamp"
          [gpxFilename]="gpxFilename"
          [gpxFile]="gpxFile"
          [form]="form"
        ></kpn-monitor-route-properties-step-5-save>
      </mat-step>
    </mat-stepper>
  `,
})
export class MonitorRoutePropertiesComponent implements OnInit {
  @Input() mode: string;
  @Input() initialProperties: MonitorRouteProperties = null;

  readonly name = new FormControl<string>('', {
    validators: [
      Validators.required,
      urlFragmentValidator,
      Validators.maxLength(15),
    ],
    //asyncValidators: this.monitorService.asyncRouteNameUniqueValidator(''),
  });
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);
  readonly relationIdKnown = new FormControl<boolean | null>(null);
  readonly relationId = new FormControl<string>('');
  readonly referenceType = new FormControl<string | null>(
    null,
    Validators.required
  );
  readonly referenceTimestamp = new FormControl<string>(
    ''
  ); /*Validators.required]*/
  readonly gpxFilename = new FormControl<string>('');
  readonly gpxFile = new FormControl<File>(null);

  readonly nameForm = new FormGroup({
    name: this.name,
    description: this.description,
  });

  readonly relationIdForm = new FormGroup(
    {
      relationIdKnown: this.relationIdKnown,
      relationId: this.relationId,
    },
    this.relationIdFormValidator()
  );

  readonly referenceTypeForm = new FormGroup({
    referenceType: this.referenceType,
  });

  readonly referenceForm = new FormGroup({
    referenceTimestamp: this.referenceTimestamp,
    gpxFilename: this.gpxFilename,
    gpxFile: this.gpxFile,
  });

  readonly form = new FormGroup({
    nameForm: this.nameForm,
    relationIdForm: this.relationIdForm,
    referenceTypeForm: this.referenceTypeForm,
    referenceForm: this.referenceForm,
  });

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    if (this.initialProperties) {
      this.nameForm.setValue({
        name: this.initialProperties.name,
        description: this.initialProperties.description,
      });
      this.relationIdForm.setValue({
        relationIdKnown: !!this.initialProperties.relationId,
        relationId: this.initialProperties.relationId,
      });
      this.referenceTypeForm.setValue({
        referenceType: this.initialProperties.referenceType,
      });
      this.referenceForm.setValue({
        referenceTimestamp: this.initialProperties.referenceTimestamp,
        gpxFilename: this.initialProperties.gpxFilename,
        gpxFile: null,
      });
    }
  }

  getRouteInformation(): void {
    this.store.dispatch(
      actionMonitorRouteInfo({ relationId: this.relationId.value })
    );
  }

  private relationIdFormValidator(): ValidatorFn {
    return (group: FormGroup): { [key: string]: any } => {
      if (this.relationIdKnown.value === false) {
        return null;
      }
      if (this.relationIdKnown.value === true) {
        if (!!this.relationId.value) {
          return null;
        }
        return { relationIdMissing: true };
      }
      return { questionUnanswered: true };
    };
  }
}
