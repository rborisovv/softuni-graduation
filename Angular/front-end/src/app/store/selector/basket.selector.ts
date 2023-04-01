import { createFeatureSelector, createSelector } from "@ngrx/store";
import { IDiscountedTotalState } from "../reducer/basket.reducer";

export const selectDiscountedTotal = createFeatureSelector<IDiscountedTotalState>('discountedTotal');

export const selectDiscountedTotalState = createSelector(
  selectDiscountedTotal, (total) => {
    return total.total !== undefined ? total : undefined;
  });
