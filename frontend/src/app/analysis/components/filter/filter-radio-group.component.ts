import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { Translations } from '@app/i18n';
import { FilterOption } from '@app/kpn/filter';
import { FilterOptionGroup } from '@app/kpn/filter';

@Component({
  selector: 'kpn-filter-radio-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <div class="group-name">{{ groupName() }}</div>
      <mat-radio-group [value]="selection()" (change)="selectionChanged($event)">
        @for (option of group().options; track $index) {
          <mat-radio-button [value]="option.name" [disabled]="option.count === 0">
            <div class="filter-option">
              <span class="option-name">{{ optionName(option) }}</span>
              <span class="option-count">{{ option.count }}</span>
            </div>
          </mat-radio-button>
        }
      </mat-radio-group>
    </div>
  `,
  styleUrl: './filter.scss',
  standalone: true,
  imports: [MatRadioModule],
})
export class FilterRadioGroupComponent {
  group = input.required<FilterOptionGroup>();

  selection() {
    const selectedOption = this.group().options.find((option) => option.selected);
    return selectedOption == null ? null : selectedOption.name;
  }

  selectionChanged(event: MatRadioChange) {
    const option = this.group().options.find((o) => o.name === event.value);
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
