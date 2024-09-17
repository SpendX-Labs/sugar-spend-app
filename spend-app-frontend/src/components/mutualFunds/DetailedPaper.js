import React from 'react';
import { Paper, Typography, Box } from '@mui/material';
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
    horizontalContainer: {
        display: 'flex',
        justifyContent: 'space-between', // This aligns the first div to the right and the second div to the left
        width: '100%',
    },
    divContent: {
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'flex-start', // Align text content to the left within each div
        paddingLeft: '16px', // Add some padding to separate text from the edges
    },
    rightDivContent: {
        alignItems: 'flex-end', // Align text content to the right within the first div
    },
    text: {
        fontSize: '15px',
        fontWeight: "bold",
    }
}));

const DetailedPaper = (props) => {
    const classes = useStyles();
    return (
        <Paper elevation={3} className={classes.paper}>
            <Box className={classes.horizontalContainer}>
                <div className={`${classes.divContent} ${classes.rightDivContent}`}>
                    <Typography variant="body1" fontWeight="bold" fontSize="15px">Invested Value :  </Typography>
                    <Typography variant="body1" fontWeight="bold" fontSize="15x">{`Total Return(%) :  `}</Typography>
                    <Typography variant="body1" fontWeight="bold" fontSize="15x">1 Day Change :  </Typography>
                    <Typography variant="body1" fontWeight="bold" fontSize="15x">XIRR :  </Typography>
                </div>
                <div className={classes.divContent}>
                    <Typography variant="body1" fontWeight="bold" fontSize="15px">
                        {RUPEE_SYMBOL + " " + props.investedAmount}
                    </Typography>
                    <Typography variant="body1" fontWeight="bold" fontSize="15px" color={props.isPositive ? "green" : "red"}>
                        {props.totalReturn}
                    </Typography>
                    <Typography variant="body1" fontWeight="bold" fontSize="15px" color={props.isDayPositive ? "green" : "red"}>
                        {props.day1Change}
                    </Typography>
                    <Typography variant="body1" fontWeight="bold" fontSize="15px" color={props.isPositive ? "green" : "red"}>
                        {props.xirr}
                    </Typography>
                </div>
            </Box>
        </Paper>
    );
}

export default DetailedPaper;