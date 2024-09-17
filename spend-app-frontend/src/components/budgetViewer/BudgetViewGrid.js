import React, { useEffect, useState } from "react";
import { DataGrid } from "@mui/x-data-grid";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Button from "@mui/material/Button";
import axios from "axios";
import { COMMON_URL } from "../../constants/URL";
import { Box, FormControl, Grid, InputLabel, OutlinedInput } from "@mui/material";
import { useCookies } from "react-cookie";
import Loading from "../../main/Loading";

const BudgetViewGrid = (props) => {
    const [data, setData] = useState([]);
    //const [dataList, setDataList] = useState([]);
    const [cookies] = useCookies(['access_token']);
    const [isDialogOpen, setDialogOpen] = useState(false);
    const [cardName, setCardName] = useState('');
    const [remainingAmount, setRemainingAmount] = useState(0);
    const [id, setId] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.defaults.headers.common['Authorization'] = cookies['access_token'];
                var reqMonth = "";
                if(!props.disableMonth)
                    reqMonth = props.selectedMonth;
                const reqData = { budgetYear: props.selectedYear, budgetMonth: reqMonth };
                const dbData = await axios.post(COMMON_URL + "app/get-budgets", reqData);
                if (dbData.status === 200 && dbData.data) {
                    var modifiedData = dbData.data;
                    modifiedData.forEach(item => {
                        item.cardName = item.creditCard.bankName + " " + item.creditCard.creditCardName;
                        item.month = item.budgetMonth + "-" + item.budgetYear;
                    })
                    setData(modifiedData);
                    props.setIsLoading(false);
                }
            }
            catch (error) {
                console.error('Error calling one or more APIs', error);
            }
        };
        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.selectedMonth, props.selectedYear, props.isSaved, props.disableMonth]);

    const columns = [
        { field: "cardName", headerName: "Card Name", flex: 1, headerAlign: "center", align: "center", },
        { field: "month", headerName: "Month", flex: 1, headerAlign: "center", align: "center", },
        { field: "actualAmount", headerName: "Actual", flex: 1 },
        { field: "remainingAmount", headerName: "Remaining", flex: 1 },
    ];

    const handleDialog = () => {
        setDialogOpen(!isDialogOpen);
    };

    const isAnyFieldEmpty = () => {
        return !remainingAmount;
    };

    const handleModify = () => {
        props.setIsLoading(true);
        handleDialog();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id, remainingAmount: remainingAmount };
        axios.post(COMMON_URL + "app/modify-remaing-amount", data).then((res) => {
            props.setSaved(!props.isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            props.setIsLoading(false);
        }).finally(() => {
            setId(null);
            setCardName('');
            setRemainingAmount(0);
        })
    };

    return (
        <Box sx={{ width: "100%", paddingTop: 6 }}>
            {props.isLoading ?
                <>
                    <Loading />
                </> :
                <>
                    <DataGrid
                        autoHeight
                        rows={data}
                        columns={columns}
                        style={{ width: "100%" }}
                        initialState={{
                            pagination: {
                                paginationModel: { page: 0, pageSize: 5 },
                            },
                        }}
                        pageSizeOptions={[5, 10]}
                        onRowDoubleClick={(newSelection) => {
                            if (!props.disableMonth && newSelection.row) {
                                const selectedData = newSelection.row;
                                setId(selectedData.id);
                                setCardName(selectedData.cardName);
                                setRemainingAmount(selectedData.remainingAmount);
                                handleDialog();
                            }
                            else {
                                setId(null);
                                setCardName('');
                                setRemainingAmount(0);
                            }
                        }}
                    // rowSelectionModel={selectedRow ? selectedRow : {}}
                    // style={{ marginBottom: "16px" }}
                    />
                </>}
            <Dialog open={isDialogOpen} onClose={handleDialog}>
                <DialogTitle>{cardName}</DialogTitle>
                <DialogContent>
                    <form>
                        <Grid container spacing={2} paddingTop={2}>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Remaing Amount</InputLabel>
                                    <OutlinedInput
                                        label="Remaing Amount"
                                        type="number"
                                        value={remainingAmount}
                                        onChange={(e) => setRemainingAmount(e.target.value)}
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={6}>
                                <Box display="flex" justifyContent="flex-end">
                                    <Button variant="contained" color="primary" onClick={handleModify} disabled={isAnyFieldEmpty()}>
                                        Modify
                                    </Button>
                                </Box>
                            </Grid>
                            <Grid item xs={6}>
                                <Box display="flex" justifyContent="flex-start">
                                    <Button variant="contained" color="secondary" onClick={handleDialog}>
                                        Cancel
                                    </Button>
                                </Box>
                            </Grid>
                        </Grid>
                    </form>
                </DialogContent>
            </Dialog>
        </Box>
    );
};

export default BudgetViewGrid;
