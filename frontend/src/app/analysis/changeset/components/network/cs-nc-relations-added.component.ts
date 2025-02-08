import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { IconInvestigateComponent } from '@app/shared/components/icon/icon-investigate.component';
import { OsmLinkRelationComponent } from '@app/shared/components/link/osm-link-relation.component';

@Component({
  selector: 'kpn-cs-nc-relations-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (relationIds().length > 0) {
      <div class="kpn-level-2">
        <div class="kpn-level-2-header kpn-line">
          <span i18n="@@change-set.network-changes.added-relations">Added non-route relations</span>
          <span class="kpn-brackets kpn-thin">{{ relationIds().length }}</span>
          <kpn-icon-investigate />
        </div>
        <div class="kpn-level-2-body kpn-comma-list">
          @for (relationId of relationIds(); track relationId) {
            <kpn-osm-link-relation [relationId]="relationId" [title]="relationId.toString()" />
          }
        </div>
      </div>
    }
  `,
  imports: [IconInvestigateComponent, OsmLinkRelationComponent],
})
export class CsNcRelationsAddedComponent {
  networkChangeInfo = input.required<NetworkChangeInfo>();
  readonly relationIds = computed(() => this.networkChangeInfo().relations.added);
}
