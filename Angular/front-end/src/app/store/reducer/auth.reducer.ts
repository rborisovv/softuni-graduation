import {createReducer, on} from "@ngrx/store";
import {loginActionSuccess} from "../action/auth.action";

export interface IUserState {
  readonly username: string,
  readonly email: string
}

const initialState: IUserState = {
  username: '',
  email: ''
}

export const authReducer = createReducer(initialState,
  on(loginActionSuccess, (state, {username, email}) => (
    {...state, username: username, email: email})
  ));
