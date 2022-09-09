import { Input, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { AsyncValidatorFn } from '@angular/forms';
import { ValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { Day } from '@app/kpn/api/custom/day';
import { urlFragmentValidator } from '@app/monitor/validator/url-fragment-validator';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { DayUtil } from '../../../components/shared/day-util';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteInfo } from '../../store/monitor.actions';
import { MonitorRouteParameters } from './monitor-route-parameters';
import { MonitorRouteSaveDialogComponent } from './monitor-route-save-dialog.component';

@Component({
  selector: 'kpn-monitor-route-properties',
  template: `
    <mat-stepper orientation="vertical" [linear]="initialProperties === null">
      <mat-step
        *ngIf="mode === 'update'"
        label="Group"
        i18n-label="@@monitor.route.properties.step.group"
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
      <mat-step
        label="Route name and description"
        i18n-label="@@monitor.route.properties.step.name-description"
        [stepControl]="nameForm"
      >
        <form [formGroup]="nameForm" #ngNameForm="ngForm">
          <kpn-monitor-route-properties-step-2-name
            [mode]="mode"
            [ngForm]="ngNameForm"
            [name]="name"
            [description]="description"
          ></kpn-monitor-route-properties-step-2-name>
        </form>
      </mat-step>
      <mat-step
        label="OSM relation"
        i18n-label="@@monitor.route.properties.step.osm-relation"
        [stepControl]="relationIdForm"
      >
        <form [formGroup]="relationIdForm" #ngForm="ngForm">
          <kpn-monitor-route-properties-step-3-relation
            [ngForm]="ngForm"
            [form]="relationIdForm"
            [relationIdKnown]="relationIdKnown"
            [relationId]="relationId"
          ></kpn-monitor-route-properties-step-3-relation>
        </form>
      </mat-step>
      <mat-step
        label="Reference type"
        i18n-label="@@monitor.route.properties.step.reference-type"
        [stepControl]="referenceTypeForm"
      >
        <form [formGroup]="referenceTypeForm" #ngReferenceTypeForm="ngForm">
          <kpn-monitor-route-properties-step-4-reference-type
            [ngForm]="ngReferenceTypeForm"
            [referenceType]="referenceType"
          ></kpn-monitor-route-properties-step-4-reference-type>
        </form>
      </mat-step>
      <mat-step
        label="Reference details"
        i18n-label="@@monitor.route.properties.step.reference-details"
      >
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
    </mat-stepper>

    <p
      *ngIf="form.errors?.routeNameNonUnique"
      class="kpn-form-error"
      i18n="@@monitor.route.properties.name.unique"
    >
      The route name should be unique within its the group. A route with name
      "{{ name.value }}" already exists within group "{{
        group.value.groupName
      }}".
    </p>

    <div class="kpn-button-group">
      <button
        mat-raised-button
        color="primary"
        (click)="save()"
        [disabled]="form.invalid"
        i18n="@@action.save"
      >
        Save
      </button>
      <a [routerLink]="groupLink()" i18n="@@action.cancel">Cancel</a>
    </div>
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
    asyncValidators: this.asyncAddRouteNameUniqueValidator(),
  });
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);
  readonly relationIdKnown = new FormControl<boolean | null>(null);
  readonly relationId = new FormControl<number | null>(null);
  readonly referenceType = new FormControl<string | null>(
    null,
    Validators.required
  );
  readonly osmReferenceDate = new FormControl<Date | null>(null);
  readonly gpxFilename = new FormControl<string | null>(null);
  readonly gpxFile = new FormControl<File | null>(null);

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

  readonly form = new FormGroup(
    {
      groupForm: this.groupForm,
      nameForm: this.nameForm,
      relationIdForm: this.relationIdForm,
      referenceTypeForm: this.referenceTypeForm,
      referenceDetailsForm: this.referenceDetailsForm,
    },
    {
      asyncValidators: this.asyncUpdateRouteNameUniqueValidator(),
    }
  );

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    if (this.mode === 'add') {
      this.groupForm.setValue({
        group: { groupName: this.groupName, groupDescription: '' },
      });
      this.osmReferenceDate.setValue(new Date());
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
      });
      this.referenceTypeForm.setValue({
        referenceType: this.initialProperties.referenceType,
      });
      this.referenceDetailsForm.patchValue({
        osmReferenceDate: DayUtil.toDate(
          this.initialProperties.osmReferenceDay
        ),
        gpxFilename: this.initialProperties.gpxFilename,
        gpxFile: null,
      });
    }
  }

  groupLink(): string {
    return `/monitor/groups/${this.groupName}`;
  }

  getRouteInformation(): void {
    this.store.dispatch(
      actionMonitorRouteInfo({ relationId: this.relationId.value })
    );
  }

  save(): void {
    const data: MonitorRouteParameters = {
      mode: this.mode,
      initialProperties: this.initialProperties,
      properties: this.buildProperties(),
      gpxFile: this.gpxFile.value,
    };

    this.dialog.open(MonitorRouteSaveDialogComponent, {
      data,
      maxWidth: 600,
    });
  }

  private buildProperties(): MonitorRouteProperties {
    let osmReferenceDay: Day = null;
    if (this.referenceType.value === 'osm') {
      osmReferenceDay = DayUtil.toDay(this.osmReferenceDate.value);
    }
    return {
      name: this.name.value,
      description: this.description.value,
      groupName: this.group.value?.groupName,
      relationId: this.relationId.value,
      referenceType: this.referenceType.value,
      osmReferenceDay,
      gpxFileChanged: !!this.gpxFile.value,
      gpxFilename: this.gpxFilename.value,
    };
  }

  private relationIdFormValidator(): ValidatorFn {
    return (): { [key: string]: any } => {
      if (this.relationIdKnown.value === false) {
        return null;
      }
      if (this.relationIdKnown.value === true) {
        if (!this.relationId.value) {
          return { relationIdMissing: true };
        }
        return null;
      }
      return { questionUnanswered: true };
    };
  }

  private previousValidationGroupName: string | null = null;
  private previousValidationRouteName: string | null = null;
  private previousValidationResult: ValidationErrors | null = null;

  private asyncAddRouteNameUniqueValidator(): AsyncValidatorFn {
    return (): Observable<ValidationErrors | null> => {
      if (this.mode === 'update') {
        return of(null);
      }
      return this.validateRouteNameUnique();
    };
  }

  private asyncUpdateRouteNameUniqueValidator(): AsyncValidatorFn {
    return (): Observable<ValidationErrors | null> => {
      if (this.mode === 'add') {
        return of(null);
      }
      return this.validateRouteNameUnique();
    };
  }

  private validateRouteNameUnique(): Observable<ValidationErrors | null> {
    const validationGroupName = this.group.value.groupName;
    const validationRouteName = this.name.value;

    if (
      validationGroupName === this.previousValidationGroupName &&
      validationRouteName === this.previousValidationRouteName
    ) {
      return of(this.previousValidationResult);
    }

    if (
      this.mode === 'update' &&
      this.groupName === validationGroupName &&
      this.initialProperties?.name === validationRouteName
    ) {
      this.previousValidationResult = null;
      return of(null);
    }

    return this.monitorService.routeNames(validationGroupName).pipe(
      map((response) => response.result),
      map((routeNames) => {
        if (routeNames.includes(validationRouteName)) {
          const result = { routeNameNonUnique: true };
          this.previousValidationResult = result;
          return result;
        }
        this.previousValidationResult = null;
        return null;
      }),
      catchError(() => of(null))
    );
  }
}
