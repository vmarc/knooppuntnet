export class User {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly age: number
  ) {
  }
}

export interface Users {
  [id: number]: User;
}

export interface UserState {
  id: number,
  users: Users
}

const users: Users = {
  1: {
    id: 1,
    name: 'user1',
    age: 10
  },
  2: {
    id: 2,
    name: 'user2',
    age: 20
  }
};

export const initialState: UserState = {
  id: 3,
  users
};
