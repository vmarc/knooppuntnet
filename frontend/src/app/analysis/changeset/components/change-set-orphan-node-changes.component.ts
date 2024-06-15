import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common';
import { ChangeSetSubsetElementRefs } from '@api/common';
import { ChangeSetDetail } from '@api/common/changes/change-set-detail';
import { Ref } from '@api/common/common';
import { RefDiffs } from '@api/common/diff';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { List } from 'immutable';
import { NodeDiffsData } from './node-diffs/node-diffs-data';
import { NodeDiffsComponent } from './node-diffs/node-diffs.component';

@Component({
  selector: 'kpn-change-set-orphan-node-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (refs of detail().orphanNodeChanges; track refs) {
      <div class="kpn-level-1">
        <div class="kpn-level-1-header kpn-line">
          <kpn-network-type-icon [networkType]="refs.subset.networkType" />
          <span>{{ refs.subset.country.toUpperCase() }}</span>
          <span i18n="@@change-set.orphan-nodes.title">Orphan nodes</span>
        </div>
        <div class="kpn-level-1-body">
          <kpn-node-diffs [data]="nodeDiffs(refs)" />
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [NetworkTypeIconComponent, NodeDiffsComponent],
})
export class ChangeSetOrphanNodeChangesComponent {
  detail = input.required<ChangeSetDetail>();

  nodeDiffs(refs: ChangeSetSubsetElementRefs): NodeDiffsData {
    const refDiffs: RefDiffs = {
      removed: this.toRefs(refs.elementRefs.removed),
      added: this.toRefs(refs.elementRefs.added),
      updated: this.toRefs(refs.elementRefs.updated),
    };

    return new NodeDiffsData(
      refDiffs,
      this.detail().summary.key.changeSetId,
      this.detail().knownElements,
      List(this.detail().nodeChanges)
    );
  }

  private toRefs(refs: ChangeSetElementRef[]): Ref[] {
    return refs.map((r) => {
      return { id: r.id, name: r.name };
    });
  }
}
