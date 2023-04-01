import { createAction, props } from "@ngrx/store";

export const storeDiscountedTotal = createAction('[BASKET] Store Discounted Total', props<{ total: number }>());
