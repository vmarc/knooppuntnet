import {Component, Input} from "@angular/core";
import {MetaData} from "../../kpn/api/common/data/meta-data";

@Component({
  selector: "kpn-version-change",
  template: `
    <div class="kpn-thin">
      <ng-container *ngIf="isNewVersion()">
        <!-- @@ Relatie gewijzigd naar -->
        <ng-container i18n="@@version-change.relation-changed-to">Relation changed to</ng-container>
        v{{after.version}}
      </ng-container>
      <ng-container *ngIf="!isNewVersion()">
        <!-- @@ Relatie ongewijzigd -->
        <ng-container i18n="@@version-change.relation-unchanged">Relation unchanged</ng-container>
        .
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
      return this.before.version != this.after.version;
    }
    return false;
  }
}
