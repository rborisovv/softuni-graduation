import {createAction, props} from "@ngrx/store";
import {HttpResponse} from "../../interface/http.response";

export const addToFavourites = createAction('[Favourites] Add to Favourites', props<{ identifier: string }>());

export const addToFavouritesSuccess = createAction('[Favourites] Add to Favourites', props<{ httpResponse: HttpResponse }>());

export const fetchRenewedFavouriteProducts = createAction('[Favourites] Fetch Renewed Favourite Products', props<{ httpResponse: HttpResponse }>());

export const addToFavouritesFail = createAction('[Favourites] Add to Favourites', props<{ error: Error }>());

export const removeFromFavourites = createAction('[Favourites] Remove from Favourites', props<{ identifier: string }>());

export const removeFromFavouritesSuccess = createAction('[Favourites] Remove from Favourites', props<{ httpResponse: HttpResponse }>());

export const removeFromFavouritesFail = createAction('[Favourites] Remove from Favourites', props<{ error: Error }>());

export const addToBasket = createAction('[Basket] Add to Basket', props<{ identifier: string }>());

export const addToBasketSuccess = createAction('[Basket] Add to Basket', props<{ httpResponse: HttpResponse }>());

export const addToBasketFail = createAction('[Basket] Add to Basket', props<{ error: Error }>());

export const fetchRenewedBasketProducts = createAction('[Basket] Fetch Renewed Basket Products', props<{ httpResponse: HttpResponse }>());

export const removeFromBasket = createAction('[Basket] Remove From Basket', props<{ identifier: string }>());

export const removeFromBasketSuccess = createAction('[Basket] Remove From Basket', props<{ httpResponse: HttpResponse }>());

export const removeFromBasketFail = createAction('[Basket] Remove From Basket', props<{ error: Error }>());
