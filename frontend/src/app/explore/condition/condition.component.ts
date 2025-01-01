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
import { ConditionForm } from './condition-controls';
import { ConditionLocationComponent } from './condition-location.component';
import { ConditionNameComponent } from './condition-name.component';
import { ConditionTagComponent } from './condition-tag.component';

@Component({
  selector: 'kpn-condition',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="card-wrapper">
      <mat-card appearance="outlined">
        <div class="condition-line kpn-small-spacer-above">
          @let subject = form().controls.subject;
          <mat-form-field appearance="outline" class="subject">
            <mat-label>condition</mat-label>
            <mat-select [formControl]="subject">
              <mat-option value="location">location</mat-option>
              <mat-option value="tag">tag</mat-option>
              <mat-option value="name">name</mat-option>
            </mat-select>
          </mat-form-field>

          @if (subject.value === 'tag') {
            <kpn-condition-tag [form]="form().controls.tag" />
          }
          @if (subject.value === 'location') {
            <kpn-condition-location [form]="form().controls.location" />
          }
          @if (subject.value === 'name') {
            <kpn-condition-name [form]="form().controls.name" />
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
    ConditionLocationComponent,
    ConditionNameComponent,
    ConditionTagComponent,
  ],
})
export class ConditionComponent {
  form = input.required<ConditionForm>();
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
