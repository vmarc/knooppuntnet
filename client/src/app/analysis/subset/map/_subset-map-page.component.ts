import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { Store } from '@ngrx/store';
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
    />

    <kpn-error/>

    <div *ngIf="response$ | async as response">
      <kpn-subset-map
        [bounds]="response.result.bounds"
        [networks]="response.result.networks"
        (networkClicked)="networkClicked($event, response.result.networks)"
      />
    </div>
  `,
})
export class SubsetMapPageComponent implements OnInit {
  readonly response$ = this.store.select(selectSubsetMapPage);

  constructor(private store: Store, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.store.dispatch(actionSubsetMapPageInit());
  }

  networkClicked(networkId: number, networks: Array<SubsetMapNetwork>): void {
    const network = networks.find((n) => n.id === networkId);
    if (network) {
      this.dialog.open(SubsetMapNetworkDialogComponent, {
        data: network,
        autoFocus: false,
        maxWidth: 600,
      });
    }
  }
}
