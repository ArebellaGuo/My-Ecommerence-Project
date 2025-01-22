import { Pagination } from "@mui/material";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";


const PaginationConfig = ({numberOfPage, totalProducts}) =>{
    const [searchParams] = useSearchParams();
    const params = new URLSearchParams(searchParams);

    const pathname = useLocation().pathname;
    const navigate = useNavigate();
    //get current page number
    const currentPageNumber = searchParams.get("pageNumber") ? Number(searchParams.get("pageNumber")) : 1;
    
    const onChangeHandler = (event, value) =>{
        params.set("pageNumber", value.toString());
        navigate(`${pathname}?${params}`);
    }
     // Ensure numberOfPage is a valid number
    const totalPages = numberOfPage ? Number(numberOfPage) : 1;

    return(
        <Pagination 
        count={totalPages} 
        page={currentPageNumber}
        defaultPage={1} 
        siblingCount={0} 
        boundaryCount={2}
        onChange={onChangeHandler} />
    )
}

export default PaginationConfig;