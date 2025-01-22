import Products from './components/products/Products'; 
import About from './components/shared/About';
import Contact from './components/shared/Contact';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from "./components/home/Home";
import Navbar from './components/shared/Navbar';
import { Toaster } from 'react-hot-toast';
import React from 'react';
import Cart from './components/cart/Cart';
import LogIn from './components/authen/Login';
import PrivateRoute from './components/shared/PrivateRoute';
import Register from './components/authen/Register';
function App() {

  return (
    <React.Fragment>
    <Router>
      <Navbar/>
      <Routes>
        <Route path='/' element={<Home />}/>
        <Route path='/products' element={<Products />}/>
        <Route path='/about' element={<About />}/>
        <Route path='/contact' element={<Contact />}/>
        <Route path='/cart' element={<Cart />}/>
        <Route path='/' element={<PrivateRoute publicPage />}>
            <Route path='/login' element={ <LogIn />}/>
            <Route path='/register' element={ <Register />}/>
          </Route>
      </Routes>
    </Router>
    <Toaster position='bottom-center'/>
    </React.Fragment>
      
  )
}

export default App
