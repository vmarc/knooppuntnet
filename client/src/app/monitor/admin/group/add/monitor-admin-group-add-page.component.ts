import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Validators} from '@angular/forms';
import {FormControl} from '@angular/forms';
import {MonitorGroup} from '@api/common/monitor/monitor-group';
import {Store} from '@ngrx/store';
import {AppState} from '../../../../core/core.state';
import {actionMonitorAddRouteGroup} from '../../../store/monitor.actions';

@Component({
  selector: 'kpn-monitor-admin-group-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>
      Monitor
    </h1>

    <kpn-page-menu>
      New group
    </kpn-page-menu>

    <div class="kpn-comment">
      <p>
        Create a new group containing routes to be monitored. Provide a short name (that will be
        used in the browser address), and a title for the group (probably describing who will
        be maintaining the route group).
      </p>
    </div>

    <form [formGroup]="form">
      <p>
        <mat-form-field>
          <mat-label>Name</mat-label>
          <input matInput [formControl]="name">
        </mat-form-field>
      </p>

      <p>
        <mat-form-field class="description">
          <mat-label>Description</mat-label>
          <input matInput [formControl]="description">
        </mat-form-field>
      </p>

      <div class="kpn-button-group">
        <button mat-stroked-button (click)="add()">Add group</button>
        <a routerLink="/monitor">Cancel</a>
      </div>

    </form>
  `,
  styles: [`
    .description {
      width: 40em;
    }

    .kpn-button-group {
      padding-top: 3em;
    }
  `]
})
export class MonitorAdminGroupAddPageComponent {

  readonly name = new FormControl('', [Validators.required]);
  readonly description = new FormControl('', [Validators.required]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description
  });

  constructor(private store: Store<AppState>) {
  }

  add(): void {
    const group: MonitorGroup = this.form.value;
    this.store.dispatch(actionMonitorAddRouteGroup({group}));
  }
}
