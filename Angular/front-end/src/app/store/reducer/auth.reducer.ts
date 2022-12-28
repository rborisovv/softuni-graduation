import {createReducer, on} from "@ngrx/store";
import {authStatus, loginAction} from "../action/auth.action";
import {state} from "@angular/animations";

export interface State {
  user?: {
    username: string,
    password: string,
    role: string
  }
}

const initialState: State = {
  user: {
    username: '',
    password: '',
    role: ''
  }
}

export const authReducer = createReducer(initialState,
  on(loginAction, (state, {user}) => user));

export const authStatusReducer = createReducer(false,
  on(authStatus, (state, {isLoggedIn}) => isLoggedIn));
