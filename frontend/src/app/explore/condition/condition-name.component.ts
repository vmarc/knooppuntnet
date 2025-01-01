import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { ConditionRouteNameForm } from './condition-controls';

@Component({
  selector: 'kpn-condition-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-form-field appearance="outline" class="operator">
      <mat-label>operation</mat-label>
      <mat-select [formControl]="form().controls.operator">
        <mat-option value="equals">equals</mat-option>
        <mat-option value="contains">contains</mat-option>
      </mat-select>
    </mat-form-field>
    <mat-form-field appearance="outline">
      <mat-label>route name</mat-label>
      <input matInput [formControl]="form().controls.name" />
    </mat-form-field>
  `,
  styles: `
    .operator {
      width: 8em;
    }

    form {
      display: flex;
      align-items: center;
      gap: 0.5em;
    }
  `,
  imports: [
    MatLabel,
    MatFormField,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    MatOption,
    MatSelect,
  ],
})
export class ConditionNameComponent {
  form = input.required<ConditionRouteNameForm>();
}
