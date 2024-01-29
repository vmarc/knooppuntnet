import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'kpn-monitor-group-description',
  template: `
    <mat-form-field class="description">
      <mat-label i18n="@@monitor.group.description.label">Description </mat-label>
      <input matInput [formControl]="description()" required />
    </mat-form-field>

    @if (
      description().invalid && (description().dirty || description().touched || ngForm().submitted)
    ) {
      <div class="kpn-form-error">
        @if (description().errors?.required) {
          <div i18n="@@monitor.group.description.required">Description is required.</div>
        }

        @if (description().errors?.maxlength) {
          <div i18n="@@monitor.group.description.maxlength">
            Too long (max=
            {{ description().errors.maxlength.requiredLength }}, actual={{
              description().errors.maxlength.actualLength
            }}).
          </div>
        }
      </div>
    }
  `,
  styles: `
    :host {
      display: block;
    }
    .description {
      width: 40em;
    }
  `,
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule],
})
export class MonitorGroupDescriptionComponent {
  ngForm = input.required<FormGroupDirective>();
  description = input.required<FormControl<string>>();
}
