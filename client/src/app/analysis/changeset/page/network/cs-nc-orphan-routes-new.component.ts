import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../../kpn/api/common/changes/details/network-change-info";
import {KnownElements} from "../../../../kpn/api/common/common/known-elements";
import {Ref} from "../../../../kpn/api/common/common/ref";

@Component({
  selector: "kpn-cs-nc-orphan-routes-new",
  template: `
    <div *ngIf="!refs().isEmpty()" class="kpn-detail kpn-line">
      <span i18n="@@change-set.network-changes.orphan-routes-introduced" class="kpn-label">
        Following routes that used to be part of this network have become orphan
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
export class CsNcOrphanRoutesNewComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;
  @Input() knownElements: KnownElements;

  refs(): List<Ref> {
    return this.networkChangeInfo.orphanRoutes.newRefs;
  }

}
