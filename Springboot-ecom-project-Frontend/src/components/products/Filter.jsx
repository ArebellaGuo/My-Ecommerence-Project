import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import { useEffect, useState } from "react";
import { FiArrowDown, FiArrowUp, FiRefreshCcw, FiSearch } from "react-icons/fi";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";
import Tooltip from '@mui/material/Tooltip';
import { Button } from "@headlessui/react";

const Filter = ({ categories }) => {

    // from router-dom : useSearchParams(), useLocation(),useNavigate(),
    //handle query parameters in the URL
    const [searchParams] = useSearchParams();
    //get current url/location name
    const pathname = useLocation().pathname;
    const navigate = useNavigate();

    const [category, setCategory] = useState("all");
    const [sortOrder, setSortOrder] = useState("asc");
    //the value of keyword
    const [searchTerm, setSearchTerm] = useState("");

    useEffect(() => {
        //if category in URL, we get the value of searchparam, or else we set the current category as all
        const currentCategory = searchParams.get("category") || "all";
        const currentSortOrder = searchParams.get("sortby") || "asc";
        const currentSearchTerm = searchParams.get("keyword") || "";

        setCategory(currentCategory);
        setSortOrder(currentSortOrder);
        setSearchTerm(currentSearchTerm);
    }, [searchParams])

    useEffect(() => {
        //setTimeout: update the URL with the keyword parameter only after a delay of 700ms.
        const handler = setTimeout(() => {
            const params = new URLSearchParams(searchParams);
            if (searchTerm) {
                params.set("keyword", searchTerm);
            } else {
                params.delete("keyword");
            }
            navigate(`${pathname}?${params.toString()}`);
        }, 700);
        return () => {
            clearTimeout(handler);
        }
    }, [searchParams, searchTerm, navigate, pathname]);

    const handleCategoryChange = (event) => {
        const selectedCategory = event.target.value;
        const params = new URLSearchParams(searchParams);
        if (selectedCategory === "all") {
            params.delete("category");
        } else {
            params.set("category", selectedCategory);
        }
        navigate(`${pathname}?${params.toString()}`);
        setCategory(event.target.value);
    }

    const toggleSortOrder = () => {
        setSortOrder((prevOrder) => {
            const newOrder = (prevOrder === "asc") ? "desc" : "asc";
            const params = new URLSearchParams(searchParams);
            params.set("sortby", newOrder);
            navigate(`${pathname}?${params.toString()}`);
            return newOrder;
        })
    }

    const handleClearFilters = () => {
        navigate(pathname);
    }

    return (
        <div className="flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4">
            <div className="relative flex items-center 2xl:w-[450px] sm:w-[420px] w-full">
                <input type="text" placeholder="Search Products" className="border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-[#1976d2]"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)} /
                >
                <FiSearch className="absolute left-3 text-slate-800 size={20}" />
            </div>

            <div className="flex sm:flex-row flex-col gap-12 items-center w-full">
                <FormControl className="text-slate-800 border-slate-700 w-full " variant="outlined" size="small" >
                <InputLabel id="category-select-label" className="text-slate-800">Category</InputLabel>
                    <Select labelId="category-select-label" value={category} onChange={handleCategoryChange} label="Category" className="min-w-[12] text-slate-800 border-slate-700">
                        <MenuItem value="all">
                        All
                        </MenuItem>

                        {categories.map((c) => (
                            <MenuItem key={c.categoryId} value={c.categoryName}>
                                {c.categoryName}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <Tooltip title={`Sorted by price: ${sortOrder.toString()}`}  >
                    <Button variant="contained"
                        onClick={toggleSortOrder}
                        color="primary"
                        className="flex items-center gap-5 h-10 whitespace-nowrap">
                        Sort By
                        {sortOrder === "asc" ? (
                            <FiArrowUp size={20} />
                        ) : (
                            <FiArrowDown size={20} />
                        )}
                    </Button>
                </Tooltip>

                <button className="flex items-center gap-2 bg-blue-500 text-white px-2 py-2 rounded-md transition duration-100 ease-in shadow-md focus:outline-none"
                onClick={handleClearFilters}>
                    <FiRefreshCcw className="font-semibold size={20}" />
                    <span className="font-semibold w-full whitespace-nowrap">Clear filter</span>
                </button>


            </div>
        </div>
    )

}
export default Filter;