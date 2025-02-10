import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ServerFilterGroup } from '@api/common/changes/filter/server-filter-group';
import { Translations } from '@app/shared/i18n/translations';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'kpn-location-filter-group',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="filter">
      <div class="title">{{ translate(title()) }}</div>
      <nz-radio-group [ngModel]="filterGroup().selected" (ngModelChange)="selectionChanged($event)">
        @for (option of filterGroup().options; track option.name) {
          <div>
            <label nz-radio [nzValue]="option.name">
              <span>{{ translate(option.name) }}</span
              ><span class="kpn-brackets">{{ option.count }}</span>
            </label>
          </div>
        }
      </nz-radio-group>
    </div>
  `,
  styles: `
    .filter {
      padding: 1em;
    }

    .title {
      padding-bottom: 1em;
    }
  `,
  imports: [NzRadioComponent, NzRadioGroupComponent, FormsModule],
})
export class LocationFilterGroupComponent {
  title = input.required<string>();
  filterGroup = input.required<ServerFilterGroup>();
  changed = output<string | null>();

  translate(option: string): string {
    return Translations.get(`filter.${option}`);
  }

  selectionChanged(value: string): void {
    if (value == 'all') {
      this.changed.emit(null);
    } else {
      this.changed.emit(value);
    }
  }
}
