import { HttpContext } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { LOCALE_ID } from '@angular/core';
import { Injectable } from '@angular/core';
import { ChangesPage } from '@api/common';
import { PoiDetail } from '@api/common';
import { PoiPage } from '@api/common';
import { SurveyDateInfo } from '@api/common';
import { ChangeSetPage } from '@api/common/changes';
import { ChangesParameters } from '@api/common/changes/filter';
import { LocationChangesPage } from '@api/common/location';
import { LocationChangesParameters } from '@api/common/location';
import { LocationEditPage } from '@api/common/location';
import { LocationFactsPage } from '@api/common/location';
import { LocationMapPage } from '@api/common/location';
import { LocationNodesPage } from '@api/common/location';
import { LocationNodesParameters } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { LocationRoutesParameters } from '@api/common/location';
import { LocationsPage } from '@api/common/location';
import { NetworkChangesPage } from '@api/common/network';
import { NetworkDetailsPage } from '@api/common/network';
import { NetworkFactsPage } from '@api/common/network';
import { NetworkMapPage } from '@api/common/network';
import { NetworkNodesPage } from '@api/common/network';
import { NetworkRoutesPage } from '@api/common/network';
import { MapNodeDetail } from '@api/common/node';
import { NodeChangesPage } from '@api/common/node';
import { NodeDetailsPage } from '@api/common/node';
import { NodeMapPage } from '@api/common/node';
import { LegBuildParams } from '@api/common/planner';
import { PlanLegDetail } from '@api/common/planner';
import { PlanParams } from '@api/common/planner';
import { MapRouteDetail } from '@api/common/route';
import { RouteChangesPage } from '@api/common/route';
import { RouteDetailsPage } from '@api/common/route';
import { RouteMapPage } from '@api/common/route';
import { StatisticValues } from '@api/common/statistics';
import { LogPage } from '@api/common/status';
import { PeriodParameters } from '@api/common/status';
import { ReplicationStatusPage } from '@api/common/status';
import { Status } from '@api/common/status';
import { SystemStatusPage } from '@api/common/status';
import { SubsetChangesPage } from '@api/common/subset';
import { SubsetFactDetailsPage } from '@api/common/subset';
import { SubsetFactRefs } from '@api/common/subset';
import { SubsetFactsPage } from '@api/common/subset';
import { SubsetMapPage } from '@api/common/subset';
import { SubsetNetworksPage } from '@api/common/subset';
import { SubsetOrphanNodesPage } from '@api/common/subset';
import { SubsetOrphanRoutesPage } from '@api/common/subset';
import { ClientPoiConfiguration } from '@api/common/tiles';
import { ApiResponse } from '@api/custom';
import { Country } from '@api/custom';
import { LocationKey } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Subset } from '@api/custom';
import { AnalysisStrategy } from '@app/core';
import { LOCAL_ERROR_HANDLING } from '@app/spinner';
import { MarkdownService } from 'ngx-markdown';
import { Observable } from 'rxjs';
import { timeout } from 'rxjs/operators';

@Injectable()
export class ApiService {
  public readonly locale: string = inject(LOCALE_ID);
  private readonly http = inject(HttpClient);
  private readonly markdownService = inject(MarkdownService);

  constructor() {
    this.markdownService.renderer.link = (href: string, title: string, text: string) =>
      `<a href="${href}" title="${title}" target="_blank" rel="nofollow noreferrer">${text}</a>`;
  }

  edit(url: string): Observable<string> {
    return this.http
      .get(url, {
        context: new HttpContext().set(LOCAL_ERROR_HANDLING, true),
        responseType: 'text',
      })
      .pipe(timeout(3000));
  }

  overview(): Observable<ApiResponse<StatisticValues[]>> {
    const url = '/api/overview';
    return this.http.get(url, { params: this.languageParams() });
  }

  subsetNetworks(subset: Subset): Observable<ApiResponse<SubsetNetworksPage>> {
    const url = this.subsetUrl(subset, 'networks');
    return this.http.get(url);
  }

  subsetFacts(subset: Subset): Observable<ApiResponse<SubsetFactsPage>> {
    const url = this.subsetUrl(subset, 'facts');
    return this.http.get(url);
  }

  subsetFactDetails(
    subset: Subset,
    factName: string
  ): Observable<ApiResponse<SubsetFactDetailsPage>> {
    const url = this.subsetUrl(subset, factName);
    return this.http.get(url);
  }

