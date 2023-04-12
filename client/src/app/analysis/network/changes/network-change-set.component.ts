import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';

@Component({
  selector: 'kpn-network-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-change-header
      [changeKey]="networkChangeInfo.key"
      [happy]="networkChangeInfo.happy"
      [investigate]="networkChangeInfo.investigate"
      [comment]="networkChangeInfo.comment"
    />
    <kpn-network-change [networkChangeInfo]="networkChangeInfo" />
  `,
})
export class NetworkChangeSetComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;
}
