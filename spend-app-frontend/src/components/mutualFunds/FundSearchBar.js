import React, { useState } from 'react';
import { AppBar, Toolbar, Button, TextField, MenuItem, FormControl, InputLabel, Grid, Tooltip } from '@mui/material';
import { makeStyles } from '@mui/styles';
import AddIcon from '@mui/icons-material/Add';
import UploadIcon from '@mui/icons-material/Upload';
import AddItemDialog from './AddItemDialog';
import UploadFormDialog from './UploadFormDialog';
import PieChartIcon from "@mui/icons-material/PieChart";
import ProfileAnalysis from './ProfileAnalysis';

const useStyles = makeStyles({
    root: {
        flexGrow: 1,
        marginBottom: '16px'
    },
    appBar: {
        boxShadow: 'none !important',
        marginTop: '16px'
    },
    spacer: {
        flexGrow: 1,
    },
    buttonGroup: {
        display: 'flex',
        justifyContent: 'flex-end',
        gap: '8px',
    },
});

const FundSearchBar = (props) => {
    const classes = useStyles();
    const [openAddForm, setOpenAddForm] = useState(false);
    const [openUploadForm, setOpenUploadForm] = useState(false);
    const [openAnalysis, setOpenAnalysis] = useState(false);

    const handleAddClick = () => {
        setOpenAddForm(true);
    };

    const handleClose = () => {
        setOpenAddForm(false);
    };

    const handleUploadClick = () => {
        setOpenUploadForm(true);
    };

    const handleUploadClose = () => {
        setOpenUploadForm(false);
    };

    const handleAnalysisClick = () => {
        setOpenAnalysis(true);
    };

    const handleAnalysisClose = () => {
        setOpenAnalysis(false);
    };

    return (
        <div className={classes.root}>
            <AppBar position="static" className={classes.appBar} color="default">
                <Toolbar>
                    <Grid container alignItems="center">
                        <Grid item xs={6}>
                            {/* Search input on the left */}
                            <TextField label="Search" fullWidth />
                        </Grid>
                        <Grid item xs={6}>
                            {/* Buttons on the right */}
                            <div className={classes.spacer} />
                            <div className={classes.buttonGroup}>
                                <Tooltip title="Profile Analysis" onClick={handleAnalysisClick}>
                                    <Button variant="outlined"><PieChartIcon /></Button>
                                </Tooltip>
                                <Tooltip title="Add">
                                    <Button variant="outlined" onClick={handleAddClick}><AddIcon /></Button>
                                </Tooltip>
                                <Tooltip title="Bulk Upload">
                                    <Button variant="outlined" onClick={handleUploadClick}><UploadIcon /></Button>
                                </Tooltip>
                            </div>
                        </Grid>
                    </Grid>
                </Toolbar>
            </AppBar>

            <ProfileAnalysis open={openAnalysis} onClose={handleAnalysisClose}/>

            <AddItemDialog open={openAddForm} onClose={handleClose} mutualFundData={props.mutualFundData}
                updateData={props.updateData} setAddLoader={props.setAddLoader} itemType="Add" />

            <UploadFormDialog open={openUploadForm} onClose={handleUploadClose} />
        </div>
    );
};

export default FundSearchBar;
