import { DOCUMENT } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChangeSetPage } from '@api/common/changes';
import { NetworkTypeIconComponent } from '@app/components/shared';
import { LinkNetworkDetailsComponent } from '@app/components/shared/link';
import { Subscriptions } from '@app/util';
import { CsNcComponent } from './network/cs-nc.component';

@Component({
  selector: 'kpn-change-set-network-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (networkChangeInfo of page().networkChanges; track networkChangeInfo) {
      <div class="kpn-level-1">
        <a [id]="networkChangeInfo.networkId"></a>
        <div class="kpn-level-1-header">
          <div class="kpn-line">
            <kpn-network-type-icon [networkType]="networkChangeInfo.networkType" />
            <span i18n="@@change-set.network-changes.network">Network</span>
            <kpn-link-network-details
              [networkId]="networkChangeInfo.networkId"
              [networkType]="networkChangeInfo.networkType"
              [networkName]="networkChangeInfo.networkName"
            />
          </div>
        </div>
        <div class="kpn-level-1-body">
          <kpn-cs-nc-component [page]="page()" [networkChangeInfo]="networkChangeInfo" />
        </div>
      </div>
    }
  `,
  standalone: true,
  imports: [CsNcComponent, LinkNetworkDetailsComponent, NetworkTypeIconComponent],
})
export class ChangeSetNetworkChangesComponent implements OnDestroy, AfterViewInit {
  page = input.required<ChangeSetPage>();

  private readonly route = inject(ActivatedRoute);
  private readonly document = inject(DOCUMENT);
  private readonly subscriptions = new Subscriptions();

  ngAfterViewInit(): void {
    this.subscriptions.add(
      this.route.fragment.subscribe((fragment) => {
        const anchor = this.document.getElementById(fragment);
        if (anchor) {
          const headerOffset = 80;
          const elementPosition = anchor.getBoundingClientRect().top;
          const offsetPosition = elementPosition - headerOffset;
          window.scrollTo({
            top: offsetPosition,
            behavior: 'smooth',
          });
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
