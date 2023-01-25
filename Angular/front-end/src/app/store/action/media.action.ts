import {createAction, props} from "@ngrx/store";
import {HttpResponse} from "../../interface/http.response";

export const createMediaAction = createAction('[MEDIA] Create media page', props<{ formData: FormData }>());

export const createMediaActionSuccess = createAction('[MEDIA] Create media page', props<{ httpResponse: HttpResponse }>());

export const createMediaActionFail = createAction('[MEDIA] Create media page', props<{ error: Error }>());

export const deleteMediaAction = createAction('[MEDIA] Medias page', props<{ pk: string }>());

export const deleteMediaActionSuccess = createAction('[MEDIA] Medias page', props<{ httpResponse: HttpResponse }>());

export const deleteMediaActionFail = createAction('[MEDIA] Medias page', props<{ error: Error }>());
