import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';
import { ServerFilterGroup } from '@api/common/changes/filter/server-filter-group';
import { Fact } from '@api/common/fact';
import { FactNameComponent } from '@app/analysis/fact/components/fact-name.component';
import { Translations } from '@app/shared/i18n/translations';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'ui-location-filter-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="filter">
      <div class="title">{{ translate(title()) }}</div>
      <nz-radio-group [ngModel]="filterGroup().selected" (ngModelChange)="selectionChanged($event)">
        @for (option of filterGroup().options; track option.name) {
          <div>
            <label nz-radio [nzValue]="option.name">
              @if (option.name === 'all') {
                {{ translate(option.name) }}
              } @else {
                <ui-fact-name [fact]="toFact(option.name)" />
              }
              <span class="kpn-brackets">{{ option.count }}</span>
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
  imports: [FactNameComponent, FormsModule, NzRadioComponent, NzRadioGroupComponent],
})
export class LocationFilterFactComponent {
  title = input.required<string>();
  filterGroup = input.required<ServerFilterGroup>();
  changed = output<Fact | null>();

  translate(option: string): string {
    return Translations.get(`filter.${option}`);
  }

  selectionChanged(change: MatRadioChange): void {
    if (change.value == 'all') {
      this.changed.emit(null);
    } else {
      this.changed.emit(change.value as Fact);
    }
  }

  toFact(factName: string): Fact {
    return factName as Fact;
  }
}
