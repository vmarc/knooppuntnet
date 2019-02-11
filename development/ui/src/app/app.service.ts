import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Observable} from "rxjs";
import {ApiResponse} from "./kpn/shared/api-response";
import {NodePage} from "./kpn/shared/node/node-page";
import {NetworkChangesPage} from "./kpn/shared/network/network-changes-page";
import {RoutePage} from "./kpn/shared/route/route-page";
import {ChangesPage} from "./kpn/shared/changes-page";
import {ChangeSetPage} from "./kpn/shared/changes/change-set-page";
import {MapDetailNode} from "./kpn/shared/node/map-detail-node";
import {MapDetailRoute} from "./kpn/shared/route/map-detail-route";
import {SubsetNetworksPage} from "./kpn/shared/subset/subset-networks-page";
import {SubsetFactsPage} from "./kpn/shared/subset/subset-facts-page";
import {SubsetFactDetailsPage} from "./kpn/shared/subset/subset-fact-details-page";
import {SubsetOrphanNodesPage} from "./kpn/shared/subset/subset-orphan-nodes-page";
import {SubsetOrphanRoutesPage} from "./kpn/shared/subset/subset-orphan-routes-page";
import {SubsetChangesPage} from "./kpn/shared/subset/subset-changes-page";
import {NetworkDetailsPage} from "./kpn/shared/network/network-details-page";
import {NetworkMapPage} from "./kpn/shared/network/network-map-page";
import {NetworkFactsPage} from "./kpn/shared/network/network-facts-page";
import {NetworkNodesPage} from "./kpn/shared/network/network-nodes-page";
import {NetworkRoutesPage} from "./kpn/shared/network/network-routes-page";
import {ChangesParameters} from "./kpn/shared/changes/filter/changes-parameters";
import {Subset} from "./kpn/shared/subset";
import {TilePoiConfiguration} from "./kpn/shared/tiles/tile-poi-configuration";
import {Poi} from "./kpn/shared/poi";

@Injectable()
export class AppService {

  constructor(private http: HttpClient) {
  }

  public overview(): Observable<any> /*ApiResponse<Statistics>*/ {
    const url = "/json-api/overview";
    return this.http.get(url).pipe(
      map(response => response)
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

  public subsetFactDetails(subset: Subset /*, fact: Fact*/): Observable<ApiResponse<SubsetFactDetailsPage>> {
    const url = this.subsetUrl(subset, "RouteBroken");
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

  public subsetChanges(subset: Subset /*parameters: ChangesParameters*/): Observable<ApiResponse<SubsetChangesPage>> {
    const url = this.subsetUrl(subset, "changes");
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, SubsetChangesPage.fromJSON))
    );
  }

  public networkDetails(networkId: string): Observable<ApiResponse<NetworkDetailsPage>> {
    const url = "/json-api/network/" + networkId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkDetailsPage.fromJSON))
    );
  }

  public networkMap(networkId: string): Observable<ApiResponse<NetworkMapPage>> {
    const url = "/json-api/network-map/" + networkId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkMapPage.fromJSON))
    );
  }

  public networkFacts(networkId: string): Observable<ApiResponse<NetworkFactsPage>> {
    const url = "/json-api/network-facts/" + networkId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkFactsPage.fromJSON))
    );
  }

  public networkNodes(networkId: string): Observable<ApiResponse<NetworkNodesPage>> {
    const url = "/json-api/network-nodes/" + networkId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkNodesPage.fromJSON))
    );
  }

  public networkRoutes(networkId: string): Observable<ApiResponse<NetworkRoutesPage>> {
    const url = "/json-api/network-routes/" + networkId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkRoutesPage.fromJSON))
    );
  }

  public networkChanges(networkId: string /*parameters: ChangesParameters*/): Observable<ApiResponse<NetworkChangesPage>> {
    const url = "/json-api/network-changes/" + networkId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NetworkChangesPage.fromJSON))
    );
  }

  public node(nodeId: string): Observable<ApiResponse<NodePage>> {
    const url = "/json-api/node/" + nodeId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NodePage.fromJSON))
    );
  }

  public route(routeId: string): Observable<ApiResponse<RoutePage>> {
    const url = "/json-api/route/" + routeId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, RoutePage.fromJSON))
    );
  }

  public changes(parameters: ChangesParameters): Observable<ApiResponse<ChangesPage>> {
    const url = "/json-api/changes";
    return this.http.post(url, parameters).pipe(
      map(response => ApiResponse.fromJSON(response, ChangesPage.fromJSON))
    );
  }

  public changeSet(changeSetId: string, replicationNumber: string): Observable<ApiResponse<ChangeSetPage>> {
    const url = "/json-api/changeset/" + changeSetId + "/" + replicationNumber;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, ChangeSetPage.fromJSON))
    );
  }

  public mapDetailNode(networkType: string /*NetworkType*/, nodeId: string): Observable<ApiResponse<MapDetailNode>> {
    const url = "/json-api/node-detail/" + nodeId + "/" + networkType;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, MapDetailNode.fromJSON))
    );
  }

  public mapDetailRoute(routeId: string): Observable<ApiResponse<MapDetailRoute>> {
    const url = "/json-api/route-detail/" + routeId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, MapDetailRoute.fromJSON))
    );
  }

  public poiConfiguration(): Observable<ApiResponse<TilePoiConfiguration>> {
    const url = "/json-api/poi-configuration";
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, TilePoiConfiguration.fromJSON))
    );
  }

  public poi(elementType: string, elementId: number): Observable<ApiResponse<Poi>> {
    const url = "/json-api/poi/" + elementType + "/" + elementId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, Poi.fromJSON))
    );
  }

  private subsetUrl(subset: Subset, target: string): string {
    return "/json-api/" + target + "/" + subset.country.domain + "/" + subset.networkType.name;
  }

}
