import { NgSwitchDefault } from '@angular/common';
import { NgSwitch } from '@angular/common';
import { NgSwitchCase } from '@angular/common';
import { NgFor } from '@angular/common';
import { NgIf } from '@angular/common';
import { inject } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';
import { MonitorRouteUpdate } from '@api/common/monitor/monitor-route-update';
import { MonitorWebsocketService } from '../../monitor-websocket.service';
import { MonitorRouteSaveStep } from '../monitor-route-save-step';

@Component({
  selector: 'kpn-monitor-route-form-save',
  template: `
    <div>
      <div *ngIf="command" class="kpn-spacer-below">
        Route: {{ routeName() }}
      </div>
      <div
        *ngFor="let step of steps(); trackBy: trackBySteps"
        class="kpn-line kpn-spacer-below"
      >
        <div class="icon">
          <mat-spinner
            *ngIf="step.status === 'busy'"
            diameter="20"
          ></mat-spinner>
          <mat-icon *ngIf="step.status === 'todo'" svgIcon="dot" class="todo" />
          <mat-icon
            *ngIf="step.status === 'done'"
            svgIcon="tick"
            class="done"
          />
        </div>
        <span>{{ step.description }}</span>
      </div>
    </div>

    <p *ngFor="let error of errors()">
      <ng-container [ngSwitch]="error">
        <span
          *ngSwitchCase="'no-relation-id'"
          i18n="@@monitor.route.save-dialog.no-relation-id"
        >
          Note: we cannot yet perform an analysis. The reference information is
          still incomplete. The relation id has not been specified.
        </span>
        <span
          *ngSwitchCase="'osm-relation-not-found'"
          i18n="@@monitor.route.save-dialog.osm-relation-not-found"
        >
          Note: we cannot yet perform an analysis. The reference information is
          still incomplete. No route with given relation id was found at given
          reference date.
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
  styles: [
    `
      .icon {
        width: 2em;
        height: 1.5em;
      }

      .done {
        color: green;
      }

      .todo {
        color: gray;
        width: 0.3em;
        height: 0.3em;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    NgFor,
    NgIf,
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
  ],
})
export class MonitorRouteFormSaveComponent {
  @Input({ required: true }) command: MonitorRouteUpdate;

  readonly #monitorWebsocketService = inject(MonitorWebsocketService);
  readonly #router = inject(Router);

  readonly steps = this.#monitorWebsocketService.steps;
  readonly errors = this.#monitorWebsocketService.errors;
  readonly done = this.#monitorWebsocketService.done;

  trackBySteps(index: number, step: MonitorRouteSaveStep): string {
    return `${step.stepId}-${step.status}`;
  }

  backToGroup(): void {
    const url = `/monitor/groups/${this.groupName()}`;
    this.#router.navigateByUrl(url);
  }

  gotoAnalysisResult(): void {
    const url = `/monitor/groups/${this.groupName()}/routes/${this.routeName()}/map`;
    this.#router.navigateByUrl(url);
  }

  groupName(): string {
    let groupName = this.command.groupName;
    if (this.command.newGroupName) {
      groupName = this.command.newGroupName;
    }
    return groupName;
  }

  routeName(): string {
    let routeName = this.command.routeName;
    if (this.command.newRouteName) {
      routeName = this.command.newRouteName;
    }
    return routeName;
  }
}
