import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { ServerFilterGroup } from '@api/common/changes/filter/server-filter-group';
import { Translations } from '@app/shared/i18n/translations';

@Component({
  selector: 'kpn-location-filter-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="filter">
      <div class="title">{{ translate(title()) }}</div>
      <mat-radio-group [value]="filterGroup().selected" (change)="selectionChanged($event)">
        @for (option of filterGroup().options; track option.name) {
          <div>
            <mat-radio-button [value]="option.name">
              <span>{{ translate(option.name) }}</span
              ><span class="kpn-brackets">{{ option.count }}</span>
            </mat-radio-button>
          </div>
        }
      </mat-radio-group>
    </div>
  `,
  styles: `
    .filter {
      padding: 25px 15px 25px 25px;
    }

    .title {
      padding-bottom: 10px;
    }
  `,
  imports: [MatRadioModule],
})
export class LocationFilterGroupComponent {
  title = input.required<string>();
  filterGroup = input.required<ServerFilterGroup>();
  changed = output<string | null>();

  translate(option: string): string {
    return Translations.get(`filter.${option}`);
  }

  selectionChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.changed.emit(null);
    } else {
      this.changed.emit(change.value);
    }
  }
}
