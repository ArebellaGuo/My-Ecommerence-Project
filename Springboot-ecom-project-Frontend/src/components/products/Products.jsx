import { useDispatch, useSelector } from "react-redux";
import useProductFilter from "../../hooks/useProductFilter";
import { useEffect } from "react";
import { fetchCategories } from "../../store/actions/fatchActions";
import Filter from "./Filter";
import Loader from "../shared/Loader";
import ProductCard from "../shared/ProductCard";
import PaginationConfig from "../shared/PaginationConfig";

const Products = () => {
    const { isLoading, errorMessage } = useSelector((state) => state.errors);
    const { products, categories, pagination } = useSelector((state) => state.products);
    const dispatch = useDispatch();

    useProductFilter();

    useEffect(() => {
        dispatch(fetchCategories());
    }, [dispatch]);

    return (
        <div className="lg:px-14 sm:px-8 px-4 py-14 2xl:w-[90%] 2xl:mx-auto">
            <Filter categories={categories ? categories : []} />
            {isLoading ? (<Loader text={"Products Loading..."} />) :
                errorMessage ? (<p>Unexpected error...</p>) : (
                    <div className="min-h-[700px]">
                        <div className="pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6">
                        {products && products.map((item, i) =>
                                    <ProductCard
                                        key={i}
                                        productId={item.productId}
                                        productName={item.productName}
                                        image={item.image}
                                        description={item.description}
                                        quantity={item.quantity}
                                        price={item.price}
                                    />)}
                            </div>
                            <div className="flex justify-center pt-10">
                                <PaginationConfig
                                    numberOfPage={pagination?.totalPages || 1} // Ensure totalPages exists
                                    totalProducts={pagination?.totalElements || 0} // Ensure totalElements exists
                                />
                            </div>
                    </div>
                )
            }
        </div>
    )
}

export default Products;