# 🛍️ London Gift Shop (LGS) Customer Analytics

## 📘 Introduction

London Gift Shop (LGS) is a UK-based e-commerce store specializing in giftware. Despite operating online for over a decade, LGS has faced stagnant revenue growth. To improve customer engagement and drive sales, the marketing team sought to understand customer purchasing behavior through data analytics.

Jarvis Consulting was brought in to develop a **proof-of-concept (PoC)** that analyzes historical sales data. The goal is to deliver insights that enable targeted marketing strategies such as email campaigns, events, and personalized promotions.

This project uses **Python**, **Pandas**, **NumPy**, and **Jupyter Notebook** for data exploration and analysis. Key deliverables include customer segmentation through **RFM (Recency, Frequency, Monetary)** analysis and actionable insights for marketing.

---

## ⚙️ Implementation

### 🏗️ Project Architecture

The PoC environment is designed to operate independently of LGS’s cloud infrastructure while still utilizing exported transaction data. The architecture includes:

- **LGS Web App** (Azure-hosted): Collects customer transaction data.
- **Production Database**: Stores sales transactions.
- **SQL Data Dump**: A dataset was provided for offline analytics.
- **Analytics Environment**: Jupyter Notebook running Python (Pandas, NumPy).
- **Customer Segmentation Output**: Insights to support targeted marketing by the LGS team.

#### Architecture Diagram


---

### 🔎 Data Analytics and Wrangling

**Notebook**: [🔗 retail_data_analytics_wrangling.ipynb](./retail_data_analytics_wrangling.ipynb)

The primary analysis involved:
- Cleaning and preprocessing the data
- Filtering out canceled transactions and nulls
- Performing RFM analysis to identify key customer segments

**Business Impact**:
- **Champions**: Customers who purchase frequently and recently — ideal for loyalty programs
- **At-Risk**: Previously valuable customers who haven’t purchased in a while — ideal for re-engagement
- **New Customers**: Recently acquired — suitable for welcome offers and onboarding campaigns

These insights empower the marketing team to:
- Allocate resources to high-value segments
- Design targeted and personalized campaigns
- Increase customer retention and sales conversion

---

## 🚀 Improvements

1. **Automated ETL Pipeline**  
   Develop an automated process for real-time ingestion and transformation of new sales data.

2. **Interactive Dashboard**  
   Build an interactive dashboard using Streamlit or Dash to allow non-technical users to explore the data visually.

3. **Advanced Segmentation**  
   Apply machine learning techniques such as **K-Means Clustering** to enhance segmentation based on behavioral and product-level data.

---

## 🛠️ Technologies Used

- Python
- Jupyter Notebook
- Pandas
- NumPy
- SQL (PostgreSQL)
- Data Analytics
- Data Wrangling
- RFM Segmentation

---

