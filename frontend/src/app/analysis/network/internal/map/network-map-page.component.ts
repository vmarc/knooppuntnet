import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NetworkMapComponent } from './components/network-map.component';
import { NetworkMapService } from './components/network-map.service';
import { NetworkMapPageService } from './network-map-page.service';

@Component({
  selector: 'ui-network-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.response()?.result; as page) {
      <ui-network-map [networkId]="service.networkId()" [page]="page" />
    }
  `,
  providers: [NetworkMapService, NetworkMapPageService],
  imports: [NetworkMapComponent],
})
export class NetworkMapPageComponent implements OnInit {
  protected readonly service = inject(NetworkMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
