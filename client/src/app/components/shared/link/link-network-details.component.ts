import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NetworkType } from '@api/custom';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-link-network-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <a (click)="clicked()">{{ title }}</a> `,
})
export class LinkNetworkDetailsComponent {
  @Input() networkId: number;
  @Input() networkType: NetworkType;
  @Input() title: string;

  constructor(private store: Store, private router: Router) {}

  clicked(): void {
    // TODO replace with route based mechanism
    // this.store.dispatch(
    //   actionNetworkLink({
    //     networkId: this.networkId,
    //     networkName: this.title,
    //     networkType: this.networkType,
    //   })
    // );
    this.router.navigate(['/analysis/network/' + this.networkId]);
  }
}
