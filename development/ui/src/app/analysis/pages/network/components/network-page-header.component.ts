import {Component, Input} from '@angular/core';
import {NetworkCacheService} from "../../../../services/network-cache.service";

@Component({
  selector: 'kpn-network-page-header',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      Network
    </div>

    <h1>
      {{networkName()}}
    </h1>

    <div>
      <a [ngClass]="{'selected': selectedPage == 'details'}" [routerLink]="'/analysis/network-details/' + networkId">Details</a> |
      <a [ngClass]="{'selected': selectedPage == 'facts'}" [routerLink]="'/analysis/network-facts/' + networkId">Facts</a> <span class="kpn-thin"> (123)</span> |
      <a [ngClass]="{'selected': selectedPage == 'nodes'}" [routerLink]="'/analysis/network-nodes/' + networkId">Nodes</a>  <span class="kpn-thin"> (123)</span> |
      <a [ngClass]="{'selected': selectedPage == 'routes'}" [routerLink]="'/analysis/network-routes/' + networkId">Routes</a> <span class="kpn-thin"> (123)</span> |
      <a [ngClass]="{'selected': selectedPage == 'map'}" [routerLink]="'/analysis/network-map/' + networkId">Map</a> |
      <a [ngClass]="{'selected': selectedPage == 'changes'}" [routerLink]="'/analysis/network-changes/' + networkId">History</a>
    </div>
    <mat-divider></mat-divider>
  `,
  styles: [`
    
    .selected {
      color: rgba(0, 0, 0, 0.87);
      font-weight: bold;
    }
    
    mat-divider {
      margin-top: 10px;
      margin-bottom: 50px;
    }
    
  `]
})
export class NetworkPageHeaderComponent {

  @Input() networkId;
  @Input() selectedPage;

  constructor(private networkCacheService: NetworkCacheService) {
  }

  isNetworkNameKnown(): boolean {
    return this.networkId && this.networkCacheService.getNetworkName(this.networkId) !== undefined;
  }

  networkName(): string {
    return this.networkCacheService.getNetworkName(this.networkId);
  }

}
