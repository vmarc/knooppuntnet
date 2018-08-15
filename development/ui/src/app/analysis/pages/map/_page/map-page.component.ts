import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'kpn-map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.scss']
})
export class MapPageComponent implements OnInit {

  networkType: string;

  constructor(private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.networkType = this.activatedRoute.snapshot.paramMap.get('networkType');
  }

}
