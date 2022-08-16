import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-admin-group-name',
  template: `
    <div>
      <mat-form-field>
        <mat-label>Name</mat-label>
        <input matInput [formControl]="_id" class="name" required />
      </mat-form-field>
      <div
        *ngIf="_id.invalid && (_id.dirty || _id.touched || ngForm.submitted)"
        class="kpn-form-error"
      >
        <div *ngIf="_id.errors?.['required']">Name is required.</div>
        <div *ngIf="_id.errors?.['urlFragmentInvalid']">
          Invalid name: only use alphanumeric characters and dashes.
        </div>
        <div *ngIf="_id.errors?.['maxlength']">
          Too long (max= {{ _id.errors.maxlength.requiredLength }}, actual={{
            _id.errors.maxlength.actualLength
          }}).
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .name {
        width: 8em;
      }
    `,
  ],
})
export class MonitorAdminGroupNameComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() _id: FormControl<string>;
}
