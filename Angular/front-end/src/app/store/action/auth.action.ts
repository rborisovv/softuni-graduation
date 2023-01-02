import {createAction, props} from "@ngrx/store";

export const loginAction = createAction('[AUTH] Login page', props<{ username: string, email: string }>());
