import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Translations } from '@app/shared/i18n/translations';
import { FilterOption } from '@app/shared/kpn/filter/filter-option';
import { FilterOptionGroup } from '@app/shared/kpn/filter/filter-option-group';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'ui-filter-radio-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <div class="group-name">{{ groupName() }}</div>
      <nz-radio-group [ngModel]="selection()" (ngModelChange)="selectionChanged($event)">
        @for (option of group().options; track option.name) {
          <div>
            <label nz-radio [nzValue]="option.name" [nzDisabled]="option.count === 0">
              <div class="filter-option">
                <span class="option-name">{{ optionName(option) }}</span>
                <span class="option-count">{{ option.count }}</span>
              </div>
            </label>
          </div>
        }
      </nz-radio-group>
    </div>
  `,
  styleUrl: './filter.scss',
  imports: [NzRadioComponent, NzRadioGroupComponent, FormsModule],
})
export class FilterRadioGroupComponent {
  readonly group = input.required<FilterOptionGroup>();

  selection() {
    const selectedOption = this.group().options.find((option) => option.selected);
    return selectedOption == null ? null : selectedOption.name;
  }

  selectionChanged(value: string) {
    const option = this.group().options.find((o) => o.name === value);
    if (option) {
      option.updateState();
    }
  }

  groupName(): string {
    return Translations.get(`filter.${this.group().name}`);
  }

  optionName(option: FilterOption): string {
    return Translations.get(`filter.${option.name}`);
  }
}
