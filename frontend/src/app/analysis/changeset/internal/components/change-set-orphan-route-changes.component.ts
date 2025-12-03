import { UpperCasePipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common/change-set-element-ref';
import { ChangeSetSubsetElementRefs } from '@api/common/change-set-subset-element-refs';
import { ChangeSetDetail } from '@api/common/changes/change-set-detail';
import { Ref } from '@api/common/common/ref';
import { RefDiffs } from '@api/common/diff/ref-diffs';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouteDiffsData } from './route-diffs/route-diffs-data';
import { RouteDiffsComponent } from './route-diffs/route-diffs.component';

@Component({
  selector: 'ui-change-set-orphan-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (refs of detail().orphanRouteChanges; track refs) {
      <div class="kpn-level-1">
        <div class="kpn-level-1-header kpn-line">
          <nz-icon [nzType]="refs.subset.routeType" />
          <span>{{ refs.subset.country | uppercase }}</span>
          <span i18n="@@change-set.orphan-routes.title">Free routes</span>
        </div>
        <div class="kpn-level-1-body">
          <ui-route-diffs [data]="routeDiffs(refs)" />
        </div>
      </div>
    }
  `,
  imports: [RouteDiffsComponent, NzIconDirective, UpperCasePipe],
})
export class ChangeSetOrphanRouteChangesComponent {
  readonly detail = input.required<ChangeSetDetail>();

  routeDiffs(refs: ChangeSetSubsetElementRefs): RouteDiffsData {
    const refDiffs: RefDiffs = {
      removed: this.toRefs(refs.elementRefs.removed),
      added: this.toRefs(refs.elementRefs.added),
      updated: this.toRefs(refs.elementRefs.updated),
    };
    return new RouteDiffsData(
      refDiffs,
      this.detail().summary.key.changeSetId,
      this.detail().knownElements,
      this.detail().routeChanges
    );
  }

  private toRefs(refs: ChangeSetElementRef[]): Ref[] {
    return refs.map((r) => {
      return { id: r.id, name: r.name };
    });
  }
}
