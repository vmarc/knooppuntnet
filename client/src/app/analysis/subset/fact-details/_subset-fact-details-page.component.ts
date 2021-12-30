import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Params } from '@angular/router';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';
import { List } from 'immutable';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { map, mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { Util } from '../../../components/shared/util';
import { Subsets } from '../../../kpn/common/subsets';
import { SubsetCacheService } from '../../../services/subset-cache.service';

class SubsetFact {
  constructor(readonly subset: Subset, readonly factName: string) {}
}

@Component({
  selector: 'kpn-subset-fact-details-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="subsetFact$ | async as subsetFact">
      <kpn-subset-page-header-block
        pageName="facts"
        pageTitle="Facts"
        i18n-pageTitle="@@subset-facts.title"
      ></kpn-subset-page-header-block>
      <h2>
        <kpn-fact-name [fact]="subsetFact.factName"></kpn-fact-name>
      </h2>
    </div>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <div *ngIf="response.result">
        <div class="fact-description">
          <kpn-fact-description [factName]="factName"></kpn-fact-description>
        </div>
        <div *ngIf="!hasFacts" class="kpn-line">
          <span i18n="@@subset-facts.no-facts">No facts</span>
          <kpn-icon-happy></kpn-icon-happy>
        </div>
        <div *ngIf="hasFacts">
          <div class="kpn-space-separated kpn-label">
            <span>{{ refCount }}</span>
            <span *ngIf="hasNodeRefs()" i18n="@@subset-facts.node-refs"
              >{refCount, plural, one {node} other {nodes}}</span
            >
            <span *ngIf="hasRouteRefs()" i18n="@@subset-facts.route-refs"
              >{refCount, plural, one {route} other {routes}}</span
            >
            <span *ngIf="hasOsmNodeRefs()" i18n="@@subset-facts.osm-node-refs"
              >{refCount, plural, one {node} other {nodes}}</span
            >
            <span *ngIf="hasOsmWayRefs()" i18n="@@subset-facts.osm-way-refs"
              >{refCount, plural, one {way} other {ways}}</span
            >
            <span
              *ngIf="hasOsmRelationRefs()"
              i18n="@@subset-facts.osm-relation-refs"
              >{refCount, plural, one {relation} other {relations}}</span
            >
            <span i18n="@@subset-facts.in-networks"
              >{networkCount, plural, one {in 1 network} other {in
              {{ networkCount }} networks}}</span
            >
          </div>

          <kpn-items>
            <kpn-item
              *ngFor="
                let networkFactRefs of response.result.networks;
                let i = index
              "
              [index]="i"
            >
              <div class="fact-detail">
                <span
                  *ngIf="networkFactRefs.networkId === 0"
                  i18n="@@subset-facts.orphan-routes"
                  >Free routes</span
                >
                <a
                  *ngIf="networkFactRefs.networkId !== 0"
                  [routerLink]="
                    '/analysis/network/' + networkFactRefs.networkId
                  "
                >
                  {{ networkFactRefs.networkName }}
                </a>
              </div>
              <div class="fact-detail">
                <span
                  *ngIf="hasNodeRefs()"
                  i18n="@@subset-facts.nodes"
                  class="kpn-label"
                  >{networkFactRefs.factRefs.length, plural, one {1 node} other
                  {{{networkFactRefs.factRefs.length}} nodes}}</span
                >
                <span
                  *ngIf="hasRouteRefs()"
                  i18n="@@subset-facts.routes"
                  class="kpn-label"
                  >{networkFactRefs.factRefs.length, plural, one {1 route} other
                  {{{networkFactRefs.factRefs.length}} routes}}</span
                >
              </div>
              <div class="kpn-comma-list fact-detail">
                <span *ngFor="let ref of networkFactRefs.factRefs">
                  <kpn-link-node
                    *ngIf="hasNodeRefs()"
                    [nodeId]="ref.id"
                    [nodeName]="ref.name"
                  ></kpn-link-node>
                  <kpn-link-route
                    *ngIf="hasRouteRefs()"
                    [routeId]="ref.id"
                    [title]="ref.name"
                    [networkType]="(subsetInfo$ | async).networkType"
                  ></kpn-link-route>
                  <kpn-osm-link-node
                    *ngIf="hasOsmNodeRefs()"
                    [nodeId]="ref.id"
                    [title]="ref.name"
                  ></kpn-osm-link-node>
                  <kpn-osm-link-way
                    *ngIf="hasOsmWayRefs()"
                    [wayId]="ref.id"
                    [title]="ref.name"
                  ></kpn-osm-link-way>
                  <kpn-osm-link-relation
                    *ngIf="hasOsmRelationRefs()"
                    [relationId]="ref.id"
                    [title]="ref.name"
                  ></kpn-osm-link-relation>
                </span>
              </div>
            </kpn-item>
          </kpn-items>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./_subset-fact-details-page.component.scss'],
})
export class SubsetFactDetailsPageComponent implements OnInit {
  subsetFact$: Observable<SubsetFact>;
  subsetInfo$ = new BehaviorSubject<SubsetInfo>(null);
  response$: Observable<ApiResponse<SubsetFactDetailsPage>>;

  factName: string;
  hasFacts: boolean;
  refCount: number;
  networkCount: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService,
    private subsetCacheService: SubsetCacheService
  ) {}

  ngOnInit(): void {
    this.subsetFact$ = this.activatedRoute.params.pipe(
      map((params) => this.interpreteParams(params)),
      tap((subsetFact) =>
        this.subsetInfo$.next(
          this.subsetCacheService.getSubsetInfo(Subsets.key(subsetFact.subset))
        )
      )
    );
    this.response$ = this.subsetFact$.pipe(
      mergeMap((subsetFact) =>
        this.appService
          .subsetFactDetails(subsetFact.subset, subsetFact.factName)
          .pipe(
            tap((response) => {
              if (response.result) {
                this.hasFacts =
                  response.result && response.result.networks.length > 0;
                this.refCount = this.calculateRefCount(response);
                this.networkCount = response.result.networks.length;
                this.subsetCacheService.setSubsetInfo(
                  Subsets.key(subsetFact.subset),
                  response.result.subsetInfo
                );
                this.subsetInfo$.next(response.result.subsetInfo);
              }
            })
          )
      )
    );
  }

  hasNodeRefs(): boolean {
    return (
      this.factName === 'NodeMemberMissing' ||
      this.factName === 'NodeInvalidSurveyDate' ||
      this.factName === 'IntegrityCheckFailed'
    );
  }

  hasOsmNodeRefs(): boolean {
    return this.factName === 'NetworkExtraMemberNode';
  }

  hasOsmWayRefs(): boolean {
    return this.factName === 'NetworkExtraMemberWay';
  }

  hasOsmRelationRefs(): boolean {
    return this.factName === 'NetworkExtraMemberRelation';
  }

  hasRouteRefs(): boolean {
    return !(
      this.hasNodeRefs() ||
      this.hasOsmNodeRefs() ||
      this.hasOsmWayRefs() ||
      this.hasOsmRelationRefs()
    );
  }

  private interpreteParams(params: Params): SubsetFact {
    const subset = Util.subsetInRoute(params);
    this.factName = params['fact'];
    return new SubsetFact(subset, this.factName);
  }

  private calculateRefCount(
    response: ApiResponse<SubsetFactDetailsPage>
  ): number {
    return Util.sum(
      List(response.result.networks.map((n) => n.factRefs.length))
    );
  }
}
