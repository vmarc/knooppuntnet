import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'kpn-monitor-group-description',
  template: `
    <div>
      <mat-form-field class="description">
        <mat-label i18n="@@monitor.group.description.label"
          >Description
        </mat-label>
        <input matInput [formControl]="description" required />
      </mat-form-field>

      <div
        *ngIf="
          description.invalid &&
          (description.dirty || description.touched || ngForm.submitted)
        "
        class="kpn-form-error"
      >
        <div
          *ngIf="description.errors?.required"
          i18n="@@monitor.group.description.required"
        >
          Description is required.
        </div>
        <div
          *ngIf="description.errors?.maxlength"
          i18n="@@monitor.group.description.maxlength"
        >
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
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, NgIf],
})
export class MonitorGroupDescriptionComponent {
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) description: FormControl<string>;
}
