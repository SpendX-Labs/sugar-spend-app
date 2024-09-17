import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import Button from "@mui/material/Button";
import axios from "axios";
import { COMMON_URL } from "../constants/URL";
import { Box, ButtonGroup, Checkbox, FormControlLabel, Grid, InputAdornment, MenuItem, OutlinedInput, Radio, RadioGroup, Select, Tooltip, Typography } from "@mui/material";
import { useCookies } from "react-cookie";
import Loading from "../main/Loading";
import dayjs from "dayjs";
import { DD_MM_YYYY } from "../constants/DateFormat";
import { makeStyles } from "@mui/styles";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DialogHeader, DialogPaper, FormControlStyled, ModifyContainerDetails } from "../components/commons/CustomFields";
import { Cell, Legend, Pie, PieChart } from "recharts";

const useStyles = makeStyles((theme) => ({
    dialogPaper: {
        width: '800px',
        maxWidth: '100%',
        minWidth: '800px',
        height: '80vh',
    },
    label: {
        fontSize: '16px',
        fontWeight: 500,
        marginBottom: '8px',
    },
    dialogHeader: {
        backgroundColor: theme.palette.primary.main,
        color: theme.palette.primary.contrastText,
        padding: theme.spacing(2),
        textAlign: 'center',
    },
    modifyContainerDetails: {
        padding: theme.spacing(2),
        borderRadius: theme.shape.borderRadius,
        boxShadow: theme.shadows[1],
        marginBottom: theme.spacing(2), // Added margin
    },
    formControl: {
        marginBottom: theme.spacing(2), // Added margin
    },
    buttonGroup: {
        marginTop: theme.spacing(2),
    },
}));

