import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { actionMonitorAdmin } from '../store/monitor.actions';
import { selectMonitorAdminRole } from '../store/monitor.selectors';
import { selectMonitorAdmin } from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-admin-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toggle">
      <mat-slide-toggle
        [disabled]="(adminRole$ | async) === false"
        [checked]="admin$ | async"
        (change)="adminChanged($event)"
        i18n="@@monitor.admin-toggle"
      >
        Admin
      </mat-slide-toggle>
    </div>
  `,
  styles: [
    `
      .toggle {
        padding-top: 0.5em;
        padding-bottom: 0.5em;
        display: flex;
        justify-content: flex-end;
      }
    `,
  ],
})
export class MonitorAdminToggleComponent {
  readonly admin$ = this.store.select(selectMonitorAdmin);
  readonly adminRole$ = this.store.select(selectMonitorAdminRole);

  constructor(private store: Store<AppState>) {}

  adminChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionMonitorAdmin({ admin: event.checked }));
  }
}
