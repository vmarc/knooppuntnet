import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { Subscriptions } from '../../../util/Subscriptions';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteAddPageInit } from '../../store/monitor.actions';
import { actionMonitorRouteInfo } from '../../store/monitor.actions';
import { selectMonitorRouteAddPage } from '../../store/monitor.selectors';
import { selectMonitorRouteInfoPage } from '../../store/monitor.selectors';
import { selectMonitorGroupDescription } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
import { urlFragmentValidator } from '../../validator/url-fragment-validator';

@Component({
  selector: 'kpn-monitor-route-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>
        <a [routerLink]="groupLink$ | async">{{ groupDescription$ | async }}</a>
      </li>
      <li>Route</li>
    </ul>

    <h1>
      {{ groupDescription$ | async }}
    </h1>

    <h2>Add route</h2>

    <mat-stepper orientation="vertical" linear="true">
      <mat-step label="Route name and description" [stepControl]="nameForm">
        <form [formGroup]="nameForm">
          <kpn-monitor-route-step-1-name
            [form]="nameForm"
            [name]="name"
            [description]="description"
          ></kpn-monitor-route-step-1-name>
        </form>
      </mat-step>
      <mat-step label="OSM relation" [stepControl]="relationIdForm">
        <form [formGroup]="relationIdForm" #ngForm="ngForm">
          <kpn-monitor-route-step-2-relation
            [ngForm]="ngForm"
            [form]="relationIdForm"
            [relationIdKnown]="relationIdKnown"
            [relationId]="relationId"
          ></kpn-monitor-route-step-2-relation>
        </form>
      </mat-step>
      <mat-step label="Reference type" [stepControl]="referenceTypeForm">
        <form [formGroup]="referenceTypeForm" #ngReferenceTypeForm="ngForm">
          <kpn-monitor-route-step-3-reference-type
            [ngForm]="ngReferenceTypeForm"
            [referenceType]="referenceType"
          ></kpn-monitor-route-step-3-reference-type>
        </form>
      </mat-step>
      <mat-step label="Reference details">
        <kpn-monitor-route-step-4-reference-details
          [referenceType]="referenceType"
          [referenceTimestamp]="referenceTimestamp"
          [gpxFilename]="gpxFilename"
          [gpxFile]="gpxFile"
        ></kpn-monitor-route-step-4-reference-details>
      </mat-step>
      <mat-step label="Save">
        <kpn-monitor-route-step-5-save
          [name]="name"
          [description]="description"
          [relationId]="relationId"
          [referenceType]="referenceType"
          [referenceTimestamp]="referenceTimestamp"
          [gpxFilename]="gpxFilename"
          [gpxFile]="gpxFile"
          [form]="form"
        ></kpn-monitor-route-step-5-save>
      </mat-step>
    </mat-stepper>
  `,
})
export class MonitorRouteAddPageComponent implements OnInit, OnDestroy {
  private groupId = '';
  readonly response$ = this.store.select(selectMonitorRouteAddPage);
  readonly routeInfo$ = this.store.select(selectMonitorRouteInfoPage);
  readonly groupName$ = this.store.select(selectMonitorGroupName);
  readonly groupDescription$ = this.store.select(selectMonitorGroupDescription);
  readonly groupLink$ = this.groupName$.pipe(
    map((groupName) => `/monitor/groups/${groupName}`)
  );

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

  isLinear = false;

  private readonly subscriptions = new Subscriptions();

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRouteAddPageInit());
    this.subscriptions.add(
      this.store.select(selectMonitorRouteAddPage).subscribe((response) => {
        this.groupId = response?.result?.groupId;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
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
