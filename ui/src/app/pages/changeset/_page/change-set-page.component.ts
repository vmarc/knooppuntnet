import {Component, OnInit} from '@angular/core';
import {AppService} from "../../../app.service";

@Component({
  selector: 'kpn-change-set-page',
  templateUrl: './change-set-page.component.html',
  styleUrls: ['./change-set-page.component.css']
})
export class ChangeSetPageComponent implements OnInit {

  content = "Loading...";

  constructor(private appService: AppService) {
  }

  ngOnInit() {
    this.appService.changeSet(55067698, 2778253).subscribe(content => {
      this.content = content;
    });
  }

}
