import { inject } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ValidationErrors } from '@angular/forms';
import { AsyncValidatorFn } from '@angular/forms';
import { ValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatStepperModule } from '@angular/material/stepper';
import { RouterLink } from '@angular/router';
import { MonitorRouteGroup } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { Timestamp } from '@api/custom';
import { TimestampUtil } from '@app/components/shared';
import { DayUtil } from '@app/components/shared';
import { Translations } from '@app/i18n';
import { Subscriptions } from '@app/util';
import { from } from 'rxjs';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorService } from '../../monitor.service';
import { MonitorRoutePropertiesStep1GroupComponent } from './monitor-route-properties-step-1-group.component';
import { MonitorRoutePropertiesStep2NameComponent } from './monitor-route-properties-step-2-name.component';
import { MonitorRoutePropertiesStep3RelationComponent } from './monitor-route-properties-step-3-relation.component';
import { MonitorRoutePropertiesStep4ReferenceTypeComponent } from './monitor-route-properties-step-4-reference-type.component';
import { MonitorRoutePropertiesStep5ReferenceDetailsComponent } from './monitor-route-properties-step-5-reference-details.component';
import { MonitorRoutePropertiesStep6CommentComponent } from './monitor-route-properties-step-6-comment.component';

@Component({
  selector: 'kpn-monitor-route-properties',
  template: `
    <mat-stepper orientation="vertical" [linear]="initialProperties() === null">
      @if (mode() === 'update') {
        <mat-step
          label="Group"
          i18n-label="@@monitor.route.properties.step.group"
          [stepControl]="groupForm"
        >
          <form [formGroup]="groupForm" #ngGroupForm="ngForm">
            <kpn-monitor-route-properties-step-1-group
              [ngForm]="ngGroupForm"
              [group]="group"
              [routeGroups]="routeGroups()"
            />
          </form>
        </mat-step>
      }

      <mat-step
        label="Route name and description"
        i18n-label="@@monitor.route.properties.step.name-description"
        [stepControl]="nameForm"
      >
        <form [formGroup]="nameForm" #ngNameForm="ngForm">
          <kpn-monitor-route-properties-step-2-name
            [mode]="mode()"
            [ngForm]="ngNameForm"
            [name]="name"
            [description]="description"
          />
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
          />
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
          />
        </form>
      </mat-step>
      <mat-step
        label="Reference details"
        i18n-label="@@monitor.route.properties.step.reference-details"
        [stepControl]="referenceDetailsForm"
      >
        <form [formGroup]="referenceDetailsForm" #ngReferenceDetailsForm="ngForm">
          <kpn-monitor-route-properties-step-5-reference-details
            [ngForm]="ngReferenceDetailsForm"
            [referenceType]="referenceType"
            [osmReferenceDate]="osmReferenceDate"
            [gpxReferenceDate]="gpxReferenceDate"
            [referenceFilename]="referenceFilename"
            [referenceFile]="referenceFile"
            [oldReferenceTimestamp]="oldReferenceTimestamp"
          />
        </form>
      </mat-step>
      <mat-step
        label="Comment"
        i18n-label="@@monitor.route.properties.step.comment"
        [stepControl]="commentForm"
      >
        <form [formGroup]="commentForm">
          <kpn-monitor-route-properties-step-6-comment [comment]="comment" />
        </form>
      </mat-step>
    </mat-stepper>

    @if (form.errors?.routeNameNonUnique) {
      <p class="kpn-form-error" i18n="@@monitor.route.properties.name.unique">
        The route name should be unique within its the group. A route with name "{{ name.value }}"
        already exists within group "{{ group.value.groupName }}".
      </p>
    }

    <div class="kpn-button-group">
      <button
        mat-raised-button
        id="save"
        color="primary"
        (click)="save()"
        [disabled]="form.invalid"
        i18n="@@action.save"
      >
        Save
      </button>
      <a [routerLink]="groupLink()" id="cancel">{{ cancelLinkText }}</a>
    </div>
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatStepperModule,
    MonitorRoutePropertiesStep1GroupComponent,
    MonitorRoutePropertiesStep2NameComponent,
    MonitorRoutePropertiesStep3RelationComponent,
    MonitorRoutePropertiesStep4ReferenceTypeComponent,
    MonitorRoutePropertiesStep5ReferenceDetailsComponent,
    MonitorRoutePropertiesStep6CommentComponent,
    ReactiveFormsModule,
    RouterLink,
  ],
})
export class MonitorRoutePropertiesComponent implements OnInit, OnDestroy {
  mode = input.required<string>();
  groupName = input.required<string>();
  initialProperties = input.required<MonitorRouteProperties>();
  routeGroups = input.required<MonitorRouteGroup[]>();
  @Output() update = new EventEmitter<MonitorRouteUpdate>();

  private readonly monitorService = inject(MonitorService);
  private readonly monitorWebsocketService = inject(MonitorWebsocketService);

  protected readonly cancelLinkText = Translations.get('action.cancel');

  readonly group = new FormControl<MonitorRouteGroup>(null);

  readonly name = new FormControl<string>('', {
    validators: [Validators.required, Validators.maxLength(15)],
    asyncValidators: this.asyncAddRouteNameUniqueValidator(),
  });
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);
  readonly relationIdKnown = new FormControl<boolean>(null);
  readonly relationId = new FormControl<number>(null);
  readonly referenceType = new FormControl<string>(null, Validators.required);
  readonly osmReferenceDate = new FormControl<Date>(null, this.osmReferenceTimestampValidator());
  readonly gpxReferenceDate = new FormControl<Date>(null, this.gpxReferenceTimestampValidator());
  readonly referenceFilename = new FormControl<string>(null, this.gpxReferenceFilenameValidator());
  readonly referenceFile = new FormControl<File>(null);

  readonly comment = new FormControl<string>(null);

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
    gpxReferenceDate: this.gpxReferenceDate,
    referenceFilename: this.referenceFilename,
    referenceFile: this.referenceFile,
  });

  readonly commentForm = new FormGroup({
    comment: this.comment,
  });

  readonly form = new FormGroup(
    {
      groupForm: this.groupForm,
      nameForm: this.nameForm,
      relationIdForm: this.relationIdForm,
      referenceTypeForm: this.referenceTypeForm,
      referenceDetailsForm: this.referenceDetailsForm,
      commentForm: this.commentForm,
    },
    {
      asyncValidators: this.asyncUpdateRouteNameUniqueValidator(),
    }
  );

  private readonly subscriptions = new Subscriptions();

  oldReferenceTimestamp: Timestamp = null;

  ngOnInit(): void {
    this.monitorWebsocketService.reset();
    if (this.mode() === 'add') {
      this.groupForm.setValue({
        group: { groupName: this.groupName(), groupDescription: '' },
      });
      this.osmReferenceDate.setValue(new Date());
      this.gpxReferenceDate.setValue(new Date());
    } else {
      const initialGroup = this.routeGroups().find(
        (g) => g.groupName === this.initialProperties().groupName
      );
      this.groupForm.setValue({
        group: initialGroup,
      });
      this.nameForm.setValue({
        name: this.initialProperties().name,
        description: this.initialProperties().description,
      });
      this.relationIdForm.patchValue({
        relationIdKnown: !!this.initialProperties().relationId,
        relationId: this.initialProperties().relationId,
      });
      let referenceType = this.initialProperties().referenceType;
      if (referenceType === 'osm') {
        referenceType = 'osm-past';
      }
      this.referenceTypeForm.setValue({
        referenceType: referenceType,
      });
      this.referenceDetailsForm.patchValue({
        osmReferenceDate: DayUtil.toDate(this.initialProperties().referenceTimestamp),
        gpxReferenceDate: DayUtil.toDate(this.initialProperties().referenceTimestamp),
        referenceFilename: this.initialProperties().referenceFilename,
        referenceFile: null,
      });
      this.commentForm.patchValue({
        comment: this.initialProperties().comment,
      });
      this.oldReferenceTimestamp = this.initialProperties().referenceTimestamp;
    }

    this.subscriptions.add(
      this.referenceTypeForm.valueChanges.subscribe(() => {
        this.referenceFilename.updateValueAndValidity();
        this.gpxReferenceDate.updateValueAndValidity();
        this.osmReferenceDate.updateValueAndValidity();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  groupLink(): string {
    return `/monitor/groups/${this.groupName()}`;
  }

  save(): void {
    if (this.referenceFile.value) {
      const file = this.referenceFile.value;
      const promise = file.text();
      console.log(`Send file ${file.name}, size=${file.size}`);
      from(promise).subscribe((referenceGpx) => {
        this.doSave(referenceGpx);
      });
    } else {
      this.doSave(null);
    }
  }

  private doSave(referenceGpx: string): void {
    let relationId = undefined;
    if (this.relationIdKnown.value === true) {
      relationId = this.relationId.value;
    }

    let referenceType = '';
    let referenceNow = false;
    let referenceTimestamp: Timestamp = null;
    if (this.referenceType.value === 'osm-now') {
      referenceType = 'osm';
      referenceNow = true;
    } else if (this.referenceType.value === 'osm-past') {
      referenceType = 'osm';
      referenceTimestamp = TimestampUtil.toTimestamp(this.osmReferenceDate.value);
    } else if (this.referenceType.value === 'gpx') {
      referenceType = 'gpx';
      referenceTimestamp = TimestampUtil.toTimestamp(this.gpxReferenceDate.value);
    } else if (this.referenceType.value === 'multi-gpx') {
      referenceType = 'multi-gpx';
    }

    let routeName = this.name.value;
    let newRouteName: string = undefined;
    if (this.mode() === 'update' && this.name.value !== this.initialProperties().name) {
      routeName = this.initialProperties().name;
      newRouteName = this.name.value;
    }

    let newGroupName: string = undefined;
    if (
      this.mode() === 'update' &&
      this.group.value.groupName !== this.initialProperties().groupName
    ) {
      newGroupName = this.group.value.groupName;
    }

    const command: MonitorRouteUpdate = {
      action: this.mode(),
      groupName: this.initialProperties().groupName,
      routeName,
      referenceType,
      description: this.description.value,
      comment: this.comment.value,
      relationId,
      referenceNow,
      referenceTimestamp,
      referenceFilename: this.referenceFilename.value,
      referenceGpx,
      newGroupName,
      newRouteName,
    };

    this.update.emit(command);
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
      if (this.mode() === 'update') {
        return of(null);
      }
      return this.validateRouteNameUnique();
    };
  }

  private asyncUpdateRouteNameUniqueValidator(): AsyncValidatorFn {
    return (): Observable<ValidationErrors | null> => {
      if (this.mode() === 'add') {
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
      this.mode() === 'update' &&
      this.groupName() === validationGroupName &&
      this.initialProperties()?.name === validationRouteName
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

  private gpxReferenceFilenameValidator(): ValidatorFn {
    return (): ValidationErrors | null => {
      if (this.referenceType.value === 'gpx') {
        if (!this.referenceFilename.value) {
          return { required: true };
        }
      }
      return null;
    };
  }

  private gpxReferenceTimestampValidator(): ValidatorFn {
    return (): ValidationErrors | null => {
      if (this.referenceType.value === 'gpx') {
        if (!this.gpxReferenceDate.value) {
          return { required: true };
        }
      }
      return null;
    };
  }

  private osmReferenceTimestampValidator(): ValidatorFn {
    return (): ValidationErrors | null => {
      if (this.referenceType.value === 'osm') {
        if (!this.osmReferenceDate.value) {
          return { required: true };
        }
      }
      return null;
    };
  }
}
