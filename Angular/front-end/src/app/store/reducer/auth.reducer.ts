import {createReducer, on} from "@ngrx/store";
import {loginAction} from "../action/auth.action";

export interface IUserState {
  username: string,
  email: string
}

const initialState: IUserState = {
  username: '',
  email: ''
}

export const authReducer = createReducer(initialState,
  on(loginAction, (state, {username, email}) => ({...state, username: username, email: email})));
