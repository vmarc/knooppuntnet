import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteSaveStep } from '../monitor-route-save-step';
import { MonitorRouteFormErrorsComponent } from './monitor-route-form-errors.component';
import { MonitorRouteFormSaveStepComponent } from './monitor-route-form-save-step.component';

@Component({
  selector: 'kpn-monitor-route-form-save',
  template: `
    <div>
      @if (command()) {
        <div class="kpn-spacer-below">Route: {{ routeName() }}</div>
      }
      @for (step of steps(); track trackByStep(step)) {
        <kpn-monitor-route-form-save-step [step]="step" />
      }
    </div>

    <kpn-monitor-route-form-errors [errors]="errors()" />

    <div class="kpn-button-group">
      <button
        mat-stroked-button
        id="back-to-group-button"
        (click)="backToGroup()"
        [disabled]="done() === false"
        i18n="@@monitor.route.save-dialog.action.back"
      >
        Back to group
      </button>
      <button
        mat-stroked-button
        id="goto-analysis-result-button"
        (click)="gotoAnalysisResult()"
        [disabled]="done() === false"
        i18n="@@monitor.route.save-dialog.action.analysis-result"
      >
        Go to analysis result
      </button>
    </div>
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MonitorRouteFormSaveStepComponent,
    MonitorRouteFormErrorsComponent,
  ],
})
export class MonitorRouteFormSaveComponent {
  command = input.required<MonitorRouteUpdate>();

  private readonly monitorWebsocketService = inject(MonitorWebsocketService);
  private readonly router = inject(Router);

  readonly steps = this.monitorWebsocketService.steps;
  readonly errors = this.monitorWebsocketService.errors;
  readonly done = this.monitorWebsocketService.done;

  trackByStep(step: MonitorRouteSaveStep): string {
    return `${step.stepId}-${step.status}`;
  }

  backToGroup(): void {
    const url = `/monitor/groups/${this.command().groupName}`;
    this.router.navigateByUrl(url);
  }

  gotoAnalysisResult(): void {
    const url = `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/map`;
    this.router.navigateByUrl(url);
  }

  groupName(): string {
    let groupName = this.command().groupName;
    if (this.command().newGroupName) {
      groupName = this.command().newGroupName;
    }
    return groupName;
  }

  routeName(): string {
    let routeName = this.command().routeName;
    if (this.command().newRouteName) {
      routeName = this.command().newRouteName;
    }
    return routeName;
  }
}
