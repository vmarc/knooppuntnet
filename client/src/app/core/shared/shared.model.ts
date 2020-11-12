import {AppState} from "../core.state";

export interface SharedState {
  defaultNetworkType: string;
}

export interface State extends AppState {
  shared: SharedState;
}
