import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {ChangeSetElementRef} from "../../kpn/shared/change-set-element-ref";
import {ChangeSetSubsetElementRefs} from "../../kpn/shared/change-set-subset-element-refs";
import {ChangeSetPage} from "../../kpn/shared/changes/change-set-page";
import {Ref} from "../../kpn/shared/common/ref";
import {RefDiffs} from "../../kpn/shared/diff/ref-diffs";
import {NodeDiffsData} from "./node-diffs/node-diffs-data";

@Component({
  selector: "kpn-change-set-orphan-node-changes",
  template: `
    <div *ngFor="let refs of page.summary.orphanNodeChanges">
      <div class="kpn-line"> <!-- orphansHeader() -->
        <kpn-network-type-icon [networkType]="refs.subset.networkType"></kpn-network-type-icon>
        <span>{{refs.subset.country.domain.toUpperCase()}}</span>
        <span i18n="@@change-set.orphan-nodes.title">Orphan nodes</span>
      </div>
      <kpn-node-diffs [data]="nodeDiffs(refs)"></kpn-node-diffs>
    </div>
  `
})
export class ChangeSetOrphanNodeChangesComponent {

  @Input() page: ChangeSetPage;

  nodeDiffs(refs: ChangeSetSubsetElementRefs): NodeDiffsData {

    const refDiffs = new RefDiffs(
      this.toRefs(refs.elementRefs.removed),
      this.toRefs(refs.elementRefs.added),
      this.toRefs(refs.elementRefs.updated)
    );

    return new NodeDiffsData(
      refDiffs,
      this.page.summary.key.changeSetId,
      this.page.knownElements,
      this.page.nodeChanges);
  }

  private toRefs(refs: List<ChangeSetElementRef>): List<Ref> {
    return refs.map(r => this.toRef(r));
  }

  private toRef(ref: ChangeSetElementRef): Ref {
    return new Ref(ref.id, ref.name);
  }

}
