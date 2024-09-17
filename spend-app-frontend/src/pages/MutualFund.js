import React, { useState } from "react";
import { Grid } from "@mui/material";
import CurrentValuePaper from "../components/mutualFunds/CurrentValuePaper";
import DetailedPaper from "../components/mutualFunds/DetailedPaper";
import Nifty50Paper from "../components/mutualFunds/Nifty50Paper";
import FundSearchBar from "../components/mutualFunds/FundSearchBar";
import { useCookies } from "react-cookie";
import axios from "axios";
import { COMMON_URL } from "../constants/URL";
import Loading from "../main/Loading";
import CustomTableV2 from "../components/mutualFunds/CustomTableV2";
import { formatDate } from "../functions/function";

const columns = [
  {
    headerName: "Fund Details",
    subHeaders: [
      { field: "fundName", headerName: "Fund Name", type: "string" },
      { field: "investedAmount", headerName: "Total Invested", type: "number", convertToInteger: true },
      { field: "totalUnits", headerName: "Total Units", type: "number", convertToTwoDeciaml: true },
      { field: "currentAmount", headerName: "Current Value", type: "number", convertToInteger: true, cellStyle: true, colorCell: "returnPercentage" },
    ],
  },
  {
    headerName: "Return",
    subHeaders: [
      {
        field: "returnPercentage",
        headerName: "Return(%)",
        type: "number",
        convertToTwoDeciaml: true,
        suffix: "%",
        cellStyle: true,
      },
      {
        field: "returnAmount",
        headerName: "Return Value",
        type: "number",
        convertToInteger: true,
        cellStyle: true,
      },
      {
        field: "xirrValue",
        headerName: "XIRR",
        type: "number",
        suffix: "%",
        cellStyle: true,
      },
    ],
  },
  {
    headerName: "NAV",
    subHeaders: [
      // { field: "avgNav", headerName: "Average NAV", type: "number" },
      { field: "currentNav", headerName: "Current NAV", type: "number", convertToTwoDeciaml: true },
    ],
  },
  {
    headerName: "Historical Data",
    subHeaders: [
      {
        field: "day1ChangeAmount",
        headerName: "1 Day Return",
        type: "number",
        cellStyle: true,
        cellValue: (param) => {
          return param.day1ChangeAmount.toFixed(2) + "(" + param.day1Change.toFixed(2) + "%)";
        }
      },
      //{ field: "week1Change", headerName: "1 Week", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%", },
      { field: "month1Change", headerName: "1 Month", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%", },
      { field: "month6Change", headerName: "6 Months", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%", },
      { field: "year1Change", headerName: "1 Year", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%", },
      { field: "year3Change", headerName: "3 Years", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%", },
      //{ field: "year5Change", headerName: "5 Years", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%", },
      { field: "allTimeChange", headerName: "All", type: "number", cellStyle: true, convertToTwoDeciaml: true, suffix: "%" },
    ],
  },
];

const internalColumns = [
  {
    field: "amount",
    headerName: "Amount",
    type: "number",
    flex: 1,
    headerAlign: "center",
    align: "center",
    valueFormatter: (params) => {
      return params.value.toFixed(2);
    },
  },
  {
    field: "nav",
    headerName: "NAV",
    type: "number",
    flex: 1,
    headerAlign: "center",
    align: "center",
    valueFormatter: (params) => {
      return params.value.toFixed(2);
    },
  },
  {
    field: "units",
    headerName: "Units",
    type: "number",
    flex: 1,
    headerAlign: "center",
    align: "center",
    valueFormatter: (params) => {
      return params.value.toFixed(2);
    },
  },
  {
    field: "side",
    headerName: "Side",
    type: "string",
    flex: 1,
    headerAlign: "center",
    align: "center",
  },
  {
    field: "dateOfEvent",
    headerName: "Date",
    type: "string",
    flex: 1,
    headerAlign: "center",
    align: "center",
    valueFormatter: (params) => {
      const formattedDate = formatDate(params.value);
      return formattedDate;
    },
  },
  {
    field: "totalReturn",
    headerName: "Total Return",
    type: "string",
    flex: 1,
    headerAlign: "center",
    align: "center",
    renderCell: (params) => {
      const totalReturn =
        parseFloat(params.row.currenValue) - parseFloat(params.row.amount);
      const color = totalReturn > 0 ? { color: "green" } : { color: "red" };
      return (
        <span style={color}>
          {params.value}
        </span>
      );
    },
  },
  {
    field: "currenValue",
    headerName: "Current Value",
    type: "number",
    flex: 1,
    headerAlign: "center",
    align: "center",
    renderCell: (params) => {
      const totalReturn =
        parseFloat(params.row.currenValue) - parseFloat(params.row.amount);
      const color = totalReturn > 0 ? { color: "green" } : { color: "red" };
      return (
        <span style={color}>
          {params.value.toFixed(2)}
        </span>
      );
    },
  },
];


const MutualFund = () => {
  const [cookies] = useCookies(['access_token']);
  const [isLoading, setIsLoading] = useState(true);
  const [currentValue, setCurrentValue] = useState("");
  const [investedValue, setInvestedValue] = useState("");
  const [returnValue, setReturnValue] = useState("");
  const [day1Change, setDay1Change] = useState("");
  const [XIRR, setXIRR] = useState("");
  const [mutualFundData, setMutualFundData] = React.useState([]);
  const [addLoader, setAddLoader] = useState(false);
  const [rows, setRows] = useState([]);
  const [soldRows, setSoldRows] = useState([]);
  const [isProfited, setIsProfited] = useState(false);
  const [isDayProfited, setIsDayProfited] = useState(false);

  const updateData = () => {
    axios.defaults.headers.common['Authorization'] = cookies['access_token'];
    axios.get(COMMON_URL + "app/get-order-details")
      .then((res) => {
        var sign = "";
        if (res.data.returnAmount >= 0) {
          sign = "+ ";
          setIsProfited(true);
        }
        setCurrentValue(Math.round(res.data.currentAmount));
        setInvestedValue(Math.round(res.data.investedAmount));
        if (res.data.xirr)
          setXIRR(res.data.xirr + "%");
        if (res.data.returnPercentage)
          setReturnValue(sign + Math.round(res.data.returnAmount) + " (" + res.data.returnPercentage.toFixed(2) + "%)");
        const newArray = [];
        const filteredArray = res.data.fundData.filter(item => {
          if (item.totalUnits === 0) {
            newArray.push(item);
            return false;
          }
          return true;
        });
        setRows(filteredArray);
        setSoldRows(newArray);
      })
      .catch((err) => {
        console.error('get-order-details failed:', err);
      })
      .finally(() => setAddLoader(false));
  }

  const fetchData = async () => {
    try {
      axios.defaults.headers.common['Authorization'] = cookies['access_token'];
      const [fundData, portfolio] = await Promise.all([
        axios.get(COMMON_URL + "app/get-mutualfunds"),
        axios.get(COMMON_URL + "app/get-order-details"),
      ]);
      if (fundData.status === 200 && portfolio.status === 200) {
        setMutualFundData(fundData.data);
        var sign = "";
        if (portfolio.data.returnAmount >= 0) {
          sign = "+ ";
          setIsProfited(true);
        }
        var daySign = "";
        if(portfolio.data.dayChange >= 0) {
          daySign = "+ ";
          setIsDayProfited(true);
        }
        setCurrentValue(Math.round(portfolio.data.currentAmount));
        setInvestedValue(Math.round(portfolio.data.investedAmount));
        if (!isNaN(portfolio.data.xirr))
          setXIRR(portfolio.data.xirr + "%");
        if (!isNaN(portfolio.data.returnPercentage))
          setReturnValue(sign + Math.round(portfolio.data.returnAmount) + " (" + portfolio.data.returnPercentage.toFixed(2) + "%)");
        if (!isNaN(portfolio.data.dayChangePercentage))
          setDay1Change(daySign + Math.round(portfolio.data.dayChange) + " (" + portfolio.data.dayChangePercentage.toFixed(2) + "%)");
        const newArray = [];
        const filteredArray = portfolio.data.fundData.filter(item => {
          if (item.totalUnits === 0) {
            newArray.push(item);
            return false;
          }
          return true;
        });
        setRows(filteredArray);
        setSoldRows(newArray);
        setIsLoading(false);
      }

    } catch (error) {
      // Handle errors if any of the API calls fail
      console.error('Error calling one or more APIs', error);
    }
  };

  React.useEffect(() => {
    fetchData();
  }, []);


  return (
    <div>
      {isLoading ?
        <>
          <Loading />
        </>
        :
        <>
          <Grid container spacing={2} paddingTop={6}>
            <Grid item xs={3}>
              <CurrentValuePaper amount={currentValue} isPositive={isProfited} />
            </Grid>
            <Grid item xs={5}>
              <DetailedPaper investedAmount={investedValue} totalReturn={returnValue} day1Change={day1Change} xirr={XIRR} isPositive={isProfited} isDayPositive={isDayProfited} />
            </Grid>
            <Grid item xs={4}>
              <Nifty50Paper />
            </Grid>
          </Grid>
          <div style={{ marginTop: '16px', backgroundColor: 'white' }}>
            <FundSearchBar mutualFundData={mutualFundData} updateData={updateData} setAddLoader={setAddLoader} />
          </div>
          {addLoader ? <Loading /> :
            <CustomTableV2
              columns={columns}
              internalColumns={internalColumns}
              rows={rows}
              mutualFundData={mutualFundData}
              updateData={updateData}
              setAddLoader={setAddLoader}
              sortBy="currentAmount"
              rowKey="fundName"
            />}
        </>
      }
    </div>
  );
};

export default MutualFund;
