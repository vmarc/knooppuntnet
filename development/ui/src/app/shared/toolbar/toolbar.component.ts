import { Component, OnInit } from '@angular/core';
import {PageService} from "../page.service";

@Component({
  selector: 'kpn-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
  }

  toggleSideNavOpen() {
    this.pageService.toggleSideNavOpen();
  }

}
