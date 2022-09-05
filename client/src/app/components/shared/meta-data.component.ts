import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MetaData } from '@api/common/data/meta-data';

@Component({
  selector: 'kpn-meta-data',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div *ngIf="metaData" class="meta">
      <div>v{{ metaData.version }}:{{ metaData.changeSetId }}</div>
      <kpn-timestamp [timestamp]="metaData.timestamp"></kpn-timestamp>
    </div>
  `,
  styleUrls: ['./meta-data.component.scss'],
})
export class MetaDataComponent {
  @Input() metaData: MetaData;
}
