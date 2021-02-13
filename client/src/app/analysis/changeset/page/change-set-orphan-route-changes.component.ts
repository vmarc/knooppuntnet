import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {ChangeSetElementRef} from '@api/common/change-set-element-ref';
import {ChangeSetSubsetElementRefs} from '@api/common/change-set-subset-element-refs';
import {ChangeSetPage} from '@api/common/changes/change-set-page';
import {Ref} from '@api/common/common/ref';
import {RefDiffs} from '@api/common/diff/ref-diffs';
import {List} from 'immutable';
import {RouteDiffsData} from './route-diffs/route-diffs-data';

@Component({
  selector: 'kpn-change-set-orphan-route-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let refs of page.summary.orphanRouteChanges" class="kpn-level-1">
      <div class="kpn-level-1-header kpn-line">
        <kpn-network-type-icon [networkType]="refs.subset.networkType"></kpn-network-type-icon>
        <span>{{refs.subset.country.toUpperCase()}}</span>
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
      List(this.page.routeChanges));
  }

  private toRefs(refs: ChangeSetElementRef[]): Ref[] {
    return refs.map(r => new Ref(r.id, r.name));
  }

}
