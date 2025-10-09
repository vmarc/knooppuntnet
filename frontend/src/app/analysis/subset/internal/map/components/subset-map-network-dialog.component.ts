import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { NZ_MODAL_DATA } from 'ng-zorro-antd/modal';

@Component({
  selector: 'ui-subset-map-network-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-km">{{ network.km }}</div>
    <div>
      <span>{{ network.nodeCount }}</span
      >&nbsp;
      <span i18n="@@subset-map.dialog.nodeCount">nodes</span>
    </div>
    <div>
      <span>{{ network.routeCount }}</span
      >&nbsp;
      <span i18n="@@subset-map.dialog.routeCount">routes</span>
    </div>
    <div class="link">
      <a
        [routerLink]="link"
        (click)="closeDialog()"
        i18n="@@subset-map.dialog.show-network-details"
      >
        Show network details
      </a>
    </div>
  `,
  styles: `
    .link {
      padding-top: 2em;
    }
  `,
  imports: [RouterLink],
})
export class SubsetMapNetworkDialogComponent {
  private readonly modalRef = inject(NzModalRef);
  protected readonly network: SubsetMapNetwork = inject(NZ_MODAL_DATA);
  protected readonly link = '/analysis/network/' + this.network.id;

  closeDialog(): void {
    this.modalRef.close();
  }
}
