import {createFeatureSelector, createSelector} from "@ngrx/store";
import {IBasketProductsState, IFavouriteProductsState} from "../reducer/user.reducer";

export const selectFavouriteProducts = createFeatureSelector<IFavouriteProductsState>('favouriteProducts');

export const selectFavouriteProductsState = createSelector(
  selectFavouriteProducts, (favouriteProducts) => {
    return favouriteProducts.favouriteProducts
  });

export const selectBasketProducts = createFeatureSelector<IBasketProductsState>('basketProducts');

export const selectBasketProductsState = createSelector(
  selectBasketProducts, (basketProducts) => {
    return basketProducts.basketProducts;
  }
)
