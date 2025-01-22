import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./ProductReducer";
import { errorReducer } from "./errorReducer";
import { cartReducer } from "./cartReducer";

//get cart items data from local browser storage
const cartItems = localStorage.getItem("cartItems")
    ? JSON.parse(localStorage.getItem("cartItems"))
    :[];

const initialState = {
    carts:{cart:cartItems},
};

const store = configureStore({
    reducer:{
        products:productReducer,
        errors:errorReducer,
        carts:cartReducer
    },
    preloadedState: initialState,
});

export default store;