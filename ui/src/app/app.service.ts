import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Observable} from "rxjs";
import {ApiResponse} from "./kpn/shared/api-response";
import {NodePage} from "./kpn/shared/node/node-page";

@Injectable()
export class AppService {

  constructor(private http: HttpClient) {
  }

  public overview(): Observable<string> {
    // ApiResponse[Statistics]
    const url = "/json-api/overview";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public subsetNetworks(/*subset: Subset*/): Observable<string> {
    // ApiResponse[SubsetNetworksPage]
    const url = "/json-api/networks/nl/rcn";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public subsetFacts(/*subset: Subset*/): Observable<string> {
    // ApiResponse[SubsetFactsPage]
    const url = "/json-api/facts/nl/rcn";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public subsetFactDetails(/*subset: Subset, fact: Fact*/): Observable<string> {
    // ApiResponse[SubsetFactDetailsPage]
    const url = "/json-api/RouteBroken/rcn/nl";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public subsetOrphanNodes(/*subset: Subset*/): Observable<string> {
    // ApiResponse[SubsetOrphanNodesPage]
    const url = "/json-api/orphan-nodes/nl/rcn";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public subsetOrphanRoutes(/*subset: Subset*/): Observable<string> {
    // ApiResponse[SubsetOrphanRoutesPage]
    const url = "/json-api/orphan-routes/nl/rcn";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public subsetChanges(/*parameters: ChangesParameters*/): Observable<string> {
    // ApiResponse[SubsetChangesPage]
    const url = "/json-api/changes/nl/rcn";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public networkDetails(networkId: number): Observable<string> {
    // ApiResponse[NetworkDetailsPage]
    const url = "/json-api/network/" + networkId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public networkMap(networkId: number): Observable<string> {
    // ApiResponse[NetworkMapPage]
    const url = "/json-api/network-map/" + networkId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public networkFacts(networkId: number): Observable<string> {
    // ApiResponse[NetworkFactsPage]
    const url = "/json-api/network-facts/" + networkId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public networkNodes(networkId: number): Observable<string> {
    // ApiResponse[NetworkNodesPage]
    const url = "/json-api/network-nodes/" + networkId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public networkRoutes(networkId: number): Observable<string> {
    // ApiResponse[NetworkRoutesPage]
    const url = "/json-api/network-routes/" + networkId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public networkChanges(networkId: number /*parameters: ChangesParameters*/): Observable<string> {
    // ApiResponse[NetworkChangesPage]
    const url = "/json-api/network-changes/" + networkId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public node(nodeId: number): Observable<ApiResponse<NodePage>> {
    const url = "/json-api/node/" + nodeId;
    return this.http.get(url).pipe(
      map(response => ApiResponse.fromJSON(response, NodePage.fromJSON))
    );
  }

  public route(routeId: number): Observable<string> {
    // ApiResponse[RoutePage]
    const url = "/json-api/route/" + routeId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public changes(/*parameters: ChangesParameters*/): Observable<string> {
    // ApiResponse[ChangesPage]
    const url = "/json-api/changes";
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public changeSet(changeSetId: number, replicationNumber: number): Observable<string> {
    // ApiResponse[ChangeSetPage]
    const url = "/json-api/changeset/" + changeSetId + "/" + replicationNumber;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public mapDetailNode(networkType: string /*NetworkType*/, nodeId: number): Observable<string> {
    // ApiResponse[MapDetailNode]
    const url = "/json-api/node-detail/" + nodeId + "/" + networkType;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  public mapDetailRoute(routeId: number): Observable<string> {
    // ApiResponse[MapDetailRoute]
    const url = "/json-api/route-detail/" + routeId;
    return this.http.get(url).pipe(
      map(response => this.formatResponse(response))
    );
  }

  private formatResponse(response: Object) {
    return JSON.stringify(response, null, 2);
  }

}
