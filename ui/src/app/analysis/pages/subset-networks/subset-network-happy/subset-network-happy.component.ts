import {Component, Input, OnInit} from '@angular/core';
import {NetworkAttributes} from "../../../../kpn/shared/network/network-attributes";
import {InterpretedNetworkAttributes} from "../interpreted-network-attributes";

@Component({
  selector: 'kpn-subset-network-happy',
  templateUrl: './subset-network-happy.component.html',
  styleUrls: ['./subset-network-happy.component.scss']
})
export class SubsetNetworkHappyComponent implements OnInit {

  @Input() network: NetworkAttributes;
  interpretedNetwork: InterpretedNetworkAttributes;

  ngOnInit() {
    this.interpretedNetwork = new InterpretedNetworkAttributes(this.network);
  }
}
