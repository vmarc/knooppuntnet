import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'kpn-monitor-group-name',
  template: `
    <div>
      <mat-form-field>
        <mat-label i18n="@@monitor.group.name.label">Name</mat-label>
        <input matInput [formControl]="name" class="name" required />
      </mat-form-field>
      <div
        *ngIf="name.invalid && (name.dirty || name.touched || ngForm.submitted)"
        class="kpn-form-error"
      >
        <div *ngIf="name.errors?.required" i18n="@@monitor.group.name.required">
          Name is required.
        </div>
        <div
          *ngIf="name.errors?.maxlength"
          i18n="@@monitor.group.name.maxlength"
        >
          Too long (max= {{ name.errors.maxlength.requiredLength }}, actual={{
            name.errors.maxlength.actualLength
          }}).
        </div>
        <div
          *ngIf="name.errors?.groupNameNonUnique"
          i18n="@@monitor.group.name.unique"
        >
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
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, NgIf],
})
export class MonitorGroupNameComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() name: FormControl<string>;
}
