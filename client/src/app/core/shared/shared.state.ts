export const initialSharedState: SharedState = {
  httpError: null,
  user: null,
  language: null,
  loginCallbackPage: null,
};

export interface SharedState {
  httpError: string;
  user: string; // when non-null, the user is logged in
  language: string;
  loginCallbackPage: string;
}
