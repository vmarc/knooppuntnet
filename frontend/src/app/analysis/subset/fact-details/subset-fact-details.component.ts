import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SubsetFactDetailsPage } from '@api/common/subset';
import { EditParameters } from '@app/analysis/components/edit';
import { EditService } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { OsmLinkWayComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-subset-fact-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (page.networks.length === 0) {
      <div class="kpn-line">
        <span i18n="@@subset-facts.no-facts">No facts</span>
        <kpn-icon-happy />
      </div>
    } @else {
      <div>
        <div class="kpn-space-separated kpn-label">
          <span>{{ refCount }}</span>
          @if (hasNodeRefs()) {
            <span i18n="@@subset-facts.node-refs">{refCount, plural, one {node} other {nodes}}</span>
          }
          @if (hasRouteRefs()) {
            <span i18n="@@subset-facts.route-refs">{refCount, plural, one {route} other {routes}}</span>
          }
          @if (hasOsmNodeRefs()) {
            <span i18n="@@subset-facts.osm-node-refs">{refCount, plural, one {node} other {nodes}}</span>
          }
          @if (hasOsmWayRefs()) {
            <span i18n="@@subset-facts.osm-way-refs">{refCount, plural, one {way} other {ways}}</span>
          }
          @if (hasOsmRelationRefs()) {
            <span i18n="@@subset-facts.osm-relation-refs">{refCount, plural, one {relation} other {relations}}</span>
          }
          <span
            i18n="@@subset-facts.in-networks">{page.networks.length, plural, one {in 1 network} other {in {{ page.networks.length }} networks}}</span>
          @if (factCount !== refCount) {
            <span
              i18n="@@subset-facts.fact-count">{factCount, plural, one {(1 fact)} other {({{ factCount }} facts)}}</span>
          }
        </div>
        <kpn-items>
          @for (networkFactRefs of page.networks; track networkFactRefs; let i = $index) {
            <kpn-item [index]="i">
              <div class="fact-detail">
                @if (networkFactRefs.networkId === 0) {
                  <span i18n="@@subset-facts.orphan-routes">Free routes</span>
                }
                @if (networkFactRefs.networkId !== 0) {
                  <a [routerLink]="'/analysis/network/' + networkFactRefs.networkId">
                    {{ networkFactRefs.networkName }}
                  </a>
                }
              </div>
              <div class="fact-detail">
                @if (hasNodeRefs()) {
                  <span i18n="@@subset-facts.nodes"
                    class="kpn-label">{networkFactRefs.factRefs.length, plural, one {1 node} other {{{ networkFactRefs.factRefs.length }} nodes}}</span>
                }
                @if (hasRouteRefs()) {
                  <span i18n="@@subset-facts.routes"
                    class="kpn-label">{networkFactRefs.factRefs.length, plural, one {1 route} other
                    {{{ networkFactRefs.factRefs.length }} routes}}</span>
                }
                <a
                  rel="nofollow"
                  (click)="edit()"
                  title="Open in editor (like JOSM)"
                  i18n-title="@@edit.link.title"
                  i18n="@@edit.link"
                >
                  edit
                </a>
              </div>
              <div class="kpn-comma-list fact-detail">
                @for (ref of networkFactRefs.factRefs; track ref) {
                  <span>
                    @if (hasNodeRefs()) {
                      <kpn-link-node
                        [nodeId]="ref.id"
                        [nodeName]="ref.name"
                      />
                    }
                    @if (hasRouteRefs()) {
                      <kpn-link-route
                        [routeId]="ref.id"
                        [routeName]="ref.name"
                        [networkType]="page.subsetInfo.networkType"
                      />
                    }
                    @if (hasOsmNodeRefs()) {
                      <kpn-osm-link-node
                        [nodeId]="ref.id"
                        [title]="ref.name"
                      />
                    }
                    @if (hasOsmWayRefs()) {
                      <kpn-osm-link-way
                        [wayId]="ref.id"
                        [title]="ref.name"
                      />
                    }
                    @if (hasOsmRelationRefs()) {
                      <kpn-osm-link-relation
                        [relationId]="ref.id"
                        [title]="ref.name"
                      />
                    }
                  </span>
                }
              </div>
            </kpn-item>
          }
        </kpn-items>
      </div>
    }
  `,
  styleUrl: './_subset-fact-details-page.component.scss',
  standalone: true,
  imports: [
    IconHappyComponent,
    ItemComponent,
    ItemsComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkWayComponent,
    RouterLink,
  ],
})
export class SubsetFactDetailsComponent implements OnInit {
  @Input() page: SubsetFactDetailsPage;

  private readonly editService = inject(EditService);

  protected refCount = 0;
  protected factCount = 0;

  ngOnInit(): void {
    this.refCount = this.calculateRefCount();
    this.factCount = this.calculateFactCount();
  }

  factName(): string {
    return this.page.fact;
  }

  private calculateRefCount(): number {
    return new Set(this.page.networks.flatMap((n) => n.factRefs.map((r) => r.id))).size;
  }

  private calculateFactCount(): number {
    return this.page.networks.flatMap((n) => n.factRefs).length;
  }

  hasNodeRefs(): boolean {
    return (
      'NodeMemberMissing' === this.factName() ||
      'NodeInvalidSurveyDate' === this.factName() ||
      'IntegrityCheckFailed' === this.factName()
    );
  }

  hasOsmNodeRefs(): boolean {
    return 'NetworkExtraMemberNode' === this.factName();
  }

  hasOsmWayRefs(): boolean {
    return 'NetworkExtraMemberWay' === this.factName();
  }

  hasOsmRelationRefs(): boolean {
    return 'NetworkExtraMemberRelation' === this.factName();
  }

  hasRouteRefs(): boolean {
    return !(
      this.hasNodeRefs() ||
      this.hasOsmNodeRefs() ||
      this.hasOsmWayRefs() ||
      this.hasOsmRelationRefs()
    );
  }

  edit() {
    const elementIds = this.page.networks.flatMap((n) => n.factRefs).map((ref) => ref.id);

    let editParameters: EditParameters = null;

    if (this.hasNodeRefs() || this.hasOsmNodeRefs()) {
      editParameters = {
        nodeIds: elementIds,
      };
    } else if (this.hasOsmWayRefs()) {
      editParameters = {
        wayIds: elementIds,
      };
    } else if (this.hasOsmRelationRefs() || this.hasRouteRefs()) {
      editParameters = {
        relationIds: elementIds,
        fullRelation: true,
      };
    }
    this.editService.edit(editParameters);
  }
}
