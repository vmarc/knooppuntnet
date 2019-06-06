import {Component, Input, OnInit} from '@angular/core';
import {PoiInformation} from "../../../../model";

@Component({
  selector: 'app-poiinformation',
  templateUrl: './poiinformation.component.html',
  styleUrls: ['./poiinformation.component.scss']
})
export class PoiinformationComponent implements OnInit {

  @Input() poiInformation: PoiInformation;

  constructor() { }

  ngOnInit() {
  }

}
