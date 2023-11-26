import { Component } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'kpn-logout',
  standalone: true,
  template: `
    <h1>Logout</h1>
    <p>Here goes some explanation about the logout process.</p>
    <div class="menu top-spacer">
      <button (click)="logout()">logout</button>
      <a (click)="cancel()">cancel</a>
    </div>
  `,
})
export class LogoutComponent {
  constructor(private userService: UserService) {}

  logout(): void {
    this.userService.logout();
  }

  cancel(): void {
    this.userService.cancelLogout();
  }
}
