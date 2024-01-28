import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { I18nService } from '@app/i18n';
import { FilterOption } from '@app/kpn/filter';
import { FilterOptionGroup } from '@app/kpn/filter';

@Component({
  selector: 'kpn-filter-checkbox-group',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    <div>
      <div class="group-name">{{ groupName() }}</div>
      @for (option of group().options; track $index) {
        <mat-checkbox [checked]="isSelected()" (change)="selectedChanged($event)">
          {{ optionName(option) }}<span class="option-count">{{ option.count }}</span>
        </mat-checkbox>
      }
    </div>
  `,
  standalone: true,
  imports: [MatCheckboxModule],
})
export class FilterCheckboxGroupComponent {
  group = input<FilterOptionGroup | undefined>();

  private readonly i18nService = inject(I18nService);

  isSelected() {
    return false;
  }

  selectedChanged(event: MatCheckboxChange) {}

  groupName(): string {
    return this.i18nService.translation(`@@filter.${this.group().name}`);
  }

  optionName(option: FilterOption): string {
    return this.i18nService.translation(`@@filter.${option.name}`);
  }
}
