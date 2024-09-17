import { Button, FormControl, InputLabel, MenuItem, Paper, Select, Switch, Tooltip } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { COMMON_URL } from "../../constants/URL";
import UpdateIcon from '@mui/icons-material/Update';
import { getLastThreeYears } from "../../functions/function";
import axios from "axios";

const Numberview = (props) => {
    const [cookies, _setCookie] = useCookies(['access_token']);
    const years = getLastThreeYears();
    const months = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"];

    const updateBudgetData = () => {
        props.setIsLoading(true);
        axios.defaults.headers.common['Authorization'] = cookies['access_token'];
        axios.post(COMMON_URL + "app/update-budget-data").then((res) => {
            props.setSaved(!props.isSaved);
        }).catch((error) => {
            console.error('userinfo failed:', error);
            props.setIsLoading(false);
        })
    }

    const handleSwitchChange = () => {
        props.setDisableMonth(!props.disableMonth);
    };

    return (
        <div>
            <div style={{ display: "flex", justifyContent: "flex-end", alignItems: "center", marginBottom: 20 }}>
                <div style={{ display: "flex", alignItems: "center" }}>
                    <Tooltip title="Show last year" arrow>
                        <Switch
                            onChange={handleSwitchChange}
                            color="primary"
                        />
                    </Tooltip>

                    <FormControl style={{ marginRight: 10, width: 180 }} disabled={props.disableMonth}>
                        <InputLabel>Month</InputLabel>
                        <Select
                            value={props.selectedMonth}
                            onChange={(e) => props.setSelectedMonth(e.target.value)}
                            label="Month"
                        >
                            {months.map((month) => (
                                <MenuItem key={month} value={month}>
                                    {month}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl style={{ marginRight: 10, width: 100 }}>
                        <InputLabel>Year</InputLabel>
                        <Select
                            value={props.selectedYear}
                            onChange={(e) => props.setSelectedYear(e.target.value)}
                            label="Year"
                        >
                            {years.map((year) => (
                                <MenuItem key={year} value={year}>
                                    {year}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <Tooltip title="Update Budget Data" arrow>
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={updateBudgetData}

                            style={{ marginRight: 10, height: 55 }}
                        >
                            <UpdateIcon />
                        </Button>
                    </Tooltip>
                </div>

            </div>
            <Paper>
                Ho
            </Paper>
        </div>
    )
}

export default Numberview;