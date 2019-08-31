import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../kpn/shared/changes/details/network-change-info";
import {KnownElements} from "../../kpn/shared/common/known-elements";
import {Ref} from "../../kpn/shared/common/ref";

@Component({
  selector: "kpn-change-set-network-change-orphan-nodes-new",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-changes.orphan-nodes-introduced" class="kpn-label">
            Following nodes that used to be part of this network have become orphan
          </span>
      <div class="kpn-comma-list">
        <kpn-link-node-ref
          *ngFor="let ref of refs()"
          [ref]="ref"
          [knownElements]="knownElements">
        </kpn-link-node-ref>
      </div>
      <kpn-icon-investigate></kpn-icon-investigate>
    </div>
  `
})
export class ChangeSetNetworkChangeOrphanNodesNewComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;
  @Input() knownElements: KnownElements;

  refs(): List<Ref> {
    return this.networkChangeInfo.orphanNodes.newRefs;
  }

}
