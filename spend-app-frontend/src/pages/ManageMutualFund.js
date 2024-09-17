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
import UpdateIcon from '@mui/icons-material/Update';
import { formatDate } from "../functions/function";

const ManageMutualFund = () => {
    const [data, setData] = useState([]);
    //const [dataList, setDataList] = useState([]);
    const [cookies, _setCookie] = useCookies(['access_token']);
    const [isLoading, setIsLoading] = useState(true);
    const [isDialogOpen, setDialogOpen] = useState(false);
    const [enableEditDeleteBtn, setEnableEditDeleteBtn] = useState(false);
    const [schemeName, setSchemeName] = useState('');
    const [schemeCode, setSchemeCode] = useState('');
    const [id, setId] = useState(null);
    const [isSaved, setIsSaved] = useState(false);
    const userData = useSelector(state => state.login);

    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.defaults.headers.common['Authorization'] = cookies['access_token'];
                //const marketDataPromise = axios.get(COMMON_URL + "app/get-mf-api-data");
                const dbData = await axios.get(COMMON_URL + "app/get-mutualfunds");

                //const [marketData, dbData] = await Promise.all([marketDataPromise, dbDataPromise]);

                if (dbData.status === 200 && dbData.data) {
                    //setDataList(marketData.data);
                    setData(dbData.data);
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
        { field: "amcName", headerName: "AMC Name", flex: 1 },
        { field: "schemeName", headerName: "Scheme Name", flex: 1 },
        { field: "option", headerName: "Option", flex: 1 },
        { field: "planType", headerName: "Plan Type", flex: 1 },
        { field: "schemeCode", headerName: "Scheme Code", flex: 1 },
        { 
            field: "currentNav", 
            headerName: "Current NAV", 
            flex: 1,
            valueFormatter: (params) => {
                return params.value !== null && params.value !== undefined ? params.value.toFixed(2) : '-';
            },
        },
        { 
            field: "lastNavDate", 
            headerName: "Last NAV Date", 
            flex: 1, 
            valueFormatter: (params) => {
                if (params.value !== null && params.value !== undefined) {
                    const formattedDate = formatDate(params.value);
                    return formattedDate;
                }
                return '-';
            },
        },
    ];

    const handleDialog = () => {
        setDialogOpen(!isDialogOpen);
    };

    const isAnyFieldEmpty = () => {
        return !schemeName || !schemeCode;
    };

    const handleAdd = () => {
        setIsLoading(true);
        handleDialog();
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        const data = { id: id, schemeName: schemeName, schemeCode: schemeCode };
        axios.post(COMMON_URL + "app/save-mutualfund", data).then((res) => {
            setIsSaved(true);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            setIsLoading(false);
        }).finally(() => {
            setSchemeCode('');
            setSchemeName('');
        })
    };

    const triggerUpdateNav = () =>{
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        axios.post(COMMON_URL + "app/trigger-update-nav", data).then((res) => {

        }).catch((error) => {
            console.error('userinfo failed:', error);
        })
    }

    const hasAdmin = () => {
        return userData && userData.authorities[0].authority === "admin";
    }

    return (
        <Box sx={{ width: "100%", paddingTop: 6 }}>
            {isLoading ?
                <>
                    <Loading />
                </> :
                <>
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 30 }}>
                        <h1>Manage Mutual Fund</h1>
                        {hasAdmin() &&
                            <div>
                                <Button 
                                    variant="contained"
                                    onClick={triggerUpdateNav}
                                    style={{ marginRight: 10 }}
                                    title="Update NAV"
                                >
                                    <UpdateIcon />
                                </Button>
                                <Button
                                    variant="contained"
                                    onClick={handleDialog}
                                    style={{ marginRight: 10 }}
                                >
                                    Add
                                </Button>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    disabled={!enableEditDeleteBtn}
                                    //onClick={() => handleRowEdit(selectedRow)}
                                    style={{ marginRight: 10 }}
                                >
                                    Edit
                                </Button>
                                <Button
                                    variant="contained"
                                    color="secondary"
                                    disabled={!enableEditDeleteBtn}
                                //onClick={() => handleDelete(selectedRow)}
                                >
                                    Delete
                                </Button>
                            </div>
                        }
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
                    // pageSizeOptions={[5, 10]}
                    // checkboxSelection
                    // autoHeight
                    // onRowSelectionModelChange={(newSelection) => {
                    //     handleRowSelection(newSelection);
                    // }}
                    // rowSelectionModel={selectedRow ? selectedRow : {}}
                    // style={{ marginBottom: "16px" }}
                    />
                </>}
            <Dialog open={isDialogOpen} onClose={handleDialog}>
                <DialogTitle>Add Item</DialogTitle>
                <DialogContent>
                    <form>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Mutual Fund</InputLabel>
                                    <Select
                                        //value={side}
                                        //onChange={(e) => setSide(e.target.value)}
                                        input={<OutlinedInput label="Side (Buy/Sell)" />}
                                        fullWidth
                                        disabled
                                    >
                                        <MenuItem value="Buy">Buy</MenuItem>
                                        <MenuItem value="Sell">Sell</MenuItem>
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Scheme Name</InputLabel>
                                    <OutlinedInput
                                        label="Scheme Name"
                                        type="string"
                                        value={schemeName}
                                        onChange={(e) => setSchemeName(e.target.value)}
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth variant="outlined">
                                    <InputLabel>Scheme Code</InputLabel>
                                    <OutlinedInput
                                        label="Scheme Code"
                                        type="number"
                                        value={schemeCode}
                                        onChange={(e) => {
                                            const inputValue = e.target.value;
                                            if (/^\d*$/.test(inputValue)) {
                                                setSchemeCode(inputValue);
                                            }
                                        }}
                                    //disabled
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={6}>
                                <Box display="flex" justifyContent="flex-end">
                                    <Button variant="contained" color="primary" onClick={handleAdd} disabled={isAnyFieldEmpty()}>
                                        Add
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

export default ManageMutualFund;
