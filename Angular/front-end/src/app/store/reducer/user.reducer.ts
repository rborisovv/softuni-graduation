import {createReducer, on} from "@ngrx/store";
import {Product} from "../../interface/product";
import {fetchRenewedFavouriteProducts} from "../action/user.action";

export interface IFavouriteProductsState {
  readonly favouriteProducts: Product[]
}

const initialState: IFavouriteProductsState = {
  favouriteProducts: []
}

export const favouriteProductsReducer = createReducer(initialState,
  on(fetchRenewedFavouriteProducts, (state, {httpResponse}) => (
    {...state, favouriteProducts: httpResponse.favouriteProducts})
  ));
