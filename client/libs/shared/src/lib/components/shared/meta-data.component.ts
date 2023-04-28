import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MetaData } from '@api/common/data';
import { TimestampComponent } from './timestamp/timestamp.component';

@Component({
  selector: 'kpn-meta-data',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div *ngIf="metaData" class="meta">
      <div>v{{ metaData.version }}:{{ metaData.changeSetId }}</div>
      <kpn-timestamp [timestamp]="metaData.timestamp" />
    </div>
  `,
  styleUrls: ['./meta-data.component.scss'],
  standalone: true,
  imports: [NgIf, TimestampComponent],
})
export class MetaDataComponent {
  @Input() metaData: MetaData;
}
