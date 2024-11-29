import React, { useState, useEffect } from "react";
import ReactPaginate from "react-paginate";
import LeftBar from "./LeftPage/LeftBar";
import { PAGINATION } from "../utils/constant";
import RightContent from "./RightPage/RightContent";

import CommonUtils from "../utils/CommonUtils";
import axios from "axios";
const JobPage = () => {
  const [countPage, setCountPage] = useState(1);
  const [post, setPost] = useState([]);
  const [count, setCount] = useState(0);
  const [numberPage, setNumberPage] = useState("");
  const [offset, setOffset] = useState(0);
  const [limit, setLimit] = useState(PAGINATION.pagerow);

  const [workType, setWorkType] = useState([]);
  const [jobType, setJobType] = useState("");
  const [salary, setSalary] = useState([]);
  const [exp, setExp] = useState([]);
  const [jobLevel, setJobLevel] = useState([]);
  const [jobLocation, setJobLocation] = useState("");
  const [search, setSearch] = useState("");
  let loadPost = async (limit, offset) => {
    let params = {
      limit: limit,
      offset: offset,
      categoryJobCode: jobType,
      addressCode: jobLocation,
      salaryJobCode: salary,
      categoryJoblevelCode: jobLevel,
      categoryWorktypeCode: workType,
      experienceJobCode: exp,
      // sortName: '1',
      search: CommonUtils.removeSpace(search),
    };
    let filteredParams = Object.keys(params)
      .filter((key) => params[key] && params[key].length !== 0)
      .reduce((obj, key) => {
        obj[key] = params[key];
        return obj;
      }, {});

    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/post/get-search-post", {
        params: filteredParams,
      })
      .then((responseFeature) => {
        // console.log(responseFeature.data);
        setPost(responseFeature.data.result.content);
        setCountPage(Math.ceil(responseFeature.data.result.totalElements / limit));
        // console.log(Math.ceil(responseFeature.data.totalElements / limit));
        setCount(responseFeature.data.result.totalElements);
      })
      .catch((error) => {
        console.error("Error fetching feature data", error);
      });

    // let arrData = await getListPostService(params)
    // if (arrData && arrData.errCode === 0) {
    //     setPost(arrData.data)
    //     setCountPage(Math.ceil(arrData.count / limit))
    //     setCount(arrData.count)
    // }
  };
  const handleSearch = (value) => {
    setSearch(value);
  };
  const recieveWorkType = (data) => {
    setWorkType((prev) => {
      let isCheck = workType.includes(data);
      if (isCheck) return workType.filter((item) => item !== data);
      else return [...prev, data];
    });
  };
  const recieveSalary = (data) => {
    setSalary((prev) => {
      let isCheck = salary.includes(data);
      if (isCheck) return salary.filter((item) => item !== data);
      else return [...prev, data];
    });
  };
  const recieveExp = (data) => {
    setExp((prev) => {
      let isCheck = exp.includes(data);
      if (isCheck) return exp.filter((item) => item !== data);
      else return [...prev, data];
    });
  };
  const recieveJobType = (data) => {
    jobType === data ? setJobType("") : setJobType(data);
  };
  //categoryJobCode=&addressCode=&search
  const recieveJobLevel = (data) => {
    setJobLevel((prev) => {
      let isCheck = jobLevel.includes(data);
      if (isCheck) return jobLevel.filter((item) => item !== data);
      else return [...prev, data];
    });
  };
  const recieveLocation = (data) => {
    jobLocation === data ? setJobLocation("") : setJobLocation(data);
  };
  const recieveCategoryJobCode = (data) => {
    setJobType((prev) => {
      let isCheck = jobType.includes(data);
      if (isCheck) return jobType.filter((item) => item !== data);
      else return [...prev, data];
    });
  };

  useEffect(() => {
    let filterdata = async () => {
      let params = {
        limit: limit,
        offset: 0,
        categoryJobCode: jobType,
        addressCode: jobLocation,
        salaryJobCode: salary,
        categoryJoblevelCode: jobLevel,
        categoryWorktypeCode: workType,
        experienceJobCode: exp,
        search: CommonUtils.removeSpace(search),
      };
      let filteredParams = Object.keys(params)
        .filter((key) => params[key] && params[key].length !== 0)
        .reduce((obj, key) => {
          obj[key] = params[key];
          return obj;
        }, {});
      
        Object.keys(filteredParams).forEach((key) => {
          if (Array.isArray(filteredParams[key])) {
            filteredParams[key] = filteredParams[key].join(',');
          }
        });  
      axios
        .get("http://localhost:8080/app-tuyen-dung/api/v1/post/get-search-post", {
          params: filteredParams,
        })
        .then((responseFeature) => {
          // console.log(responseFeature.data.result);
          setNumberPage(0);
          setOffset(0);
          setPost(responseFeature.data.result.content);
          // console.log(post)
          setCountPage(Math.ceil(responseFeature.data.result.totalElements / limit));
          setCount(responseFeature.data.result.totalElements);

        })
        .catch((error) => {
          console.error("Error fetching feature data", error);
        });
    };
    filterdata();
  }, [workType, jobLevel, exp, jobType, jobLocation, salary, search]);
  const handleChangePage = (number) => {
    // console.log(number);
    const newOffset = number.selected; // Tính toán offset mới
    setNumberPage(newOffset);
    loadPost(limit, newOffset); // Truyền offset mới vào hàm loadPost
    setOffset(newOffset);
  };
  return (
    <>
      <main>
        {/* <!-- Hero Area Start--> */}
        <div class="slider-area ">
          <div
            class="single-slider section-overly slider-height2 d-flex align-items-center"
            style={{
              backgroundImage: `url("assets/img/hero/about.jpg")`,
            }}
          >
            <div class="container">
              <div class="row">
                <div class="col-xl-12">
                  <div class="hero-cap text-center">
                    <h2>Tìm việc</h2>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        {/* <!-- Hero Area End -->
        <!-- Job List Area Start --> */}
        <div class="job-listing-area pt-120 pb-120">
          <div class="container">
            <div class="row">
              {/* <!-- Left content --> */}
              <div class="col-xl-3 col-lg-3 col-md-4">
                <div class="row">
                  <div class="col-12">
                    <div class="small-section-tittle2 mb-45">
                      <div class="ion">
                        {" "}
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          width="20px"
                          height="12px"
                        >
                          <path
                            fill-rule="evenodd"
                            fill="rgb(27, 207, 107)"
                            d="M7.778,12.000 L12.222,12.000 L12.222,10.000 L7.778,10.000 L7.778,12.000 ZM-0.000,-0.000 L-0.000,2.000 L20.000,2.000 L20.000,-0.000 L-0.000,-0.000 ZM3.333,7.000 L16.667,7.000 L16.667,5.000 L3.333,5.000 L3.333,7.000 Z"
                          />
                        </svg>
                      </div>
                      <h4>Lọc công việc</h4>
                    </div>
                  </div>
                </div>
                {/* <!-- Job Category Listing start --> */}
                <LeftBar
                  worktype={recieveWorkType}
                  recieveSalary={recieveSalary}
                  recieveExp={recieveExp}
                  recieveJobType={recieveJobType}
                  recieveJobLevel={recieveJobLevel}
                  recieveLocation={recieveLocation}
                />
                {/* <!-- Job Category Listing End --> */}
              </div>
              {/* <!-- Right content --> */}
              <div class="col-xl-9 col-lg-9 col-md-8">
                <RightContent
                  handleSearch={handleSearch}
                  count={count}
                  post={post}
                />
                <ReactPaginate
                  forcePage={numberPage}
                  previousLabel={"Quay lại"}
                  nextLabel={"Tiếp"}
                  breakLabel={"..."}
                  pageCount={countPage}
                  marginPagesDisplayed={3}
                  containerClassName={"pagination justify-content-center pb-3"}
                  pageClassName={"page-item"}
                  pageLinkClassName={"page-link"}
                  previousLinkClassName={"page-link"}
                  previousClassName={"page-item"}
                  nextClassName={"page-item"}
                  nextLinkClassName={"page-link"}
                  breakLinkClassName={"page-link"}
                  breakClassName={"page-item"}
                  activeClassName={"active"}
                  onPageChange={handleChangePage}
                />
              </div>
            </div>
          </div>
        </div>

        {/* <!--Pagination End  --> */}
      </main>
    </>
  );
};
export default JobPage;
