import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { Facts } from '@app/analysis/fact/components/facts';
import { DividerComponent } from '@app/shared/components/divider.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';

@Component({
  selector: 'kpn-subset-fact-details-summary',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-divider />
    @if (page().networks.length === 0) {
      <div class="kpn-line">
        <span i18n="@@subset-facts.no-facts">No facts</span>
        <kpn-icon-happy />
      </div>
    } @else {
      @if (fact(); as fact) {
        <div class="kpn-space-separated">
          <span>{{ refCount() }}</span>
          @if (fact.hasNodeRefs()) {
            @if (refCount() > 0) {
              <span i18n="@@subset-facts.node-refs">nodes</span>
            } @else {
              <span i18n="@@subset-facts.node-ref">node</span>
            }
          }
          @if (fact.hasRouteRefs()) {
            @if (refCount() > 0) {
              <span i18n="@@subset-facts.route-refs">routes</span>
            } @else {
              <span i18n="@@subset-facts.route-ref">route</span>
            }
          }
          @if (fact.hasOsmNodeRefs()) {
            @if (refCount() > 0) {
              <span i18n="@@subset-facts.osm-node-refs">nodes</span>
            } @else {
              <span i18n="@@subset-facts.osm-node-ref">node</span>
            }
          }
          @if (fact.hasOsmWayRefs()) {
            @if (refCount() > 0) {
              <span i18n="@@subset-facts.osm-way-refs">ways</span>
            } @else {
              <span i18n="@@subset-facts.osm-way-ref">way</span>
            }
          }
          @if (fact.hasOsmRelationRefs()) {
            @if (refCount() > 0) {
              <span i18n="@@subset-facts.osm-relation-refs">relations</span>
            } @else {
              <span i18n="@@subset-facts.osm-relation-ref">relation</span>
            }
          }
          <span i18n="@@subset-facts.in">in</span>
          <span>{{ page().networks.length }}</span>

          @if (page().networks.length > 0) {
            <span i18n="@@subset-facts.networks">networks</span>
          } @else {
            <span i18n="@@subset-facts.network">network</span>
          }

          @if (factCount() !== refCount()) {
            <span class="kpn-brackets"
              >{{ factCount() }}
              @if (factCount() === 1) {
                <ng-container i18n="@@subset-facts.fact">fact</ng-container>
              } @else {
                <ng-container i18n="@@subset-facts.facts">facts</ng-container>
              }
            </span>
          }
        </div>
      }
    }
  `,
  styleUrl: '../subset-fact-details-page.component.scss',
  imports: [IconHappyComponent, DividerComponent],
})
export class SubsetFactDetailsSummaryComponent {
  page = input.required<SubsetFactDetailsPage>();

  protected refCount = computed(
    () => new Set(this.page().networks.flatMap((n) => n.factRefs.map((r) => r.id))).size
  );
  protected factCount = computed(() => this.page().networks.flatMap((n) => n.factRefs).length);
  protected fact = computed(() => Facts.facts.get(this.page().fact));
}
