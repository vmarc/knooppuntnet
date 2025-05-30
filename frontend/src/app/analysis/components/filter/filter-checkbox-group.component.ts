import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Translations } from '@app/shared/i18n/translations';
import { FilterOption } from '@app/shared/kpn/filter/filter-option';
import { FilterOptionGroup } from '@app/shared/kpn/filter/filter-option-group';

@Component({
  selector: 'ui-filter-checkbox-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <div class="group-name">{{ groupName() }}</div>
      @for (option of group().options; track $index) {
        <mat-checkbox [checked]="isSelected()" (change)="selectedChanged()">
          {{ optionName(option) }}<span class="option-count">{{ option.count }}</span>
        </mat-checkbox>
      }
    </div>
  `,
  imports: [MatCheckboxModule],
})
export class FilterCheckboxGroupComponent {
  group = input<FilterOptionGroup>();

  isSelected() {
    return false;
  }

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  selectedChanged() {}

  groupName(): string {
    return Translations.get(`filter.${this.group().name}`);
  }

  optionName(option: FilterOption): string {
    return Translations.get(`filter.${option.name}`);
  }
}
