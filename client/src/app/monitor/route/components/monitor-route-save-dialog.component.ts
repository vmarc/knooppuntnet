import { OnInit } from '@angular/core';
import { Inject } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MonitorService } from '@app/monitor/monitor.service';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { first } from 'rxjs/operators';
import { AppState } from '../../../core/core.state';
import { actionMonitorRouteSaveInit } from '../../store/monitor.actions';
import { selectMonitorRouteSaveState } from '../../store/monitor.selectors';
import { selectMonitorGroupName } from '../../store/monitor.selectors';
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
            (click)="backToGroup()"
            [disabled]="!(done$ | async)"
          >
            Back to group
          </button>
          <button
            mat-stroked-button
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
export class MonitorRouteSaveDialogComponent implements OnInit {
  saveState$ = this.store.select(selectMonitorRouteSaveState);
  saveRouteEnabled$ = this.state((state) => state.saveRouteEnabled);
  saveRouteStatus$ = this.state((state) => state.saveRouteStatus);
  uploadGpxEnabled$ = this.state((state) => state.uploadGpxEnabled);
  uploadGpxStatus$ = this.state((state) => state.uploadGpxStatus);
  analyzeEnabled$ = this.state((state) => state.analyzeEnabled);
  analyzeStatus$ = this.state((state) => state.analyzeStatus);
  done$ = this.state((state) => state.done);

  constructor(
    private dialogRef: MatDialogRef<MonitorRouteSaveDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public parameters: MonitorRouteParameters,
    private monitorService: MonitorService,
    private store: Store<AppState>,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.store.dispatch(
      actionMonitorRouteSaveInit({ parameters: this.parameters })
    );
  }

  backToGroup(): void {
    this.store
      .select(selectMonitorGroupName)
      .pipe(first())
      .subscribe((groupName) => {
        const url = `/monitor/groups/${groupName}`;
        this.router.navigateByUrl(url);
        this.dialogRef.close();
      });
  }

  gotoAnalysisResult(): void {
    this.store
      .select(selectMonitorGroupName)
      .pipe(first())
      .subscribe((groupName) => {
        const routeName = this.parameters.properties.name;
        const url = `/monitor/groups/${groupName}/routes/${routeName}/map`;
        this.router.navigateByUrl(url);
        this.dialogRef.close();
      });
  }

  private state<T>(project: (MonitorRouteSaveState) => T): Observable<T> {
    return this.saveState$.pipe(map(project));
  }
}
