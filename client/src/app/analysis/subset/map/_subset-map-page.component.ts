import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { Store } from '@ngrx/store';
import { PageService } from '../../../components/shared/page.service';
import { AppState } from '../../../core/core.state';
import { actionSubsetMapPageInit } from '../store/subset.actions';
import { selectSubsetMapPage } from '../store/subset.selectors';
import { SubsetMapNetworkDialogComponent } from './subset-map-network-dialog.component';

@Component({
  selector: 'kpn-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-subset-page-header-block
      pageName="map"
      pageTitle="Map"
      i18n-pageTitle="@@subset-map.title"
    ></kpn-subset-page-header-block>

    <kpn-error></kpn-error>

    <div *ngIf="response$ | async as response">
      <kpn-subset-map
        [bounds]="response.result.bounds"
        [networks]="response.result.networks"
        (networkClicked)="networkClicked($event, response.result.networks)"
      ></kpn-subset-map>
    </div>
  `,
})
export class SubsetMapPageComponent implements OnInit, OnDestroy {
  readonly response$ = this.store.select(selectSubsetMapPage);

  constructor(
    private store: Store<AppState>,
    private pageService: PageService,
    private dialog: MatDialog
  ) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.store.dispatch(actionSubsetMapPageInit());
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }

  networkClicked(networkId: number, networks: Array<SubsetMapNetwork>): void {
    const network = networks.find((n) => n.id === networkId);
    if (network) {
      this.dialog.open(SubsetMapNetworkDialogComponent, {
        data: network,
        maxWidth: 600,
      });
    }
  }
}
