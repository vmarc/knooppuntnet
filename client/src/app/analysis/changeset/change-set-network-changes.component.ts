import {Component, Input} from "@angular/core";
import {ChangeSetPage} from "../../kpn/shared/changes/change-set-page";

@Component({
  selector: "kpn-change-set-network-changes",
  template: `
    <div *ngFor="let networkChangeInfo of page.networkChanges" class="kpn-level-1">

      <div class="kpn-level-1-header">
        <a [id]="networkChangeInfo.networkId"></a>
        <div class="kpn-line">
          <kpn-network-type-icon [networkType]="networkChangeInfo.networkType"></kpn-network-type-icon>
          <span i18n="@@change-set.network-changes.network">Network</span>
          <kpn-link-network-details [networkId]="networkChangeInfo.networkId" [title]="networkChangeInfo.networkName"></kpn-link-network-details>
        </div>
      </div>

      <div class="kpn-level-1-body">
        <kpn-cs-nc-component [page]="page" [networkChangeInfo]="networkChangeInfo"></kpn-cs-nc-component>
      </div>

    </div>
  `
})
export class ChangeSetNetworkChangesComponent {
  @Input() page: ChangeSetPage;
}