  subsetOrphanNodes(subset: Subset): Observable<ApiResponse<SubsetOrphanNodesPage>> {
    const url = this.subsetUrl(subset, 'orphan-nodes');
    return this.http.get(url);
  }

  subsetOrphanRoutes(subset: Subset): Observable<ApiResponse<SubsetOrphanRoutesPage>> {
    const url = this.subsetUrl(subset, 'orphan-routes');
    return this.http.get(url);
  }

  subsetMap(subset: Subset): Observable<ApiResponse<SubsetMapPage>> {
    const url = this.subsetUrl(subset, 'map');
    return this.http.get(url);
  }

  subsetChanges(
    subset: Subset,
    parameters: ChangesParameters
  ): Observable<ApiResponse<SubsetChangesPage>> {
    const url = this.subsetUrl(subset, 'changes');
    return this.http.post(url, parameters);
  }

  networkDetails(networkId: number): Observable<ApiResponse<NetworkDetailsPage>> {
    const url = `/api/network/${networkId}`;
    return this.http.get(url);
  }

  networkMap(networkId: number): Observable<ApiResponse<NetworkMapPage>> {
    const url = `/api/network/${networkId}/map`;
    return this.http.get(url);
  }

  networkFacts(networkId: number): Observable<ApiResponse<NetworkFactsPage>> {
    const url = `/api/network/${networkId}/facts`;
    return this.http.get(url);
  }

  networkNodes(networkId: number): Observable<ApiResponse<NetworkNodesPage>> {
    const url = `/api/network/${networkId}/nodes`;
    return this.http.get(url);
  }

  networkRoutes(networkId: number): Observable<ApiResponse<NetworkRoutesPage>> {
    const url = `/api/network/${networkId}/routes`;
    return this.http.get(url);
  }

  networkChanges(
    networkId: number,
    parameters: ChangesParameters
  ): Observable<ApiResponse<NetworkChangesPage>> {
    const url = `/api/network/${networkId}/changes`;
    return this.http.post(url, parameters);
  }

  nodeDetails(nodeId: string): Observable<ApiResponse<NodeDetailsPage>> {
    const url = `/api/node/${nodeId}`;
    return this.http.get(url, { params: this.languageParams() });
  }

  nodeMap(nodeId: string): Observable<ApiResponse<NodeMapPage>> {
    const url = `/api/node/${nodeId}/map`;
    return this.http.get(url);
  }

  nodeChanges(
    nodeId: string,
    parameters: ChangesParameters
  ): Observable<ApiResponse<NodeChangesPage>> {
    const url = `/api/node/${nodeId}/changes`;
    return this.http.post(url, parameters);
  }

  routeDetails(routeId: string): Observable<ApiResponse<RouteDetailsPage>> {
    const url = `/api/route/${routeId}`;
    return this.http.get(url, { params: this.languageParams() });
  }

  routeMap(routeId: string): Observable<ApiResponse<RouteMapPage>> {
    const url = `/api/route/${routeId}/map`;
    return this.http.get(url);
  }

  routeChanges(
    routeId: string,
    parameters: ChangesParameters
  ): Observable<ApiResponse<RouteChangesPage>> {
    const url = `/api/route/${routeId}/changes`;
    return this.http.post(url, parameters);
  }

  changes(
    strategy: AnalysisStrategy,
    parameters: ChangesParameters
  ): Observable<ApiResponse<ChangesPage>> {
    const url = '/api/changes';
    const params = new HttpParams().set('language', this.locale).set('strategy', strategy);
    return this.http.post(url, parameters, { params });
  }

  public changeSet(
    changeSetId: string,
    replicationNumber: string
  ): Observable<ApiResponse<ChangeSetPage>> {
    const url = `/api/changeset/${changeSetId}/${replicationNumber}`;
    return this.http.get(url, { params: this.languageParams() });
  }

  public mapNodeDetail(
    networkType: NetworkType,
    nodeId: number
  ): Observable<ApiResponse<MapNodeDetail>> {
    const url = `/api/node-detail/${nodeId}/${networkType}`;
    return this.http.get(url);
  }

  public mapRouteDetail(routeId: number): Observable<ApiResponse<MapRouteDetail>> {
    const url = `/api/route-detail/${routeId}`;
    return this.http.get(url);
  }

