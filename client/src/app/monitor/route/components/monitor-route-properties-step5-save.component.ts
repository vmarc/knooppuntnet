import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { Store } from '@ngrx/store';
import { BehaviorSubject } from 'rxjs';
import { from } from 'rxjs';
import { of } from 'rxjs';
import { concatMap } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { MonitorService } from '../../monitor.service';
import { selectMonitorGroupName } from '../../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-route-properties-step-5-save',
  template: `
    <div class="kpn-line">
      <mat-spinner *ngIf="(status$ | async) !== ''" diameter="20"></mat-spinner>
      <span>{{ status$ | async }}</span>
    </div>
    <div class="kpn-button-group">
      <button mat-stroked-button matStepperPrevious>Back</button>
      <button mat-raised-button color="primary" matStepperNext (click)="save()">
        Save
      </button>
    </div>
  `,
})
export class MonitorRoutePropertiesStep5SaveComponent {
  @Input() mode: string;
  @Input() name: FormControl<string>;
  @Input() description: FormControl<string>;
  @Input() relationId: FormControl<string>;
  @Input() referenceType: FormControl<string | null>;
  @Input() referenceTimestamp: FormControl<string>;
  @Input() gpxFilename: FormControl<string>;
  @Input() gpxFile: FormControl<File>;
  @Input() form: FormGroup;
  status$ = new BehaviorSubject('');

  constructor(
    private monitorService: MonitorService,
    private store: Store<AppState>,
    private router: Router
  ) {}

  save(): void {
    if (this.mode === 'add') {
      this.add();
    }
    if (this.mode === 'update') {
      this.update();
    }
  }

  private add(): void {
    this.store
      .select(selectMonitorGroupName)
      .pipe(
        mergeMap((groupName) => {
          const properties: MonitorRouteProperties = {
            name: this.name.value,
            description: this.description.value,
            relationId: this.relationId.value,
            referenceType: this.referenceType.value,
            referenceTimestamp: this.referenceTimestamp.value,
            gpxFilename: this.gpxFilename.value,
          };
          this.status$.next('Saving route definition...');
          return this.monitorService.addRoute(groupName, properties).pipe(
            concatMap(() => {
              if (this.referenceType.value === 'gpx') {
                this.status$.next('Uploading gpx file...');
                const file = this.gpxFile.value;
                return this.monitorService
                  .routeGpxUpload(groupName, this.name.value, file)
                  .pipe(
                    mergeMap(() => {
                      this.status$.next(
                        'Analyzing route, this can take some time, please wait..'
                      );
                      return this.monitorService.routeAnalyze(
                        groupName,
                        this.name.value
                      );
                    }),
                    concatMap(() =>
                      from(
                        this.router.navigateByUrl(
                          `/monitor/groups/${groupName}/routes/${this.name.value}/map`
                        )
                      )
                    )
                  );
              } else {
                return of(true);
              }
            })
          );
        })
      )
      .subscribe();
  }

  private update(): void {
    this.store
      .select(selectMonitorGroupName)
      .pipe(
        mergeMap((groupName) => {
          const properties: MonitorRouteProperties = {
            name: this.name.value,
            description: this.description.value,
            relationId: this.relationId.value,
            referenceType: this.referenceType.value,
            referenceTimestamp: this.referenceTimestamp.value,
            gpxFilename: this.gpxFilename.value,
          };
          this.status$.next('Saving route definition...');
          return this.monitorService.addRoute(groupName, properties).pipe(
            concatMap(() => {
              if (this.referenceType.value === 'gpx') {
                this.status$.next('Uploading gpx file...');
                const file = this.gpxFile.value;
                return this.monitorService
                  .routeGpxUpload(groupName, this.name.value, file)
                  .pipe(
                    mergeMap(() => {
                      this.status$.next(
                        'Analyzing route, this can take some time, please wait..'
                      );
                      return this.monitorService.routeAnalyze(
                        groupName,
                        this.name.value
                      );
                    }),
                    concatMap(() =>
                      from(
                        this.router.navigateByUrl(
                          `/monitor/groups/${groupName}/routes/${this.name.value}/map`
                        )
                      )
                    )
                  );
              } else {
                return of(true);
              }
            })
          );
        })
      )
      .subscribe();
  }
}
