import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { IconHappyComponent } from '@app/components/shared/icon';
import { InterpretedNetworkAttributes } from './interpreted-network-attributes';

@Component({
  selector: 'kpn-subset-network-happy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (interpretedNetwork.happy()) {
      <kpn-icon-happy></kpn-icon-happy>
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
  standalone: true,
  imports: [IconHappyComponent],
})
export class SubsetNetworkHappyComponent implements OnInit {
  @Input() network: NetworkAttributes;
  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
