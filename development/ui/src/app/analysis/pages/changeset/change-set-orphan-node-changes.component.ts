import {Component, Input} from '@angular/core';
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";
import {ChangeSetSubsetElementRefs} from "../../../kpn/shared/change-set-subset-element-refs";
import {RefDiffs} from "../../../kpn/shared/diff/ref-diffs";
import {Ref} from "../../../kpn/shared/common/ref";
import {ChangeSetElementRef} from "../../../kpn/shared/change-set-element-ref";
import {List} from "immutable";

@Component({
  selector: 'kpn-change-set-orphan-node-changes',
  template: `
    <div *ngFor="let changeSetSubsetElementRefs of page.summary.orphanNodeChanges">
      <div class="kpn-line"> <!-- orphansHeader() -->
        <kpn-network-type-icon [networkType]="changeSetSubsetElementRefs.subset.networkType"></kpn-network-type-icon>
        <span>{{changeSetSubsetElementRefs.subset.country.domain.toUpperCase()}}</span>
        <span>Orphan nodes</span>
        <!-- Knooppunt wezen -->
      </div>
      <kpn-node-diffs
        [changeSetId]="page.summary.key.changeSetId"
        [nodeDiffs]="nodeDiffs(changeSetSubsetElementRefs)"
        [nodeChangeInfos]="page.nodeChanges">
      </kpn-node-diffs>
    </div>
  `
})
export class ChangeSetOrphanNodeChangesComponent {

  @Input() page: ChangeSetPage;

  nodeDiffs(refs: ChangeSetSubsetElementRefs) {
    return new RefDiffs(
      this.toRefs(refs.elementRefs.removed),
      this.toRefs(refs.elementRefs.added),
      this.toRefs(refs.elementRefs.updated)
    );
  }

  private toRefs(refs: List<ChangeSetElementRef>): List<Ref> {
    return refs.map(r => this.toRef(r));
  }

  private toRef(ref: ChangeSetElementRef): Ref {
    return new Ref(ref.id, ref.name);
  }

}
