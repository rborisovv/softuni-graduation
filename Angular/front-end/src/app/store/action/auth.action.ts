import {createAction, props} from "@ngrx/store";

export const loginAction = createAction('[AUTH] Login page', props<{ user: object }>());
export const authStatus = createAction('[AUTH] Login status', props<{ isLoggedIn: boolean }>());
