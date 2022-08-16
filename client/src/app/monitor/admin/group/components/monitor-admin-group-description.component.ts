import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-admin-group-description',
  template: `
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
        class="kpn-form-error"
      >
        <div *ngIf="description.errors?.['required']">
          Description is required.
        </div>
        <div *ngIf="description.errors?.['maxlength']">
          Too long (max=
          {{ description.errors.maxlength.requiredLength }}, actual={{
            description.errors.maxlength.actualLength
          }}).
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .description {
        width: 40em;
      }
    `,
  ],
})
export class MonitorAdminGroupDescriptionComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() description: FormControl<string>;
}
