import {createFeatureSelector, createSelector} from "@ngrx/store";
import {State} from "../reducer/auth.reducer";

const selectFeatureUser = createFeatureSelector<State>('auth');

const selectFeatureLoginStatus = createFeatureSelector<boolean>('authStatus');

export const selectUser = createSelector(
  selectFeatureUser,
  (user) => {
    return user;
  });

export const selectLoginStatus = createSelector(selectFeatureLoginStatus,
  (status) => {
    return status
  });
