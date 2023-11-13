import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { NetworkAttributes } from '@api/common/network';
import { IconHappyComponent } from '@app/components/shared/icon';
import { InterpretedNetworkAttributes } from './interpreted-network-attributes';

@Component({
  selector: 'kpn-subset-network-happy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-icon-happy *ngIf="interpretedNetwork.happy()"></kpn-icon-happy>
    <kpn-icon-happy *ngIf="interpretedNetwork.veryHappy()" class="very-happy" />
  `,
  styles: [
    `
      .very-happy {
        padding-left: 5px;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, IconHappyComponent],
})
export class SubsetNetworkHappyComponent implements OnInit {
  @Input() network: NetworkAttributes;
  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
