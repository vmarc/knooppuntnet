import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { ChangeHeaderComponent } from '@app/analysis/components/change-set/change-header.component';
import { NetworkChangeComponent } from './network-change.component';

@Component({
  selector: 'ui-network-change-set',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-change-header
      [changeKey]="networkChangeInfo().key"
      [happy]="networkChangeInfo().happy"
      [investigate]="networkChangeInfo().investigate"
      [comment]="networkChangeInfo().comment"
    />
    <ui-network-change [networkChangeInfo]="networkChangeInfo()" />
  `,
  imports: [ChangeHeaderComponent, NetworkChangeComponent],
})
export class NetworkChangeSetComponent {
  readonly networkChangeInfo = input.required<NetworkChangeInfo>();
}
