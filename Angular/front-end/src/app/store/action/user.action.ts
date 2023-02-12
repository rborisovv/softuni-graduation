import {createAction, props} from "@ngrx/store";
import {HttpResponse} from "../../interface/http.response";

export const addToFavourites = createAction('[Favourites] Category page', props<{ identifier: string }>());

export const addToFavouritesSuccess = createAction('[Favourites] Category page', props<{ httpResponse: HttpResponse }>());

export const addToFavouritesFail = createAction('[Favourites] Category page', props<{ error: Error }>());
