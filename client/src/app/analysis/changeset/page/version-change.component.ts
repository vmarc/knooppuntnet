import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {MetaData} from "../../../kpn/api/common/data/meta-data";

@Component({
  selector: "kpn-version-change",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-thin">
      <ng-container *ngIf="isNewVersion()" i18n="@@version-change.relation-changed-to">
        Relation changed to v{{after.version}}.
      </ng-container>
      <span *ngIf="!isNewVersion()" i18n="@@version-change.relation-unchanged"
            class="kpn-label">Relation unchanged</span>
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
