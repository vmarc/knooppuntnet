import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { input } from '@angular/core';
import { NetworkAttributes } from '@api/common/network/network-attributes';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { InterpretedNetworkAttributes } from './interpreted-network-attributes';

@Component({
  selector: 'ui-subset-network-happy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (interpretedNetwork.happy()) {
      <ui-icon-happy />
    }
    @if (interpretedNetwork.veryHappy()) {
      <ui-icon-happy class="very-happy" />
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
  readonly network = input.required<NetworkAttributes>();

  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network());
  }
}
