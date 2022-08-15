export const initialUserState: UserState = {
  user: null,
  returnUrl: null,
};

export interface UserState {
  user: string; // when non-null, the user is logged in
  returnUrl: string; // url of page to return to after login or logout
}
