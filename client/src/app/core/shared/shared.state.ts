export const initialSharedState: SharedState = {
  httpError: null,
};

export interface SharedState {
  httpError: string;
}
