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
import { filter } from 'rxjs/operators';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteParameters } from './monitor-route-parameters';
import { MonitorRouteSaveDialogService } from './monitor-route-save-dialog.service';
import { MonitorRouteSaveStepComponent } from './monitor-route-save-step.component';

@Component({
  selector: 'kpn-monitor-route-save-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <ng-container *ngIf="service.state() as state">
        <div mat-dialog-title i18n="@@monitor.route.save-dialog.title">
          Save route
        </div>
        <div mat-dialog-content>
          <kpn-monitor-route-save-step
            [enabled]="state.saveRouteEnabled"
            [status]="state.saveRouteStatus"
            label="Save route definition"
          />

          <kpn-monitor-route-save-step
            [enabled]="state.uploadGpxEnabled"
            [status]="state.uploadGpxStatus"
            label="Upload GPX file"
          />

          <kpn-monitor-route-save-step
            [enabled]="state.analyzeEnabled"
            [status]="state.analyzeStatus"
            label="Route analysis"
          />

          <div class="done kpn-spacer-below">
            <span
              *ngIf="state.analyzeStatus === 'busy'"
              i18n="@@monitor.route.save-dialog.busy"
              >This may take a while, please wait...</span
            >
            <span
              *ngIf="state.done"
              id="route-saved"
              i18n="@@monitor.route.save-dialog.saved"
            >
              Route saved!
            </span>
          </div>

          <p *ngFor="let error of state.errors">
            <ng-container [ngSwitch]="error">
              <span
                *ngSwitchCase="'no-relation-id'"
                i18n="@@monitor.route.save-dialog.no-relation-id"
              >
                Note: we cannot yet perform an analysis. The reference
                information is still incomplete. The relation id has not been
                specified.
              </span>
              <span
                *ngSwitchCase="'osm-relation-not-found'"
                i18n="@@monitor.route.save-dialog.osm-relation-not-found"
              >
                Note: we cannot yet perform an analysis. The reference
                information is still incomplete. No route with given relation id
                was found at given reference date.
              </span>
              <span *ngSwitchDefault>
                {{ error }}
              </span>
            </ng-container>
          </p>
          <div class="kpn-button-group">
            <button
              mat-stroked-button
              id="back-to-group-button"
              (click)="backToGroup()"
              [disabled]="state.done === false"
              i18n="@@monitor.route.save-dialog.action.back"
            >
              Back to group
            </button>
            <button
              mat-stroked-button
              id="goto-analysis-result-button"
              (click)="gotoAnalysisResult()"
              [disabled]="state.done === false"
              i18n="@@monitor.route.save-dialog.action.analysis-result"
            >
              Go to analysis result
            </button>
          </div>
        </div>
      </ng-container>
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
  providers: [MonitorService],
  standalone: true,
  imports: [
    AsyncPipe,
    DialogComponent,
    MatButtonModule,
    MatDialogModule,
    MonitorRouteSaveStepComponent,
    NgFor,
    NgIf,
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
  ],
})
export class MonitorRouteSaveDialogComponent implements OnInit, OnDestroy {
  private readonly subscriptions = new Subscriptions();

  constructor(
    private dialogRef: MatDialogRef<MonitorRouteSaveDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public parameters: MonitorRouteParameters,
    private monitorService: MonitorService,
    protected service: MonitorRouteSaveDialogService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.closeDialogUponHttpError();
    this.service.init(this.parameters);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
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
}
