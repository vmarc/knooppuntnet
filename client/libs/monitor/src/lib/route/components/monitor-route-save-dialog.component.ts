import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { NgSwitch } from '@angular/common';
import { NgSwitchCase } from '@angular/common';
import { NgSwitchDefault } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';
import { selectSharedHttpError } from '@app/core';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { MonitorService } from '../../monitor.service';
import { actionMonitorRouteSaveDestroy } from '../../store/monitor.actions';
import { actionMonitorRouteSaveInit } from '../../store/monitor.actions';
import { selectMonitorRouteSaveState } from '../../store/monitor.selectors';
import { MonitorRouteParameters } from './monitor-route-parameters';
import { MonitorRouteSaveStepComponent } from './monitor-route-save-step.component';

@Component({
  selector: 'kpn-monitor-route-save-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@monitor.route.save-dialog.title">
        Save route
      </div>
      <div mat-dialog-content>
        <kpn-monitor-route-save-step
          [enabled]="saveRouteEnabled$ | async"
          [status]="saveRouteStatus$ | async"
          label="Save route definition"
        />

        <kpn-monitor-route-save-step
          [enabled]="uploadGpxEnabled$ | async"
          [status]="uploadGpxStatus$ | async"
          label="Upload GPX file"
        />

        <kpn-monitor-route-save-step
          [enabled]="analyzeEnabled$ | async"
          [status]="analyzeStatus$ | async"
          label="Route analysis"
        />

        <div class="done kpn-spacer-below">
          <span
            *ngIf="(analyzeStatus$ | async) === 'busy'"
            i18n="@@monitor.route.save-dialog.busy"
            >This may take a while, please wait...</span
          >
          <span *ngIf="done$ | async" i18n="@@monitor.route.save-dialog.saved"
            >Route saved!</span
          >
        </div>

        <p *ngFor="let error of errors$ | async">
          <ng-container [ngSwitch]="error">
            <span
              *ngSwitchCase="'no-relation-id'"
              i18n="@@monitor.route.save-dialog.no-relation-id"
            >
              Note: we cannot yet perform an analysis. The reference information
              is still incomplete. The relation id has not been specified.
            </span>
            <span
              *ngSwitchCase="'osm-relation-not-found'"
              i18n="@@monitor.route.save-dialog.osm-relation-not-found"
            >
              Note: we cannot yet perform an analysis. The reference information
              is still incomplete. No route with given relation id was found at
              given reference date.
            </span>
            <span *ngSwitchDefault>
              {{ error }}
            </span>
          </ng-container>
        </p>
        <div class="kpn-button-group">
          <button
            mat-stroked-button
            (click)="backToGroup()"
            [disabled]="(done$ | async) === false"
            i18n="@@monitor.route.save-dialog.action.back"
          >
            Back to group
          </button>
          <button
            mat-stroked-button
            (click)="gotoAnalysisResult()"
            [disabled]="(done$ | async) === false"
            i18n="@@monitor.route.save-dialog.action.analysis-result"
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
  standalone: true,
  imports: [
    DialogComponent,
    MatDialogModule,
    MonitorRouteSaveStepComponent,
    NgIf,
    NgFor,
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
    MatButtonModule,
    AsyncPipe,
  ],
})
export class MonitorRouteSaveDialogComponent implements OnInit, OnDestroy {
  saveState$ = this.store.select(selectMonitorRouteSaveState);
  saveRouteEnabled$ = this.state((state) => state.saveRouteEnabled);
  saveRouteStatus$ = this.state((state) => state.saveRouteStatus);
  uploadGpxEnabled$ = this.state((state) => state.uploadGpxEnabled);
  uploadGpxStatus$ = this.state((state) => state.uploadGpxStatus);
  analyzeEnabled$ = this.state((state) => state.analyzeEnabled);
  analyzeStatus$ = this.state((state) => state.analyzeStatus);
  errors$ = this.state((state) => state.errors);
  done$ = this.state((state) => state.done);

  private readonly subscriptions = new Subscriptions();

  constructor(
    private dialogRef: MatDialogRef<MonitorRouteSaveDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public parameters: MonitorRouteParameters,
    private monitorService: MonitorService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.closeDialogUponHttpError();
    this.store.dispatch(actionMonitorRouteSaveInit(this.parameters));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.store.dispatch(actionMonitorRouteSaveDestroy());
  }

  backToGroup(): void {
    this.dialogRef.close('navigate-to-route-list');
  }

  gotoAnalysisResult(): void {
    this.dialogRef.close('navigate-to-analysis-result');
  }

  private closeDialogUponHttpError(): void {
    this.subscriptions.add(
      this.store
        .select(selectSharedHttpError)
        .pipe(filter((error) => !!error))
        .subscribe(() => {
          this.dialogRef.close();
        })
    );
  }

  private state<T>(project: (MonitorRouteSaveState) => T): Observable<T> {
    return this.saveState$.pipe(map(project));
  }
}
