import {createReducer, on} from "@ngrx/store";
import {Product} from "../../interface/product";
import {fetchRenewedBasketProducts, fetchRenewedFavouriteProducts} from "../action/user.action";

export interface IFavouriteProductsState {
  readonly favouriteProducts: Product[]
}

export interface IBasketProductsState {
  readonly basketProducts: Product[]
}

const initialFavouritesState: IFavouriteProductsState = {
  favouriteProducts: []
}

const initialBasketProducts: IBasketProductsState = {
  basketProducts: []
}

export const favouriteProductsReducer = createReducer(initialFavouritesState,
  on(fetchRenewedFavouriteProducts, (state, {httpResponse}) => (
    {...state, favouriteProducts: httpResponse.favouriteProducts})
  ));

export const basketProductsReducer = createReducer(initialBasketProducts,
  on(fetchRenewedBasketProducts, (state, {httpResponse}) => (
    {...state, basketProducts: httpResponse.basketProducts})
  ));
