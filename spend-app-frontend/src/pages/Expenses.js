import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Button from "@mui/material/Button";
import axios from "axios";
import { COMMON_URL } from "../constants/URL";
import { Box, FormControl, Grid, InputLabel, MenuItem, OutlinedInput, Select } from "@mui/material";
import { useCookies } from "react-cookie";
import Loading from "../main/Loading";
import { formatDate, formatTime } from "../functions/function";
import dayjs from "dayjs";
import { DatePicker, LocalizationProvider, TimePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DD_MM_YYYY, HH_MM } from "../constants/DateFormat";

const Expenses = () => {
    const [data, setData] = useState([]);
    const [creditCardData, setCreditCardData] = useState([]);
    //const [dataList, setDataList] = useState([]);
    const [cookies, setCookie] = useCookies(['access_token']);
    const [isLoading, setIsLoading] = useState(true);
    const [isDialogOpen, setDialogOpen] = useState(false);
    const [enableEditDeleteBtn, setEnableEditDeleteBtn] = useState(false);
    const [cardId, setCardId] = useState(-1);
    const [cardName, setCardName] = useState('');
    const [amount, setAmount] = useState(0);
    const [expenseDate, setExpenseDate] = useState(dayjs(new Date()));
    const [expenseTime, setExpenseTime] = useState(dayjs(new Date()));
    const [reason, setReason] = useState('');
    const [id, setId] = useState(null);
    const [isSaved, setIsSaved] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.defaults.headers.common['Authorization'] = cookies['access_token'];
                const [dbData, creditCards] = await Promise.all([
                    axios.get(COMMON_URL + "app/get-expense"),
                    axios.get(COMMON_URL + "app/get-credit-cards"),
                ]);
                if (dbData.status === 200 && creditCards.status === 200 && dbData.data && creditCards.data) {
                    var modifiedData = dbData.data;
                    modifiedData.forEach(item => {
                        item.cardName = item.creditCard.bankName + " " + item.creditCard.creditCardName;
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
        { field: "cardName", headerName: "Card Name", flex: 1, headerAlign: "center", align: "center", },
        {
            field: "amount",
            headerName: "Amount",
            type: "number",
            flex: 1,
            headerAlign: "center",
            align: "center",
        },
        {
            field: "expenseDate",
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
            field: "expenseTime",
            headerName: "Time",
            flex: 1,
            headerAlign: "center",
            align: "center",
            valueFormatter: (params) => {
                const formattedTime = formatTime(params.value);
                return formattedTime;
            }
        },
        { field: "reason", headerName: "Reason", flex: 1, headerAlign: "center", align: "center", },
    ];

    const handleDialog = () => {
        setDialogOpen(!isDialogOpen);
    };

    const isAnyFieldEmpty = () => {
        return !cardId || !amount || !expenseDate;
    };

    const handleAdd = () => {
        setIsLoading(true);
        handleDialog();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const dateToSend = expenseDate.format('YYYY-MM-DDTHH:mm:ss');
        const data = { id: id, creditCard: { id: cardId }, amount: amount, expenseDate: dateToSend, expenseTime: dayjs(expenseTime).format(HH_MM), reason: reason };
        axios.post(COMMON_URL + "app/save-expense", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setId(null);
            setCardId(-1);
            setCardName('');
            setAmount(0);
            setExpenseDate(dayjs(new Date()));
            setExpenseTime(dayjs(new Date()));
            setReason('');
            setEnableEditDeleteBtn(false);
        })
    };

    const handleDelete = () => {
        setIsLoading(true);
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id }
        axios.post(COMMON_URL + "app/delete-expense", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setId(null);
            setCardId(-1);
            setCardName('');
            setAmount(0);
            setExpenseDate(dayjs(new Date()));
            setExpenseTime(dayjs(new Date()));
            setReason('');
            setEnableEditDeleteBtn(false);
        })
    }

    return (
        <Box sx={{ width: "100%", paddingTop: 6 }}>
            {isLoading ?
                <>
                    <Loading />
                </> :
                <>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 30 }}>
                        <h1>Expenses</h1>
                        <div>
                            <Button
                                variant="contained"
                                disabled={enableEditDeleteBtn}
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
                                setId(selectedData.id);
                                setCardId(selectedData.creditCard.id);
                                setCardName(selectedData.creditCard.creditCardName);
                                setAmount(selectedData.amount);
                                setExpenseDate(dayjs(selectedData.expenseDate));
                                setExpenseTime(dayjs(selectedData.expenseTime, HH_MM));
                                setReason(selectedData.reason);
                                setEnableEditDeleteBtn(true);
                            }
                            else {
                                setId(null);
                                setCardId(-1);
                                setCardName('');
                                setAmount(0);
                                setExpenseDate(dayjs(new Date()));
                                setExpenseTime(dayjs(new Date()));
                                setReason('');
                                setEnableEditDeleteBtn(false);
                            }
                        }}
                    // rowSelectionModel={selectedRow ? selectedRow : {}}
                    // style={{ marginBottom: "16px" }}
                    />
                </>}
            <Dialog open={isDialogOpen} onClose={handleDialog}>
                <DialogTitle>{!enableEditDeleteBtn ? `Add Expense` : `Edit Expense`}</DialogTitle>
                <DialogContent>
                    <form>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined" style={{ marginTop: "16px" }}>
                                    <InputLabel>Card Name</InputLabel>
                                    <Select
                                        value={cardName}
                                        onChange={(e) => {
                                            var cardName = e.target.value;
                                            const selectedCreditCard = creditCardData.find(item => item.creditCardName === cardName);
                                            if (selectedCreditCard) {
                                                setCardId(selectedCreditCard.id);
                                            }
                                            setCardName(cardName);
                                        }}
                                        input={<OutlinedInput label="Card Name" />}
                                        fullWidth
                                    >
                                        {creditCardData.map((item) => (
                                            <MenuItem key={item.id} value={item.creditCardName}>
                                                {item.bankName + " " + item.creditCardName}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Amount</InputLabel>
                                    <OutlinedInput
                                        label="Amount"
                                        type="number"
                                        value={amount}
                                        onChange={(e) => setAmount(e.target.value)}
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={6}>
                                <Box display="flex" justifyContent="flex-start">
                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                        <DatePicker
                                            value={expenseDate}
                                            onChange={(newDate) => setExpenseDate(newDate)}
                                            format={DD_MM_YYYY}
                                        />
                                    </LocalizationProvider>
                                </Box>
                            </Grid>
                            <Grid item xs={6}>
                                <Box display="flex" justifyContent="flex-end">
                                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                                        <TimePicker
                                            value={expenseTime.toDate()}
                                            onChange={(newTime) => setExpenseTime(newTime)}
                                            format={HH_MM}
                                        />
                                    </LocalizationProvider>
                                </Box>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Reason</InputLabel>
                                    <OutlinedInput
                                        label="Reason"
                                        type="string"
                                        value={reason}
                                        onChange={(e) => setReason(e.target.value)}
                                    />
                                </FormControl>
                            </Grid>
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
        </Box>
    );
};

export default Expenses;
