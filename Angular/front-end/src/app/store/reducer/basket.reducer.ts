import { createReducer, on } from "@ngrx/store";
import { storeDiscountedTotal } from "../action/basket.action";

export interface IDiscountedTotalState {
  readonly total: number
}

const initialState: IDiscountedTotalState = {
  total: undefined
}

export const discountedTotalReducer = createReducer(initialState,
  on(storeDiscountedTotal, (state, { total }) => (
    { ...state, total: total })
  ));
