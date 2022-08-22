import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-group-name',
  template: `
    <div>
      <mat-form-field>
        <mat-label>Name</mat-label>
        <input matInput [formControl]="name" class="name" required />
      </mat-form-field>
      <div
        *ngIf="name.invalid && (name.dirty || name.touched || ngForm.submitted)"
        class="kpn-form-error"
      >
        <div *ngIf="name.errors?.required">Name is required.</div>
        <div *ngIf="name.errors?.urlFragmentInvalid">
          Invalid name: only use alphanumeric characters and dashes.
        </div>
        <div *ngIf="name.errors?.maxlength">
          Too long (max= {{ name.errors.maxlength.requiredLength }}, actual={{
            name.errors.maxlength.actualLength
          }}).
        </div>
        <div *ngIf="name.errors?.groupNameNonUnique">
          Name should be unique. A group with this name already exists.
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
export class MonitorGroupNameComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() name: FormControl<string>;
}
