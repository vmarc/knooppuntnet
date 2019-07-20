import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {Subscriptions} from "../../../../util/Subscriptions";
import {NodeDetailsPage} from "../../../../kpn/shared/node/node-details-page";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {Ref} from "../../../../kpn/shared/common/ref";
import {FactInfo} from "../../../fact/fact-info";
import {List} from "immutable";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: "kpn-node-details-page",
  template: `

    <kpn-node-page-header [nodeId]="nodeId" [nodeName]="nodeName"></kpn-node-page-header>

    <div *ngIf="response?.result">
      <div *ngIf="!response.result" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result">

        <kpn-data title="Summary" i18n-title="@@node.summary">
          <node-summary [nodeInfo]="nodeInfo"></node-summary>
        </kpn-data>

        <kpn-data title="Situation on" i18n-title="@@node.situation-on">
          <kpn-timestamp [timestamp]="response.situationOn"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Last updated" i18n-title="@@node.last-updated">
          <kpn-timestamp [timestamp]="nodeInfo.lastUpdated"></kpn-timestamp>
        </kpn-data>

        <kpn-data title="Tags" i18n-title="@@node.tags">
          <kpn-tags-table [tags]="tags"></kpn-tags-table>
        </kpn-data>

        <kpn-data title="Networks" i18n-title="@@node.networks">
          <kpn-node-network-references [nodeInfo]="nodeInfo" [references]="references.networkReferences"></kpn-node-network-references>
        </kpn-data>

        <kpn-data title="Orphan routes" i18n-title="@@node.orphan-routes">
          <kpn-node-orphan-route-references [references]="references.routeReferences"></kpn-node-orphan-route-references>
        </kpn-data>

        <kpn-data title="Facts" i18n-title="@@node.facts">
          <kpn-facts [factInfos]="factInfos"></kpn-facts>
        </kpn-data>

        <json [object]="response"></json>
      </div>
    </div>
  `
})
export class NodeDetailsPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  nodeId: number;
  nodeName: string;
  tags: InterpretedTags;
  response: ApiResponse<NodeDetailsPage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.nodeName = history.state.nodeName;
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.pipe(
        map(params => params["nodeId"]),
        tap(nodeId => this.nodeId = nodeId),
        flatMap(nodeId => this.appService.nodeDetails(nodeId))
      ).subscribe(response => this.processResponse(response))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  get nodeInfo() {
    return this.response.result.nodeInfo;
  }

  get references() {
    return this.response.result.references;
  }

  get factInfos(): List<FactInfo> {

    const nodeFacts = this.nodeInfo.facts.map(fact => new FactInfo(fact));

    const extraFacts = this.response.result.references.networkReferences.flatMap(networkReference => {
      return networkReference.facts.map(fact => {
        const networkRef = new Ref(networkReference.networkId, networkReference.networkName);
        return new FactInfo(fact, networkRef, null, null);
      });
    });

    return nodeFacts.concat(extraFacts);
  }

  private processResponse(response: ApiResponse<NodeDetailsPage>) {
    this.response = response;
    this.nodeName = response.result.nodeInfo.name;
    this.tags = InterpretedTags.nodeTags(response.result.nodeInfo.tags);
  }

}
