import {Component, Input, OnInit} from "@angular/core";
import {NetworkAttributes} from "../../../kpn/shared/network/network-attributes";
import {InterpretedNetworkAttributes} from "./interpreted-network-attributes";

@Component({
  selector: "kpn-subset-network-happy",
  template: `
    <mat-icon svgIcon="happy" *ngIf="interpretedNetwork.happy()"></mat-icon>
    <mat-icon svgIcon="happy" *ngIf="interpretedNetwork.veryHappy()" class="very-happy"></mat-icon>
  `,
  styles: [`
    .very-happy {
      padding-left: 5px;
    }
  `]
})
export class SubsetNetworkHappyComponent implements OnInit {

  @Input() network: NetworkAttributes;
  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit(): void {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
