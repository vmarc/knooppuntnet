import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { AsyncValidatorFn } from '@angular/forms';
import { ValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MonitorAction } from '@api/common/monitor/monitor-action';
import { MonitorReferenceType } from '@api/common/monitor/monitor-reference-type';
import { MonitorRouteGroup } from '@api/common/monitor/monitor-route-group';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { Timestamp } from '@api/custom/timestamp';
import { DayUtil } from '@app/shared/components/day-util';
import { TimestampUtil } from '@app/shared/components/timestamp-util';
import { Subscriptions } from '@app/util/subscriptions';
import { from } from 'rxjs';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteForm {
  mode: MonitorAction;
  groupName: string;
  initialProperties: MonitorRouteProperties;
  routeGroups: MonitorRouteGroup[];
  // TODO redesign -  readonly update = output<MonitorRouteUpdate>();

  private readonly monitorService = inject(MonitorService);
  private readonly monitorWebsocketService = inject(MonitorWebsocketService);
  private isSubmitted = false;

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

  readonly nameForm = new FormGroup({
    group: this.group,
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

  init(
    mode: MonitorAction,
    groupName: string,
    initialProperties: MonitorRouteProperties,
    routeGroups: MonitorRouteGroup[]
  ): void {
    this.mode = mode;
    this.groupName = groupName;
    this.initialProperties = initialProperties;
    this.routeGroups = routeGroups;

    this.monitorWebsocketService.reset();
    if (this.mode === 'add') {
      this.osmReferenceDate.setValue(new Date());
      this.gpxReferenceDate.setValue(new Date());
    } else {
      const initialGroup = this.routeGroups.find(
        (g) => g.groupName === this.initialProperties.groupName
      );
      this.nameForm.setValue({
        group: initialGroup,
        name: this.initialProperties.name,
        description: this.initialProperties.description,
      });
      this.relationIdForm.patchValue({
        relationIdKnown: !!this.initialProperties.relationId,
        relationId: this.initialProperties.relationId,
      });
      let referenceType = this.initialProperties.referenceType;
      if (referenceType === 'osm') {
        referenceType = 'osm-past';
      }
      this.referenceTypeForm.setValue({
        referenceType: referenceType,
      });
      this.referenceDetailsForm.patchValue({
        osmReferenceDate: DayUtil.toDate(this.initialProperties.referenceTimestamp),
        gpxReferenceDate: DayUtil.toDate(this.initialProperties.referenceTimestamp),
        referenceFilename: this.initialProperties.referenceFilename,
        referenceFile: null,
      });
      this.commentForm.patchValue({
        comment: this.initialProperties.comment,
      });
      this.oldReferenceTimestamp = this.initialProperties.referenceTimestamp;
    }

    this.subscriptions.add(
      this.referenceTypeForm.valueChanges.subscribe(() => {
        this.referenceFilename.updateValueAndValidity();
        this.gpxReferenceDate.updateValueAndValidity();
        this.osmReferenceDate.updateValueAndValidity();
      })
    );
  }

  destroy(): void {
    this.subscriptions.unsubscribe();
  }

  validateStatus(formControl: FormControl): string {
    if (
      formControl.invalid &&
      formControl.errors &&
      (formControl.dirty || formControl.touched || this.isSubmitted)
    ) {
      return 'error';
    }
    if (this.name.valid) {
      return 'success';
    }
    return undefined;
  }

  save(): void {
    this.isSubmitted = true;
    if (this.form.valid) {
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
  }

  private doSave(referenceGpx: string): void {
    let relationId = undefined;
    if (this.relationIdKnown.value === true) {
      relationId = this.relationId.value;
    }

    let referenceType: MonitorReferenceType = undefined;
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
    if (this.mode === 'update' && this.name.value !== this.initialProperties.name) {
      routeName = this.initialProperties.name;
      newRouteName = this.name.value;
    }

    let newGroupName: string = undefined;
    if (this.mode === 'update' && this.group.value.groupName !== this.initialProperties.groupName) {
      newGroupName = this.group.value.groupName;
    }

    const command: MonitorRouteUpdate = {
      action: this.mode,
      groupName: this.initialProperties.groupName,
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

    // TODO redesign - this.update.emit(command);
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
    const validationGroupName = this.group.value?.groupName;
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
