import { Input, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { urlFragmentValidator } from '@app/monitor/validator/url-fragment-validator';
import { Store } from '@ngrx/store';
import { DayUtil } from '../../../components/shared/day-util';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteInfo } from '../../store/monitor.actions';

@Component({
  selector: 'kpn-monitor-route-properties',
  template: `
    <mat-stepper orientation="vertical" [linear]="initialProperties === null">
      <mat-step
        *ngIf="mode === 'update'"
        label="Group"
        [stepControl]="groupForm"
      >
        <form [formGroup]="groupForm" #ngGroupForm="ngForm">
          <kpn-monitor-route-properties-step-1-group
            [ngForm]="ngGroupForm"
            [group]="group"
            [routeGroups]="routeGroups"
          ></kpn-monitor-route-properties-step-1-group>
        </form>
      </mat-step>
      <mat-step label="Route name and description" [stepControl]="nameForm">
        <form [formGroup]="nameForm" #ngNameForm="ngForm">
          <kpn-monitor-route-properties-step-2-name
            [mode]="mode"
            [ngForm]="ngNameForm"
            [name]="name"
            [description]="description"
          ></kpn-monitor-route-properties-step-2-name>
        </form>
      </mat-step>
      <mat-step label="OSM relation" [stepControl]="relationIdForm">
        <form [formGroup]="relationIdForm" #ngForm="ngForm">
          <kpn-monitor-route-properties-step-3-relation
            [ngForm]="ngForm"
            [form]="relationIdForm"
            [relationIdKnown]="relationIdKnown"
            [relationIdVerified]="relationIdVerified"
            [relationId]="relationId"
          ></kpn-monitor-route-properties-step-3-relation>
        </form>
      </mat-step>
      <mat-step label="Reference type" [stepControl]="referenceTypeForm">
        <form [formGroup]="referenceTypeForm" #ngReferenceTypeForm="ngForm">
          <kpn-monitor-route-properties-step-4-reference-type
            [ngForm]="ngReferenceTypeForm"
            [referenceType]="referenceType"
          ></kpn-monitor-route-properties-step-4-reference-type>
        </form>
      </mat-step>
      <mat-step label="Reference details">
        <form
          [formGroup]="referenceDetailsForm"
          #ngReferenceDetailsForm="ngForm"
        >
          <kpn-monitor-route-properties-step-5-reference-details
            [ngForm]="ngReferenceDetailsForm"
            [referenceType]="referenceType"
            [osmReferenceDate]="osmReferenceDate"
            [gpxFilename]="gpxFilename"
            [gpxFile]="gpxFile"
          ></kpn-monitor-route-properties-step-5-reference-details>
        </form>
      </mat-step>
      <mat-step label="Save">
        <kpn-monitor-route-properties-step-6-save
          [initialProperties]="initialProperties"
          [mode]="mode"
          [group]="group"
          [name]="name"
          [description]="description"
          [relationId]="relationId"
          [referenceType]="referenceType"
          [osmReferenceDate]="osmReferenceDate"
          [gpxFilename]="gpxFilename"
          [gpxFile]="gpxFile"
          [form]="form"
        ></kpn-monitor-route-properties-step-6-save>
      </mat-step>
    </mat-stepper>
  `,
})
export class MonitorRoutePropertiesComponent implements OnInit {
  @Input() mode: string;
  @Input() groupName: string;
  @Input() initialProperties: MonitorRouteProperties = null;
  @Input() routeGroups: MonitorRouteGroup[];

  readonly group = new FormControl<MonitorRouteGroup | null>(null);

  readonly name = new FormControl<string>('', {
    validators: [
      Validators.required,
      urlFragmentValidator,
      Validators.maxLength(15),
    ],
    asyncValidators: this.monitorService.asyncRouteNameUniqueValidator(() => {
      if (this.mode === 'update') {
        return this.initialProperties.name;
      }
      return '';
    }),
  });
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);
  readonly relationIdKnown = new FormControl<boolean | null>(null);
  readonly relationIdVerified = new FormControl<boolean>(false);
  readonly relationId = new FormControl<string>('');
  readonly referenceType = new FormControl<string | null>(
    null,
    Validators.required
  );
  readonly osmReferenceDate = new FormControl<Date | null>(null);
  readonly gpxFilename = new FormControl<string>('');
  readonly gpxFile = new FormControl<File>(null);

  readonly groupForm = new FormGroup({
    group: this.group,
  });

  readonly nameForm = new FormGroup({
    name: this.name,
    description: this.description,
  });

  readonly relationIdForm = new FormGroup(
    {
      relationIdKnown: this.relationIdKnown,
      relationIdVerified: this.relationIdVerified,
      relationId: this.relationId,
    },
    this.relationIdFormValidator()
  );

  readonly referenceTypeForm = new FormGroup({
    referenceType: this.referenceType,
  });

  readonly referenceDetailsForm = new FormGroup({
    osmReferenceDate: this.osmReferenceDate,
    gpxFilename: this.gpxFilename,
    gpxFile: this.gpxFile,
  });

  readonly form = new FormGroup({
    groupForm: this.groupForm,
    nameForm: this.nameForm,
    relationIdForm: this.relationIdForm,
    referenceTypeForm: this.referenceTypeForm,
    referenceForm: this.referenceDetailsForm,
  });

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    if (this.mode === 'add') {
      this.groupForm.setValue({
        group: { groupName: this.groupName, groupDescription: '' },
      });
    } else {
      const initialGroup = this.routeGroups.find(
        (g) => g.groupName === this.initialProperties.groupName
      );
      this.groupForm.setValue({
        group: initialGroup,
      });
      this.nameForm.setValue({
        name: this.initialProperties.name,
        description: this.initialProperties.description,
      });
      this.relationIdForm.setValue({
        relationIdKnown: !!this.initialProperties.relationId,
        relationId: this.initialProperties.relationId,
        relationIdVerified: false,
      });
      this.referenceTypeForm.setValue({
        referenceType: this.initialProperties.referenceType,
      });
      this.referenceDetailsForm.setValue({
        osmReferenceDate: DayUtil.toDate(
          this.initialProperties.osmReferenceDay
        ),
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
        if (this.relationIdVerified.value) {
          return null;
        }
        if (!this.relationId.value) {
          return { relationIdMissing: true };
        }
        if (!this.relationIdVerified.value) {
          return { relationIdNotVerified: true };
        }
      }
      return { questionUnanswered: true };
    };
  }
}
