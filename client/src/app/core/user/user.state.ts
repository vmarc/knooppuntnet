export const initialUserState: UserState = {
  user: null,
  loginCallbackPage: null,
};

export interface UserState {
  user: string; // when non-null, the user is logged in
  loginCallbackPage: string;
}
