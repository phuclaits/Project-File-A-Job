import React from 'react'
import moment from 'moment';
const Job = (props) => {
    const handleSplitTime = (time) => {
        const now = moment();
        const postTime = moment(new Date(+time));  // Chuyển timestamp thành đối tượng moment
        const diffInYears = now.diff(postTime, 'years');  // Tính số năm
        const diffInMonths = now.diff(postTime, 'months');  // Tính số tháng
        const diffInDays = now.diff(postTime, 'days');  // Tính số ngày
        const diffInWeeks = Math.floor(diffInDays / 7);  // Tính số tuần
    
        // Nếu thời gian hết hạn trong tương lai (so với now)
        if (postTime.isAfter(now)) {
            const diffInFutureDays = postTime.diff(now, 'days');
            const diffInFutureWeeks = Math.floor(diffInFutureDays / 7);
            const diffInFutureMonths = postTime.diff(now, 'months');
            const diffInFutureYears = postTime.diff(now, 'years');
    
            // Nếu hết hạn vào ngày mai
            if (diffInFutureDays === 1) {
                return "Hết hạn vào ngày mai";
            }
            // Nếu hết hạn vào tuần sau
            else if (diffInFutureWeeks === 1) {
                return "Hết hạn vào tuần sau";
            }
            // Nếu hết hạn vào tháng tới
            else if (diffInFutureMonths === 1) {
                return "Hết hạn vào tháng tới";
            }
            // Nếu hết hạn vào năm sau
            else if (diffInFutureYears === 1) {
                return "Hết hạn vào năm sau";
            }
            else {
                return `Hết hạn vào ${postTime.format('DD-MM-YYYY')}`;
            }
        }
    
        // Trường hợp đăng trong quá khứ (đã hết hạn)
        if (diffInYears >= 1) {
            return `Đã hết hạn ${diffInYears} năm trước`;
        } else if (diffInMonths >= 1) {
            return `Đã hết hạn ${diffInMonths} tháng trước`;
        } else if (diffInWeeks >= 1) {
            return `Đã hết hạn ${diffInWeeks} tuần trước`;
        } else if (diffInDays >= 1) {
            return `Đã hết hạn ${diffInDays} ngày trước`;
        } else {
            const diffInHours = now.diff(postTime, 'hours');
            if (diffInHours > 0) {
                return `Đã hết hạn ${diffInHours} giờ trước`;
            } else {
                return "Hết hạn vừa xong";
            }
        }
    };

    return (
        <>
            <div class="job-items">
                <div class="company-img">
                    <a href="#"><img src={props.data?.thumbnail} alt="" style={{ width: "85px", height: "85px" }} /></a>
                </div>
                <div class="job-tittle job-tittle2">
                    <a href="#">
                    <h5>{props.data.postDetailData?.name.length > 40 ? `${props.data.postDetailData?.name.substring(0, 46)}...` : props.data.postDetailData?.name}</h5>
                    </a>
                    <ul className='my-font'>
                        <li><i class="fa-solid fa-briefcase"></i>{props.data.postDetailData?.jobLevelPostData.value}</li>
                        <li><i class="fas fa-map-marker-alt"></i>{props.data.postDetailData?.provincePostData.value}</li>
                        <li><i class="fas fa-money-bill-wave"></i>{props.data.postDetailData?.salaryTypePostData.value}</li>
                    </ul>
                </div>
            </div>
            <div class="items-link items-link2 f-right">
                <a className='my-font' href="job_details.html">{props.data.postDetailData?.workTypePostData.value}</a>
                <span style={{ position: 'absolute', right: '70px' }}>{handleSplitTime(props.data?.timeEnd)}</span>
            </div>

        </>
    )
}

export default Job