  public poiConfiguration(): Observable<ApiResponse<ClientPoiConfiguration>> {
    const url = '/api/poi-configuration';
    return this.http.get(url);
  }

  public poi(elementType: string, elementId: number): Observable<ApiResponse<PoiPage>> {
    const url = `/api/poi/${elementType}/${elementId}`;
    return this.http.get(url);
  }

  public leg(legBuildParams: LegBuildParams): Observable<ApiResponse<PlanLegDetail>> {
    const url = `/api/leg`;
    return this.http.post(url, legBuildParams);
  }

  public plan(planParams: PlanParams): Observable<ApiResponse<PlanLegDetail[]>> {
    const url = `/api/plan`;
    return this.http.post(url, planParams);
  }

  public qrCode(url: string): Observable<Blob> {
    return this.http.post('/api/qr-code', url, { responseType: 'blob' });
  }

  public locations(
    networkType: NetworkType,
    country: Country
  ): Observable<ApiResponse<LocationsPage>> {
    const url = `/api/locations/${this.locale}/${networkType}/${country}`;
    return this.http.get(url);
  }

  public locationNodes(
    locationKey: LocationKey,
    parameters: LocationNodesParameters
  ): Observable<ApiResponse<LocationNodesPage>> {
    const url = this.locationUrl(locationKey, 'nodes');
    return this.http.post(url, parameters, { params: this.languageParams() });
  }

  public locationRoutes(
    locationKey: LocationKey,
    parameters: LocationRoutesParameters
  ): Observable<ApiResponse<LocationRoutesPage>> {
    const url = this.locationUrl(locationKey, 'routes');
    return this.http.post(url, parameters, { params: this.languageParams() });
  }

  public locationFacts(locationKey: LocationKey): Observable<ApiResponse<LocationFactsPage>> {
    const url = this.locationUrl(locationKey, 'facts');
    return this.http.get(url, { params: this.languageParams() });
  }

  public locationMap(locationKey: LocationKey): Observable<ApiResponse<LocationMapPage>> {
    const url = this.locationUrl(locationKey, 'map');
    return this.http.get(url, { params: this.languageParams() });
  }

  public locationChanges(
    locationKey: LocationKey,
    parameters: LocationChangesParameters
  ): Observable<ApiResponse<LocationChangesPage>> {
    const url = this.locationUrl(locationKey, 'changes');
    return this.http.post(url, parameters, { params: this.languageParams() });
  }

  public locationEdit(locationKey: LocationKey): Observable<ApiResponse<LocationEditPage>> {
    const url = this.locationUrl(locationKey, 'edit');
    return this.http.post(url, '', { params: this.languageParams() });
  }

  public status(): Observable<ApiResponse<Status>> {
    const url = '/api/status';
    return this.http.get(url);
  }

  public replicationStatus(
    parameters: PeriodParameters
  ): Observable<ApiResponse<ReplicationStatusPage>> {
    const url = '/api/status/replication';
    return this.http.post(url, parameters);
  }

  public systemStatus(parameters: PeriodParameters): Observable<ApiResponse<SystemStatusPage>> {
    const url = '/api/status/system';
    return this.http.post(url, parameters);
  }

  public logStatus(parameters: PeriodParameters): Observable<ApiResponse<LogPage>> {
    const url = '/api/status/log';
    return this.http.post(url, parameters);
  }

  public surveyDateInfo(): Observable<ApiResponse<SurveyDateInfo>> {
    const url = '/api/survey-date-info';
    return this.http.get(url);
  }

  public poiAreas(): Observable<ApiResponse<string>> {
    const url = '/api/poi/areas';
    return this.http.get(url);
  }

  public poiDetail(elementType: string, elementId: number): Observable<ApiResponse<PoiDetail>> {
    const url = `/api/poi-detail/${elementType}/${elementId}`;
    return this.http.get(url, { params: this.languageParams() });
  }

  private locationUrl(locationKey: LocationKey, target: string): string {
    return `/api/${locationKey.networkType}/${locationKey.country}/${locationKey.name}/${target}`;
  }

  private subsetUrl(subset: Subset, target: string): string {
    return `/api/${subset.country}/${subset.networkType}/${target}`;
  }

  private languageParams(): HttpParams {
    return new HttpParams().set('language', this.locale);
  }
}