const Loan = () => {
    const classes = useStyles();
    const [data, setData] = useState([]);
    const [creditCardData, setCreditCardData] = useState([]);
    //const [dataList, setDataList] = useState([]);
    const [cookies, __setCookie] = useCookies(['access_token']);
    const [isLoading, setIsLoading] = useState(true);
    const [isDialogOpen, setDialogOpen] = useState(false);
    const [isModifyDialogOpen, setModifyDialogOpen] = useState(false);
    const [enableEditDeleteBtn, setEnableEditDeleteBtn] = useState(false);
    const [enableModifyBtn, setEnableModifyBtn] = useState(false);
    const [cardId, setCardId] = useState(-1);
    const [cardName, setCardName] = useState('');
    const [id, setId] = useState(null);
    const [isSaved, setIsSaved] = useState(false);
    const [noCostEmi, setNoCostEmi] = useState(false);
    const [loanType, setLoanType] = useState('Reducing');
    const [interestRate, setInterestRate] = useState('');
    const [totalAmount, setTotalAmount] = useState('');
    const [tenure, setTenure] = useState('');
    const [lenderName, setLenderName] = useState('');
    const [last4Digit, setLast4Digit] = useState('');
    const [selectedOption, setSelectedOption] = useState('creditCard');
    const [loanStartDate, setLoanStartDate] = useState(dayjs(new Date()));
    const [emiDateOfMonth, setEmiDateOfMonth] = useState(1);
    const [emiAmount, setEmiAmount] = useState('');
    const [interestAmount, setInterestAmount] = useState('');
    const [principalAmount, setPrincipalAmount] = useState('');
    const [remainingTenure, setRemainingTenure] = useState('');
    const [paymentOption, setPaymentOption] = useState('monthsPaid');
    const [tenureRange, setTenureRange] = useState({});
    const [monthsPaid, setMonthsPaid] = useState('');
    const [principalPaid, setPrincipalPaid] = useState('');
    const [loanPieData, setLoanPieData] = useState([]);
    const [paymentPieData, setPaymentPieData] = useState([]);


    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.defaults.headers.common['Authorization'] = cookies['access_token'];
                const [dbData, creditCards] = await Promise.all([
                    axios.get(COMMON_URL + "app/get-loan-details"),
                    axios.get(COMMON_URL + "app/get-credit-cards"),
                ]);
                if (dbData.status === 200 && creditCards.status === 200 && dbData.data && creditCards.data) {
                    var modifiedData = dbData.data;
                    modifiedData.forEach(item => {
                        item.cardOrLenderName = item.creditCardName ?
                            item.creditCardName : item.lenderName;
                    })
                    setData(modifiedData);
                    setCreditCardData(creditCards.data);
                    setIsLoading(false);
                }
            }
            catch (error) {
                // Handle errors if any of the API calls fail
                console.error('Error calling one or more APIs', error);
            }
        };
        fetchData();
    }, [isSaved]);

    const columns = [
        {
            field: "cardOrLenderName",
            headerName: "Card/Lender",
            flex: 1,
            headerAlign: "center",
            align: "center"
        },
        {
            field: "totalAmount",
            headerName: "Loan Amount",
            type: "number",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                return params.value ? params.value.toFixed(2) : '';
            }
        },
        {
            field: "remainingAmount",
            headerName: "Remaining to Pay",
            type: "number",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                return params.value ? params.value.toFixed(2) : '';
            }
        },
        {
            field: "loanType",
            headerName: "Loan Type",
            flex: 1,
            headerAlign: "center",
            align: "center",
        },
        {
            field: "interestRate",
            headerName: "Interest Rate",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                return `${params.value}%`;
            }
        },
        {
            field: "noCostEmi",
            headerName: "No Cost EMI",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                return params.value ? "Yes" : "No";
            }
        },
        {
            field: "tenure",
            headerName: "Tenure (Months)",
            type: "number",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                return `${params.value} months`;
            }
        },
        {
            field: "remainingTenure",
            headerName: "Remaining Tenure",
            type: "number",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                return `${params.value} months`;
            }
        },
        {
            field: "emiAmount",
            headerName: "EMI Amount",
            type: "string",
            flex: 1,
            headerAlign: "center",
            align: "center",

        },
    ];

    const handleDialog = () => {
        setDialogOpen(!isDialogOpen);
    };

    const handleModifyDialog = () => {
        setModifyDialogOpen(!isModifyDialogOpen);
    };

    const isAnyFieldEmpty = () => {
        return !totalAmount || !interestRate || !tenure;
    };

    const isAnyModifyFieldEmpty = () => {
        if (paymentOption === 'monthsPaid') {
            return !monthsPaid;
        }
        return true; //have to implement 
    }

    const handleAdd = () => {
        setIsLoading(true);
        handleDialog();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const dateToSend = loanStartDate.format('YYYY-MM-DDTHH:mm:ss');
        const data = {
            id: id,
            creditCardId: cardId > 0 ? cardId : null,
            creditCardName: cardName,
            lenderName: lenderName,
            last4Digit: last4Digit,
            totalAmount: totalAmount,
            loanType: loanType,
            interestRate: interestRate,
            noCostEmi: noCostEmi,
            tenure: tenure,
            loanStartDate: dateToSend,
            emiDateOfMonth: emiDateOfMonth
        };
        axios.post(COMMON_URL + "app/save-loan-detail", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setAllFieldToDefault()
        })
    };

    const handleModify = () => {
        setIsLoading(true);
        handleModifyDialog();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = {
            id: id,
            alreadyPaidMonth: monthsPaid
        };
        axios.post(COMMON_URL + "app/modify-loan", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setAllFieldToDefault()
        })
    };

    const handleDelete = () => {
        setIsLoading(true);
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id }
        axios.post(COMMON_URL + "app/delete", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setAllFieldToDefault()
        })
    }

    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
        if (event.target.value === 'creditCard') {
            setLenderName('');
            setLast4Digit('');
        }
        else if (event.target.value === 'lender') {
            setCardId(-1);
            setCardName('');
        }
    };

    const handleNoCostEMIChange = (event) => {
        setNoCostEmi(event.target.checked);
        if (event.target.checked) {
            setLoanType('Reducing'); // Reset loan type if no-cost EMI is selected
        }
    };

    const setAllFieldToDefault = () => {
        setSelectedOption('creditCard');
        setId(null);
        setCardId(-1);
        setCardName('');
        setLenderName('');
        setLast4Digit('');
        setNoCostEmi(false);
        setLoanType('Reducing');
        setTotalAmount('');
        setInterestRate('');
        setTenure('');
        setLoanStartDate(dayjs(new Date()));
        setEmiDateOfMonth(1);
        setLoanPieData([]);
        setPaymentPieData([]);
        setTenureRange({});
        setMonthsPaid('');
        setEnableEditDeleteBtn(false);
        setEnableModifyBtn(false);
    }

    const setSeletedDataTOTheForm = (selectedData) => {
        setId(selectedData.id);
        if (selectedData.creditCard == null) {
            setSelectedOption('lender');
            setLenderName(selectedData.lenderName);
            setLast4Digit(selectedData.last4Digit);
        } else {
            setSelectedOption('creditCard');
            setCardId(selectedData.creditCard?.id);
            setCardName(selectedData.creditCardName);
        }
        setNoCostEmi(selectedData.noCostEmi);
        setLoanType(selectedData.loanType);
        setTotalAmount(selectedData.totalAmount);
        setInterestRate(selectedData.interestRate);
        setTenure(selectedData.tenure);
        setRemainingTenure(selectedData.remainingTenure)
        setLoanStartDate(dayjs(selectedData.loanStartDate));
        setEmiDateOfMonth(selectedData.emiDateOfMonth);
        setEmiAmount(selectedData.emiAmount);
        setPrincipalAmount(selectedData.principalAmount);
        setInterestAmount(selectedData.interestAmount);
        setLoanPieData([{ dataKey: "Principal Amount", amount: selectedData.principalAmount },
        { dataKey: "Interest Amount", amount: selectedData.interestAmount }]);
        var totalAmount = selectedData.principalAmount + selectedData.interestAmount;
        var alreadyPaid = totalAmount - selectedData.remainingAmount;
        setPaymentPieData([{ dataKey: "Remaining", amount: selectedData.remainingAmount },
        { dataKey: "Paid", amount: alreadyPaid }]);
        if (!selectedData.updateLock) {
            setEnableEditDeleteBtn(true);
        } else {
            setEnableEditDeleteBtn(false);
        }
        var left = selectedData.tenure - selectedData.remainingTenure;
        setTenureRange({ left: left, right: selectedData.tenure });
        setEnableModifyBtn(true);
    }

    return (
        <Box sx={{ width: "100%", paddingTop: 6 }}>
            {isLoading ?
                <>
                    <Loading />
                </> :
                <>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 30 }}>
                        <h1>Loan</h1>
                        <div>
                            <Button
                                variant="contained"
                                disabled={!enableModifyBtn}
                                onClick={handleModifyDialog}
                                style={{ marginRight: 10 }}
                            >
                                Modify Loan
                            </Button>
                            <Button
                                variant="contained"
                                disabled={enableEditDeleteBtn || enableModifyBtn}
                                onClick={handleDialog}
                                style={{ marginRight: 10 }}
                            >
                                Add
                            </Button>
                            <Button
                                variant="contained"
                                color="primary"
                                disabled={!enableEditDeleteBtn}
                                onClick={handleDialog}
                                style={{ marginRight: 10 }}
                            >
                                Edit
                            </Button>
                            <Button
                                variant="contained"
                                color="secondary"
                                disabled={!enableEditDeleteBtn}
                                onClick={handleDelete}
                            >
                                Delete
                            </Button>
                        </div>

                    </div>

                    <DataGrid
                        autoHeight
                        rows={data}
                        columns={columns}
                        initialState={{
                            pagination: {
                                paginationModel: { page: 0, pageSize: 10 },
                            },
                        }}
                        pageSizeOptions={[5, 10]}
                        // checkboxSelection
                        onRowSelectionModelChange={(newSelection) => {
                            if (newSelection.length === 1) {
                                const selectedData = data.find(item => item.id === newSelection[0]);
                                setSeletedDataTOTheForm(selectedData);
                            }
                            else {
                                setAllFieldToDefault();
                            }
                        }}
                    />
                </>}
            <Dialog
                open={isDialogOpen}
                onClose={handleDialog}
                PaperProps={{
                    className: classes.dialogPaper,
                }}
            >
                <DialogHeader>{!enableEditDeleteBtn ? `Add Loan` : `Edit Loan`}</DialogHeader>
                <DialogContent>
                    <form>
                        <Grid container spacing={2}>

                            {/* Lender Details - Radio Buttons */}
                            <Grid item xs={12}>
                                <Grid container alignItems="center">
                                    <Grid item xs={6}>
                                        <div className={classes.label}>Lender Details</div>
                                    </Grid>
                                    <Grid item xs={6}>
                                        <RadioGroup row value={selectedOption} onChange={handleOptionChange}>
                                            <FormControlLabel value="creditCard" control={<Radio />} label="Credit Card" />
                                            <FormControlLabel value="lender" control={<Radio />} label="Custom Lender" />
                                        </RadioGroup>
                                    </Grid>
                                </Grid>
                            </Grid>

                            {/* Credit Card, Lender Name, and Account Number */}
                            <Grid item xs={12}>
                                <Grid container spacing={2}>
                                    <Grid item xs={4}>
                                        <div className={classes.label}>Card Name</div>
                                        <Select
                                            value={cardName}
                                            onChange={(e) => {
                                                const cardName = e.target.value;
                                                const selectedCreditCard = creditCardData.find(item => item.creditCardName === cardName);
                                                if (selectedCreditCard) {
                                                    setCardId(selectedCreditCard.id);
                                                }
                                                setCardName(cardName);
                                            }}
                                            input={<OutlinedInput label="Card Name" />}
                                            fullWidth
                                            disabled={selectedOption !== 'creditCard'}
                                        >
                                            {creditCardData.map((item) => (
                                                <MenuItem key={item.id} value={item.creditCardName}>
                                                    {item.bankName + " " + item.creditCardName}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                    </Grid>

                                    <Grid item xs={4}>
                                        <div className={classes.label}>Lender Name</div>
                                        <OutlinedInput
                                            value={lenderName}
                                            onChange={(e) => setLenderName(e.target.value)}
                                            disabled={selectedOption !== 'lender'}
                                            fullWidth
                                        />
                                    </Grid>

                                    <Grid item xs={4}>
                                        <div className={classes.label}>Account Number (Last 4 digits)</div>
                                        <div style={{ display: 'flex', alignItems: 'center' }}>
                                            <OutlinedInput
                                                value={last4Digit}
                                                onChange={(e) => {
                                                    const value = e.target.value;
                                                    if (/^\d*$/.test(value) && value.length <= 4) {
                                                        setLast4Digit(value);
                                                    }
                                                }}
                                                placeholder="1234"
                                                inputProps={{ maxLength: 4 }}
                                                disabled={selectedOption !== 'lender'}
                                                startAdornment={<InputAdornment position="end">XXXXXX</InputAdornment>}
                                                fullWidth
                                            />
                                        </div>
                                    </Grid>
                                </Grid>
                            </Grid>

                            {/* No Cost EMI Checkbox and Loan Type */}
                            <Grid item xs={12}>
                                <Grid container spacing={2} alignItems="center">
                                    <Grid item xs={6}>
                                        <FormControlLabel
                                            control={
                                                <Checkbox
                                                    checked={noCostEmi}
                                                    onChange={handleNoCostEMIChange}
                                                />
                                            }
                                            label="No Cost EMI"
                                        />
                                    </Grid>

                                    <Grid item xs={6}>
                                        <div className={classes.label}>Loan Type</div>
                                        <Select
                                            value={loanType}
                                            onChange={(e) => setLoanType(e.target.value)}
                                            fullWidth
                                            disabled={noCostEmi}
                                        >
                                            <MenuItem value="Reducing">Reducing</MenuItem>
                                            <MenuItem value="flat">Flat</MenuItem>
                                        </Select>
                                    </Grid>
                                </Grid>
                            </Grid>

                            {/* Total Amount and Interest Rate */}
                            <Grid item xs={12}>
                                <Grid container spacing={2}>
                                    <Grid item xs={6}>
                                        <div className={classes.label}>Total Amount</div>
                                        <OutlinedInput
                                            type="number"
                                            value={totalAmount}
                                            onChange={(e) => setTotalAmount(e.target.value)}
                                            fullWidth
                                        />
                                    </Grid>
                                    <Grid item xs={6}>
                                        <div className={classes.label}>Interest Rate</div>
                                        <OutlinedInput
                                            type="number"
                                            value={interestRate}
                                            onChange={(e) => setInterestRate(e.target.value)}
                                            endAdornment={<InputAdornment position="end">%</InputAdornment>}
                                            fullWidth
                                        />
                                    </Grid>
                                </Grid>
                            </Grid>

                            {/* Tenure and statementDate*/}
                            <Grid item xs={12}>
                                <Grid container spacing={2}>
                                    <Grid item xs={4}>
                                        <div className={classes.label}>Tenure</div>
                                        <OutlinedInput
                                            type="number"
                                            value={tenure}
                                            onChange={(e) => setTenure(e.target.value)}
                                            endAdornment={<InputAdornment position="end">In month</InputAdornment>}
                                            fullWidth
                                        />
                                    </Grid>
                                    <Grid item xs={4}>
                                        <div className={classes.label}>Laon Start Date</div>
                                        <Box display="flex" justifyContent="flex-start">
                                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                                <DatePicker
                                                    value={loanStartDate}
                                                    onChange={(newDate) => setLoanStartDate(newDate)}
                                                    format={DD_MM_YYYY}
                                                />
                                            </LocalizationProvider>
                                        </Box>
                                    </Grid>
                                    <Grid item xs={4}>
                                        <div className={classes.label}>EMI Day of Month</div>
                                        <OutlinedInput
                                            type="number"
                                            value={emiDateOfMonth}
                                            onChange={(e) => setEmiDateOfMonth(e.target.value)}
                                            startAdornment={<InputAdornment position="end" style={{ paddingRight: 2 }}>Day</InputAdornment>}
                                            fullWidth
                                        />
                                    </Grid>
                                </Grid>
                            </Grid>

                            {/* Add and Cancel Buttons */}
                            <Grid item xs={12}>
                                <Grid container justifyContent="flex-end" spacing={2}>
                                    <Grid item>
                                        <Button variant="contained" color="secondary" onClick={handleDialog}>
                                            Cancel
                                        </Button>
                                    </Grid>
                                    <Grid item>
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            onClick={handleAdd}
                                            disabled={isAnyFieldEmpty()}
                                        >
                                            {!enableEditDeleteBtn ? `Add` : `Edit`}
                                        </Button>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </Grid>
                    </form>
                </DialogContent>
            </Dialog>
            {/* modify loan */}
            <DialogPaper
                open={isModifyDialogOpen}
                onClose={handleModifyDialog}
                PaperProps={{
                    className: classes.dialogPaper,
                }}
            >
                <DialogHeader>
                    <strong>Modify Loan Detail</strong>
                </DialogHeader>
                <DialogContent>
                    <Grid container spacing={2}>
                        {/* Display Loan Details */}
                        <Grid item xs={12}>
                            <ModifyContainerDetails>
                                {/* Remove <Typography variant="h6">Loan Details</Typography> */}
                                <Grid container spacing={2}>
                                    {selectedOption === 'creditCard' && (
                                        <Grid item xs={12}>
                                            <Typography variant="body1">
                                                <strong>Credit Card Name:</strong> {cardName}
                                            </Typography>
                                        </Grid>
                                    )}
                                    {selectedOption === 'lender' && (
                                        <Grid container spacing={2}>
                                            <Grid item xs={12} sm={6}>
                                                <Typography variant="body1">
                                                    <strong>Lender Name:</strong> {lenderName}
                                                </Typography>
                                            </Grid>
                                            <Grid item xs={12} sm={6}>
                                                <Typography variant="body1">
                                                    <strong>Last 4 Digits:</strong> {last4Digit}
                                                </Typography>
                                            </Grid>
                                        </Grid>
                                    )}
                                    <Grid container spacing={2}>
                                        {/* Loan Pie Chart */}
                                        <Grid item xs={12} sm={6} style={{ textAlign: 'center' }}>
                                            <PieChart width={350} height={300}>
                                                <Pie
                                                    data={loanPieData}
                                                    dataKey="amount"
                                                    nameKey="dataKey"
                                                    cx="50%"
                                                    cy="45%" // Adjusting to move it up to fit the legend
                                                    outerRadius={80}
                                                    label={({ amount }) => `Rs. ${amount}`} // Displaying amount inside the pie
                                                >
                                                    {
                                                        loanPieData.map((entry, index) => (
                                                            <Cell key={`cell-${index}`} fill={`#${Math.floor(Math.random() * 16777215).toString(16)}`} />
                                                        ))
                                                    }
                                                </Pie>
                                                <Legend verticalAlign="bottom" height={36} />
                                            </PieChart>
                                        </Grid>

                                        {/* Payment Pie Chart */}
                                        <Grid item xs={12} sm={6} style={{ textAlign: 'center' }}>
                                            <PieChart width={350} height={300}>
                                                <Pie
                                                    data={paymentPieData}
                                                    dataKey="amount"
                                                    nameKey="dataKey"
                                                    cx="50%"
                                                    cy="45%"
                                                    outerRadius={80}
                                                    label={({ amount }) => `Rs. ${amount}`} // Displaying amount inside the pie
                                                >
                                                    {
                                                        paymentPieData.map((entry, index) => (
                                                            <Cell key={`cell-${index}`} fill={`#${Math.floor(Math.random() * 16777215).toString(16)}`} />
                                                        ))
                                                    }
                                                </Pie>
                                                <Legend verticalAlign="bottom" height={36} />
                                            </PieChart>
                                        </Grid>
                                    </Grid>
                                    <Grid container spacing={2}>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>Loan Amount:</strong> {totalAmount}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>Interest Rate:</strong> {interestRate}%
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>EMI Amount:</strong> {emiAmount}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>EMI Date of Month:</strong> {emiDateOfMonth}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>Loan Start Date:</strong> {dayjs(loanStartDate).format('DD-MM-YYYY')}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>Tenure:</strong> {tenure} months
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>Total RePayment:</strong> {principalAmount + interestAmount}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <Typography variant="body1">
                                                <strong>Remaining Tenure:</strong> {remainingTenure} months
                                            </Typography>
                                        </Grid>
                                    </Grid>
                                </Grid>
                            </ModifyContainerDetails>
                        </Grid>

                        {/* Paid Loan Options */}
                        <Grid item xs={12}>
                            <FormControlStyled component="fieldset">
                                <RadioGroup
                                    row
                                    value={paymentOption}
                                    onChange={(e) => setPaymentOption(e.target.value)}
                                >
                                    <FormControlLabel value="monthsPaid" control={<Radio />} label="Mark Number of Months Paid" />
                                    <FormControlLabel value="principalPaid" control={<Radio />} label="Pay Principal Amount" />
                                </RadioGroup>
                            </FormControlStyled>

                            <Grid container spacing={2} alignItems="center">
                                {/* Mark Number of Months Paid */}
                                <Grid item xs={6} sm={6}>
                                    <Typography variant="body1">Months Paid</Typography>
                                    <OutlinedInput
                                        type="number"
                                        value={monthsPaid}
                                        onChange={(e) => {
                                            if (e.target.value == '' ||
                                                (tenureRange.left < e.target.value && tenureRange.right >= e.target.value)) {
                                                setMonthsPaid(e.target.value);
                                            }
                                        }}
                                        fullWidth
                                        startAdornment={<InputAdornment position="start">{tenureRange.left + " <"}</InputAdornment>}
                                        endAdornment={<InputAdornment position="end">{" < " + tenureRange.right + "  In month"}</InputAdornment>}
                                        disabled={paymentOption !== 'monthsPaid'}
                                    />
                                </Grid>
                                {/* Pay Principal Amount */}
                                <Grid item xs={6} sm={6}>
                                    <Typography variant="body1">Principal Amount Paid</Typography>
                                    <OutlinedInput
                                        type="number"
                                        value={principalPaid}
                                        onChange={(e) => setPrincipalPaid(e.target.value)}
                                        fullWidth
                                        startAdornment={<InputAdornment position="start">Rs.</InputAdornment>}
                                        disabled={paymentOption !== 'principalPaid' || true}
                                    />
                                </Grid>
                            </Grid>
                        </Grid>

                        {/* Add and Cancel Buttons */}
                        <Grid item xs={12}>
                            <Grid container justifyContent="flex-end" spacing={2}>
                                <Grid item>
                                    <Button variant="contained" color="secondary" onClick={handleModifyDialog}>
                                        Cancel
                                    </Button>
                                </Grid>
                                <Grid item>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={handleModify}
                                        disabled={isAnyModifyFieldEmpty()}
                                    >
                                        Modify
                                    </Button>
                                </Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </DialogContent>
            </DialogPaper>
        </Box>
    );
};

export default Loan;
