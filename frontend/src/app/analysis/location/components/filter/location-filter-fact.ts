import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { ServerFilterGroup } from '@api/common/changes/filter';
import { Fact } from '@api/custom';
import { FactNameComponent } from '@app/analysis/fact';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { Translations } from '@app/i18n';

@Component({
  selector: 'kpn-location-filter-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="filter">
      <div class="title">{{ translate(title()) }}</div>
      <mat-radio-group [value]="filterGroup().selected" (change)="selectionChanged($event)">
        @for (option of filterGroup().options; track option.name) {
          <div>
            <mat-radio-button [value]="option.name">
              @if (option.name === 'all') {
                {{ translate(option.name) }}
              } @else {
                <kpn-fact-name [fact]="option.name" />
              }
              <span class="kpn-brackets">{{ option.count }}</span>
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
  standalone: true,
  imports: [SidebarComponent, MatRadioModule, FactNameComponent],
})
export class LocationFilterFactComponent {
  title = input.required<string>();
  filterGroup = input.required<ServerFilterGroup>();
  @Output() changed = new EventEmitter<Fact | null>();

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
}
