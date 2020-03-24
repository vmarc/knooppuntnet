import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {MarkdownService} from "ngx-markdown";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {ChangesPage} from "./kpn/api/common/changes-page";
import {ChangeSetPage} from "./kpn/api/common/changes/change-set-page";
import {ChangesParameters} from "./kpn/api/common/changes/filter/changes-parameters";
import {LocationChangesPage} from "./kpn/api/common/location/location-changes-page";
import {LocationChangesParameters} from "./kpn/api/common/location/location-changes-parameters";
import {LocationFactsPage} from "./kpn/api/common/location/location-facts-page";
import {LocationMapPage} from "./kpn/api/common/location/location-map-page";
import {LocationNodesPage} from "./kpn/api/common/location/location-nodes-page";
import {LocationNodesParameters} from "./kpn/api/common/location/location-nodes-parameters";
import {LocationPage} from "./kpn/api/common/location/location-page";
import {LocationRoutesPage} from "./kpn/api/common/location/location-routes-page";
import {LocationRoutesParameters} from "./kpn/api/common/location/location-routes-parameters";
import {NetworkChangesPage} from "./kpn/api/common/network/network-changes-page";
import {NetworkDetailsPage} from "./kpn/api/common/network/network-details-page";
import {NetworkFactsPage} from "./kpn/api/common/network/network-facts-page";
import {NetworkMapPage} from "./kpn/api/common/network/network-map-page";
import {NetworkNodesPage} from "./kpn/api/common/network/network-nodes-page";
import {NetworkRoutesPage} from "./kpn/api/common/network/network-routes-page";
import {MapNodeDetail} from "./kpn/api/common/node/map-node-detail";
import {NodeChangesPage} from "./kpn/api/common/node/node-changes-page";
import {NodeDetailsPage} from "./kpn/api/common/node/node-details-page";
import {NodeMapPage} from "./kpn/api/common/node/node-map-page";
import {RouteLeg} from "./kpn/api/common/planner/route-leg";
import {PoiPage} from "./kpn/api/common/poi-page";
import {MapRouteDetail} from "./kpn/api/common/route/map-route-detail";
import {RouteChangesPage} from "./kpn/api/common/route/route-changes-page";
import {RouteDetailsPage} from "./kpn/api/common/route/route-details-page";
import {RouteMapPage} from "./kpn/api/common/route/route-map-page";
import {BarChart} from "./kpn/api/common/status/bar-chart";
import {BarChart2D} from "./kpn/api/common/status/bar-chart2d";
import {PeriodParameters} from "./kpn/api/common/status/period-parameters";
import {ReplicationStatusPage} from "./kpn/api/common/status/replication-status-page";
import {Status} from "./kpn/api/common/status/status";
import {SubsetChangesPage} from "./kpn/api/common/subset/subset-changes-page";
import {SubsetFactDetailsPage} from "./kpn/api/common/subset/subset-fact-details-page";
import {SubsetFactsPage} from "./kpn/api/common/subset/subset-facts-page";
import {SubsetNetworksPage} from "./kpn/api/common/subset/subset-networks-page";
import {SubsetOrphanNodesPage} from "./kpn/api/common/subset/subset-orphan-nodes-page";
import {SubsetOrphanRoutesPage} from "./kpn/api/common/subset/subset-orphan-routes-page";
import {ClientPoiConfiguration} from "./kpn/api/common/tiles/client-poi-configuration";
import {ApiResponse} from "./kpn/api/custom/api-response";
import {LocationKey} from "./kpn/api/custom/location-key";
import {NetworkType} from "./kpn/api/custom/network-type";
import {Statistics} from "./kpn/api/custom/statistics";
import {Subset} from "./kpn/api/custom/subset";

@Injectable()
export class AppService {

  constructor(private http: HttpClient,
              markdownService: MarkdownService) {
    markdownService.renderer.link = (href: string, title: string, text: string) => {
      return `<a href="${href}" title="${title}" target="_blank">${text}</a>`;
    };
  }

