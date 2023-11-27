import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'kpn-monitor-route-description',
  template: `
    <div>
      <mat-form-field class="description">
        <mat-label i18n="@@monitor.route.description.label"
          >Description
        </mat-label>
        <input matInput id="description" [formControl]="description" required />
      </mat-form-field>

      @if (
        description.invalid &&
        (description.dirty || description.touched || ngForm.submitted)
      ) {
        <div class="kpn-form-error">
          @if (description.errors?.required) {
            <div i18n="@@monitor.route.description.required">
              Description is required.
            </div>
          }

          @if (description.errors?.maxlength) {
            <div i18n="@@monitor.route.description.maxlength">
              Too long (max=
              {{ description.errors.maxlength.requiredLength }}, actual={{
                description.errors.maxlength.actualLength
              }}).
            </div>
          }
        </div>
      }
    </div>
  `,
  styles: `
    .description {
      width: 40em;
    }
  `,
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule],
})
export class MonitorRouteDescriptionComponent {
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) description: FormControl<string>;
}
