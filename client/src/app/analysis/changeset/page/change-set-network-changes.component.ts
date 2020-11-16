import {DOCUMENT} from '@angular/common';
import {ChangeDetectionStrategy} from '@angular/core';
import {Inject} from '@angular/core';
import {OnDestroy} from '@angular/core';
import {AfterViewInit} from '@angular/core';
import {Component, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ChangeSetPage} from '../../../kpn/api/common/changes/change-set-page';
import {Subscriptions} from '../../../util/Subscriptions';

@Component({
  selector: 'kpn-change-set-network-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let networkChangeInfo of page.networkChanges" class="kpn-level-1">
      <a [id]="networkChangeInfo.networkId"></a>
      <div class="kpn-level-1-header">
        <div class="kpn-line">
          <kpn-network-type-icon [networkType]="networkChangeInfo.networkType"></kpn-network-type-icon>
          <span i18n="@@change-set.network-changes.network">Network</span>
          <kpn-link-network-details [networkId]="networkChangeInfo.networkId" [title]="networkChangeInfo.networkName">
          </kpn-link-network-details>
        </div>
      </div>

      <div class="kpn-level-1-body">
        <kpn-cs-nc-component [page]="page" [networkChangeInfo]="networkChangeInfo"></kpn-cs-nc-component>
      </div>

    </div>
  `
})
export class ChangeSetNetworkChangesComponent implements OnDestroy, AfterViewInit {

  @Input() page: ChangeSetPage;

  private readonly subscriptions = new Subscriptions();

  constructor(private route: ActivatedRoute, @Inject(DOCUMENT) private document) {
  }

  ngAfterViewInit(): void {
    this.subscriptions.add(
      this.route.fragment.subscribe(fragment => {
        const anchor = this.document.getElementById(fragment);
        if (anchor) {
          const headerOffset = 80;
          const elementPosition = anchor.getBoundingClientRect().top;
          const offsetPosition = elementPosition - headerOffset;
          window.scrollTo({
            top: offsetPosition,
            behavior: 'smooth'
          });
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
