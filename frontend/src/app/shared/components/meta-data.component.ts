import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MetaData } from '@api/common/data/meta-data';
import { TimestampComponent } from './timestamp/timestamp.component';

@Component({
  selector: 'ui-meta-data',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    @if (metaData()) {
      <div class="meta">
        <div>v{{ metaData().version }}:{{ metaData().changeSetId }}</div>
        <ui-timestamp [timestamp]="metaData().timestamp" />
      </div>
    }
  `,
  styleUrl: './meta-data.component.scss',
  imports: [TimestampComponent],
})
export class MetaDataComponent {
  readonly metaData = input.required<MetaData>();
}
