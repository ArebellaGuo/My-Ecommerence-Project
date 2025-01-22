import { Badge } from "@mui/material";
import { FaShoppingCart, FaSignInAlt, FaStore } from "react-icons/fa";
import { IoIosMenu } from "react-icons/io";
import { RxCross2 } from "react-icons/rx";
import { Link } from "react-router-dom";
import { useState } from "react";
import { useSelector } from "react-redux";

const Navbar = () =>{
    const [navbarOpen, setNavbarOpen] = useState(false);
    const {cart} = useSelector((state)=>state.carts);
    return(
        <div className="h-[70px] bg-custom-gradient text-white z-50 flex items-center sticky">
            <div className="lg:px-14 sm:px-8 px-4 w-full flex justify-between">
                <Link to="/" className="flex items-center text-2xl font-semibold">
                    <FaStore className="mr3 text-3xl"/>
                    <span className="font-[Poppins]">E-shop</span>
                </Link>
                <ul className={`flex sm:gap-10 gap-4 sm:items-center  text-slate-800 sm:static absolute left-0 top-[70px] sm:shadow-none shadow-md ${
            navbarOpen ? "h-fit sm:pb-0 pb-5" : "h-0 overflow-hidden"
          }  transition-all duration-100 sm:h-fit sm:bg-none bg-custom-gradient   text-white sm:w-fit w-full sm:flex-row flex-col px-4 sm:px-0`}>
                                    <li className="font-[500] transition-all duration-150">
                        <Link to='/'>Home</Link>
                    </li>
                    <li className="font-[500] transition-all duration-150">
                        <Link to='/products'>Products</Link>
                    </li>
                    <li className="font-[500] transition-all duration-150">
                        <Link to='/about'>About</Link>
                    </li>
                    <li className="font-[500] transition-all duration-150">
                        <Link to='/contact'>Contact</Link>
                    </li>
                    <li className="font-[500] transition-all duration-150">
                        <Link to='/cart'>
                        <Badge showZero
                                badgeContent={cart?.length || 0}
                                color="primary"
                                overlap="circular"
                                anchorOrigin={{vertical:'top',horizontal:'right'}}
                                >
                                    <FaShoppingCart size={25}/>
                        </Badge>
                        </Link>
                    </li>
                    <li className="font-[500] transition-all duration-150">
                        <Link to='/login' className="flex items-center space-x-2 px-4 py-[6px] 
                            bg-gradient-to-r from-purple-600 to-red-500 
                            text-white font-semibold rounded-md shadow-lg 
                            hover:from-purple-500 hover:to-red-400 transition 
                            duration-300 ease-in-out transform ">
                        <FaSignInAlt/>
                        <span>Login</span>
                        </Link>
                    </li>
                </ul>
                <button onClick={() => setNavbarOpen(!navbarOpen)}
                className="sm:hidden flex items-center sm:mt-0 mt-2">
                    {navbarOpen ? (
                        <RxCross2 className="text-white text-3xl" />
                    ) : (
                        <IoIosMenu className="text-white text-3xl" />
                    )}

                </button>
            </div>
        </div>
    )
}

export default Navbar;