  public overview(): Observable<ApiResponse<Statistics>> {
    const url = "/json-api/overview";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, Statistics.fromJSON))
    );
  }

  public subsetNetworks(subset: Subset): Observable<ApiResponse<SubsetNetworksPage>> {
    const url = this.subsetUrl(subset, "networks");
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetNetworksPage.fromJSON))
    );
  }

  public subsetFacts(subset: Subset): Observable<ApiResponse<SubsetFactsPage>> {
    const url = this.subsetUrl(subset, "facts");
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
    const url = this.subsetUrl(subset, "orphan-nodes");
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetOrphanNodesPage.fromJSON))
    );
  }

  public subsetOrphanRoutes(subset: Subset): Observable<ApiResponse<SubsetOrphanRoutesPage>> {
    const url = this.subsetUrl(subset, "orphan-routes");
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetOrphanRoutesPage.fromJSON))
    );
  }

  public subsetChanges(subset: Subset, parameters: ChangesParameters): Observable<ApiResponse<SubsetChangesPage>> {
    const url = this.subsetUrl(subset, "changes");
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetChangesPage.fromJSON))
    );
  }

  public networkDetails(networkId: number): Observable<ApiResponse<NetworkDetailsPage>> {
    const url = `/json-api/network/${networkId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkDetailsPage.fromJSON))
    );
  }

  public networkMap(networkId: number): Observable<ApiResponse<NetworkMapPage>> {
    const url = `/json-api/network/${networkId}/map`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkMapPage.fromJSON))
    );
  }

  public networkFacts(networkId: number): Observable<ApiResponse<NetworkFactsPage>> {
    const url = `/json-api/network/${networkId}/facts`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkFactsPage.fromJSON))
    );
  }

  public networkNodes(networkId: number): Observable<ApiResponse<NetworkNodesPage>> {
    const url = `/json-api/network/${networkId}/nodes`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkNodesPage.fromJSON))
    );
  }

  public networkRoutes(networkId: number): Observable<ApiResponse<NetworkRoutesPage>> {
    const url = `/json-api/network/${networkId}/routes`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkRoutesPage.fromJSON))
    );
  }

  public networkChanges(networkId: number, parameters: ChangesParameters): Observable<ApiResponse<NetworkChangesPage>> {
    const url = `/json-api/network/${networkId}/changes`;
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkChangesPage.fromJSON))
    );
  }

  public nodeDetails(nodeId: string): Observable<ApiResponse<NodeDetailsPage>> {
    const url = `/json-api/node/${nodeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NodeDetailsPage.fromJSON))
    );
  }

  public nodeMap(nodeId: string): Observable<ApiResponse<NodeMapPage>> {
    const url = `/json-api/node/${nodeId}/map`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NodeMapPage.fromJSON))
    );
  }

  public nodeChanges(nodeId: string, parameters: ChangesParameters): Observable<ApiResponse<NodeChangesPage>> {
    const url = `/json-api/node/${nodeId}/changes`;
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, NodeChangesPage.fromJSON))
    );
  }

  public routeDetails(routeId: string): Observable<ApiResponse<RouteDetailsPage>> {
    const url = `/json-api/route/${routeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, RouteDetailsPage.fromJSON))
    );
  }

  public routeMap(routeId: string): Observable<ApiResponse<RouteMapPage>> {
    const url = `/json-api/route/${routeId}/map`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, RouteMapPage.fromJSON))
    );
  }

  public routeChanges(routeId: string, parameters: ChangesParameters): Observable<ApiResponse<RouteChangesPage>> {
    const url = `/json-api/route/${routeId}/changes`;
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, RouteChangesPage.fromJSON))
    );
  }

  public changes(parameters: ChangesParameters): Observable<ApiResponse<ChangesPage>> {
    const url = "/json-api/changes";
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, ChangesPage.fromJSON))
    );
  }

  public changeSet(changeSetId: string, replicationNumber: string): Observable<ApiResponse<ChangeSetPage>> {
    const url = `/json-api/changeset/${changeSetId}/${replicationNumber}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, ChangeSetPage.fromJSON))
    );
  }

  public mapNodeDetail(networkType: NetworkType, nodeId: number): Observable<ApiResponse<MapNodeDetail>> {
    const url = `/json-api/node-detail/${nodeId}/${networkType.name}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, MapNodeDetail.fromJSON))
    );
  }

  public mapRouteDetail(routeId: number): Observable<ApiResponse<MapRouteDetail>> {
    const url = `/json-api/route-detail/${routeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, MapRouteDetail.fromJSON))
    );
  }

  public poiConfiguration(): Observable<ApiResponse<ClientPoiConfiguration>> {
    const url = "/json-api/poi-configuration";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, ClientPoiConfiguration.fromJSON))
    );
  }

  public poi(elementType: string, elementId: number): Observable<ApiResponse<PoiPage>> {
    const url = `/json-api/poi/${elementType}/${elementId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, PoiPage.fromJSON))
    );
  }

  public routeLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<ApiResponse<RouteLeg>> {
    const url = `/json-api/leg/${networkType}/${legId}/${sourceNodeId}/${sinkNodeId}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, RouteLeg.fromJSON))
    );
  }

  public location(networkType: string): Observable<ApiResponse<LocationPage>> {
    const url = `/json-api/location/${networkType}`;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, LocationPage.fromJSON))
    );
  }

  public locationNodes(locationKey: LocationKey, parameters: LocationNodesParameters): Observable<ApiResponse<LocationNodesPage>> {
    const url = this.locationUrl(locationKey, "nodes");
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, LocationNodesPage.fromJSON))
    );
  }

  public locationRoutes(locationKey: LocationKey, parameters: LocationRoutesParameters): Observable<ApiResponse<LocationRoutesPage>> {
    const url = this.locationUrl(locationKey, "routes");
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, LocationRoutesPage.fromJSON))
    );
  }

  public locationFacts(locationKey: LocationKey): Observable<ApiResponse<LocationFactsPage>> {
    const url = this.locationUrl(locationKey, "facts");
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, LocationFactsPage.fromJSON))
    );
  }

  public locationMap(locationKey: LocationKey): Observable<ApiResponse<LocationMapPage>> {
    const url = this.locationUrl(locationKey, "facts");
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, LocationFactsPage.fromJSON))
    );
  }

  public locationChanges(locationKey: LocationKey, parameters: LocationChangesParameters): Observable<ApiResponse<LocationChangesPage>> {
    const url = this.locationUrl(locationKey, "changes");
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, LocationChangesPage.fromJSON))
    );
  }

  public status(): Observable<ApiResponse<Status>> {
    const url = "/json-api/status";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, Status.fromJSON))
    );
  }

  public replicationStatus(parameters: PeriodParameters): Observable<ApiResponse<ReplicationStatusPage>> {
    const url = "/json-api/status/replication";
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, ReplicationStatusPage.fromJSON))
    );
  }

  public statusExample(): Observable<ApiResponse<BarChart2D>> {
    const url = "/json-api/status/example";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart2D.fromJSON))
    );
  }

  public dayDelay(): Observable<ApiResponse<BarChart2D>> {
    const url = "/json-api/status/day-delay";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart2D.fromJSON))
    );
  }

  public dayReplicationDelay(): Observable<ApiResponse<BarChart>> {
    const url = "/json-api/status/day-replication-delay";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart.fromJSON))
    );
  }

  public dayReplicationBytes(): Observable<ApiResponse<BarChart>> {
    const url = "/json-api/status/day-replication-bytes";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart.fromJSON))
    );
  }

  public dayReplicationElements(): Observable<ApiResponse<BarChart>> {
    const url = "/json-api/status/day-replication-elements";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart.fromJSON))
    );
  }

  public dayReplicationChangesets(): Observable<ApiResponse<BarChart>> {
    const url = "/json-api/status/day-replication-changesets";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart.fromJSON))
    );
  }

  public dayUpdateDelay(): Observable<ApiResponse<BarChart>> {
    const url = "/json-api/status/day-update-delay";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart.fromJSON))
    );
  }

  public dayAnalysisDelay(): Observable<ApiResponse<BarChart>> {
    const url = "/json-api/status/day-analysis-delay";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, BarChart.fromJSON))
    );
  }

  private locationUrl(locationKey: LocationKey, target: string): string {
    return `/json-api/${locationKey.networkType.name}/${locationKey.country.domain}/${locationKey.name}/${target}`;
  }

  private subsetUrl(subset: Subset, target: string): string {
    return `/json-api/${subset.country.domain}/${subset.networkType.name}/${target}`;
  }

}
