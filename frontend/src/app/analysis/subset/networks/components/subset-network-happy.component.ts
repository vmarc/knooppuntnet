import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { InterpretedNetworkAttributes } from './interpreted-network-attributes';

@Component({
  selector: 'kpn-subset-network-happy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (interpretedNetwork.happy()) {
      <kpn-icon-happy />
    }
    @if (interpretedNetwork.veryHappy()) {
      <kpn-icon-happy class="very-happy" />
    }
  `,
  styles: `
    .very-happy {
      padding-left: 5px;
    }
  `,
  imports: [IconHappyComponent],
})
export class SubsetNetworkHappyComponent implements OnInit {
  network = input.required<NetworkAttributes>();

  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network());
  }
}
