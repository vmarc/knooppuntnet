import { Inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { ApiResponse } from '@api/custom/api-response';
import { MonitorService } from '@app/monitor/monitor.service';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { concatMap } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { MonitorRouteParameters } from './monitor-route-parameters';

@Component({
  selector: 'kpn-monitor-route-save-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title>Save route</div>
      <div mat-dialog-content>
        <kpn-monitor-route-save-step
          [enabled]="saveRouteEnabled$ | async"
          [status]="saveRouteStatus$ | async"
          label="Save route definition"
        ></kpn-monitor-route-save-step>

        <kpn-monitor-route-save-step
          [enabled]="uploadGpxEnabled$ | async"
          [status]="uploadGpxStatus$ | async"
          label="Upload GPX file"
        ></kpn-monitor-route-save-step>

        <kpn-monitor-route-save-step
          [enabled]="analyzeEnabled$ | async"
          [status]="analyzeStatus$ | async"
          label="Route analysis"
        ></kpn-monitor-route-save-step>

        <div class="done kpn-spacer-below">
          <span *ngIf="(analyzeStatus$ | async) === 'busy'"
            >This may take a wile, please wait...</span
          >
          <span *ngIf="done$ | async">Route saved!</span>
        </div>

        <div class="kpn-button-group">
          <button
            mat-stroked-button
            *ngIf="doneButtonVisible"
            (click)="backToGroup()"
            [disabled]="!(done$ | async)"
          >
            Done
          </button>
          <button
            mat-stroked-button
            *ngIf="groupButtonVisible"
            (click)="backToGroup()"
            [disabled]="!(done$ | async)"
          >
            Back to group
          </button>
          <button
            mat-stroked-button
            *ngIf="analysisResultButtonVisible"
            (click)="gotoAnalysisResult()"
            [disabled]="!(done$ | async)"
          >
            Go to analysis result
          </button>
        </div>
      </div>
    </kpn-dialog>
  `,
  styles: [
    `
      .done {
        height: 1em;
        padding-top: 0.5em;
        padding-bottom: 0.5em;
        padding-right: 1.5em;
      }
    `,
  ],
})
export class MonitorRouteSaveDialogComponent {
  saveRouteEnabled$ = new BehaviorSubject(false);
  saveRouteStatus$ = new BehaviorSubject<'todo' | 'busy' | 'done'>('todo');

  uploadGpxEnabled$ = new BehaviorSubject(false);
  uploadGpxStatus$ = new BehaviorSubject<'todo' | 'busy' | 'done'>('todo');

  analyzeEnabled$ = new BehaviorSubject(false);
  analyzeStatus$ = new BehaviorSubject<'todo' | 'busy' | 'done'>('todo');

  done$ = new BehaviorSubject(false);

  doneButtonVisible = false;
  groupButtonVisible = false;
  analysisResultButtonVisible = false;

  constructor(
    private dialogRef: MatDialogRef<MonitorRouteSaveDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public parameters: MonitorRouteParameters,
    private monitorService: MonitorService,
    private store: Store<AppState>,
    private router: Router
  ) {
    this.save();
  }

  backToGroup(): void {
    const groupName = this.parameters.properties.groupName;
    const url = `/monitor/groups/${groupName}`;
    this.dialogRef.close();
    this.router.navigateByUrl(url);
  }

  gotoAnalysisResult(): void {
    const groupName = this.parameters.properties.groupName;
    const routeName = this.parameters.properties.name;
    const url = `/monitor/groups/${groupName}/routes/${routeName}/map`;
    this.dialogRef.close();
    this.router.navigateByUrl(url);
  }

  private save(): void {
    if (this.parameters.mode === 'add') {
      this.prepareAdd();
      this.add(this.parameters.properties);
    } else {
      this.prepareUpdate();
      this.update(this.parameters.properties);
    }
  }

  private prepareAdd(): void {
    this.saveRouteEnabled$.next(true);
    if (this.parameters.properties.referenceType === 'gpx') {
      this.uploadGpxEnabled$.next(true);
      this.analyzeEnabled$.next(true);
    }
    this.doneButtonVisible = false;
    this.groupButtonVisible = true;
    this.analysisResultButtonVisible = true;
  }

  private prepareUpdate(): void {
    this.saveRouteEnabled$.next(true);
    if (
      this.parameters.properties.referenceType === 'gpx' &&
      this.parameters.properties.gpxFileChanged
    ) {
      this.uploadGpxEnabled$.next(true);
      this.analyzeEnabled$.next(true);
    }
    this.doneButtonVisible = false;
    this.groupButtonVisible = true;
    this.analysisResultButtonVisible = true;
  }

  private add(properties: MonitorRouteProperties): void {
    this.done$.next(false);
    this.store
      .select(selectRouteParam('groupName'))
      .pipe(
        mergeMap((groupName) => {
          return this.addRoute(groupName, properties).pipe(
            concatMap(() => {
              if (properties.referenceType === 'gpx') {
                if (this.parameters.gpxFile) {
                  return this.uploadGpx(groupName, properties).pipe(
                    mergeMap(() => {
                      return this.analyze(groupName, properties);
                    })
                  );
                } else {
                  return of(true);
                }
              } else {
                return of(true);
              }
            })
          );
        }),
        tap(() => this.done$.next(true))
      )
      .subscribe();
  }

  private update(properties: MonitorRouteProperties): void {
    this.done$.next(false);
    this.store
      .select(selectRouteParam('groupName'))
      .pipe(
        mergeMap((groupName) => {
          return this.updateRoute(
            groupName,
            this.parameters.initialProperties.name,
            properties
          ).pipe(
            concatMap(() => {
              if (
                properties.referenceType === 'gpx' &&
                properties.gpxFileChanged
              ) {
                return this.uploadGpx(groupName, properties).pipe(
                  mergeMap(() => {
                    return this.analyze(groupName, properties);
                  })
                );
              } else {
                return of(true);
              }
            })
          );
        }),
        tap(() => this.done$.next(true))
      )
      .subscribe();
  }

  private addRoute(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorGroupPage>> {
    this.saveRouteStatus$.next('busy');
    return this.monitorService
      .addRoute(groupName, properties)
      .pipe(tap(() => this.saveRouteStatus$.next('done')));
  }

  private updateRoute(
    groupName: string,
    routeName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorGroupPage>> {
    this.saveRouteStatus$.next('busy');
    return this.monitorService
      .updateRoute(groupName, routeName, properties)
      .pipe(tap(() => this.saveRouteStatus$.next('done')));
  }

  private uploadGpx(groupName: string, properties: MonitorRouteProperties) {
    this.uploadGpxStatus$.next('busy');
    return this.monitorService
      .routeGpxUpload(groupName, properties.name, this.parameters.gpxFile)
      .pipe(tap(() => this.uploadGpxStatus$.next('done')));
  }

  private analyze(groupName: string, properties: MonitorRouteProperties) {
    this.analyzeStatus$.next('busy');
    return this.monitorService
      .routeAnalyze(groupName, properties.name)
      .pipe(tap(() => this.analyzeStatus$.next('done')));
  }
}
