import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatAutocompleteTrigger } from '@angular/material/autocomplete';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { MatIconButton } from '@angular/material/button';
import { MatCardContent } from '@angular/material/card';
import { MatCard } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'kpn-search-condition',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div style="padding-top: 0.5em;">
      <mat-card appearance="outlined">
        <mat-card-content>
          <div class="condition-line kpn-small-spacer-above">
            <mat-form-field appearance="outline">
              <mat-label>condition</mat-label>
              <mat-select [formControl]="conditionType">
                <mat-option value="location">location</mat-option>
                <mat-option value="tag">tag</mat-option>
                <mat-option value="route-name">route name</mat-option>
              </mat-select>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>tag key</mat-label>
              <input
                type="text"
                placeholder="Pick one"
                matInput
                [formControl]="myControl"
                [matAutocomplete]="auto"
              />
              <mat-autocomplete autoActiveFirstOption #auto="matAutocomplete">
                <mat-option value="operator">operator</mat-option>
                <mat-option value="symbol">symbol</mat-option>
                <mat-option value="option3">option3</mat-option>
                <mat-option value="option4">option4</mat-option>
              </mat-autocomplete>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>operation</mat-label>
              <mat-select [formControl]="operation">
                <mat-option value="equals">equals</mat-option>
                <mat-option value="contains">contains</mat-option>
              </mat-select>
            </mat-form-field>
            <mat-form-field appearance="outline">
              <mat-label>Tag value</mat-label>
              <input matInput />
            </mat-form-field>
            <button mat-icon-button>
              <mat-icon svgIcon="remove"></mat-icon>
            </button>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: `
    .condition-line {
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
    MatIcon,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatOption,
    MatSelect,
    MatIconButton,
    MatCard,
    MatCardContent,
  ],
})
export class SearchConditionComponent {
  myControl = new FormControl('');
  conditionType = new FormControl<string>('tag');
  operation = new FormControl<string>('contains');
}
