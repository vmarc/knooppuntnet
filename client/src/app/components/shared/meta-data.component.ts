import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {MetaData} from '../../kpn/api/common/data/meta-data';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-meta-data',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="metaData" class="meta">
      <div>v{{metaData.version}}:{{metaData.changeSetId}}</div>
      <kpn-timestamp [timestamp]="metaData.timestamp"></kpn-timestamp>
    </div>
  `,
  styleUrls: [
    './meta-data.component.scss'
  ]
})
export class MetaDataComponent {
  @Input() metaData: MetaData;
}
