import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../../app.service";
import {PageService} from "../../../../components/shared/page.service";
import {ApiResponse} from "../../../../kpn/shared/api-response";
import {NodePage} from "../../../../kpn/shared/node/node-page";
import {UserService} from "../../../../services/user.service";
import {Subscriptions} from "../../../../util/Subscriptions";

@Component({
  selector: "kpn-node-changes-page",
  template: `

    <kpn-node-page-header [nodeId]="nodeId" [nodeName]="response?.result?.nodeInfo.name" [pageName]="'node-changes'"></kpn-node-page-header>

    <div *ngIf="!isLoggedIn()">
      <span>The node history is available to registered OpenStreetMap contributors only, after</span>
      <!-- De node historiek is enkel beschikbaar voor OpenStreetMap gebruikers, na  -->
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response?.result">
      <div *ngIf="!response.result">
        Node not found
      </div>
      <div *ngIf="response.result">


        <div *ngIf="response.result.nodeChanges.changes.isEmpty()">
          No history
          <!-- Geen historiek -->
        </div>

        <div *ngIf="!response.result.nodeChanges.changes.isEmpty()">

          <kpn-items>
            <kpn-item *ngFor="let nodeChangeInfo of response.result.nodeChanges.changes; let i=index" index="{{i}}">
              <kpn-node-change [nodeChangeInfo]="nodeChangeInfo"></kpn-node-change>
            </kpn-item>
          </kpn-items>

          <div *ngIf="response.result.nodeChanges.incompleteWarning">
            <kpn-history-incomplete-warning></kpn-history-incomplete-warning>
          </div>

          <div *ngIf="response.result.nodeChanges.more">
            <!-- TODO not needed anymore with paging mechanism -->
            'more' link to be added here...
          </div>

        </div>

        <json [object]="response"></json>
      </div>
    </div>
  `
})
export class NodeChangesPageComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  nodeId: string;
  response: ApiResponse<NodePage>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private userService: UserService) {
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  ngOnInit() {
    this.pageService.defaultMenu();
    this.subscriptions.add(this.activatedRoute.params.subscribe(params => {
      this.nodeId = params["nodeId"];
      if (this.userService.isLoggedIn()) {
        this.subscriptions.add(this.appService.node(this.nodeId).subscribe(response => {
          this.response = response;
        }));
      }
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
