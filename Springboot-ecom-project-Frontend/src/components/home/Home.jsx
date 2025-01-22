import { useDispatch, useSelector } from "react-redux";
import HomeBanners from "./HeroBanner";
import { useEffect } from "react";
import { fetchProducts } from "../../store/actions/fatchActions";
import ProductCard from "../shared/ProductCard";
import { Link } from "react-router-dom";

const Home = () => {
    const dispatch = useDispatch();
    const { products } = useSelector((state) => state.products);


    useEffect(() => {
        dispatch(fetchProducts());
    }, [dispatch]);
    return (
        <div className="lg:px-14 sm:px-8 px-4">
            <div className="py-6">
                <HomeBanners />
            </div>

            <div className="py-5">
                <div className="flex flex-col justify-center items-center space-y-2">
                    <h1 className="text-slate-700 text-2xl font-bold">
                        Products
                    </h1>
                    <span className="text-slate-700">
                        Discover our selection of top-rated items just for you!
                    </span>
                </div>
            </div>
            <div className="pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6">
                {products && products?.slice(0, 8)
                    .map((item, i) =>
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
            <div className="flex justify-end">
            <button className="bg-blue-500 text-white text-lg font-semibold py-3 px-6 rounded-md shadow-md hover:bg-blue-700 transition-transform transform hover:scale-105">
            <Link to='/products'>Find out more...</Link>
    </button>
           
</div>
            
        </div>

    )
}

export default Home;