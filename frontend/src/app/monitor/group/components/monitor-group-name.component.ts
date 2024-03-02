import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'kpn-monitor-group-name',
  template: `
    <mat-form-field>
      <mat-label i18n="@@monitor.group.name.label">Name</mat-label>
      <input matInput [formControl]="name()" class="name" required />
    </mat-form-field>

    @if (name().invalid && (name().dirty || name().touched || ngForm().submitted)) {
      <div class="kpn-form-error">
        @if (name().errors?.required) {
          <div i18n="@@monitor.group.name.required">Name is required.</div>
        }
        @if (name().errors?.maxlength) {
          <div i18n="@@monitor.group.name.maxlength">
            Too long (max= {{ name().errors.maxlength.requiredLength }}, actual={{
              name().errors.maxlength.actualLength
            }}).
          </div>
        }
        @if (name().errors?.groupNameNonUnique) {
          <div i18n="@@monitor.group.name.unique">
            Name should be unique. A group with this name already exists.
          </div>
        }
      </div>
    }
  `,
  styles: `
    :host {
      display: block;
    }
    .name {
      width: 8em;
    }
  `,
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule],
})
export class MonitorGroupNameComponent {
  ngForm = input.required<FormGroupDirective>();
  name = input.required<FormControl<string>>();
}
