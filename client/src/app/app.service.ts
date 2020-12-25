import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ChangesPage} from '@api/common/changes-page';
import {ChangeSetPage} from '@api/common/changes/change-set-page';
import {ChangesParameters} from '@api/common/changes/filter/changes-parameters';
import {LocationChangesPage} from '@api/common/location/location-changes-page';
import {LocationChangesParameters} from '@api/common/location/location-changes-parameters';
import {LocationEditPage} from '@api/common/location/location-edit-page';
import {LocationFactsPage} from '@api/common/location/location-facts-page';
import {LocationMapPage} from '@api/common/location/location-map-page';
import {LocationNodesPage} from '@api/common/location/location-nodes-page';
import {LocationNodesParameters} from '@api/common/location/location-nodes-parameters';
import {LocationRoutesPage} from '@api/common/location/location-routes-page';
import {LocationRoutesParameters} from '@api/common/location/location-routes-parameters';
import {LocationsPage} from '@api/common/location/locations-page';
import {NetworkChangesPage} from '@api/common/network/network-changes-page';
import {NetworkDetailsPage} from '@api/common/network/network-details-page';
import {NetworkFactsPage} from '@api/common/network/network-facts-page';
import {NetworkMapPage} from '@api/common/network/network-map-page';
import {NetworkNodesPage} from '@api/common/network/network-nodes-page';
import {NetworkRoutesPage} from '@api/common/network/network-routes-page';
import {MapNodeDetail} from '@api/common/node/map-node-detail';
import {NodeChangesPage} from '@api/common/node/node-changes-page';
import {NodeDetailsPage} from '@api/common/node/node-details-page';
import {NodeMapPage} from '@api/common/node/node-map-page';
import {LegBuildParams} from '@api/common/planner/leg-build-params';
import {PlanLegDetail} from '@api/common/planner/plan-leg-detail';
import {PlanParams} from '@api/common/planner/plan-params';
import {PoiPage} from '@api/common/poi-page';
import {MapRouteDetail} from '@api/common/route/map-route-detail';
import {RouteChangesPage} from '@api/common/route/route-changes-page';
import {RouteDetailsPage} from '@api/common/route/route-details-page';
import {RouteMapPage} from '@api/common/route/route-map-page';
import {PeriodParameters} from '@api/common/status/period-parameters';
import {ReplicationStatusPage} from '@api/common/status/replication-status-page';
import {Status} from '@api/common/status/status';
import {SystemStatusPage} from '@api/common/status/system-status-page';
import {SubsetChangesPage} from '@api/common/subset/subset-changes-page';
import {SubsetFactDetailsPage} from '@api/common/subset/subset-fact-details-page';
import {SubsetFactsPage} from '@api/common/subset/subset-facts-page';
import {SubsetMapPage} from '@api/common/subset/subset-map-page';
import {SubsetNetworksPage} from '@api/common/subset/subset-networks-page';
import {SubsetOrphanNodesPage} from '@api/common/subset/subset-orphan-nodes-page';
import {SubsetOrphanRoutesPage} from '@api/common/subset/subset-orphan-routes-page';
import {SurveyDateInfo} from '@api/common/survey-date-info';
import {ClientPoiConfiguration} from '@api/common/tiles/client-poi-configuration';
import {ApiResponse} from '@api/custom/api-response';
import {Country} from '@api/custom/country';
import {LocationKey} from '@api/custom/location-key';
import {NetworkType} from '@api/custom/network-type';
import {Statistics} from '@api/custom/statistics';
import {Subset} from '@api/custom/subset';
import {MarkdownService} from 'ngx-markdown';
import {Observable} from 'rxjs';
import {timeout} from 'rxjs/operators';
import {map} from 'rxjs/operators';

@Injectable()
export class AppService {

  constructor(private http: HttpClient,
              markdownService: MarkdownService) {
    markdownService.renderer.link = (href: string, title: string, text: string) => `<a href="${href}" title="${title}" target="_blank" rel="nofollow noreferrer">${text}</a>`;
  }

  public edit(url: string): Observable<Object> {
    return this.http.get(url, {responseType: 'text'}).pipe(
      timeout(5000)
    );
  }

