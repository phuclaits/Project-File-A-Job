import axios from "axios";
import React, { useEffect, useState } from "react";
import {
  useFetchDataJobType,
  useFetchDataJobLevel,
  useFetchDataSalaryType,
  useFetchDataExpType,
  useFetchDataWorkType,
  useFetchDataJobLocation,
} from "../../utils/fetch";
const LeftBar = (props) => {
  const [dataJobType, setdataJobType] = useState([]);
  useEffect(() => {
    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/job-types")
      .then((response) => {     
        setdataJobType(response.data);
      })
      .catch((error) => {
        console.error("Error fetching job type data", error);
      });
  }, []);
  const [dataJobLevel,setDataJobLevel] = useState([])
  useEffect(() => {
    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/job-levels")
      .then((response) => {
        setDataJobLevel(response.data);
      })
      .catch((error) => {
        console.error("Error fetching job type data", error);
      });
  }, []);
  const [dataSalaryType, setDataSalaryType] = useState([])
  useEffect(() => {
    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/salary-types")
      .then((response) => {
        setDataSalaryType(response.data);
      })
      .catch((error) => {
        console.error("Error fetching job type data", error);
      });
  }, []);
  const [dataExpType,setDataExpType] = useState([])
  useEffect(() => {
    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/exp-types")
      .then((response) => {
        setDataExpType(response.data);
      })
      .catch((error) => {
        console.error("Error fetching job type data", error);
      });
  }, []);
  const [dataWorkType, setdataWorkType] = useState([]);
  useEffect(() => {
    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/work-types")
      .then((response) => {
        setdataWorkType(response.data);
      })
      .catch((error) => {
        console.error("Error fetching job type data", error);
      });
  }, []);
  const [dataJobLocation, setdataJobLocation] = useState([]);
  useEffect(() => {
    axios
      .get("http://localhost:8080/app-tuyen-dung/api/v1/get-all-code/provinces")
      .then((response) => {
        setdataJobLocation(response.data);
      })
      .catch((error) => {
        console.error("Error fetching job type data", error);
      });
  }, []);
  return (
    <>
      <div className="job-category-listing mb-50">
        {/* <!-- single one --> */}
        <div className="single-listing">
          <div className="small-section-tittle2">
            <h4>Lĩnh vực</h4>
          </div>
          {/* <!-- Select job items start --> */}
          <div className="select-job-items2">
            <select
              name="select"
              onChange={(e) => {
                props.recieveJobType(e.target.value);
              }}
            >
              <option value="">Tất cả</option>
              {dataJobType.map((data, index) => {
                return (
                  <option value={data.code} key={index}>
                    {data.value}
                  </option>
                );
              })}
            </select>
          </div>
          {/* <!--  Select job items End--> */}
          {/* <!-- select-Categories start --> */}
          <div className="select-Categories pt-80 pb-50">
            <div className="small-section-tittle2">
              <h4>Hình thức làm việc</h4>
            </div>
            {dataWorkType.map((data, index) => {
              return (
                <label className="container" key={index}>
                  {data.value}
                  <input
                    type="checkbox"
                    value={data.code}
                    onChange={(e) => {
                      props.worktype(e.target.value);
                    }}
                    required
                  />
                  <span className="checkmark"></span>
                </label>
              );
            })}
          </div>
          {/* <!-- select-Categories End --> */}
        </div>
        {/* <!-- single two --> */}
        <div className="single-listing">
          <div className="small-section-tittle2">
            <h4>Vị trí</h4>
          </div>
          {/* <!-- Select job items start --> */}
          <div className="select-job-items2">
            <select
              name="select"
              onChange={(e) => {
                props.recieveLocation(e.target.value);
              }}
            >
              <option value="">Tất cả</option>
              {dataJobLocation.map((data, index) => {
                                return (
                                    <option value={data.code} key={index}>{data.value}</option>
                                )
                            })}
            </select>
          </div>
          {/* <!--  Select job items End--> */}
          {/* <!-- select-Categories start --> */}
          <div className="select-Categories pt-80 pb-50">
            <div className="small-section-tittle2">
              <h4>Kinh nghiệm làm việc</h4>
            </div>
            {dataExpType.map((data, index) => {
                            return (
                                <label className="container" key={index}>{data.value}
                                    <input type="checkbox" value={data.code} onChange={(e) => props.recieveExp(e.target.value)} />
                                    <span className="checkmark"></span>
                                </label>
                            )
                        })}
          </div>
          {/* <!-- select-Categories End --> */}
        </div>
        {/* <!-- single three --> */}
        <div className="single-listing">
          {/* <!-- select-Categories start --> */}
          <div className="select-Categories pb-50">
            <div className="small-section-tittle2">
              <h4>Cấp bậc</h4>
            </div>
            {dataJobLevel.map((data, index) => {
                            return (
                                <label className="container" key={index}>{data.value}
                                    <input type="checkbox" value={data.code} onChange={(e) => {
                                        props.recieveJobLevel(e.target.value)
                                    }} />
                                    <span className="checkmark"></span>
                                </label>
                            )
                        })}
          </div>
          {/* <!-- select-Categories End --> */}
          <div className="single-listing">
            {/* <!-- select-Categories start --> */}
            <div className="select-Categories pb-50">
              <div className="small-section-tittle2">
                <h4>Lương bổng</h4>
              </div>
              {dataSalaryType.map((data, index) => {
                                return (
                                    <label className="container" key={index}>{data.value}
                                        <input type="checkbox" value={data.code} onChange={(e) => {
                                            props.recieveSalary(e.target.value)

                                        }}
                                        />
                                        <span className="checkmark"></span>
                                    </label>
                                )
                            })}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default LeftBar;
