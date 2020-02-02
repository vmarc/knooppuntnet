import {Component, OnDestroy, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";
import {NodeChangesPage} from "../../../kpn/api/common/node/node-changes-page";
import {UserService} from "../../../services/user.service";
import {Subscriptions} from "../../../util/Subscriptions";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {NodeChangesService} from "./node-changes.service";

@Component({
  selector: "kpn-node-changes-page",
  template: `

    <kpn-node-page-header
      [nodeId]="nodeId"
      [nodeName]="nodeName"
      [changeCount]="response?.result?.changeCount">
    </kpn-node-page-header>

    <p *ngIf="response">
      <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
    </p>

    <div *ngIf="!isLoggedIn()" i18n="@@node.login-required">
      The details of the node changes history is available to registered OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response?.result">
      <div *ngIf="!page" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="page">

        <kpn-changes [(parameters)]="parameters" [totalCount]="page.totalCount" [changeCount]="page.changes.size">
          <kpn-items>
            <kpn-item *ngFor="let nodeChangeInfo of page.changes; let i=index" [index]="i">
              <kpn-node-change [nodeChangeInfo]="nodeChangeInfo"></kpn-node-change>
            </kpn-item>
          </kpn-items>
        </kpn-changes>

        <div *ngIf="page.incompleteWarning">
          <kpn-history-incomplete-warning></kpn-history-incomplete-warning>
        </div>

      </div>

      <kpn-json [object]="response"></kpn-json>
    </div>
  `
})
export class NodeChangesPageComponent implements OnInit, OnDestroy {

  nodeId: number;
  nodeName: string;
  response: ApiResponse<NodeChangesPage>;

  private readonly subscriptions = new Subscriptions();

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private nodeChangesService: NodeChangesService,
              private pageService: PageService,
              private userService: UserService) {
  }

  private _parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, false);

  get parameters() {
    return this._parameters;
  }

  set parameters(parameters: ChangesParameters) {
    this._parameters = parameters;
    if (this.isLoggedIn()) {
      this.reload();
    } else {
      this.nodeChangesService.resetFilterOptions();
    }
  }

  get page(): NodeChangesPage {
    return this.response.result;
  }

  ngOnInit(): void {
    this.nodeName = history.state.nodeName;
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        const nodeId = params["nodeId"];
        this.nodeId = +nodeId;
        this.updateParameters();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  private reload() {
    this.subscriptions.add(
      this.appService.nodeChanges(this.nodeId.toString(), this.parameters).subscribe(response => {
        this.processResponse(response);
      })
    );
  }

  private processResponse(response: ApiResponse<NodeChangesPage>) {
    this.response = response;
    this.nodeName = Util.safeGet(() => response.result.nodeInfo.name);
    this.nodeChangesService.filterOptions.next(
      ChangeFilterOptions.from(
        this.parameters,
        this.response.result.filter,
        (parameters: ChangesParameters) => this.parameters = parameters
      )
    );
  }

  private updateParameters() {
    this.parameters = new ChangesParameters(
      null,
      null,
      null,
      this.nodeId,
      this.parameters.year,
      this.parameters.month,
      this.parameters.day,
      this.parameters.itemsPerPage,
      this.parameters.pageIndex,
      this.parameters.impact
    );
  }

}
