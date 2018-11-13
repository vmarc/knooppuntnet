import {Component, Input, OnInit} from '@angular/core';
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-subset-sidenav',
  templateUrl: './subset-sidenav.component.html',
  styleUrls: ['./subset-sidenav.component.scss']
})
export class SubsetSidenavComponent {

  @Input() subset: Subset;

}
