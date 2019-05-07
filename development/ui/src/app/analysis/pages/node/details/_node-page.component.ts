import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {Subscriptions} from "../../../../util/Subscriptions";

@Component({
  selector: "kpn-node-page",
  template: `

    <kpn-node-page-header [nodeId]="nodeId" [nodeName]="response?.result?.nodeInfo.name" [pageName]="'node'"></kpn-node-page-header>

    <div *ngIf="response?.result">
      <div *ngIf="!response.result">
        Node not found
      </div>
      <div *ngIf="response.result">

        <!--
          UiPageContents(
        -->

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
          <tags [tags]="nodeInfo.tags"></tags>
        </kpn-data>

        <kpn-data title="Networks" i18n-title="@@node.networks">
          <node-networks [networkReferences]="references.networkReferences"></node-networks>
        </kpn-data>

        <kpn-data title="Routes" i18n-title="@@node.routes">
          <node-routes [routes]="references.routeReferences"></node-routes>
        </kpn-data>

        <kpn-data title="Facts" i18n-title="@@node.feiten">
          TODO UiFacts(page.nodeInfo.facts)
        </kpn-data>

        <!--
          TagMod.when(PageWidth.isVeryLarge) {
            UiEmbeddedMap(new NodeMap(page.nodeInfo))
          },
          UiNodeChanges(page.nodeChanges)
          )
        -->

        <kpn-link-login></kpn-link-login>

        <json [object]="response"></json>
      </div>
    </div>
  `
})
export class NodePageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  nodeId: string;
  response: ApiResponse<NodePage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.nodeId = params["nodeId"];
      this.subscriptions.add(this.appService.node(this.nodeId).subscribe(response => {
        this.response = response;
      }));
    }));
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

}
