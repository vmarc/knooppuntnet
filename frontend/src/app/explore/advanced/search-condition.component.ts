import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatIconButton } from '@angular/material/button';
import { MatCard } from '@angular/material/card';
import { MatIcon } from '@angular/material/icon';
import { MatSelect } from '@angular/material/select';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';
import { SearchConditionLocationComponent } from './search-condition-location.component';
import { SearchConditionNameComponent } from './search-condition-name.component';
import { SearchConditionTagComponent } from './search-condition-tag.component';
import { SearchService } from './search.service';

@Component({
  selector: 'kpn-search-condition',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div style="padding-top: 0.5em;">
      <mat-card appearance="outlined">
        <div class="condition-line kpn-small-spacer-above" style="padding-left: 0.5em;">
          <mat-form-field appearance="outline">
            <mat-label>condition</mat-label>
            <mat-select [value]="condition().subject">
              <mat-option value="location">location</mat-option>
              <mat-option value="tag">tag</mat-option>
              <mat-option value="name">route name</mat-option>
            </mat-select>
          </mat-form-field>

          @if (condition().subject === 'tag') {
            <kpn-search-condition-tag [condition]="condition()" />
          }
          @if (condition().subject === 'location') {
            <kpn-search-condition-location [condition]="condition()" />
          }
          @if (condition().subject === 'name') {
            <kpn-search-condition-name [condition]="condition()" />
          }

          <button mat-icon-button>
            <mat-icon svgIcon="remove" (click)="remove()"></mat-icon>
          </button>
        </div>
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
    FormsModule,
    ReactiveFormsModule,
    MatIcon,
    MatOption,
    MatSelect,
    MatIconButton,
    MatCard,
    SearchConditionTagComponent,
    SearchConditionLocationComponent,
    SearchConditionNameComponent,
  ],
})
export class SearchConditionComponent {
  private readonly service = inject(SearchService);

  condition = input.required<Condition>();
  indexes = input.required<number[]>();

  remove() {
    this.service.removeCondition(this.indexes());
  }
}
