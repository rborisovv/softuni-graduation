import {createAction, props} from "@ngrx/store";
import {HttpResponse} from "../../interface/http.response";

export const createCategoryAction = createAction('[CATEGORY] Create category page', props<{ formData: FormData }>());

export const createCategoryActionSuccess = createAction('[CATEGORY] Create category page', props<{ httpResponse: HttpResponse }>());

export const createCategoryActionFail = createAction('[CATEGORY] Create category page', props<{ error: Error }>());

export const updateCategoryAction = createAction('[CATEGORY] Update category page', props<{ formData: FormData }>());

export const updateCategoryActionSuccess = createAction('[CATEGORY] Update category page', props<{ httpResponse: HttpResponse }>());

export const updateCategoryActionFail = createAction('[CATEGORY] Update category page', props<{ error: Error }>());

export const deleteCategoryAction = createAction('[CATEGORY] Categories page', props<{ identifier: string }>());

export const deleteCategoryActionSuccess = createAction('[CATEGORY] Categories page', props<{ httpResponse: HttpResponse }>());

export const deleteCategoryActionFail = createAction('[CATEGORY] Categories page', props<{ error: Error }>());
