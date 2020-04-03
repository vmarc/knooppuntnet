import {OnDestroy} from "@angular/core";
import {AfterViewInit} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {combineLatest} from "rxjs";
import {Observable} from "rxjs";
import {shareReplay} from "rxjs/operators";
import {switchMap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PageService} from "../../../components/shared/page.service";
import {Util} from "../../../components/shared/util";
import {ChangesParameters} from "../../../kpn/api/common/changes/filter/changes-parameters";
import {NodeChangesPage} from "../../../kpn/api/common/node/node-changes-page";
import {ApiResponse} from "../../../kpn/api/custom/api-response";
import {UserService} from "../../../services/user.service";
import {ChangeFilterOptions} from "../../components/changes/filter/change-filter-options";
import {NodeChangesService} from "./node-changes.service";

@Component({
  selector: "kpn-node-changes-page",
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@breadcrumb.node-changes">Node changes</li>
    </ul>

    <kpn-node-page-header
      *ngIf="nodeId$ | async as nodeId"
      pageName="changes"
      [nodeId]="nodeId"
      [nodeName]="nodeName"
      [changeCount]="changeCount">
    </kpn-node-page-header>

    <div *ngIf="!isLoggedIn()" i18n="@@node.login-required">
      The details of the node changes history is available to registered OpenStreetMap contributors only, after
      <kpn-link-login></kpn-link-login>
      .
    </div>

    <div *ngIf="isLoggedIn() && response$ | async as response">
      <p>
        <kpn-situation-on [timestamp]="response.situationOn"></kpn-situation-on>
      </p>
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
    </div>
  `
})
export class NodeChangesPageComponent implements OnInit, OnDestroy, AfterViewInit {

  nodeId$: Observable<string>;
  response$: Observable<ApiResponse<NodeChangesPage>>;

  nodeName: string;
  changeCount = 0;
  page: NodeChangesPage;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService,
              private nodeChangesService: NodeChangesService,
              private pageService: PageService,
              private userService: UserService) {
  }

  get parameters() {
    return this.nodeChangesService.parameters$.value;
  }

  set parameters(parameters: ChangesParameters) {
    if (this.isLoggedIn()) {
      this.nodeChangesService.updateParameters(parameters);
    }
  }

  ngOnInit(): void {
    this.nodeName = history.state.nodeName;
    this.changeCount = history.state.changeCount;
    this.nodeId$ = this.activatedRoute.params.pipe(
      map(params => params["nodeId"]),
      tap(nodeId => this.updateParameters(nodeId)),
      shareReplay()
    );
  }

  ngOnDestroy(): void {
    this.nodeChangesService.resetFilterOptions();
  }

  ngAfterViewInit(): void {
    this.response$ = combineLatest([this.nodeId$, this.nodeChangesService.parameters$]).pipe(
      switchMap(([nodeId, changeParameters]) =>
        this.appService.nodeChanges(nodeId, changeParameters).pipe(
          tap(response => {
            this.page = Util.safeGet(() => response.result);
            this.nodeName = Util.safeGet(() => response.result.nodeInfo.name);
            this.changeCount = Util.safeGet(() => response.result.changeCount);
            this.nodeChangesService.filterOptions$.next(
              ChangeFilterOptions.from(
                this.parameters,
                response.result.filter,
                (parameters: ChangesParameters) => this.parameters = parameters
              )
            );
          })
        )
      )
    );
  }

  isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  private updateParameters(nodeId: string) {
    this.parameters = new ChangesParameters(
      null,
      null,
      null,
      +nodeId,
      this.parameters.year,
      this.parameters.month,
      this.parameters.day,
      this.parameters.itemsPerPage,
      this.parameters.pageIndex,
      this.parameters.impact
    );
  }

}
