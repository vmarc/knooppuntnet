import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {NetworkChangeInfo} from "../../../../kpn/api/common/changes/details/network-change-info";

@Component({
  selector: "kpn-cs-nc-type",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="networkChangeInfo.changeType.name == 'Create'" class="kpn-detail">
      <b i18n="@@change-set.network-diffs.network-created">
        Network created
      </b>
    </div>
    <div *ngIf="networkChangeInfo.changeType.name == 'Delete'" class="kpn-detail">
      <b i18n="@@change-set.network-diffs.network-deleted">
        Network deleted
      </b>
    </div>
    <!-- no changeType text for "Update" -->
  `
})
export class CsNcTypeComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;
}
