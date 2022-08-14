export const initialSharedState: SharedState = {
  httpError: null,
  language: null,
};

export interface SharedState {
  httpError: string;
  language: string;
}
