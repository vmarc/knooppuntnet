import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/core.state';
import { actionMonitorGroupAdd } from '../../../store/monitor.actions';
import { urlFragmentValidator } from '../../../validator/url-fragment-validator';

@Component({
  selector: 'kpn-monitor-admin-group-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-monitor-admin-group-breadcrumb></kpn-monitor-admin-group-breadcrumb>

    <h1>Monitor - add group</h1>

    <div class="kpn-comment">
      <p>Create a new group containing routes to be monitored.</p>
      <p>
        Provide a short name (that will be used in the browser address), and a
        title for the group (probably describing who will be maintaining the
        route group).
      </p>
    </div>

    <form [formGroup]="form" class="kpn-form" #ngForm="ngForm">
      <kpn-monitor-admin-group-name [ngForm]="ngForm" [name]="name">
      </kpn-monitor-admin-group-name>
      <kpn-monitor-admin-group-description
        [ngForm]="ngForm"
        [description]="description"
      ></kpn-monitor-admin-group-description>
      <div class="kpn-form-buttons">
        <button mat-stroked-button (click)="add()">Add group</button>
        <a routerLink="/monitor">Cancel</a>
      </div>
    </form>
  `,
})
export class MonitorAdminGroupAddPageComponent {
  readonly name = new FormControl<string>('', [
    Validators.required,
    urlFragmentValidator,
    Validators.maxLength(15),
  ]);
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description,
  });

  constructor(private store: Store<AppState>) {}

  add(): void {
    if (this.form.valid) {
      this.store.dispatch(
        actionMonitorGroupAdd({ properties: this.form.value })
      );
    }
  }
}
