// Import Swiper React components
import { Swiper,SwiperSlide } from 'swiper/react';
// Import Swiper styles
import 'swiper/css';
import { Autoplay,  Pagination, EffectFade, Navigation } from 'swiper/modules';
import { bannerList } from '../../utils';
import { Link } from 'react-router-dom';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import 'swiper/css/effect-fade';
import 'swiper/css/autoplay';

const HomeBanners = () => {
  return (
    <div className="py-2 rounded-md">
        <Swiper
            grabCursor={true}
            autoplay={{
                delay:4000,
                disableOnInteraction:false,
            }}
            navigation
            modules={[Pagination, EffectFade,Navigation,Autoplay]}
            pagination={{clickable:true}}
            scrollbar={{draggable:true}}
            slidesPerView={1}>
            {bannerList.map((item,i) =>(
                <SwiperSlide key={i}>
                    <div className='carousel-item rounded-md sm:h-[500px] h-96 bg-gray-300'>
                        <div className='flex items-center justify-center'>
                            <div className='hiden lg:flex justify-center w-1/2 p-8'>
                            <div className='text-center'>
                                <h3 className='text-3xl text-black font-bold'>
                                    {item.title}
                                </h3>
                                <h1 className='text-3xl text-black font-bold'>
                                    {item.subtitle}
                                </h1>
                                <p className='text-white font-bold mt-4'>
                                    {item.description}
                                </p>
                                <Link to ="/products" className='mt-6 inline-block bg-black text-white py-2 px-4 rounded-md hover:bg-gray-400'>
                                Shop
                                </Link>
                            </div>
                        </div>
                        <div className='w-full flex justify-center lg:w-1/2 p-4'>
                            <img src={item?.image}></img>
                        </div>
                        </div>
                    </div> 
                </SwiperSlide>

            ))}
            
        </Swiper>
    </div>
    
  );
};

export default HomeBanners;