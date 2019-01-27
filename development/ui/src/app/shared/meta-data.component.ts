import {Component, Input} from "@angular/core";
import {MetaData} from "../kpn/shared/data/meta-data";

@Component({
  selector: 'kpn-meta-data',
  template: `
    <div *ngIf="metaData" class="meta">
      <div>v{{metaData.version}}:{{metaData.changeSetId}}</div>
      <kpn-timestamp [timestamp]="metaData.timestamp"></kpn-timestamp>
    </div>
  `,
  styleUrls: [
    "./meta-data.component.scss"
  ]
})
export class MetaDataComponent {
  @Input() metaData: MetaData;
}
