import React from 'react';
import { Paper, Typography, Box } from '@mui/material';
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
    justifyContent: 'space-between',
    width: '100%',
  },
  divContent: {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center', // Align text content to the center within each div
  },
}));

const Nifty50Paper = () => {
  const classes = useStyles();

  return (
    <Paper elevation={3} className={classes.paper}>
      <Box className={classes.horizontalContainer}>
        <div className={classes.divContent}>
          <Typography variant="body1">Middle</Typography>
          <Typography variant="body1">Aligned</Typography>
        </div>
        <div className={classes.divContent}>
          <Typography variant="body1">Middle</Typography>
          <Typography variant="body1">Aligned</Typography>
        </div>
      </Box>
    </Paper>
  );
};

export default Nifty50Paper;
