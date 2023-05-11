import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageWidthService } from '@app/components/shared';
import { ErrorComponent } from '@app/components/shared/error';
import { IntegerFormatPipe } from '@app/components/shared/format';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { Store } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { actionSubsetNetworksPageInit } from '../store/subset.actions';
import { selectSubsetNetworksPage } from '../store/subset.selectors';
import { SubsetNetworkListComponent } from './subset-network-list.component';
import { SubsetNetworkTableComponent } from './subset-network-table.component';

@Component({
  selector: 'kpn-subset-networks-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="networks"
      pageTitle="Networks"
      i18n-pageTitle="@@subset-networks.title"
    />

    <kpn-error />

    <div *ngIf="apiResponse() as response" class="kpn-spacer-above">
      <div
        *ngIf="response.result.networks.length === 0"
        i18n="@@subset-networks.no-networks"
      >
        No networks
      </div>
      <div *ngIf="response.result.networks.length > 0">
        <p>
          <kpn-situation-on
            [timestamp]="response.situationOn"
          ></kpn-situation-on>
        </p>

        <markdown i18n="@@subset-networks.summary">
          _There are __{{ response.result.networkCount | integer }}__ networks,
          with a total of __{{ response.result.nodeCount | integer }}__ nodes
          and __{{ response.result.routeCount | integer }}__ routes with an
          overall length of __{{ response.result.km | integer }}__ km._
        </markdown>

        <ng-container
          *ngIf="large$ | async; then table; else list"
        ></ng-container>
        <ng-template #table>
          <kpn-subset-network-table [networks]="response.result.networks" />
        </ng-template>
        <ng-template #list>
          <kpn-subset-network-list [networks]="response.result.networks" />
        </ng-template>
      </div>
    </div>
  `,
  standalone: true,
  imports: [
    SubsetPageHeaderBlockComponent,
    ErrorComponent,
    NgIf,
    SituationOnComponent,
    MarkdownModule,
    SubsetNetworkTableComponent,
    SubsetNetworkListComponent,
    AsyncPipe,
    IntegerFormatPipe,
  ],
})
export class SubsetNetworksPageComponent implements OnInit {
  large$: Observable<boolean>;

  readonly apiResponse = this.store.selectSignal(selectSubsetNetworksPage);

  constructor(
    private store: Store,
    private pageWidthService: PageWidthService
  ) {
    this.large$ = pageWidthService.current$.pipe(
      map(() => this.pageWidthService.isVeryLarge())
    );
  }

  ngOnInit(): void {
    this.store.dispatch(actionSubsetNetworksPageInit());
  }
}
