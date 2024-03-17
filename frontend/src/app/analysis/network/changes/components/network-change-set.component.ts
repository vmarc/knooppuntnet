import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { ChangeHeaderComponent } from '@app/analysis/components/change-set';
import { NetworkChangeComponent } from './network-change.component';

@Component({
  selector: 'kpn-network-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="networkChangeInfo().key"
      [happy]="networkChangeInfo().happy"
      [investigate]="networkChangeInfo().investigate"
      [comment]="networkChangeInfo().comment"
    />
    <kpn-network-change [networkChangeInfo]="networkChangeInfo()" />
  `,
  standalone: true,
  imports: [ChangeHeaderComponent, NetworkChangeComponent],
})
export class NetworkChangeSetComponent {
  networkChangeInfo = input.required<NetworkChangeInfo>();
}
