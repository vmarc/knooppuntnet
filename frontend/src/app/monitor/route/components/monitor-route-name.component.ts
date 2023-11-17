import { NgIf } from '@angular/common';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'kpn-monitor-route-name',
  template: `
    <div>
      <mat-form-field>
        <mat-label i18n="@@monitor.route.name.label">Name</mat-label>
        <input matInput id="name" [formControl]="name" class="name" required />
      </mat-form-field>
      <div
        *ngIf="name.invalid && (name.dirty || name.touched || ngForm.submitted)"
        class="kpn-form-error"
      >
        <div *ngIf="name.errors?.required" i18n="@@monitor.route.name.required">
          Name is required.
        </div>
        <div
          *ngIf="name.errors?.maxlength"
          i18n="@@monitor.route.name.maxlength"
        >
          Too long (max= {{ name.errors.maxlength.requiredLength }}, actual={{
            name.errors.maxlength.actualLength
          }}).
        </div>
        <div
          *ngIf="name.errors?.routeNameNonUnique"
          i18n="@@monitor.route.name.unique"
        >
          The route name should be unique within the group. A route with this
          name already exists within this group.
        </div>
      </div>
    </div>
  `,
  styles: `
    .name {
      width: 8em;
    }
  `,
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, ReactiveFormsModule, NgIf],
})
export class MonitorRouteNameComponent {
  @Input({ required: true }) ngForm: FormGroupDirective;
  @Input({ required: true }) name: FormControl<string>;
}
