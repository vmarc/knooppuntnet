import { Component, OnInit } from '@angular/core';
import {AppService} from "../../../../app.service";

@Component({
  selector: 'kpn-changes-page',
  templateUrl: './changes-page.component.html',
  styleUrls: ['./changes-page.component.scss']
})
export class ChangesPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.changes().subscribe(content => {
      this.content = content;
    });
  }

}
