import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../kpn/shared/changes/details/network-change-info";
import {KnownElements} from "../../../kpn/shared/common/known-elements";
import {Ref} from "../../../kpn/shared/common/ref";

@Component({
  selector: "kpn-change-set-network-change-orphan-routes-old",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-detail kpn-line">
      <span i18n="@@change-set.network-changes.orphan-routes-introduced" class="kpn-label">
        Orphan routes added to this network
      </span>
      <div class="kpn-comma-list">
        <kpn-link-route-ref
          *ngFor="let ref of refs()"
          [ref]="ref"
          [knownElements]="knownElements">
        </kpn-link-route-ref>
      </div>
      <kpn-icon-investigate></kpn-icon-investigate>
    </div>
  `
})
export class ChangeSetNetworkChangeOrphanRoutesOldComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;
  @Input() knownElements: KnownElements;

  refs(): List<Ref> {
    return this.networkChangeInfo.orphanRoutes.oldRefs;
  }

}
