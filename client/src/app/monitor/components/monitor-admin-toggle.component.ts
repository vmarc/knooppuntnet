import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
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
        [disabled]="adminDisabled$ | async"
        [checked]="admin$ | async"
        (change)="adminChanged($event)"
        >Admin
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
  readonly adminDisabled$ = this.store
    .select(selectMonitorAdminRole)
    .pipe(map((adminRole) => !adminRole));

  constructor(private store: Store<AppState>) {}

  adminChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionMonitorAdmin({ admin: event.checked }));
  }
}
