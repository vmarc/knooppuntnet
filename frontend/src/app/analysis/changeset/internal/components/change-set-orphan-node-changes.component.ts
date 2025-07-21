import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common/change-set-element-ref';
import { ChangeSetSubsetElementRefs } from '@api/common/change-set-subset-element-refs';
import { ChangeSetDetail } from '@api/common/changes/change-set-detail';
import { Ref } from '@api/common/common/ref';
import { RefDiffs } from '@api/common/diff/ref-diffs';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NodeDiffsData } from './node-diffs/node-diffs-data';
import { NodeDiffsComponent } from './node-diffs/node-diffs.component';

@Component({
  selector: 'ui-change-set-orphan-node-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (refs of detail().orphanNodeChanges; track refs) {
      <div class="kpn-level-1">
        <div class="kpn-level-1-header kpn-line">
          <nz-icon [nzType]="refs.subset.routeType" />
          <span>{{ refs.subset.country.toUpperCase() }}</span>
          <span i18n="@@change-set.orphan-nodes.title">Orphan nodes</span>
        </div>
        <div class="kpn-level-1-body">
          <ui-node-diffs [data]="nodeDiffs(refs)" />
        </div>
      </div>
    }
  `,
  imports: [NodeDiffsComponent, NzIconDirective],
})
export class ChangeSetOrphanNodeChangesComponent {
  readonly detail = input.required<ChangeSetDetail>();

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
      this.detail().nodeChanges
    );
  }

  private toRefs(refs: ChangeSetElementRef[]): Ref[] {
    return refs.map((r) => {
      return { id: r.id, name: r.name };
    });
  }
}
