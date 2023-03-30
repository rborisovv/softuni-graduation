import { createAction, props } from "@ngrx/store";
import { IUserRegisterModel } from "../../authentication/register/IUserRegisterModel";
import { Voucher } from "../../model/voucher";
import { HttpResponse } from "../../interface/http.response";

export const loginAction = createAction('[AUTH] Login page', props<{ formData: FormData }>());

export const loginActionSuccess = createAction('[AUTH] Login page', props<{ username: string, email: string }>());

export const loginActionFail = createAction('[AUTH] Login page', props<{ error: Error }>());

export const registerAction = createAction('[AUTH] Register page', props<{ registerModel: IUserRegisterModel }>());

export const registerActionSuccess = createAction('[AUTH] Register page', props<{ username: string }>());

export const registerActionFail = createAction('[AUTH] Register page', props<{ error: Error }>());

export const createVoucher = createAction('[Voucher] Create Voucher', props<{ voucher: Voucher }>());

export const createVoucherSuccess = createAction('[Voucher] Create Voucher Success', props<{ response: HttpResponse }>());

export const createVoucherFail = createAction('[Voucher] Create Voucher Fail', props<{ error: Error }>());
