import { useEffect } from "react";
import { useState } from "react";
import { toast } from "react-toastify";
import { getListJobTypeAndCountPost } from "../../service/userService";
import Category from "./Category";
import axios from "axios";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Slider from "react-slick";

const Categories = () => {
  const [allCategory, setAllCategory] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      try {
        axios
          .get(
            `http://localhost:8080/app-tuyen-dung/api/v1/post/get-list-job-count-post`
          )
          .then((res) => {
            setAllCategory(res.data.content);
          })
          .catch((err) => {
            toast.error(err);
          });
      } catch (error) {
        toast.error("Đã xảy ra lỗi khi lấy dữ liệu");
      }
    };
    fetchData();
  }, []);

  return (
    <>
      <div className="category-slider">
        <Slider {...settings}>
          {allCategory?.map((data, index) => {
            return <Category data={data} key={index} />;
          })}
        </Slider>
      </div>
    </>
  );
};

const settings = {
  dots: true,
  infinite: true,
  speed: 500,
  slidesToShow: 3,
  slidesToScroll: 1,
  autoplay: true,
  autoplaySpeed: 2000,
  responsive: [
    {
      breakpoint: 1024,
      settings: {
        slidesToShow: 2,
        slidesToScroll: 1,
      },
    },
    {
      breakpoint: 600,
      settings: {
        slidesToShow: 1,
        slidesToScroll: 1,
      },
    },
  ],
};

export default Categories;
