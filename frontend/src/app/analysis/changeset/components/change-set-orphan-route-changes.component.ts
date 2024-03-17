import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeSetElementRef } from '@api/common';
import { ChangeSetSubsetElementRefs } from '@api/common';
import { ChangeSetPage } from '@api/common/changes';
import { Ref } from '@api/common/common';
import { RefDiffs } from '@api/common/diff';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { List } from 'immutable';
import { RouteDiffsComponent } from './route-diffs/route-diffs.component';
import { RouteDiffsData } from './route-diffs/route-diffs-data';

@Component({
  selector: 'kpn-change-set-orphan-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (refs of page().orphanRouteChanges; track refs) {
      <div class="kpn-level-1">
        <div class="kpn-level-1-header kpn-line">
          <kpn-network-type-icon [networkType]="refs.subset.networkType" />
          <span>{{ refs.subset.country.toUpperCase() }}</span>
          <span i18n="@@change-set.orphan-routes.title">Free routes</span>
        </div>
        <div class="kpn-level-1-body">
          <kpn-route-diffs [data]="routeDiffs(refs)" />
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [NetworkTypeIconComponent, RouteDiffsComponent],
})
export class ChangeSetOrphanRouteChangesComponent {
  page = input.required<ChangeSetPage>();

  routeDiffs(refs: ChangeSetSubsetElementRefs): RouteDiffsData {
    const refDiffs: RefDiffs = {
      removed: this.toRefs(refs.elementRefs.removed),
      added: this.toRefs(refs.elementRefs.added),
      updated: this.toRefs(refs.elementRefs.updated),
    };
    return new RouteDiffsData(
      refDiffs,
      this.page().summary.key.changeSetId,
      this.page().knownElements,
      List(this.page().routeChanges)
    );
  }

  private toRefs(refs: ChangeSetElementRef[]): Ref[] {
    return refs.map((r) => {
      return { id: r.id, name: r.name };
    });
  }
}
