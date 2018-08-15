import {Component, Input, OnInit} from '@angular/core';
import {NetworkAttributes} from "../../../../kpn/shared/network/network-attributes";
import {InterpretedNetworkAttributes} from "../interpreted-network-attributes";

@Component({
  selector: 'kpn-subset-network',
  templateUrl: './subset-network.component.html',
  styleUrls: ['./subset-network.component.scss']
})
export class SubsetNetworkComponent implements OnInit {

  @Input() network: NetworkAttributes;
  interpretedNetwork: InterpretedNetworkAttributes;

  constructor() {
  }

  ngOnInit() {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
