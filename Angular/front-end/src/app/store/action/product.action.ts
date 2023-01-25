import {createAction, props} from "@ngrx/store";
import {HttpResponse} from "../../interface/http.response";

export const deleteProductAction = createAction('[PRODUCT] Products page', props<{ identifier: string }>());

export const deleteProductActionSuccess = createAction('[PRODUCT] Products page', props<{ httpResponse: HttpResponse }>());

export const deleteProductActionFail = createAction('[ACTION] Products page', props<{ error: Error }>());
