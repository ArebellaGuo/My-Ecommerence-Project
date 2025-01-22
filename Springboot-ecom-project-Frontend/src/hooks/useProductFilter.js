import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useSearchParams } from "react-router-dom";
import { fetchProducts } from "../store/actions/fatchActions";

const useProductFilter = () =>{
    const [searchParams] = useSearchParams();
    const dispatch = useDispatch();

    useEffect(()=>{
        const params = new URLSearchParams();
        //add pageNumber to recent url
        const currentPage = searchParams.get("pageNumber") ? Number(searchParams.get("pageNumber")) : 1;
        params.set("pageNumber", (currentPage - 1));
        //add sortby to recent url
        params.set("sortBy","price")
        //add sortorder to recent url
        const sortOrder = searchParams.get("sortby") || "asc";
        params.set("sortOrder", sortOrder);
        //add category to recent url
        const categoryParams = searchParams.get("category") || null;
        if(categoryParams){
            params.set("category",categoryParams);
        }
        //add keyword to recent url
        const keyword = searchParams.get("keyword") || null;  
        if(keyword){
            params.set("keyword",keyword);
        }
        //convert recent url to a string
        const queryString = params.toString();
        console.log("QUERY STRING", queryString);
        // send querystring to endpoint
        dispatch(fetchProducts(queryString));
    },[dispatch, searchParams]);
};

export default useProductFilter;