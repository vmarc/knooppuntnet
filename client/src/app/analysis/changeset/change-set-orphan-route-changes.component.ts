import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {ChangeSetElementRef} from "../../kpn/api/common/change-set-element-ref";
import {ChangeSetSubsetElementRefs} from "../../kpn/api/common/change-set-subset-element-refs";
import {ChangeSetPage} from "../../kpn/api/common/changes/change-set-page";
import {Ref} from "../../kpn/api/common/common/ref";
import {RefDiffs} from "../../kpn/api/common/diff/ref-diffs";
import {RouteDiffsData} from "./route-diffs/route-diffs-data";

@Component({
  selector: "kpn-change-set-orphan-route-changes",
  template: `
    <div *ngFor="let refs of page.summary.orphanRouteChanges" class="kpn-level-1">
      <div class="kpn-level-1-header kpn-line">
        <kpn-network-type-icon [networkType]="refs.subset.networkType"></kpn-network-type-icon>
        <span>{{refs.subset.country.domain.toUpperCase()}}</span>
        <span i18n="@@change-set.orphan-routes.title">Orphan routes</span>
      </div>
      <div class="kpn-level-1-body">
        <kpn-route-diffs [data]="routeDiffs(refs)"></kpn-route-diffs>
      </div>
    </div>
  `
})
export class ChangeSetOrphanRouteChangesComponent {

  @Input() page: ChangeSetPage;

  routeDiffs(refs: ChangeSetSubsetElementRefs): RouteDiffsData {

    const refDiffs = new RefDiffs(
      this.toRefs(refs.elementRefs.removed),
      this.toRefs(refs.elementRefs.added),
      this.toRefs(refs.elementRefs.updated)
    );

    return new RouteDiffsData(
      refDiffs,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      this.page.routeChanges);
  }

  private toRefs(refs: List<ChangeSetElementRef>): List<Ref> {
    return refs.map(r => this.toRef(r));
  }

  private toRef(ref: ChangeSetElementRef): Ref {
    return new Ref(ref.id, ref.name);
  }

}
