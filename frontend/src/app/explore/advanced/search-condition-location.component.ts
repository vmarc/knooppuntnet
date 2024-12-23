import { computed } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { Condition } from '@api/common/search/condition';

@Component({
  selector: 'kpn-search-condition-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    {{ conditionString() }}
    <mat-form-field appearance="outline">
      <mat-label>Name</mat-label>
      <input matInput [value]="name()" />
    </mat-form-field>
  `,
  styles: ``,
  imports: [MatLabel, MatFormField, MatInput, FormsModule, ReactiveFormsModule],
})
export class SearchConditionLocationComponent {
  condition = input.required<Condition>();
  conditionString = computed(() => JSON.stringify(this.condition));
  name = computed(() => this.condition().location?.name);
}
