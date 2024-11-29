import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import Job from "../../../components/Job/Job";
import { Input } from "antd";

const RightContent = (props) => {
  useEffect(() => {
    console.log(props);
  }, []);

  return (
    <>
      <section class="featured-job-area">
        <div class="container">
          <div class="row">
            <div class="col-lg-12">
              <div class="count-job mb-35">
                <span>{props.count} công việc được tìm thấy</span>
                <Input.Search
                  onSearch={props.handleSearch}
                  className="mt-5"
                  placeholder="Nhập tên bài đăng"
                  allowClear
                  enterButton="Tìm kiếm"
                ></Input.Search>
              </div>
            </div>
          </div>
          {props.post?.map((data, index) => {
            return (
              <Link to={`/detail-job/${data.id}`}>
                <div class="single-job-items mb-30">
                  <Job key={data.id} data={data} />
                </div>
              </Link>
            );
          })}
        </div>
      </section>
    </>
  );
};

export default RightContent;
