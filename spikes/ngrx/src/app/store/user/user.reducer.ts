import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {actionUserAdd} from './user.actions';
import {User} from './user.state';
import {initialState} from './user.state';

export const userReducer = createReducer(
  initialState,
  on(actionUserAdd, (state) => {
    const id = state.id;
    // const newUser = new User(id, 'name' + id, id * 10);  // this does not work: runtime-checks#strictstateserializability
    const newUser: User = {
      id,
      name: 'name' + id,
      age: id * 10
    };
    return {
      ...state,
      id: id + 1,
      users: {
        ...state.users,
        [newUser.id]: newUser
      }
    };
  })
);
