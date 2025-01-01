import { output } from '@angular/core';
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
import { ConditionSubject } from '@api/common/search/condition-subject';
import { ExploreState } from '@app/state';
import { SearchConditionLocationComponent } from './search-condition-location.component';
import { SearchConditionNameComponent } from './search-condition-name.component';
import { SearchConditionTagComponent } from './search-condition-tag.component';

@Component({
  selector: 'kpn-search-condition',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="card-wrapper">
      <mat-card appearance="outlined">
        <div class="condition-line kpn-small-spacer-above">
          <mat-form-field appearance="outline" class="subject">
            <mat-label>condition</mat-label>
            <mat-select
              [value]="condition().subject"
              (selectionChange)="onSubjectChange($event.value)"
            >
              <mat-option value="location">location</mat-option>
              <mat-option value="tag">tag</mat-option>
              <mat-option value="name">name</mat-option>
            </mat-select>
          </mat-form-field>

          @if (condition().subject === 'tag') {
            <kpn-search-condition-tag
              [condition]="condition()"
              (conditionChange)="onConditionChange($event)"
            />
          }
          @if (condition().subject === 'location') {
            <kpn-search-condition-location
              [condition]="condition()"
              (conditionChange)="onConditionChange($event)"
            />
          }
          @if (condition().subject === 'name') {
            <kpn-search-condition-name
              [condition]="condition()"
              (conditionChange)="onConditionChange($event)"
            />
          }

          <button mat-icon-button>
            <mat-icon svgIcon="remove" (click)="onRemove()"></mat-icon>
          </button>
        </div>
      </mat-card>
    </div>
  `,
  styles: `
    .card-wrapper {
      padding-top: 0.5em;
    }

    .condition-line {
      display: flex;
      padding-left: 0.5em;
      gap: 0.5em;
    }

    .subject {
      width: 8em;
      min-width: 8em;
    }

    button {
      margin-top: 0.4em;
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
  condition = input.required<Condition>();
  update = output<Condition>();
  remove = output<void>();

  onSubjectChange(subject: ConditionSubject): void {
    const updated = ExploreState.defaultConditionSubject(subject);
    this.update.emit(updated);
  }

  onConditionChange(value: Condition): void {
    this.update.emit(value);
  }

  onRemove() {
    this.remove.emit();
  }
}
