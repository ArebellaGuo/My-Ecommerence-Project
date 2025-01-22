import { useState } from "react";
import { FaShoppingCart } from "react-icons/fa";
import { truncateText } from "../../utils/truncateText";
import ProductViewModel from "./ProductViewModel";
import { useDispatch } from "react-redux";
import { addToCart } from "../../store/actions/fatchActions";
import toast from "react-hot-toast";
import { formatPrice } from "../../utils/formatPrice";
/**
 * 
 *  productId,productName,image,description, quantity,price
 */
const ProductCard = ({ productId, productName, image, description, quantity, price }) => {
  const product = {
    productId: productId,
    productName: productName,
    image: image,
    description: description,
    quantity: quantity,
    price: price,
  };
  const [openProductViewModel, setOpenProductViewModel] = useState(false);
  const btnLoader = false;
  const [selectedViewProduct, setSelectedViewProduct] = useState("");
  const isAvailable = quantity && Number(quantity) > 0;
  const dispatch = useDispatch();

  const handleProductView = (product) => {
    setSelectedViewProduct(product);
    setOpenProductViewModel(true);
  };

  const addToCartHandler = (cartItems) =>{
    dispatch(addToCart(cartItems,1,toast));
  };

  return (
    <div className="border rounded-lg shadow-xl overflow-hidden transition-shadow duration-300">
      {/* Image Section */}
      <div onClick={() => {handleProductView(product);}} className="w-full overflow-hidden aspect-[3/2] ">
        <img className="w-full h-full cursor-pointer transition-transform duration-300 transaform hover:scale-105"
          src={image}
          alt={productName} />
      </div>

      {/* Details Section */}
      <div className="p-4">
        <h2 onClick={() => { handleProductView(product); }} className="text-lg font-semibold mb-2 cursor-pointer">
          {truncateText(productName, 50)}
        </h2>

        <div className="min-h-20 max-h-20">
          <p className="text-gray-600 text-sm">
            {truncateText(description, 90)}
          </p>
        </div>

        <div className="flex flex-col">
          <span className="text-xl font-bold text-slate-700 ">
            {formatPrice(Number(price)) }
          </span>
        </div>

        <button
          disabled={!isAvailable || btnLoader}
          onClick={() =>addToCartHandler(product)}
          className={`flex items-center justify-center w-36 py-2 px-3 rounded-lg text-white transition-colors duration-300 ${isAvailable
              ? "bg-blue-500 hover:bg-blue-600"
              : "bg-gray-400 cursor-not-allowed"
            }`}
        >
          <FaShoppingCart className="mr-2" />
          {isAvailable ? "Add to cart" : "Stock out!"}
        </button>

        <ProductViewModel
          open={openProductViewModel}
          setOpen={setOpenProductViewModel}
          product={selectedViewProduct}
          isAvailable={isAvailable}
        />
      </div>
    </div>
  );
};

export default ProductCard;
