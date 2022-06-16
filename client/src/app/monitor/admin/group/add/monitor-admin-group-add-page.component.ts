import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { UntypedFormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { UntypedFormControl } from '@angular/forms';
import { MonitorGroup } from '@api/common/monitor/monitor-group';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/core.state';
import { actionMonitorGroupAdd } from '../../../store/monitor.actions';
import { urlFragmentValidator } from '../../../validator/url-fragment-validator';

@Component({
  selector: 'kpn-monitor-admin-group-add-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/monitor">Monitor</a></li>
      <li>Group</li>
    </ul>

    <h1>Monitor</h1>

    <h2>New group</h2>

    <div class="kpn-comment">
      <p>
        Create a new group containing routes to be monitored. Provide a short
        name (that will be used in the browser address), and a title for the
        group (probably describing who will be maintaining the route group).
      </p>
    </div>

    <form [formGroup]="form" class="kpn-form" #ngForm="ngForm">
      <div>
        <mat-form-field>
          <mat-label>Name</mat-label>
          <input matInput [formControl]="_id" class="name" required />
        </mat-form-field>
        <div
          *ngIf="_id.invalid && (_id.dirty || _id.touched || ngForm.submitted)"
          class="error"
        >
          <div *ngIf="_id.errors?.['required']">Name is required.</div>
          <div *ngIf="_id.errors?.['url-fragment-invalid']">
            Invalid name: only use alphanumeric characters and dashes.
          </div>
          <div *ngIf="_id.errors?.['maxlength']">
            Too long (max= {{ _id.errors.maxlength.requiredLength }}, actual={{
              _id.errors.maxlength.actualLength
            }}).
          </div>
        </div>
      </div>

      <div>
        <mat-form-field class="description">
          <mat-label>Description</mat-label>
          <input matInput [formControl]="description" required />
        </mat-form-field>
        <div
          *ngIf="
            description.invalid &&
            (description.dirty || description.touched || ngForm.submitted)
          "
          class="error"
        >
          <div *ngIf="description.errors?.['required']">
            Description is required.
          </div>
          <div *ngIf="description.errors?.['maxlength']">
            Too long (max= {{ description.errors.maxlength.requiredLength }},
            actual={{ description.errors.maxlength.actualLength }}).
          </div>
        </div>
      </div>

      <div class="kpn-form-buttons">
        <button mat-stroked-button (click)="add()">Add group</button>
        <a routerLink="/monitor">Cancel</a>
      </div>
    </form>
  `,
  styles: [
    `
      .description {
        width: 40em;
      }

      .name {
        width: 8em;
      }

      .error {
        color: red;
        font-size: 0.8em;
        padding-bottom: 3em;
      }
    `,
  ],
})
export class MonitorAdminGroupAddPageComponent {
  readonly _id = new UntypedFormControl('', [
    Validators.required,
    urlFragmentValidator,
    Validators.maxLength(15),
  ]);
  readonly description = new UntypedFormControl('', [
    Validators.required,
    Validators.maxLength(100),
  ]);

  readonly form = new UntypedFormGroup({
    _id: this._id,
    description: this.description,
  });

  constructor(private store: Store<AppState>) {}

  add(): void {
    if (this.form.valid) {
      const group: MonitorGroup = this.form.value;
      this.store.dispatch(actionMonitorGroupAdd({ group }));
    }
  }
}
