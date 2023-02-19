import {createFeatureSelector, createSelector} from "@ngrx/store";
import {IFavouriteProductsState} from "../reducer/user.reducer";

export const selectFavouriteProducts = createFeatureSelector<IFavouriteProductsState>('favouriteProducts');

export const selectFavouriteProductsState = createSelector(
  selectFavouriteProducts, (favouriteProducts) => {
    return favouriteProducts.favouriteProducts
  });
