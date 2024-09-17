import React, { useState } from 'react';
import { TextField, Button, Grid, Typography, Paper, Box, FormControl, InputLabel, OutlinedInput } from '@mui/material';
import { makeStyles } from '@mui/styles';

const useStyles = makeStyles({
  container: {
    padding: '24px',
    maxWidth: '600px',
    margin: '1 auto',
  },
  paper: {
    padding: '16px',
    marginTop: 60,
  },
  button: {
    marginTop: '16px',
  },
  formControl: {
    width: '100%',
  },
  outBoxGrid: {
    marginBottom: '16px',
  },
  headerOfGrid: {
    marginBottom: '30px',
  }
});

const ProfileSettings = (props) => {
  const classes = useStyles();
  
  const [formData, setFormData] = useState({
    firstName: props.userData.fullName,
    lastName: props.userData.fullName,
    email: props.userData.email,
    phoneNumber: props.userData.phoneNumber,
    currentPassword: '',
    newPassword: '',
    totalSalary: '',
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission
    console.log('Form submitted:', formData);
  };

  return (
    <Paper className={classes.paper}>
      <Typography variant="h6" gutterBottom>
        Profile Settings
      </Typography>
      <Box component="form" onSubmit={handleSubmit} className={classes.container}>
        <Typography className={classes.headerOfGrid}>Full Name</Typography>
        <Grid container spacing={2} className={classes.outBoxGrid}>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="firstName">First Name</InputLabel>
              <OutlinedInput
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                label="First Name"
              />
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="lastName">Last Name</InputLabel>
              <OutlinedInput
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                label="Last Name"
              />
            </FormControl>
          </Grid>
        </Grid>
        <Typography className={classes.headerOfGrid}>Contacts</Typography>
        <Grid container spacing={2} className={classes.outBoxGrid}>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="email">Email</InputLabel>
              <OutlinedInput
                id="email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                label="Email"
              />
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="phoneNumber">Phone Number</InputLabel>
              <OutlinedInput
                id="phoneNumber"
                name="phoneNumber"
                type="tel"
                value={formData.phoneNumber}
                onChange={handleChange}
                label="Phone Number"
              />
            </FormControl>
          </Grid>
        </Grid>
        <Typography className={classes.headerOfGrid}>Change Password</Typography>  
        <Grid container spacing={2} className={classes.outBoxGrid}>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="currentPassword">Current Password</InputLabel>
              <OutlinedInput
                id="currentPassword"
                name="currentPassword"
                type="password"
                value={formData.currentPassword}
                onChange={handleChange}
                label="Current Password"
              />
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="newPassword">New Password</InputLabel>
              <OutlinedInput
                id="newPassword"
                name="newPassword"
                type="password"
                value={formData.newPassword}
                onChange={handleChange}
                label="New Password"
              />
            </FormControl>
          </Grid>
        </Grid>
        <Typography className={classes.headerOfGrid}>Income</Typography>
        <Grid container spacing={2} className={classes.outBoxGrid}>
          <Grid item xs={12} sm={6}>
            <FormControl className={classes.formControl} variant="outlined">
              <InputLabel htmlFor="totalSalary">Total Salary</InputLabel>
              <OutlinedInput
                id="totalSalary"
                name="totalSalary"
                type="number"
                value={formData.totalSalary}
                onChange={handleChange}
                label="Total Salary"
              />
            </FormControl>
          </Grid>
        </Grid>
        <Button type="submit" variant="contained" color="primary" className={classes.button}>
          Save Changes
        </Button>
      </Box>
    </Paper>
  );
};

export default ProfileSettings;