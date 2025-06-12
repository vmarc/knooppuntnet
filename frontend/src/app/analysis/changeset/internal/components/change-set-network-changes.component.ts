import { DOCUMENT } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChangeSetDetail } from '@api/common/changes/change-set-detail';
import { LinkNetworkDetailsComponent } from '@app/shared/components/link/link-network-details.component';
import { Subscriptions } from '@app/util/subscriptions';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { CsNcComponent } from './network/cs-nc.component';

@Component({
  selector: 'ui-change-set-network-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (networkChangeInfo of detail().networkChanges; track networkChangeInfo) {
      <div class="kpn-level-1">
        <!-- eslint-disable-next-line @angular-eslint/template/elements-content -->
        <a [id]="networkChangeInfo.networkId"></a>
        <div class="kpn-level-1-header">
          <div class="kpn-line">
            <nz-icon [nzType]="networkChangeInfo.routeType" />
            <span i18n="@@change-set.network-changes.network">Network</span>
            <ui-link-network-details
              [networkId]="networkChangeInfo.networkId"
              [routeType]="networkChangeInfo.routeType"
              [networkName]="networkChangeInfo.networkName"
            />
          </div>
        </div>
        <div class="kpn-level-1-body">
          <ui-cs-nc-component [detail]="detail()" [networkChangeInfo]="networkChangeInfo" />
        </div>
      </div>
    }
  `,
  imports: [CsNcComponent, LinkNetworkDetailsComponent, NzIconDirective],
})
export class ChangeSetNetworkChangesComponent implements OnDestroy, AfterViewInit {
  readonly detail = input.required<ChangeSetDetail>();

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
