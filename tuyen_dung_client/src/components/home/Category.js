import React, { useState } from "react";

const Category = React.memo((props) => {
  const [isHovered, setIsHovered] = useState(false);

  const styles = {
    card: {
      backgroundColor: "#fff",
      borderRadius: "10px",
      padding: "20px",
      willChange: "transform, box-shadow",
      boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)",
      textAlign: "center",
      transition: "transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out",
      marginBottom: "30px",
      width: "250px",
      height: "300px",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center",
      transform: isHovered ? "translateY(-5px)" : "none",
      boxShadow: isHovered
        ? "0 6px 15px rgba(0, 0, 0, 0.15)"
        : "0 4px 12px rgba(0, 0, 0, 0.1)",
    },
    image: {
      width: "100%",
      height: "auto",
      maxHeight: "150px",
      objectFit: "contain",
      borderRadius: "10px",
      marginBottom: "15px",
    },
    title: {
      fontSize: "18px",
      fontWeight: "bold",
      marginBottom: "10px",
    },
    span: {
      display: "block",
      fontSize: "14px",
      color: "#666",
    },
    link: {
      color: "#007bff",
      textDecoration: "none",
      transition: "color 0.3s ease-in-out",
    },
    linkHover: {
      color: "#0056b3",
      textDecoration: "underline",
    },
  };

  return (
    <div className="col-xl-3 col-lg-4 col-md-6 col-sm-12">
      <div
        className="category-card"
        style={styles.card}
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        <div className="category-image">
          <img
            src={props.data.image}
            alt="ảnh"
            style={styles.image}
          />
        </div>
        <div className="category-content">
          <h5 style={styles.title}>
            <a href="job_listing.html" style={styles.link}>
              {props.data.value}
            </a>
          </h5>
        </div>
      </div>
    </div>
  );
});

export default Category;