import React from 'react';
import { Paper, Typography } from '@mui/material';
import { RUPEE_SYMBOL } from '../../constants/Currency';
import { makeStyles } from '@mui/styles';

const useStyles = makeStyles(() => ({
    paper: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100%', // Ensure the Paper takes up the full height of the grid item
    },
}));

const CurrentValuePaper = (props) => {
    const classes = useStyles();
    return (
        <Paper elevation={3} className={classes.paper}>
            <Typography variant="h5" color={props.isPositive ? "green" : "red"} fontWeight="bold" fontSize="35px">
                {RUPEE_SYMBOL + " " + props.amount}
            </Typography>
            <Typography fontWeight="bold" fontSize="13px">
                Cuurent Value
            </Typography>
        </Paper>
    );
}

export default CurrentValuePaper;