  public overview(): Observable<ApiResponse<Statistics>> {
    const url = '/api/overview';
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, Statistics.fromJSON))
    );
  }

  public subsetNetworks(subset: Subset): Observable<ApiResponse<SubsetNetworksPage>> {
    const url = this.subsetUrl(subset, 'networks');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetNetworksPage.fromJSON))
    );
  }

  public subsetFacts(subset: Subset): Observable<ApiResponse<SubsetFactsPage>> {
    const url = this.subsetUrl(subset, 'facts');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetFactsPage.fromJSON))
    );
  }

  public subsetFactDetails(subset: Subset, factName: string): Observable<ApiResponse<SubsetFactDetailsPage>> {
    const url = this.subsetUrl(subset, factName);
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetFactDetailsPage.fromJSON))
    );
  }

  public subsetOrphanNodes(subset: Subset): Observable<ApiResponse<SubsetOrphanNodesPage>> {
    const url = this.subsetUrl(subset, 'orphan-nodes');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetOrphanNodesPage.fromJSON))
    );
  }

  public subsetOrphanRoutes(subset: Subset): Observable<ApiResponse<SubsetOrphanRoutesPage>> {
    const url = this.subsetUrl(subset, 'orphan-routes');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetOrphanRoutesPage.fromJSON))
    );
  }

  public subsetMap(subset: Subset): Observable<ApiResponse<SubsetMapPage>> {
    const url = this.subsetUrl(subset, 'map');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetMapPage.fromJSON))
    );
  }

  public subsetChanges(subset: Subset, parameters: ChangesParameters): Observable<ApiResponse<SubsetChangesPage>> {
    const url = this.subsetUrl(subset, 'changes');
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetChangesPage.fromJSON))
    );
  }

  public networkDetails(networkId: number): Observable<ApiResponse<NetworkDetailsPage>> {
    const url = `/api/network/${networkId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkDetailsPage.fromJSON))
    );
  }

  public networkMap(networkId: number): Observable<ApiResponse<NetworkMapPage>> {
    const url = `/api/network/${networkId}/map`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkMapPage.fromJSON))
    );
  }

  public networkFacts(networkId: number): Observable<ApiResponse<NetworkFactsPage>> {
    const url = `/api/network/${networkId}/facts`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkFactsPage.fromJSON))
    );
  }

  public networkNodes(networkId: number): Observable<ApiResponse<NetworkNodesPage>> {
    const url = `/api/network/${networkId}/nodes`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkNodesPage.fromJSON))
    );
  }

  public networkRoutes(networkId: number): Observable<ApiResponse<NetworkRoutesPage>> {
    const url = `/api/network/${networkId}/routes`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkRoutesPage.fromJSON))
    );
  }

  public networkChanges(networkId: number, parameters: ChangesParameters): Observable<ApiResponse<NetworkChangesPage>> {
    const url = `/api/network/${networkId}/changes`;
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkChangesPage.fromJSON))
    );
  }

  public nodeDetails(nodeId: string): Observable<ApiResponse<NodeDetailsPage>> {
    const url = `/api/node/${nodeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NodeDetailsPage.fromJSON))
    );
  }

  public nodeMap(nodeId: string): Observable<ApiResponse<NodeMapPage>> {
    const url = `/api/node/${nodeId}/map`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NodeMapPage.fromJSON))
    );
  }

  public nodeChanges(nodeId: string, parameters: ChangesParameters): Observable<ApiResponse<NodeChangesPage>> {
    const url = `/api/node/${nodeId}/changes`;
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, NodeChangesPage.fromJSON))
    );
  }

  public routeDetails(routeId: string): Observable<ApiResponse<RouteDetailsPage>> {
    const url = `/api/route/${routeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, RouteDetailsPage.fromJSON))
    );
  }

  public routeMap(routeId: string): Observable<ApiResponse<RouteMapPage>> {
    const url = `/api/route/${routeId}/map`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, RouteMapPage.fromJSON))
    );
  }

  public routeChanges(routeId: string, parameters: ChangesParameters): Observable<ApiResponse<RouteChangesPage>> {
    const url = `/api/route/${routeId}/changes`;
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, RouteChangesPage.fromJSON))
    );
  }

  public changes(parameters: ChangesParameters): Observable<ApiResponse<ChangesPage>> {
    const url = '/api/changes';
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, ChangesPage.fromJSON))
    );
  }

  public changeSet(changeSetId: string, replicationNumber: string): Observable<ApiResponse<ChangeSetPage>> {
    const url = `/api/changeset/${changeSetId}/${replicationNumber}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, ChangeSetPage.fromJSON))
    );
  }

  public mapNodeDetail(networkType: NetworkType, nodeId: number): Observable<ApiResponse<MapNodeDetail>> {
    const url = `/api/node-detail/${nodeId}/${networkType.name}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, MapNodeDetail.fromJSON))
    );
  }

  public mapRouteDetail(routeId: number): Observable<ApiResponse<MapRouteDetail>> {
    const url = `/api/route-detail/${routeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, MapRouteDetail.fromJSON))
    );
  }

  public poiConfiguration(): Observable<ApiResponse<ClientPoiConfiguration>> {
    const url = '/api/poi-configuration';
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, ClientPoiConfiguration.fromJSON))
    );
  }

  public poi(elementType: string, elementId: number): Observable<ApiResponse<PoiPage>> {
    const url = `/api/poi/${elementType}/${elementId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, PoiPage.fromJSON))
    );
  }

  public leg(legBuildParams: LegBuildParams): Observable<ApiResponse<PlanLegDetail>> {
    const url = `/api/leg`;
    return this.http.post(url, legBuildParams).pipe(
      map(response => ApiResponse.fromJSON(response, PlanLegDetail.fromJSON))
    );
  }

  public plan(planParams: PlanParams): Observable<ApiResponse<PlanLegDetail>> {
    const url = `/api/plan`;
    return this.http.post(url, planParams).pipe(
      map(response => ApiResponse.fromJSON(response, PlanLegDetail.fromJSON))
    );
  }

  public locations(networkType: NetworkType, country: Country): Observable<ApiResponse<LocationsPage>> {
    const url = `/api/locations/${networkType.name}/${country.domain}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, LocationsPage.fromJSON))
    );
  }

  public locationEdit(locationKey: LocationKey): Observable<ApiResponse<LocationEditPage>> {
    const url = this.locationUrl(locationKey, 'edit');
    return this.http.post(url, '').pipe(
      map(response => ApiResponse.fromJSON(response, LocationEditPage.fromJSON))
    );
  }

  public locationNodes(locationKey: LocationKey, parameters: LocationNodesParameters): Observable<ApiResponse<LocationNodesPage>> {
    const url = this.locationUrl(locationKey, 'nodes');
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, LocationNodesPage.fromJSON))
    );
  }

  public locationRoutes(locationKey: LocationKey, parameters: LocationRoutesParameters): Observable<ApiResponse<LocationRoutesPage>> {
    const url = this.locationUrl(locationKey, 'routes');
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, LocationRoutesPage.fromJSON))
    );
  }

  public locationFacts(locationKey: LocationKey): Observable<ApiResponse<LocationFactsPage>> {
    const url = this.locationUrl(locationKey, 'facts');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, LocationFactsPage.fromJSON))
    );
  }

  public locationMap(locationKey: LocationKey): Observable<ApiResponse<LocationMapPage>> {
    const url = this.locationUrl(locationKey, 'map');
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, LocationMapPage.fromJSON))
    );
  }

  public locationChanges(locationKey: LocationKey, parameters: LocationChangesParameters): Observable<ApiResponse<LocationChangesPage>> {
    const url = this.locationUrl(locationKey, 'changes');
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, LocationChangesPage.fromJSON))
    );
  }

  public status(): Observable<ApiResponse<Status>> {
    const url = '/api/status';
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, Status.fromJSON))
    );
  }

  public replicationStatus(parameters: PeriodParameters): Observable<ApiResponse<ReplicationStatusPage>> {
    const url = '/api/status/replication';
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, ReplicationStatusPage.fromJSON))
    );
  }

  public systemStatus(parameters: PeriodParameters): Observable<ApiResponse<SystemStatusPage>> {
    const url = '/api/status/system';
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, SystemStatusPage.fromJSON))
    );
  }

  public surveyDateInfo(): Observable<ApiResponse<SurveyDateInfo>> {
    const url = '/api/survey-date-info';
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SurveyDateInfo.fromJSON))
    );
  }

  public poiAreas(): Observable<ApiResponse<string>> {
    const url = '/api/poi/areas';
    return this.http.get(url);
  }

  private locationUrl(locationKey: LocationKey, target: string): string {
    return `/api/${locationKey.networkType.name}/${locationKey.country.domain}/${locationKey.name}/${target}`;
  }

  private subsetUrl(subset: Subset, target: string): string {
    return `/api/${subset.country.domain}/${subset.networkType.name}/${target}`;
  }

}
