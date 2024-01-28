import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MetaData } from '@api/common/data';
import { TimestampComponent } from './timestamp/timestamp.component';

@Component({
  selector: 'kpn-meta-data',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    @if (metaData()) {
      <div class="meta">
        <div>v{{ metaData().version }}:{{ metaData().changeSetId }}</div>
        <kpn-timestamp [timestamp]="metaData().timestamp" />
      </div>
    }
  `,
  styleUrl: './meta-data.component.scss',
  standalone: true,
  imports: [TimestampComponent],
})
export class MetaDataComponent {
  metaData = input.required<MetaData>();
}
