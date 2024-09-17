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
import { useSelector } from "react-redux";
import { formatDate } from "../functions/function";

const CreditCards = () => {
    const [data, setData] = useState([]);
    //const [dataList, setDataList] = useState([]);
    const [cookies, _setCookie] = useCookies(['access_token']);
    const [isLoading, setIsLoading] = useState(true);
    const [isDialogOpen, setDialogOpen] = useState(false);
    const [enableEditDeleteBtn, setEnableEditDeleteBtn] = useState(false);
    const [bankName, setBankName] = useState('');
    const [creditCardName, setCreditCardName] = useState('');
    const [statementDate, setStatementDate] = useState('');
    const [dueDate, setDueDate] = useState('');
    const [id, setId] = useState(null);
    const [isSaved, setIsSaved] = useState(false);
    const viewPrefix = "Day ";
    const viewSuffix = " of Every month";

    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.defaults.headers.common['Authorization'] = cookies['access_token'];
                const dbData = await axios.get(COMMON_URL + "app/get-credit-cards");
                if (dbData.status === 200 && dbData.data) {
                    setData(dbData.data);
                    setIsLoading(false);
                }
            }
            catch (error) {
                console.error('Error calling one or more APIs', error);
            }
        };
        fetchData();
    }, [isSaved]);

    const columns = [
        { field: "bankName", headerName: "Bank Name", flex: 1 },
        { field: "creditCardName", headerName: "Credit Card Name", flex: 1 },
        {
            field: "statementDate",
            headerName: "Statement Date",
            flex: 1,
            valueFormatter: (params) => {
                const formattedDate = `${viewPrefix}${params.value}${viewSuffix}`;
                return formattedDate;
            },
        },
        {
            field: "dueDate",
            headerName: "Due Date",
            flex: 1,
            valueFormatter: (params) => {
                const formattedDate = `${viewPrefix}${params.value}${viewSuffix}`;
                return formattedDate;
            },
        },
    ];

    const handleDialog = () => {
        setDialogOpen(!isDialogOpen);
    };

    const isAnyFieldEmpty = () => {
        return !bankName || !creditCardName || !statementDate || !dueDate;
    };

    const handleAdd = () => {
        setIsLoading(true);
        handleDialog();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id, bankName: bankName, creditCardName: creditCardName, statementDate: statementDate, dueDate: dueDate };
        axios.post(COMMON_URL + "app/save-credit-card", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setId(null);
            setBankName('');
            setCreditCardName('');
            setStatementDate('');
            setDueDate('');
            setEnableEditDeleteBtn(false);
        })
    };

    const handleDelete = () => {
        setIsLoading(true);
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id }
        axios.post(COMMON_URL + "app/delete-credit-card", data).then((res) => {
            setIsSaved(!isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setId(null);
            setBankName('');
            setCreditCardName('');
            setStatementDate('');
            setDueDate('');
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
                        <h1>Credit Card</h1>
                        <div>
                            <Button
                                variant="contained"
                                onClick={handleDialog}
                                disabled={enableEditDeleteBtn}
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
                        onRowSelectionModelChange={(newSelection) => {
                            if (newSelection.length == 1) {
                                const selectedData = data.find(item => item.id === newSelection[0]);
                                setId(selectedData.id);
                                setBankName(selectedData.bankName);
                                setCreditCardName(selectedData.creditCardName);
                                setStatementDate(selectedData.statementDate);
                                setDueDate(selectedData.dueDate);
                                setEnableEditDeleteBtn(true);
                            }
                            else {
                                setId(null);
                                setBankName('');
                                setCreditCardName('');
                                setStatementDate('');
                                setDueDate('');
                                setEnableEditDeleteBtn(false);
                            }
                        }}
                    // rowSelectionModel={selectedRow ? selectedRow : {}}
                    // style={{ marginBottom: "16px" }}
                    />
                </>}
            <Dialog open={isDialogOpen} onClose={handleDialog}>
                <DialogTitle>{!enableEditDeleteBtn ? `Add Card` : `Edit Card`}</DialogTitle>
                <DialogContent>
                    <form>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Bank Name</InputLabel>
                                    <OutlinedInput
                                        label="Bank Name"
                                        type="string"
                                        value={bankName}
                                        onChange={(e) => setBankName(e.target.value)}
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Credit Card Name</InputLabel>
                                    <OutlinedInput
                                        label="Credit Card Name"
                                        type="string"
                                        value={creditCardName}
                                        onChange={(e) => setCreditCardName(e.target.value)}
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Statement Date</InputLabel>
                                    <OutlinedInput
                                        label="Statement Date"
                                        type="number"
                                        value={statementDate}
                                        inputProps={{
                                            min: 1,
                                            max: 31,
                                            step: 1,
                                        }}
                                        onChange={(e) => {
                                            const newValue = parseInt(e.target.value, 10) || 1;
                                            const clampedValue = Math.min(31, Math.max(1, newValue));
                                            setStatementDate(clampedValue);
                                        }}
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Due Date</InputLabel>
                                    <OutlinedInput
                                        label="Due Date"
                                        type="number"
                                        value={dueDate}
                                        inputProps={{
                                            min: 1,
                                            max: 31,
                                            step: 1,
                                        }}
                                        onChange={(e) => {
                                            const newValue = parseInt(e.target.value, 10) || 1;
                                            const clampedValue = Math.min(31, Math.max(1, newValue));
                                            setDueDate(clampedValue);
                                        }}
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

export default CreditCards;
