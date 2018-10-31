import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../user.service";

@Component({
  selector: 'kpn-authenticate-page',
  templateUrl: './authenticate-page.component.html',
  styleUrls: ['./authenticate-page.component.scss']
})
export class AuthenticatePageComponent implements OnInit {

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userService.authenticated();
  }

}
