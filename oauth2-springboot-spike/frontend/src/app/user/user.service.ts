import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { UserStore } from './user.store';

@Injectable()
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly document = inject(DOCUMENT);
  private readonly userStore = inject(UserStore);

  constructor() {
    this.http.get('/oauth2/user', { responseType: 'text' }).subscribe((user) => {
      this.userStore.updateUser(user);
    });
  }

  login(): void {
    this.document.location.assign('/oauth2/authorization/osm');
  }

  logout(): void {
    this.http.post('/oauth2/logout', { responseType: 'text' }).subscribe({
      next: () => this.userStore.updateUser(null),
      error: () => this.userStore.updateUser(null),
    });
  }
}
