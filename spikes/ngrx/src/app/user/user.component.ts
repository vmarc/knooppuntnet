import {Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {Observable} from 'rxjs';
import {AppState} from '../store/app.state';
import {actionUserAdd} from '../store/user/user.actions';
import {selectUsers} from '../store/user/user.selectors';
import {User} from '../store/user/user.state';

@Component({
  selector: 'app-user',
  template: `
    <button (click)="addUser()">Add</button>
    <p>
      Users:
    </p>
    <table>
      <tr>
        <th>
          Id
        </th>
        <th>
          Name
        </th>
        <th>
          Age
        </th>
      </tr>
      <tr *ngFor="let user of users$ | async">
        <td>
          {{user.id}}
        </td>
        <td>
          {{user.name}}
        </td>
        <td>
          {{user.age}}
        </td>
      </tr>
    </table>
  `,
  styles: [`
    table {
      border-collapse: collapse;
    }

    th, td {
      border: solid 1px gray;
      padding: 5px;
    }

  `]
})
export class UserComponent {

  users$: Observable<User[]> = this.store.pipe(
    select(selectUsers)
  );

  constructor(private store: Store<AppState>) {
  }

  addUser(): void {
    this.store.dispatch(actionUserAdd());
  }

}


