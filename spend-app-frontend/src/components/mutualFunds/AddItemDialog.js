import React, { useEffect, useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
    Button,
    Grid,
    Box,
    OutlinedInput,
} from '@mui/material';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import axios from 'axios';
import { COMMON_URL } from '../../constants/URL';
import { useCookies } from 'react-cookie';
import { DD_MM_YYYY } from '../../constants/DateFormat';
import dayjs from 'dayjs';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AddItemDialog = (props) => {
    const [cookies, setCookie] = useCookies(['access_token']);
    const [id, setId] = useState(null);
    const [mutualFund, setMutualFund] = useState('');
    const [investedAmount, setInvestedAmount] = useState('');
    const [side, setSide] = useState('');
    const [date, setDate] = useState(dayjs(new Date()));

    const updateStateWithNewData = (newData) => {
        if (newData) {
            setId(newData.id);
            setMutualFund(newData.mutualFund.schemeCode);
            setInvestedAmount(newData.amount);
            setSide(newData.side);
            setDate(dayjs(newData.dateOfEvent));
        } else {
            setId(null);
            setMutualFund('');
            setInvestedAmount('');
            setSide('');
            setDate(dayjs(new Date()));
        }
    };

    useEffect(() => {
        updateStateWithNewData(props.selectedRowData);
    }, [props.selectedRowData]);

    const isAnyFieldEmpty = () => {
        return !mutualFund || !investedAmount || !side;
    };

    const handleAdd = () => {
        props.setAddLoader(true);
        props.onClose();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id, mutualFund: { schemeCode: mutualFund }, side: side, amount: investedAmount, dateOfEvent: date };
        axios.post(COMMON_URL + "app/save-order-detail", data).then((res) => {
            toast.success(res.data, {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'green', color: '#fff' },
            });
            props.updateData();
        }).catch((error) => {
            toast.error(error.response.data, {
                position: toast.POSITION.TOP_RIGHT,
                style: { backgroundColor: 'red', color: '#fff' },
            });
            props.setAddLoader(false);
        }).finally(() => {
            setMutualFund('');
            setInvestedAmount('');
            setSide('');
            setDate(dayjs(new Date()));
        })
    };

    return (
        <Dialog open={props.open} onClose={props.onClose}>
            <DialogTitle>{props.itemType} Item</DialogTitle>
            <DialogContent>
                <form>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <FormControl fullWidth variant="outlined" style={{ marginTop: "16px" }}>
                                <InputLabel>Mutual Fund</InputLabel>
                                <Select
                                    value={mutualFund}
                                    onChange={(e) => setMutualFund(e.target.value)}
                                    input={<OutlinedInput label="Mutual Fund" />}
                                    fullWidth
                                >
                                    {props.mutualFundData.map((item) => (
                                        <MenuItem key={item.schemeCode} value={item.schemeCode}>
                                            {item.schemeName}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                        </Grid>
                        <Grid item xs={12}>
                            <FormControl fullWidth variant="outlined">
                                <InputLabel>Invested Amount</InputLabel>
                                <OutlinedInput
                                    label="Invested Amount"
                                    type="number" // Set the input type to number
                                    step="0.01"   // Specify the step value for decimal input
                                    value={investedAmount}
                                    onChange={(e) => setInvestedAmount(e.target.value)}
                                />
                            </FormControl>
                        </Grid>
                        <Grid item xs={6}>
                            <FormControl fullWidth variant="outlined">
                                <InputLabel>Side (Buy/Sell)</InputLabel>
                                <Select
                                    value={side}
                                    onChange={(e) => setSide(e.target.value)}
                                    input={<OutlinedInput label="Side (Buy/Sell)" />}
                                    fullWidth
                                >
                                    <MenuItem value="Buy">Buy</MenuItem>
                                    <MenuItem value="Sell">Sell</MenuItem>
                                </Select>
                            </FormControl>
                        </Grid>
                        <Grid item xs={6}>
                            <Box display="flex" justifyContent="flex-end">
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DatePicker
                                        value={date}
                                        onChange={(newDate) => setDate(newDate)}
                                        format={DD_MM_YYYY}
                                    />
                                </LocalizationProvider>
                            </Box>
                        </Grid>
                        <Grid item xs={6}>
                            <Box display="flex" justifyContent="flex-end">
                                <Button variant="contained" color="primary" onClick={handleAdd} disabled={isAnyFieldEmpty()}>
                                    {props.itemType}
                                </Button>
                            </Box>
                        </Grid>
                        <Grid item xs={6}>
                            <Box display="flex" justifyContent="flex-start">
                                <Button variant="contained" color="secondary" onClick={props.onClose}>
                                    Cancel
                                </Button>
                            </Box>
                        </Grid>
                    </Grid>
                </form>
            </DialogContent>
        </Dialog>
    );
};

export default AddItemDialog;
