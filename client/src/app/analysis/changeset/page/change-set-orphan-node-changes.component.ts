import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {List} from 'immutable';
import {ChangeSetElementRef} from '../../../kpn/api/common/change-set-element-ref';
import {ChangeSetSubsetElementRefs} from '../../../kpn/api/common/change-set-subset-element-refs';
import {ChangeSetPage} from '../../../kpn/api/common/changes/change-set-page';
import {Ref} from '../../../kpn/api/common/common/ref';
import {RefDiffs} from '../../../kpn/api/common/diff/ref-diffs';
import {NodeDiffsData} from './node-diffs/node-diffs-data';

@Component({
  selector: 'kpn-change-set-orphan-node-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let refs of page.summary.orphanNodeChanges" class="kpn-level-1">
      <div class="kpn-level-1-header kpn-line">
        <kpn-network-type-icon [networkType]="refs.subset.networkType"></kpn-network-type-icon>
        <span>{{refs.subset.country.domain.toUpperCase()}}</span>
        <span i18n="@@change-set.orphan-nodes.title">Orphan nodes</span>
      </div>
      <div class="kpn-level-1-body">
        <kpn-node-diffs [data]="nodeDiffs(refs)"></kpn-node-diffs>
      </div>
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
