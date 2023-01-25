import {createFeatureSelector, createSelector} from "@ngrx/store";
import {Category} from "../../interface/category";

export const selectLoadedCategories = createFeatureSelector<Category[]>('loadCategories');

export const selectLoadedCategoryState = createSelector(
  selectLoadedCategories, (categories) => {
    return categories
  });
