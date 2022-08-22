import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-step-1-name',
  template: `
    <p>
      <mat-form-field>
        <mat-label>Name</mat-label>
        <input matInput [formControl]="name" />
      </mat-form-field>
    </p>
    <p>
      <mat-form-field class="description">
        <mat-label>Description</mat-label>
        <input matInput [formControl]="description" />
      </mat-form-field>
    </p>
    <div class="kpn-button-group">
      <button mat-stroked-button matStepperNext>Next</button>
    </div>
  `,
})
export class MonitorRouteStep1NameComponent {
  @Input() form: FormGroup;
  @Input() name: FormControl<string>;
  @Input() description: FormControl<string>;
}
