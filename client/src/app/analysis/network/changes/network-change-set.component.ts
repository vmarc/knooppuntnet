import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';

@Component({
  selector: 'kpn-network-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="networkChangeInfo.key"
      [happy]="networkChangeInfo.happy"
      [investigate]="networkChangeInfo.investigate"
      [comment]="networkChangeInfo.comment"
    >
    </kpn-change-header>
    <kpn-network-change
      [networkChangeInfo]="networkChangeInfo"
    ></kpn-network-change>
  `,
})
export class NetworkChangeSetComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;
}
