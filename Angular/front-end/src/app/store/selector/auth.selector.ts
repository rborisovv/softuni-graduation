import {createFeatureSelector, createSelector} from "@ngrx/store";
import {IUserState} from "../reducer/auth.reducer";

export const selectUser = createFeatureSelector<IUserState>('auth');

export const selectUserState = createSelector(
  selectUser, (user) => {return user});
