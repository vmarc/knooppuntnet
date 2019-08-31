import {Component, Input} from "@angular/core";
import {NetworkChangeInfo} from "../../../kpn/shared/changes/details/network-change-info";

@Component({
  selector: "kpn-change-set-network-change-type",
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
export class ChangeSetNetworkChangeTypeComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;
}
