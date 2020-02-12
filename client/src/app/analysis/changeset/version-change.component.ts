import {Component, Input} from "@angular/core";
import {MetaData} from "../../kpn/api/common/data/meta-data";

@Component({
  selector: "kpn-version-change",
  template: `
    <div class="kpn-thin">
      <ng-container *ngIf="isNewVersion()">
        <ng-container i18n="@@version-change.relation-changed-to">Relation changed to v{{after.version}}</ng-container>
      </ng-container>
      <ng-container *ngIf="!isNewVersion()">
        <ng-container i18n="@@version-change.relation-unchanged">Relation unchanged.</ng-container>
      </ng-container>
      <kpn-meta-data [metaData]="after"></kpn-meta-data>
    </div>
  `
})
export class VersionChangeComponent {

  @Input() before: MetaData;
  @Input() after: MetaData;

  isNewVersion(): boolean {
    if (this.before && this.after) {
      return this.before.version !== this.after.version;
    }
    return false;
  }
}
