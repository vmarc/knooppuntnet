import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NetworkNotFoundComponent } from '@app/analysis/network/internal/components/network-not-found.component';
import { NetworkPageHeaderComponent } from '@app/analysis/network/internal/components/network-page-header.component';
import { NetworkService } from '@app/analysis/network/internal/network.service';
import { PageComponent } from '@app/shared/components/page/page.component';

@Component({
  selector: 'ui-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-network-page-header [pageName]="pageName()" />
      @if (networkNotFound()) {
        <div class="kpn-error">
          <ui-network-not-found />
        </div>
      }
      <router-outlet />
    </ui-page>
  `,
  imports: [PageComponent, RouterOutlet, NetworkNotFoundComponent, NetworkPageHeaderComponent],
})
export class NetworkComponent implements OnInit {
  private networkService = inject(NetworkService);

  readonly networkId = input<number>();

  protected readonly pageName = this.networkService.pageName;
  protected readonly networkNotFound = this.networkService.networkNotFound;

  ngOnInit(): void {
    this.networkService.onInit(this.networkId());
  }
}
