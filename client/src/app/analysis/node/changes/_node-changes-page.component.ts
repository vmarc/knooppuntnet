import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {MatPaginator} from "@angular/material";
import {ActivatedRoute} from "@angular/router";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {ChangesParameters} from "../../../kpn/shared/changes/filter/changes-parameters";
import {NodeChangesPage} from "../../../kpn/shared/node/node-changes-page";
import {UserService} from "../../../services/user.service";
import {Subscriptions} from "../../../util/Subscriptions";

@Component({
  selector: "kpn-node-changes-page",
  template: `

    <kpn-node-page-header
      [nodeId]="nodeId"
      [nodeName]="nodeName"
      [changeCount]="response?.result?.totalCount">
    </kpn-node-page-header>

    <p *ngIf="response">
      <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
    </p>

    <mat-paginator
      [pageIndex]="0"
      [pageSize]="parameters.itemsPerPage"
      [pageSizeOptions]="[5, 25, 50, 100, 250, 1000]">
    </mat-paginator>

    <div *ngIf="!isLoggedIn()">
      <span i18n="@@node.login-required">The node history is available to registered OpenStreetMap contributors only, after</span>
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="response?.result">
      <div *ngIf="!response.result" i18n="@@node.node-not-found">
        Node not found
      </div>
      <div *ngIf="response.result">

        <div *ngIf="response.result.changes.isEmpty()" i18n="@@node.no-history">
          No history
        </div>

        <div *ngIf="!response.result.changes.isEmpty()">

          <kpn-items>
            <kpn-item *ngFor="let nodeChangeInfo of response.result.changes; let i=index" [index]="i">
              <kpn-node-change [nodeChangeInfo]="nodeChangeInfo"></kpn-node-change>
            </kpn-item>
          </kpn-items>

          <div *ngIf="response.result.incompleteWarning">
            <kpn-history-incomplete-warning></kpn-history-incomplete-warning>
          </div>

        </div>

        <kpn-json [object]="response"></kpn-json>
      </div>
    </div>
  `
})
export class NodeChangesPageComponent implements OnInit, AfterViewInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  nodeId: number;
  nodeName: string;
  response: ApiResponse<NodeChangesPage>;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  parameters = new ChangesParameters(null, null, null, null, null, null, null, 5, 0, false);

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private pageService: PageService,
              private userService: UserService) {
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  ngOnInit(): void {
    this.nodeName = history.state.nodeName;
    this.pageService.defaultMenu();
    this.subscriptions.add(
      this.activatedRoute.params.subscribe(params => {
        const nodeId = params["nodeId"];
        this.nodeId = +nodeId;
      })
    );
  }

  ngAfterViewInit() {
    this.subscriptions.add(
      this.paginator.page.subscribe(event => this.reload())
    );
    this.reload();
  }

  private reload() {
    this.updateParameters();
    this.subscriptions.add(
      this.appService.nodeChanges(this.nodeId.toString(), this.parameters).subscribe(response => {
        this.processResponse(response);
        this.paginator.length = this.response.result.totalCount;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private processResponse(response: ApiResponse<NodeChangesPage>) {
    this.response = response;
    this.nodeName = Util.safeGet(() => response.result.nodeInfo.name);
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
      this.paginator.pageSize,
      this.paginator.pageIndex,
      this.parameters.impact
    );
  }

}
